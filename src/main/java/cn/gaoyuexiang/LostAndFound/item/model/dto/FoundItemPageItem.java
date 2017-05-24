package cn.gaoyuexiang.LostAndFound.item.model.dto;

public class FoundItemPageItem {

  private long id;
  private String title;
  private String itemName;
  private long createTime;
  private long foundTime;

  public FoundItemPageItem() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FoundItemPageItem that = (FoundItemPageItem) o;

    if (getId() != that.getId()) return false;
    if (getCreateTime() != that.getCreateTime()) return false;
    if (getFoundTime() != that.getFoundTime()) return false;
    if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
    return getItemName() != null ? getItemName().equals(that.getItemName()) : that.getItemName() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
    result = 31 * result + (getItemName() != null ? getItemName().hashCode() : 0);
    result = 31 * result + (int) (getCreateTime() ^ (getCreateTime() >>> 32));
    result = 31 * result + (int) (getFoundTime() ^ (getFoundTime() >>> 32));
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

  public long getFoundTime() {
    return foundTime;
  }

  public void setFoundTime(long foundTime) {
    this.foundTime = foundTime;
  }

}
