package cn.gaoyuexiang.LostAndFound.item.model.dto;

public class ClaimItemPageItem {

  private String claimUser;
  private long applyTime;
  private String state;

  public ClaimItemPageItem() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClaimItemPageItem that = (ClaimItemPageItem) o;

    if (getApplyTime() != that.getApplyTime()) return false;
    if (getClaimUser() != null ? !getClaimUser().equals(that.getClaimUser()) : that.getClaimUser() != null)
      return false;
    return getState() != null ? getState().equals(that.getState()) : that.getState() == null;
  }

  @Override
  public int hashCode() {
    int result = getClaimUser() != null ? getClaimUser().hashCode() : 0;
    result = 31 * result + (int) (getApplyTime() ^ (getApplyTime() >>> 32));
    result = 31 * result + (getState() != null ? getState().hashCode() : 0);
    return result;
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

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
