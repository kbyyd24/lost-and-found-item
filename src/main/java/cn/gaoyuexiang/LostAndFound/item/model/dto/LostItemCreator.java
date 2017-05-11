package cn.gaoyuexiang.LostAndFound.item.model.dto;

import java.util.List;

public class LostItemCreator {

  private String title;
  private String itemName;
  private long beginTime;
  private long endTime;
  private String description;
  private List<String> pictures;

  public LostItemCreator() {}

  public LostItemCreator(String title,
                         String itemName,
                         long beginTime,
                         long endTime,
                         String description,
                         List<String> pictures) {
    this.title = title;
    this.itemName = itemName;
    this.beginTime = beginTime;
    this.endTime = endTime;
    this.description = description;
    this.pictures = pictures;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LostItemCreator that = (LostItemCreator) o;

    if (getBeginTime() != that.getBeginTime()) return false;
    if (getEndTime() != that.getEndTime()) return false;
    if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
    if (getItemName() != null ? !getItemName().equals(that.getItemName()) : that.getItemName() != null) return false;
    if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
      return false;
    return getPictures() != null ? getPictures().equals(that.getPictures()) : that.getPictures() == null;
  }

  @Override
  public int hashCode() {
    int result = getTitle() != null ? getTitle().hashCode() : 0;
    result = 31 * result + (getItemName() != null ? getItemName().hashCode() : 0);
    result = 31 * result + (int) (getBeginTime() ^ (getBeginTime() >>> 32));
    result = 31 * result + (int) (getEndTime() ^ (getEndTime() >>> 32));
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + (getPictures() != null ? getPictures().hashCode() : 0);
    return result;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getPictures() {
    return pictures;
  }

  public void setPictures(List<String> pictures) {
    this.pictures = pictures;
  }
}
