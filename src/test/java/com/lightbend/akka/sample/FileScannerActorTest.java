package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by lidaina on 3/3/2018.
 */
public class FileScannerActorTest {

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
  public void testFileScanner() {
    final Props props = Props.create(FileScannerActor.class);
    final TestActorRef<FileScannerActor> ref = TestActorRef.create(system, props, "test-A1");
    final FileScannerActor actor = ref.underlyingActor();
    ref.tell("scan", ActorRef.noSender());
    assertTrue(actor.getFileList().size() == 4);
  }

  @Test
  public void testFileListContent() {
    final Props props = Props.create(FileScannerActor.class);
    final TestActorRef<FileScannerActor> ref = TestActorRef.create(system, props, "test-A2");
    final FileScannerActor actor = ref.underlyingActor();
    ref.tell("scan", ActorRef.noSender());
    assertTrue(actor.getFileList().contains(System.getProperty("user.dir")
        + "/src/main/java/com/lightbend/akka/sample/demoFiles/demo0"));
  }
}