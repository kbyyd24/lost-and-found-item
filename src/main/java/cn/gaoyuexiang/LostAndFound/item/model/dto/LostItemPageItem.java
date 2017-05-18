package cn.gaoyuexiang.LostAndFound.item.model.dto;

import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;

import java.util.List;

public class LostItemPageItem {

  private long id;
  private String title;
  private String itemName;
  private long createTime;
  private long beginTime;
  private long endTime;

  public LostItemPageItem() {}

  public LostItemPageItem(LostItem lostItem) {
    this.id = lostItem.getId();
    this.title = lostItem.getTitle();
    this.itemName = lostItem.getItemName();
    this.createTime = lostItem.getCreateTime();
    this.beginTime = lostItem.getBeginTime();
    this.endTime = lostItem.getEndTime();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LostItemPageItem that = (LostItemPageItem) o;

    if (getId() != that.getId()) return false;
    if (getCreateTime() != that.getCreateTime()) return false;
    if (getBeginTime() != that.getBeginTime()) return false;
    if (getEndTime() != that.getEndTime()) return false;
    if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
    return getItemName() != null ? getItemName().equals(that.getItemName()) : that.getItemName() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
    result = 31 * result + (getItemName() != null ? getItemName().hashCode() : 0);
    result = 31 * result + (int) (getCreateTime() ^ (getCreateTime() >>> 32));
    result = 31 * result + (int) (getBeginTime() ^ (getBeginTime() >>> 32));
    result = 31 * result + (int) (getEndTime() ^ (getEndTime() >>> 32));
    return result;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public long getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(long beginTime) {
    this.beginTime = beginTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

}
