package com.lightbend.akka.sample;

import static com.lightbend.akka.sample.Constants.*;
import akka.actor.AbstractActor;
import akka.actor.ActorPath;
import akka.actor.PoisonPill;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by lidaina on 24/2/2018.
 */

public class AggregatorActor extends AbstractActor {

  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private int countSum;

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .matchEquals(START_OF_FILE, s -> { countSum = 0; })
        .match(Line.class, t -> { this.countSum = t.getCount() + this.countSum; })
        .match(EndMessage.class, f -> {
          if(!f.status)
            log.info(CANNOT_READ_FILE+f.fileName);
          log.info("File:" + f.fileName+ " has " + countSum + " words.");
          ActorPath recepient = getContext().getSystem().child(FILE_SCANNER_ACTOR_NAME);
          getContext().actorSelection(recepient).tell(f, getSelf());
        })
        .matchEquals(STOP_MESSAGE, s -> {getSelf().tell(PoisonPill.getInstance(), getSelf());})
        .matchAny(o -> { log.info(UNKNOWN_MESSAGE); })
        .build();
  }

  public int getCountSum() { return countSum; }

  @Override
  public void postStop(){
    log.info(getSelf().path()+" stopped...");
  }

}
