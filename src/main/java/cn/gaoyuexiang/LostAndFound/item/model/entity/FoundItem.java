package cn.gaoyuexiang.LostAndFound.item.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "found_item")
public class FoundItem {

  @Id
  private long id;

  private String title;
  private String owner;
  private String itemName;
  @Column(name = "create_time")
  private long createTime;
  @Column(name = "found_time")
  private long foundTime;
  private String description;
  private String state;

  @ElementCollection
  @CollectionTable(name = "found_picture")
  private List<String> pictures;

  public FoundItem() {}

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

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
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

  public long getFoundTime() {
    return foundTime;
  }

  public void setFoundTime(long foundTime) {
    this.foundTime = foundTime;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public List<String> getPictures() {
    return pictures;
  }

  public void setPictures(List<String> pictures) {
    this.pictures = pictures;
  }
}
