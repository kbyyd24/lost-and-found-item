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
