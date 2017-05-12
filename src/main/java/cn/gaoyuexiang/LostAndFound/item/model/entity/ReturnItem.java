package cn.gaoyuexiang.LostAndFound.item.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "return_item")
public class ReturnItem {

  @Id
  private long id;

  @Column(name = "return_user")
  private String returnUser;
  @Column(name = "apply_time")
  private long applyTime;
  @Column(length = 1024)
  private String reason;
  @Column(length = 32)
  private String contact;
  private String state;
  @Column(name = "lost_item_id")
  private long lostItemId;

  public ReturnItem() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReturnItem that = (ReturnItem) o;

    if (getId() != that.getId()) return false;
    if (getApplyTime() != that.getApplyTime()) return false;
    if (getLostItemId() != that.getLostItemId()) return false;
    if (getReturnUser() != null ? !getReturnUser().equals(that.getReturnUser()) : that.getReturnUser() != null)
      return false;
    if (getReason() != null ? !getReason().equals(that.getReason()) : that.getReason() != null) return false;
    if (getContact() != null ? !getContact().equals(that.getContact()) : that.getContact() != null) return false;
    return getState() != null ? getState().equals(that.getState()) : that.getState() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getReturnUser() != null ? getReturnUser().hashCode() : 0);
    result = 31 * result + (int) (getApplyTime() ^ (getApplyTime() >>> 32));
    result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
    result = 31 * result + (getContact() != null ? getContact().hashCode() : 0);
    result = 31 * result + (getState() != null ? getState().hashCode() : 0);
    result = 31 * result + (int) (getLostItemId() ^ (getLostItemId() >>> 32));
    return result;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getReturnUser() {
    return returnUser;
  }

  public void setReturnUser(String returnUser) {
    this.returnUser = returnUser;
  }

  public long getApplyTime() {
    return applyTime;
  }

  public void setApplyTime(long applyTime) {
    this.applyTime = applyTime;
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

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public long getLostItemId() {
    return lostItemId;
  }

  public void setLostItemId(long lostItemId) {
    this.lostItemId = lostItemId;
  }
}
