package com.lightbend.akka.sample;

import static com.lightbend.akka.sample.Constants.*;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import java.util.ArrayList;
import java.util.List;
import scala.collection.Iterator;

/**
 * Created by lidaina on 10/3/2018.
 */
public class RouterActor extends AbstractActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  akka.routing.Router router;
  {
    List<Routee> routees = new ArrayList<Routee>();
    for (int i = 0; i < 5; i++) {
      ActorRef r = getContext().actorOf(Props.create(FileParserActor.class, "fileParser" + i));
      getContext().watch(r);
      routees.add(new ActorRefRoutee(r));
    }
    router = new akka.routing.Router(new RoundRobinRoutingLogic(), routees);
  }


  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(ParseMessage.class, s -> {
          router.route(s, getSelf());
        })
        .matchEquals(STOP_MESSAGE, s -> {
            try{
              Iterator<Routee> i = router.routees().iterator();
              while(i.hasNext()){
                Routee routee = i.next();
                routee.send(STOP_MESSAGE, getSelf());
              }
            }catch(Exception e){
                log.info(e.getMessage());
            }finally {
              getSelf().tell(PoisonPill.getInstance(), getSelf());
            }
        })
        .build();
  }

  @Override
  public void postStop() throws Exception {
    log.info(ROUTER_STOPPED);
  }
}