package cn.gaoyuexiang.LostAndFound.item.enums;

public enum NotFoundReason {
  PAGE_OUT_OF_BOUND("page out of bound"),
  LOST_ITEM_NOT_EXIST("can not found lost item");
  private String reason;

  NotFoundReason(String reason) {
    this.reason = reason;
  }

  public String getReason() {
    return reason;
  }
}
