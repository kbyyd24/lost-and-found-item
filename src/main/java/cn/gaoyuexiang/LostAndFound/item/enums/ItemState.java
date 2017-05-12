package cn.gaoyuexiang.LostAndFound.item.enums;

public enum ItemState {
  CLOSED("closed"),
  ENABLE("enable"),
  FOUND("found"),
  DISABLE("disable"),
  UNREAD("unread"),
  CANCELED("canceled"),
  REJECTED("rejected"),
  ACCEPTED("accepted");

  private String value;

  ItemState(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
