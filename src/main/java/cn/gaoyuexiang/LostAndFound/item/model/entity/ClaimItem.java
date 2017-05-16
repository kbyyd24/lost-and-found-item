package cn.gaoyuexiang.LostAndFound.item.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "claimItem")
public class ClaimItem {

  @Id
  private long id;

  private String claimUser;
  private long applyTime;
  @Column(length = 1024)
  private String reason;
  @Column(length = 1024)
  private String contact;
  private String state;
  private long foundItemId;

  public ClaimItem() {}

  public ClaimItem(long id, String claimUser, long applyTime, String reason,
                   String contact, String state, long foundItemId) {
    this.id = id;
    this.claimUser = claimUser;
    this.applyTime = applyTime;
    this.reason = reason;
    this.contact = contact;
    this.state = state;
    this.foundItemId = foundItemId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClaimItem claimItem = (ClaimItem) o;

    if (getId() != claimItem.getId()) return false;
    if (getApplyTime() != claimItem.getApplyTime()) return false;
    if (getFoundItemId() != claimItem.getFoundItemId()) return false;
    if (getClaimUser() != null ? !getClaimUser().equals(claimItem.getClaimUser()) : claimItem.getClaimUser() != null)
      return false;
    if (getReason() != null ? !getReason().equals(claimItem.getReason()) : claimItem.getReason() != null) return false;
    if (getContact() != null ? !getContact().equals(claimItem.getContact()) : claimItem.getContact() != null)
      return false;
    return getState() != null ? getState().equals(claimItem.getState()) : claimItem.getState() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getClaimUser() != null ? getClaimUser().hashCode() : 0);
    result = 31 * result + (int) (getApplyTime() ^ (getApplyTime() >>> 32));
    result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
    result = 31 * result + (getContact() != null ? getContact().hashCode() : 0);
    result = 31 * result + (getState() != null ? getState().hashCode() : 0);
    result = 31 * result + (int) (getFoundItemId() ^ (getFoundItemId() >>> 32));
    return result;
  }

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
