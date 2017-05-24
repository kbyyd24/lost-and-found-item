package cn.gaoyuexiang.LostAndFound.item.enums;

import java.util.stream.Stream;

public enum ActionType {
  REJECT("reject"),
  ACCEPT("accept"),
  CANCEL("cancel"),
  UNKNOWN("unknown action");

  private String value;

  ActionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static ActionType getByValue(String queryValue) {
    return Stream.of(values())
        .filter(value -> value.getValue().equals(queryValue))
        .findFirst()
        .orElse(UNKNOWN);
  }

}
