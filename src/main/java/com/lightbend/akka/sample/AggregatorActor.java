package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by lidaina on 24/2/2018.
 */
public class AggregatorActor extends AbstractActor {

  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public int getCountSum() {
    return countSum;
  }

  private int countSum;
  private static String fileName;

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .matchEquals("startOfFile", s -> {
          countSum = 0;
        })
        .match(Line.class, t -> {
          fileName = t.getFileName();
          this.countSum = t.getCount() + this.countSum;
        })
        .matchEquals("endOfFile", f -> {
          log.info("File:" + fileName + " has " + countSum + " words.");
        })
        .matchAny(o -> {
          log.info("Receive unknown message.");
        })
        .build();
  }
}
