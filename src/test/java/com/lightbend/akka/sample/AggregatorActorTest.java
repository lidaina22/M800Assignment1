package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.lightbend.akka.sample.Constants.START_OF_FILE;
import static org.junit.Assert.*;

/**
 * Created by lidaina on 3/3/2018.
 */
public class AggregatorActorTest {

  static ActorSystem system;

  @BeforeClass
  public static void setup() {
    system = ActorSystem.create();
  }

  @AfterClass
  public static void teardown() {
    TestKit.shutdownActorSystem(system);
    system = null;
  }

  @Test
  public void testStartOfFileEvent() {
    final Props props = Props.create(AggregatorActor.class);
    final TestActorRef<AggregatorActor> ref = TestActorRef.create(system, props, "test-C1");
    final AggregatorActor actor = ref.underlyingActor();
    ref.tell(START_OF_FILE, ActorRef.noSender());
    assertTrue(actor.getCountSum() == 0);
  }

  @Test
  public void testLineEvent() {
    final Props props = Props.create(AggregatorActor.class);
    final TestActorRef<AggregatorActor> ref = TestActorRef.create(system, props, "test-C2");
    final AggregatorActor actor = ref.underlyingActor();
    ref.tell(new Line("", "Tomorrow is Sunday..."), ActorRef.noSender());
    assertTrue(actor.getCountSum() == 3);
  }

  @Test
  public void testAnotherLineEvent() {
    final Props props = Props.create(AggregatorActor.class);
    final TestActorRef<AggregatorActor> ref = TestActorRef.create(system, props, "test-C3");
    final AggregatorActor actor = ref.underlyingActor();
    ref.tell(new Line("", "Tomorrow is Sunday..."), ActorRef.noSender());
    ref.tell(new Line("", "Sunday is not tomorrow..."), ActorRef.noSender());
    assertTrue(actor.getCountSum() == 7);
  }
}