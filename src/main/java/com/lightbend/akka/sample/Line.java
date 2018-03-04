package com.lightbend.akka.sample;

/**
 * Created by lidaina on 26/2/2018.
 */

public class Line {

  private final String fileName;
  private final int wordCount;
  private final String line;

  public Line(String fileName, String line) {
    this.fileName = fileName;
    this.line = line.trim();
    if (this.line.length() > 0 & !this.line.isEmpty()) {
      this.wordCount = this.line.split("\\s+").length;
    } else {
      this.wordCount = 0;
    }
  }

  public int getCount() {
    return this.wordCount;
  }

  public String getFileName() {
    return this.fileName;
  }

}
