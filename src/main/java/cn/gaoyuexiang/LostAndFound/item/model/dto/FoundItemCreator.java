package cn.gaoyuexiang.LostAndFound.item.model.dto;

import java.util.List;

public class FoundItemCreator {

  private String title;
  private String itemName;
  private long foundTime;
  private String description;

  public FoundItemCreator() {
  }

  public FoundItemCreator(String title, String itemName, long foundTime,
                          String description) {
    this.title = title;
    this.itemName = itemName;
    this.foundTime = foundTime;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FoundItemCreator that = (FoundItemCreator) o;

    if (getFoundTime() != that.getFoundTime()) return false;
    if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
    if (getItemName() != null ? !getItemName().equals(that.getItemName()) : that.getItemName() != null) return false;
    return getDescription() != null ? getDescription().equals(that.getDescription()) : that.getDescription() == null;
  }

  @Override
  public int hashCode() {
    int result = getTitle() != null ? getTitle().hashCode() : 0;
    result = 31 * result + (getItemName() != null ? getItemName().hashCode() : 0);
    result = 31 * result + (int) (getFoundTime() ^ (getFoundTime() >>> 32));
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
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

}
