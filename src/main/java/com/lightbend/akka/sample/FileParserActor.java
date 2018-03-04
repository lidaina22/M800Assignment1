package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
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
  private ActorRef aggregator = getContext()
      .actorOf(Props.create(AggregatorActor.class), "aggregator");

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(ParseMessage.class, s -> {
          try {
            this.parse(s.getFilePath());
          } catch (Exception e) {
            log.info(String.valueOf(e.getStackTrace()));
            this.getSender().tell("wrongFilePath", getSelf());
          }
        })
        .matchAny(o -> {
          log.info("Receive unknown message.");
        })
        .build();
  }

  private void parse(String filePath) {
    try {
      BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
      String line = null;
      aggregator.tell("startOfFile", getSelf());
      while ((line = reader.readLine()) != null) {
        aggregator.tell(new Line(filePath, line), getSelf());
      }
      aggregator.tell("endOfFile", getSelf());
    } catch (IOException e) {
      log.info(String.valueOf(e.getStackTrace()));
    }
  }
}
