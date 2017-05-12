package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ReturnItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class ReturnItemServiceImpl implements ReturnItemService {

  private ReturnItemRepo returnItemRepo;
private Map<ItemSort, String> sortMap;

  @Autowired
  public ReturnItemServiceImpl(ReturnItemRepo returnItemRepo) {
    this.returnItemRepo = returnItemRepo;
    this.sortMap = new HashMap<>(4);
    sortMap.put(ItemSort.CREATE_TIME, "create_time");
    sortMap.put(ItemSort.BEGIN_TIME, "begin_time");
    sortMap.put(ItemSort.END_TIME, "end_time");
  }

  @Override
  public boolean hasUnreadItem(long lostItemId) {
    return false;
  }

  @Override
  public List<ReturnItemPageItem> getReturnItemPageItems(long itemId,
                                                         int page,
                                                         int listSize,
                                                         ItemSort sort) {
    PageRequest pageRequest = new PageRequest(page, listSize, DESC, sortMap.get(sort));
    List<ReturnItem> returnItems = returnItemRepo.findAllByLostItemId(itemId, pageRequest);
    return returnItems.stream()
        .map(ReturnItemPageItem::new)
        .collect(toList());
  }
}
