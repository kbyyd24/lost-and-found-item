package cn.gaoyuexiang.LostAndFound.item.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum ItemSort {
  END_TIME("end_time"),
  BEGIN_TIME("begin_time"),
  CREATE_TIME("create_time"),
  FOUND_TIME("found_time");

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
