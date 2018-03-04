package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ActorLogging;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by lidaina on 24/2/2018.
 */

public class FileScannerActor extends AbstractActor {

  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private ActorRef fileParser = getContext()
      .actorOf(Props.create(FileParserActor.class), "parse-actor");

  public List<String> getFileList() {
    return fileList;
  }

  private List<String> fileList = new ArrayList<String>();

  public static Props props() {
    return Props.create(FileScannerActor.class);
  }

  @Override
  public void preStart() {
    log.info("Starting file scanner...");
  }

  @Override
  public void postStop() {
    log.info("File scanner stopped...");
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .matchEquals("scan", s -> {
          String workDirectory = System.getProperty("user.dir");
          String partialPath = "/src/main/java/com/lightbend/akka/sample/demoFiles";
          listFilesForFolder(workDirectory + partialPath);
          partialPath.replaceAll("/", File.separator);
          if (fileList.isEmpty()) {
            System.out.println("No files exists in the directory!");
          } else {
            for (String file : fileList) {
              fileParser.tell(new ParseMessage(file), getSelf());
            }
          }
        })
        .matchEquals("wrongFilePath", k -> {
          getContext().stop(getSender());
        })
        .matchAny(o -> {
          Logging.getLogger(getContext().getSystem(), this).info("Receive unknown message.");
        })
        .build();
  }

  private void listFilesForFolder(String folder) {
    try {
      Stream<Path> paths = Files.walk(Paths.get(folder));
      paths.filter(f -> Files.isRegularFile(f)).forEach(o -> fileList.add(o.toString()));
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
