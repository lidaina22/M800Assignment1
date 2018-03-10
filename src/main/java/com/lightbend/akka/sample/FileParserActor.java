package com.lightbend.akka.sample;

import static com.lightbend.akka.sample.Constants.*;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by lidaina on 25/2/2018.
 */
public class FileParserActor extends AbstractActor {

  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private String name;
  private String fileName;
  private ActorRef aggregator = getContext()
      .actorOf(Props.create(AggregatorActor.class), AGGREGATOR_NAME);

  public FileParserActor(String name){
    this.name = name;
    getContext().watch(aggregator);
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(ParseMessage.class, s -> {
          try {
            this.fileName = s.getFilePath();
            if(this.fileName != null)
              this.parse(this.fileName);
            else{
              log.info(FILEPATH_NOT_EXSIT_ERROR);
              EndMessage message = new EndMessage(this.fileName);
              message.setStatus(false);
              aggregator.tell(message, getSelf());
            }
          } catch (Exception e) {
            log.info(e.getMessage());
            this.getSender().tell(WRONG_FILEPATH, getSelf());
          }
        })
        .matchEquals(STOP_MESSAGE, s -> {
          aggregator.tell(STOP_MESSAGE, getSelf());
          getSelf().tell(PoisonPill.getInstance(), getSelf());
        })
        .matchAny(o -> {
          log.info(UNKNOWN_MESSAGE);
        })
        .build();
  }

  private void parse(String filePath) {
    try {
      BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
      String line = null;
      aggregator.tell(START_OF_FILE, getSelf());
      while ((line = reader.readLine()) != null) {
        aggregator.tell(new Line(filePath, line), getSelf());
      }
      aggregator.tell(new EndMessage(filePath), getSelf());
    } catch (IOException e) {
      log.info(e.getMessage());
    }
  }

  @Override
  public void postStop(){
    log.info("File parser: "+getSelf().path()+" stopped...");
  }
}
