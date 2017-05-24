package cn.gaoyuexiang.LostAndFound.item.model.dto;

public class LostItemCreator {

  private String title;
  private String itemName;
  private long lostTime;
  private String description;

  public LostItemCreator() {}

  public LostItemCreator(String title,
                         String itemName,
                         long lostTime,
                         String description) {
    this.title = title;
    this.itemName = itemName;
    this.lostTime = lostTime;
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LostItemCreator that = (LostItemCreator) o;

    if (getLostTime() != that.getLostTime()) return false;
    if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
    if (getItemName() != null ? !getItemName().equals(that.getItemName()) : that.getItemName() != null) return false;
    return getDescription() != null ? getDescription().equals(that.getDescription()) : that.getDescription() == null;
  }

  @Override
  public int hashCode() {
    int result = getTitle() != null ? getTitle().hashCode() : 0;
    result = 31 * result + (getItemName() != null ? getItemName().hashCode() : 0);
    result = 31 * result + (int) (getLostTime() ^ (getLostTime() >>> 32));
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

}
