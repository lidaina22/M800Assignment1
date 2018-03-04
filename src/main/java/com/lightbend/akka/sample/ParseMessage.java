package com.lightbend.akka.sample;

import java.io.File;

/**
 * Created by lidaina on 3/3/2018.
 */
public class ParseMessage {

  private String filePath;

  public ParseMessage(String path) {
    File f = new File(path);
    if (f.exists() && !f.isDirectory()) {
      this.filePath = path;
    } else {
      this.filePath = null;
    }
  }

  public String getFilePath() {
    return filePath;
  }
}
