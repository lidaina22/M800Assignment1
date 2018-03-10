package com.lightbend.akka.sample;

import junit.framework.TestCase;

import static com.lightbend.akka.sample.Constants.PARTIAL_PATH;
import static com.lightbend.akka.sample.Constants.WORK_DIRECTORY;
import static org.junit.Assert.assertTrue;

/**
 * Created by lidaina on 3/3/2018.
 */
public class ParseMessageTest extends TestCase {

  public void testPathIsDirectory() {
    // when pass a directory path rather than file path should return null
    String workDirectory = System.getProperty(WORK_DIRECTORY);
    ParseMessage pm = new ParseMessage(workDirectory);
    assertTrue(pm.getFilePath() == null);
  }

  public void testPathNotExists() {
    //when pass a wrong file path should return null
    String workDirectory = System.getProperty(WORK_DIRECTORY);
    ParseMessage pm = new ParseMessage(
        workDirectory + PARTIAL_PATH+"/notdemo");
    assertTrue(pm.getFilePath() == null);
  }

  public void testCorrectFilePath() {
    //when pass a correct file path should return file path
    String workDirectory = System.getProperty(WORK_DIRECTORY);
    ParseMessage pm = new ParseMessage(
        workDirectory + PARTIAL_PATH+"/demo0");
    assertTrue(pm.getFilePath()
        .equals(workDirectory + PARTIAL_PATH+"/demo0"));
  }
}