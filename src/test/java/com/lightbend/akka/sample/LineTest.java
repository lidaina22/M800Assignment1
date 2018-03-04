package com.lightbend.akka.sample;

import junit.framework.TestCase;

import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by lidaina on 27/2/2018.
 */
public class LineTest extends TestCase {

  public void testEmptyString() {
    Line line = new Line("", "");
    assertTrue(0 == line.getCount());
  }

  public void testStringWithSpecialCharacters() {
    String str = "\"";
    Line line = new Line("",
        "Each timer has a key and can be replaced or cancelled.!@~#$%&*()_+{} \"<>?|\\][',./'] ");
    assertTrue(12 == line.getCount());
  }

  public void testOnlySpace() {
    Line line = new Line("", " ");
    assertTrue(0 == line.getCount());
  }

  public void testShortWords() {
    Line line = new Line("", "a, b, c, d");
    assertTrue(4 == line.getCount());
  }

  public void testLinkWord() {
    Line line = new Line("", "linked-words and passed-test");
    assertTrue(3 == line.getCount());
  }

  public void testMultipleSpaceBetweenWords() {
    Line line = new Line("", "Summer is     coming...");
    assertTrue(3 == line.getCount());
  }

  public void testWordsWithDash() {
    Line line = new Line("", "count the Words_with Dash...");
    assertTrue(4 == line.getCount());
  }

  public void testBillingWords() {
    Line line = new Line("", "a fooooooooo<40MILLIONLETTERS>ooooooo a");
    assertTrue(3 == line.getCount());
  }
}