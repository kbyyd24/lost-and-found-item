package cn.gaoyuexiang.LostAndFound.item.model.dto;

public class ReturnItemCreator {

  private String reason;
  private String contact;

  public ReturnItemCreator() {}

  public ReturnItemCreator(String reason, String contact) {
    this.reason = reason;
    this.contact = contact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReturnItemCreator that = (ReturnItemCreator) o;

    if (getReason() != null ? !getReason().equals(that.getReason()) : that.getReason() != null) return false;
    return getContact() != null ? getContact().equals(that.getContact()) : that.getContact() == null;
  }

  @Override
  public int hashCode() {
    int result = getReason() != null ? getReason().hashCode() : 0;
    result = 31 * result + (getContact() != null ? getContact().hashCode() : 0);
    return result;
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
