package com.deliveroo.parser;

import com.deliveroo.parser.exceptions.CronParsingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {

  private static final int DEFAULT_PADDING = 14;
  private static final Pattern RANGE_PATTERN = Pattern.compile("[0-9]+-[0-9]+");

  /**
   * Checks if the expression has six tokens (inc command), separated by space.
   *
   * @param expression
   * @return
   */
  public static boolean isValidExpression(String expression) {
    String[] tokens = expression.split(" ");
    return tokens.length == 6;
  }

  /**
   * Padded string function, using the default padding constant
   *
   * @param val
   * @return
   */
  public static String paddedString(String val) {
    return String.format("%1$-" + DEFAULT_PADDING + "s", val);
  }

  /**
   * Return the integer values as space separated string values
   *
   * @param values
   * @return
   */
  public static String intRangeAsSpaceDelimitedString(List<Integer> values) {
    return values.stream().map(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  /**
   * Create a list of values from a range token based on regex
   *
   * @param rangeToken
   * @return
   */
  public static List<Integer> getRangeAsValues(String rangeToken) {
    Matcher matcher = RANGE_PATTERN.matcher(rangeToken);

    if (matcher.find()) {
      String start = rangeToken.substring(0, rangeToken.indexOf("-"));
      String end = rangeToken.substring(rangeToken.indexOf("-") + 1);

      int startIndex = Integer.parseInt(start);
      int endIndex = Integer.parseInt(end);

      return IntStream.rangeClosed(startIndex, endIndex).boxed().collect(Collectors.toList());
    } else {
      throw new CronParsingException("Unable to determine range from: " + rangeToken);
    }

  }

  /**
   * Create a list of values from comma separated values
   *
   * @param token
   * @return
   */
  public static List<Integer> getIndividualValues(String token) {
    List<Integer> values = new ArrayList<>();
    String[] valueSeparator = token.split(",");

    for (String value : valueSeparator) {
      try {
        values.add(Integer.parseInt(value));
      } catch (NumberFormatException nfe) {
        throw new CronParsingException("Unable to parse values from token: " + token);
      }
    }

    return values;
  }

  /**
   * Create a list of values from step expression values
   *
   * @param stepToken
   * @param range
   * @return
   */
  public static List<Integer> getStepValues(String stepToken, Range range) {
    List<Integer> values = new ArrayList<>();

    String start = stepToken.substring(0, stepToken.indexOf("/"));
    int step = Integer.parseInt(stepToken.substring(stepToken.indexOf("/") + 1));

    if ("*".equals(start)) {
      values = IntStream.rangeClosed(range.getMin(), range.getMax())
          .filter(i -> i % step == 0).boxed().collect(Collectors.toList());
    } else if (start.contains("-")) {
      values = getRangeAsValues(start).stream().filter(i -> i % step == 0).collect(Collectors.toList());
    } else if (Integer.parseInt(start) % step == 0) {
      values.add(Integer.parseInt(start));
    }

    return values;
  }


}
