package cn.gaoyuexiang.LostAndFound.item.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "foundItem")
public class FoundItem {

  @Id
  private long id;

  private String title;
  private String owner;
  private String itemName;
  private long createTime;
  private long foundTime;
  private String description;
  private String state;

  public FoundItem() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FoundItem foundItem = (FoundItem) o;

    if (getId() != foundItem.getId()) return false;
    if (getCreateTime() != foundItem.getCreateTime()) return false;
    if (getFoundTime() != foundItem.getFoundTime()) return false;
    if (getTitle() != null ? !getTitle().equals(foundItem.getTitle()) : foundItem.getTitle() != null) return false;
    if (getOwner() != null ? !getOwner().equals(foundItem.getOwner()) : foundItem.getOwner() != null) return false;
    if (getItemName() != null ? !getItemName().equals(foundItem.getItemName()) : foundItem.getItemName() != null)
      return false;
    if (getDescription() != null ? !getDescription().equals(foundItem.getDescription()) : foundItem.getDescription() != null)
      return false;
    return getState() != null ? getState().equals(foundItem.getState()) : foundItem.getState() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
    result = 31 * result + (getOwner() != null ? getOwner().hashCode() : 0);
    result = 31 * result + (getItemName() != null ? getItemName().hashCode() : 0);
    result = 31 * result + (int) (getCreateTime() ^ (getCreateTime() >>> 32));
    result = 31 * result + (int) (getFoundTime() ^ (getFoundTime() >>> 32));
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

}
