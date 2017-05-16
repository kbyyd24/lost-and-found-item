package cn.gaoyuexiang.LostAndFound.item.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum ItemSort {
  END_TIME("endTime"),
  BEGIN_TIME("beginTime"),
  CREATE_TIME("createTime"),
  FOUND_TIME("foundTime");

  private String columnName;

  ItemSort(String columnName) {
    this.columnName = columnName;
  }

  public String getColumnName() {
    return columnName;
  }

  public static ItemSort getItemSortByColumnName(String columnName) {
    Optional<ItemSort> itemSort = Stream.of(values())
        .filter(value -> value.getColumnName().equals(columnName))
        .findFirst();
    return itemSort.orElse(CREATE_TIME);
  }
}
