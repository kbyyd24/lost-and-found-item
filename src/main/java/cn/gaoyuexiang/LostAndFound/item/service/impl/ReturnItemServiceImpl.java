package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ReturnItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class ReturnItemServiceImpl implements ReturnItemService {

  private ReturnItemRepo returnItemRepo;

  @Autowired
  public ReturnItemServiceImpl(ReturnItemRepo returnItemRepo) {
    this.returnItemRepo = returnItemRepo;
  }

  @Override
  public boolean hasUnreadItem(long lostItemId) {
    String state = ItemState.UNREAD.getValue();
    ReturnItem returnItem = returnItemRepo.findByLostItemIdAndState(lostItemId, state);
    return returnItem != null;
  }

  @Override
  public List<ReturnItemPageItem> getReturnItemPageItems(long itemId,
                                                         int page,
                                                         int listSize,
                                                         ItemSort sort) {
    PageRequest pageRequest = new PageRequest(page, listSize, DESC, sort.getColumnName());
    List<ReturnItem> returnItems = returnItemRepo.findAllByLostItemId(itemId, pageRequest);
    return returnItems.stream()
        .map(ReturnItemPageItem::new)
        .collect(toList());
  }

  @Override
  public ReturnItem getReturnItem(String username, long lostItemId) {
    return returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId);
  }
}
