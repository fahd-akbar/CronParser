package com.deliveroo.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CronParserTest {

  @Test
  void testParseCronExpForAllValues() {
    String result = "minute        0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59\n"
        + "hour          0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23\n"
        + "day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31\n"
        + "month         1 2 3 4 5 6 7 8 9 10 11 12\n"
        + "day of week   0 1 2 3 4 5 6\n"
        + "command       /usr/bin/find";
    assertEquals(result, CronParser.parseCronExp("* * * * * /usr/bin/find"));
  }

  @Test
  void testParseCronExpForInvalidValues() {
    String result = "Unable to parse expression: * abc def * * /usr/bin/find";
    assertEquals(result, CronParser.parseCronExp("* abc def * * /usr/bin/find"));
  }

  @Test
  void testParseCronExpForRanges() {
    String result = "minute        10 11 12 13 14 15\n"
        + "hour          10 11 12 13\n"
        + "day of month  15 16 17 18 19 20\n"
        + "month         6 7 8 9\n"
        + "day of week   4 5\n"
        + "command       /usr/bin/find";
    assertEquals(result, CronParser.parseCronExp("10-15 10-13 15-20 6-9 4-5 /usr/bin/find"));
  }

  @Test
  void testParseCronExpForIndividualValues() {
    String result = "minute        10 12 14\n"
        + "hour          10 13\n"
        + "day of month  15\n"
        + "month         6 9\n"
        + "day of week   3 5\n"
        + "command       /usr/bin/find";
    assertEquals(result, CronParser.parseCronExp("10,12,14 10,13 15 6,9 3,5 /usr/bin/find"));
  }

  @Test
  void testParseCronExpForStepValues() {
    String result = "minute        0 10 20 30 40 50\n"
        + "hour          0 5 10\n"
        + "day of month  15\n"
        + "month         6\n"
        + "day of week   5\n"
        + "command       /usr/bin/find";
    assertEquals(result, CronParser.parseCronExp("*/10 0-10/5 15/15 6/3 5/5 /usr/bin/find"));
  }

  @Test
  void testParseCronExpForStepsRangeAndValues() {
    String result = "minute        0 15 30 45\n"
        + "hour          0\n"
        + "day of month  1 15\n"
        + "month         1 2 3 4 5 6 7 8 9 10 11 12\n"
        + "day of week   1 2 3 4 5\n"
        + "command       /usr/bin/find";
    assertEquals(result, CronParser.parseCronExp("*/15 0 1,15 * 1-5 /usr/bin/find"));
  }
}