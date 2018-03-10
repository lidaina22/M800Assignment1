package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import java.io.File;
import java.util.regex.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.lightbend.akka.sample.Constants.*;
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
    ref.tell(SCAN_MESSAGE, ActorRef.noSender());
    String workDirectory = System.getProperty(WORK_DIRECTORY);
    String partialPath = PARTIAL_PATH;
    partialPath = partialPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
    assertTrue(actor.getFileList().size() == new File(workDirectory+partialPath).listFiles().length);
  }

  @Test
  public void testFileListContent() {
    final Props props = Props.create(FileScannerActor.class);
    final TestActorRef<FileScannerActor> ref = TestActorRef.create(system, props, "test-A2");
    final FileScannerActor actor = ref.underlyingActor();
    ref.tell(SCAN_MESSAGE, ActorRef.noSender());
    String partialPath = PARTIAL_PATH+"/demo0";
    partialPath = partialPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
    assertTrue(actor.getFileList().contains(System.getProperty(WORK_DIRECTORY)+partialPath));
  }
}