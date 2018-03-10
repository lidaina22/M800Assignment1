package com.lightbend.akka.sample;

/**
 * Created by lidaina on 5/3/2018.
 */
public class Constants {

  // SYSTEM MESSAGES
  static String SCAN_MESSAGE = "scan";
  static String STOP_MESSAGE = "stop";
  static String UNKNOWN_MESSAGE = "Warning, receive unknown message...";
  static String EMPTY_DIRECTORY = "Warning, empty file directory, system will exist...";
  static String START_FILE_SCANNER = "Starting file scanner...";
  static String STOP_FILE_SCANNER = "File scanner stopped...";
  static String WRONG_FILEPATH = "wrongFilePath";
  static String START_OF_FILE = "startOfFile";
  static String ROUTER_STOPPED = "Router stopped...";
  static String FILEPATH_NOT_EXSIT_ERROR = "Warning, file path doesn't exsit, system will exit...";
  static String CANNOT_READ_FILE = "Error, cannot read file: ";

  // SYSTEM PATHS
  static String PARTIAL_PATH = "/src/test/resource";
  static String WORK_DIRECTORY = "user.dir";

  // OBJECT NAMES
  static String SYSTEM_NAME = "word-count-system";
  static String FILE_SCANNER_ACTOR_NAME = "file-scanner";
  static String ROUTER_NAME = "router";
  static String AGGREGATOR_NAME = "aggregator";


  private Constants() {
  }
}
