package cn.gaoyuexiang.LostAndFound.item.enums;

public enum ItemSort {
  END_TIME("end_time"),
  BEGIN_TIME("begin_time"),
  CREATE_TIME("create_time");

  private String columnName;

  ItemSort(String columnName) {
    this.columnName = columnName;
  }

  public String getColumnName() {
    return columnName;
  }
}
