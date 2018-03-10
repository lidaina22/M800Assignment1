package com.lightbend.akka.sample;

/**
 * Created by lidaina on 24/2/2018.
 */

import static com.lightbend.akka.sample.Constants.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.io.IOException;
import akka.actor.ActorSystem;
import akka.actor.ActorRef;

public class WordCountMain {

  public static void main(String[] args) throws IOException {

    ActorSystem system = ActorSystem.create(SYSTEM_NAME);
    ActorRef fileScanner = system.actorOf(FileScannerActor.props(), FILE_SCANNER_ACTOR_NAME);
    LoggingAdapter log = Logging.getLogger(system, system);

    try {
      fileScanner.tell(SCAN_MESSAGE, ActorRef.noSender());
    }catch(Exception e){
      log.info(e.getMessage());
    }
  }
}
