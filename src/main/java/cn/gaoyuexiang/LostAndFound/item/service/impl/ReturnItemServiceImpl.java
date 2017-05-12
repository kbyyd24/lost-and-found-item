package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ReturnItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class ReturnItemServiceImpl implements ReturnItemService {

  private ReturnItemRepo returnItemRepo;
  private IdCreateService idCreateService;
  private TimeService timeService;

  @Autowired
  public ReturnItemServiceImpl(ReturnItemRepo returnItemRepo,
                               IdCreateService idCreateService,
                               TimeService timeService) {
    this.returnItemRepo = returnItemRepo;
    this.idCreateService = idCreateService;
    this.timeService = timeService;
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

  @Override
  public ReturnItem create(String username, long lostItemId, ReturnItemCreator creator) {
    checkCompletion(creator);
    ReturnItem existItem = returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId);
    if (existItem == null) {
      existItem = buildNewItem(username, lostItemId, creator);
    } else if (existItem.getState().equals(ItemState.CLOSED.getValue())) {
      throw new UpdateItemException("item closed");
    } else {
      updateItem(creator, existItem);
    }
    return returnItemRepo.save(existItem);
  }

  private void updateItem(ReturnItemCreator creator, ReturnItem existItem) {
    existItem.setReason(creator.getReason());
    existItem.setContact(creator.getContact());
  }

  private ReturnItem buildNewItem(String username, long lostItemId, ReturnItemCreator creator) {
    ReturnItem existItem;
    long latestId = returnItemRepo.findLatestId();
    existItem = new ReturnItem();
    existItem.setId(idCreateService.create(latestId));
    existItem.setReturnUser(username);
    existItem.setApplyTime(timeService.getCurrentTime());
    existItem.setReason(creator.getReason());
    existItem.setContact(creator.getContact());
    existItem.setState(ItemState.UNREAD.getValue());
    existItem.setLostItemId(lostItemId);
    return existItem;
  }

  private void checkCompletion(ReturnItemCreator creator) {
    if (creator == null || creator.getContact() == null || creator.getReason() == null) {
      throw new MissPropertyException();
    }
  }
}
