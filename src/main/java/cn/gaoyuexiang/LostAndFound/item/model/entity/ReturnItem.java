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
