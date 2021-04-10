package com.deliveroo.parser;

import com.deliveroo.parser.exceptions.CronParsingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CronParser {

  private static final String LABEL_MINUTE = "minute";
  private static final String LABEL_HOUR = "hour";
  private static final String LABEL_DAY_OF_MONTH = "day of month";
  private static final String LABEL_MONTH = "month";
  private static final String LABEL_DAY_OF_WEEK = "day of week";
  private static final String LABEL_COMMAND = "command";

  private static final String NEWLINE = "\n";
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Unable to run, required number of parameter(s) not provided.\n\nUsage: java -jar ./build/libs/CronParser-1.0-SNAPSHOT.jar <cron_exp>");
      System.exit(0);
    }

    String expression = args[0];
    String result = parseCronExp(expression);
    System.out.println(result);
  }

  public static String parseCronExp(String expression) {

    // validate expression for required tokens
    boolean valid = Util.isValidExpression(expression);

    if (!valid) {
      return "Invalid cron expression: " + expression;
    }

    // parse expression and get values
    String[] tokens = expression.split(" ");
    StringBuilder sb = new StringBuilder();

    try {
      // append the result
      sb.append(getMinute(tokens[0]));
      sb.append(NEWLINE);
      sb.append(getHour(tokens[1]));
      sb.append(NEWLINE);
      sb.append(getDayOfMonth(tokens[2]));
      sb.append(NEWLINE);
      sb.append(getMonth(tokens[3]));
      sb.append(NEWLINE);
      sb.append(getDayOfWeek(tokens[4]));
      sb.append(NEWLINE);
      sb.append(getCommand(tokens[5]));

    } catch (CronParsingException cpe) {
      return cpe.getLocalizedMessage();
    } catch (Exception e) {
      return "Unable to parse expression: " + expression;
    }
    return sb.toString();
  }

  private static String getMinute(String token) {
    List<Integer> values = new ArrayList<>();
    Range minuteRange = new Range(0, 59);

    if ("*".equals(token)) {
      values = IntStream.rangeClosed(0, 59).boxed().collect(Collectors.toList());
      return Util.paddedString(LABEL_MINUTE) + Util.intRangeAsSpaceDelimitedString(values);

    } else if (token.contains(",")) {
      values = Util.getIndividualValues(token);
    } else if (token.contains("/")) {
      values = Util.getStepValues(token, minuteRange);
    } else if (token.contains("-")) {
      values = Util.getRangeAsValues(token);
    } else {
      values.add(Integer.parseInt(token));
    }

    if (values.isEmpty()) {
      throw new CronParsingException("Unable to parse minute token : " + token);
    }

    Collections.sort(values);
    if (!minuteRange.withinRange(values)) {
      throw new CronParsingException("minute values: " + Util.intRangeAsSpaceDelimitedString(values) + " are not within valid range");
    }

    return  Util.paddedString(LABEL_MINUTE) + Util.intRangeAsSpaceDelimitedString(values);
  }

  private static String getHour(String token) {
    List<Integer> values  = new ArrayList<>();
    Range hourRange = new Range(0, 23);

    if ("*".equals(token)) {
      values = IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList());
      return Util.paddedString(LABEL_HOUR) + Util.intRangeAsSpaceDelimitedString(values);
    } else if (token.contains(",")) {
      values = Util.getIndividualValues(token);
    } else if (token.contains("/")) {
      values = Util.getStepValues(token, hourRange);
    } else if (token.contains("-")) {
      values = Util.getRangeAsValues(token);
    } else {
      values.add(Integer.parseInt(token));
    }

    if (values.isEmpty()) {
      throw new CronParsingException("Unable to parse hour token: " + token);
    }

    Collections.sort(values);
    if (!hourRange.withinRange(values)) {
      throw new CronParsingException("hour values: " + Util.intRangeAsSpaceDelimitedString(values) + " are not within valid range");
    }

    return  Util.paddedString(LABEL_HOUR) + Util.intRangeAsSpaceDelimitedString(values);
  }

  private static String getDayOfMonth(String token) {
    List<Integer> values = new ArrayList<>();
    Range dayOfMonthRange = new Range(1, 31);

    if ("*".equals(token)) {
      values = IntStream.rangeClosed(1, 31).boxed().collect(Collectors.toList());
      return Util.paddedString(LABEL_DAY_OF_MONTH) + Util.intRangeAsSpaceDelimitedString(values);
    } else if (token.contains(",")) {
      values = Util.getIndividualValues(token);
    } else if (token.contains("/")) {
      values = Util.getStepValues(token, dayOfMonthRange);
    } else if (token.contains("-")) {
      values = Util.getRangeAsValues(token);
    } else {
      values.add(Integer.parseInt(token));
    }

    if (values.isEmpty()) {
      throw new CronParsingException("Unable to parse day-of-month token: " + token);
    }

    Collections.sort(values);
    if (!dayOfMonthRange.withinRange(values)) {
      throw new CronParsingException("day-of-month values: " + Util.intRangeAsSpaceDelimitedString(values) + " are not within valid range");
    }

    return  Util.paddedString(LABEL_DAY_OF_MONTH) + Util.intRangeAsSpaceDelimitedString(values);
  }

  private static String getMonth(String token) {
    List<Integer> values = new ArrayList<>();
    Range monthRange = new Range(1, 12);

    if ("*".equals(token)) {
      values = IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList());
      return Util.paddedString(LABEL_MONTH) + Util.intRangeAsSpaceDelimitedString(values);
    } else if (token.contains(",")) {
      values = Util.getIndividualValues(token);
    } else if (token.contains("/")) {
      values = Util.getStepValues(token, monthRange);
    } else if (token.contains("-")) {
      values = Util.getRangeAsValues(token);
    } else {
      //TODO: Not handled JAN-DEC formats for the month, numbers only
      values.add(Integer.parseInt(token));
    }

    if (values.isEmpty()) {
      throw new CronParsingException("Unable to parse month token: " + token);
    }

    Collections.sort(values);
    if (!monthRange.withinRange(values)) {
      throw new CronParsingException("month values: " + Util.intRangeAsSpaceDelimitedString(values) + " are not within valid range");
    }

    return  Util.paddedString(LABEL_MONTH) + Util.intRangeAsSpaceDelimitedString(values);
  }

  private static String getDayOfWeek(String token) {
    List<Integer> values = new ArrayList<>();
    Range dayOfWeekRange = new Range(0, 6);

    if ("*".equals(token)) {
      values = IntStream.rangeClosed(0, 6).boxed().collect(Collectors.toList());
      return Util.paddedString(LABEL_DAY_OF_WEEK) + Util.intRangeAsSpaceDelimitedString(values);
    } else if (token.contains(",")) {
      values = Util.getIndividualValues(token);
    } else if (token.contains("/")) {
      values = Util.getStepValues(token, dayOfWeekRange);
    } else if (token.contains("-")) {
      values = Util.getRangeAsValues(token);
    } else {
      //TODO: Not handled SUN-SAT formats for the days of week, numbers only
      values.add(Integer.parseInt(token));
    }

    if (values.isEmpty()) {
      throw new CronParsingException("Unable to parse day-of-week token: " + token);
    }

    Collections.sort(values);
    if (!dayOfWeekRange.withinRange(values)) {
      throw new CronParsingException("parse day-of-week values: " + Util.intRangeAsSpaceDelimitedString(values) + " are not within valid range");
    }

    return  Util.paddedString(LABEL_DAY_OF_WEEK) + Util.intRangeAsSpaceDelimitedString(values);
  }

  private static String getCommand(String token) {
    return Util.paddedString(LABEL_COMMAND) + token;
  }
}
