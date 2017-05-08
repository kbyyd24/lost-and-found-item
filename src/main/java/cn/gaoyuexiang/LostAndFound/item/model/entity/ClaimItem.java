package cn.gaoyuexiang.LostAndFound.item.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "claim_item")
public class ClaimItem {

  @Id
  private long id;

  @Column(name = "claim_user")
  private String claimUser;
  @Column(name = "apply_time")
  private long applyTime;
  @Column(length = 1024)
  private String reason;
  @Column(length = 1024)
  private String contact;
  private String state;
  @Column(name = "found_item_id")
  private long foundItemId;

  public ClaimItem() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getClaimUser() {
    return claimUser;
  }

  public void setClaimUser(String claimUser) {
    this.claimUser = claimUser;
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

  public long getFoundItemId() {
    return foundItemId;
  }

  public void setFoundItemId(long foundItemId) {
    this.foundItemId = foundItemId;
  }
}
