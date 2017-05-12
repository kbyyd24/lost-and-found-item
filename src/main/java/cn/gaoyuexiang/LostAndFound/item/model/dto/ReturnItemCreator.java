package cn.gaoyuexiang.LostAndFound.item.model.dto;

public class ReturnItemCreator {

  private String reason;
  private String contact;

  public ReturnItemCreator() {}

  public ReturnItemCreator(String reason, String contact) {
    this.reason = reason;
    this.contact = contact;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }
}
