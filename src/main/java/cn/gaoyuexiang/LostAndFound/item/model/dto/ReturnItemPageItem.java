package cn.gaoyuexiang.LostAndFound.item.model.dto;

import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;

public class ReturnItemPageItem {

  private String returnUser;
  private long applyTime;
  private String state;

  public ReturnItemPageItem() {}

  public ReturnItemPageItem(ReturnItem returnItem) {
    this.returnUser = returnItem.getReturnUser();
    this.applyTime = returnItem.getCreateTime();
    this.state = returnItem.getState();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReturnItemPageItem that = (ReturnItemPageItem) o;

    if (getApplyTime() != that.getApplyTime()) return false;
    if (getReturnUser() != null ? !getReturnUser().equals(that.getReturnUser()) : that.getReturnUser() != null)
      return false;
    return getState() != null ? getState().equals(that.getState()) : that.getState() == null;
  }

  @Override
  public int hashCode() {
    int result = getReturnUser() != null ? getReturnUser().hashCode() : 0;
    result = 31 * result + (int) (getApplyTime() ^ (getApplyTime() >>> 32));
    result = 31 * result + (getState() != null ? getState().hashCode() : 0);
    return result;
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

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
