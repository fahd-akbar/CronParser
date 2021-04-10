package com.deliveroo.parser;

import java.util.List;

public class Range {

  private final int min;
  private final int max;

  public Range(int min, int max) {
    this.min = min;
    this.max = max;
  }

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }

  public boolean withinRange(int val) {
    return this.min <= val && val <= this.max;
  }

  public boolean withinRange(List<Integer> sortedValues) {
    int minFromList = sortedValues.get(0);
    int maxFromList = sortedValues.get(sortedValues.size() - 1);

    return this.min <= minFromList && maxFromList <= this.max;
  }

}
