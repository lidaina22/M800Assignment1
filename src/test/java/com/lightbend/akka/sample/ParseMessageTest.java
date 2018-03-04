package com.lightbend.akka.sample;

import junit.framework.TestCase;

import static org.junit.Assert.assertTrue;

/**
 * Created by lidaina on 3/3/2018.
 */
public class ParseMessageTest extends TestCase {

  public void testPathIsDirectory() {
    // when pass a directory path rather than file path should return null
    String workDirectory = System.getProperty("user.dir");
    ParseMessage pm = new ParseMessage(workDirectory);
    assertTrue(pm.getFilePath() == null);
  }

  public void testPathNotExists() {
    //when pass a wrong file path should return null
    String workDirectory = System.getProperty("user.dir");
    ParseMessage pm = new ParseMessage(
        workDirectory + "/src/main/java/com/lightbend/akka/sample/demoFiles/notdemo");
    assertTrue(pm.getFilePath() == null);
  }

  public void testCorrectFilePath() {
    //when pass a correct file path should return file path
    String workDirectory = System.getProperty("user.dir");
    ParseMessage pm = new ParseMessage(
        workDirectory + "/src/main/java/com/lightbend/akka/sample/demoFiles/demo0");
    assertTrue(pm.getFilePath()
        .equals(workDirectory + "/src/main/java/com/lightbend/akka/sample/demoFiles/demo0"));
  }
}