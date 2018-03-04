package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by lidaina on 3/3/2018.
 */
public class FileParserActorTest {

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

  Exception e1;

  @Test
  public void testParseCorrectFile() {
    final Props props = Props.create(FileParserActor.class);
    final TestActorRef<FileParserActor> ref = TestActorRef.create(system, props, "test-B1");
    final FileParserActor actor = ref.underlyingActor();
    ref.tell(new ParseMessage(System.getProperty("user.dir")
        + "/src/main/java/com/lightbend/akka/sample/demoFiles/demo0"), ActorRef.noSender());
    try {
      ref.receive(new ParseMessage(System.getProperty("user.dir")
          + "/src/main/java/com/lightbend/akka/sample/demoFiles/demo"));
    } catch (Exception e) {
      e1 = e;
    }
    assertEquals(null, e1);
  }

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  Exception ex;

  @Test
  public void testNullPointException() {

    final Props props = Props.create(FileParserActor.class);
    final TestActorRef<FileParserActor> ref = TestActorRef.create(system, props, "test-B2");

    try {
      ref.receive(new ParseMessage(System.getProperty("user.dir")
          + "/src/main/java/com/lightbend/akka/sample/demoFiles/demo"));
    } catch (Exception e) {
      ex = e;
    }
    assertEquals(null, ex);

  }
}