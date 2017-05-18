package cn.gaoyuexiang.LostAndFound.item.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "lostItem")
public class LostItem {

  @Id
  private long id;

  private String title;
  private String owner;
  private String itemName;
  private long createTime;
  private long lostTime;
  @Column(length = 1024)
  private String description;
  private String state;

  public LostItem() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LostItem lostItem = (LostItem) o;

    if (getId() != lostItem.getId()) return false;
    if (getCreateTime() != lostItem.getCreateTime()) return false;
    if (getLostTime() != lostItem.getLostTime()) return false;
    if (getTitle() != null ? !getTitle().equals(lostItem.getTitle()) : lostItem.getTitle() != null) return false;
    if (getOwner() != null ? !getOwner().equals(lostItem.getOwner()) : lostItem.getOwner() != null) return false;
    if (getItemName() != null ? !getItemName().equals(lostItem.getItemName()) : lostItem.getItemName() != null)
      return false;
    if (getDescription() != null ? !getDescription().equals(lostItem.getDescription()) : lostItem.getDescription() != null)
      return false;
    return getState() != null ? getState().equals(lostItem.getState()) : lostItem.getState() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
    result = 31 * result + (getOwner() != null ? getOwner().hashCode() : 0);
    result = 31 * result + (getItemName() != null ? getItemName().hashCode() : 0);
    result = 31 * result + (int) (getCreateTime() ^ (getCreateTime() >>> 32));
    result = 31 * result + (int) (getLostTime() ^ (getLostTime() >>> 32));
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + (getState() != null ? getState().hashCode() : 0);
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

  public long getLostTime() {
    return lostTime;
  }

  public void setLostTime(long lostTime) {
    this.lostTime = lostTime;
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

}
