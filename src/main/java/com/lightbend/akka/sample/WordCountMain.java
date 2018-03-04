package com.lightbend.akka.sample;

/**
 * Created by lidaina on 24/2/2018.
 */

import java.io.IOException;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;

public class WordCountMain {

  public static void main(String[] args) throws IOException {

    ActorSystem system = ActorSystem.create("word-count-system");

    try {
      // Create file scanner checking any files exist in predefined directory
      ActorRef fileScanner = system.actorOf(FileScannerActor.props(), "file-scanner");
      fileScanner.tell("scan", ActorRef.noSender());

      System.out.println("Press ENTER to exit Word-Count system");
      System.in.read();

    } finally {
      system.terminate();
    }
  }
}
