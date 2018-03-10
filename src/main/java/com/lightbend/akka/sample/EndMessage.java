package com.lightbend.akka.sample;

/**
 * Created by lidaina on 7/3/2018.
 */
public class EndMessage {

  String fileName;
  Boolean status = true;

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public EndMessage(String fileName){
    this.fileName = fileName;
  }

}
