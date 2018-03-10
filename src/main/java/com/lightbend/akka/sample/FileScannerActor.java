package com.lightbend.akka.sample;

import static com.lightbend.akka.sample.Constants.*;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Stream;


/**
 * Created by lidaina on 24/2/2018.
 */

public class FileScannerActor extends AbstractActor {

  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private List<String> fileList = new ArrayList<String>();
  private ActorRef router = getContext()
      .actorOf(Props.create(RouterActor.class), ROUTER_NAME);
  private Map<String, Boolean> taskTracker = new HashMap<>();

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .matchEquals(SCAN_MESSAGE, s -> {
          String workDirectory = System.getProperty(WORK_DIRECTORY);
          String partialPath = PARTIAL_PATH;
          partialPath = partialPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
          listFilesForFolder(workDirectory + partialPath);
          if (fileList.isEmpty()) {
            log.info(EMPTY_DIRECTORY);
            getSelf().tell(STOP_MESSAGE, getSelf());
          } else {
            for (String file : fileList) {
              taskTracker.put(file, false);
              router.tell(new ParseMessage(file), getSelf());
            }
          }
        })
        .match(EndMessage.class, r -> {
          taskTracker.replace(r.fileName, true);
          if(!taskTracker.containsValue(false))
            getSelf().tell(STOP_MESSAGE, getSelf());
        })
        .matchEquals(WRONG_FILEPATH, k -> {
          getContext().stop(getSender());
        })
        .matchEquals(STOP_MESSAGE, k -> {
          try {
            router.tell(STOP_MESSAGE, getSelf());
          } catch (Exception e) {
            log.info(e.getMessage());
          } finally {
            getSelf().tell(akka.actor.PoisonPill.getInstance(), getSelf());
          }
        })
        .matchAny(o -> {
          log.info(UNKNOWN_MESSAGE);
        })
        .build();
  }

  private void listFilesForFolder(String folder) {
    try {
      Stream<Path> paths = Files.walk(Paths.get(folder));
      paths.filter(f -> Files.isRegularFile(f)).forEach(o -> fileList.add(o.toString()));
    } catch (IOException e) {
      log.info(e.getMessage());
    }
  }

  public List<String> getFileList() {
    return fileList;
  }

  public static Props props() { return Props.create(FileScannerActor.class); }

  @Override
  public void preStart() {
    log.info(START_FILE_SCANNER);
  }

  @Override
  public void postStop() {
    log.info(STOP_FILE_SCANNER);
    getContext().getSystem().terminate();
  }

}
