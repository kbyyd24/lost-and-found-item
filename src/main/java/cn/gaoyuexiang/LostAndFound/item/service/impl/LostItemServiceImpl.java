package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.CloseItemException;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.gaoyuexiang.LostAndFound.item.enums.ItemState.CLOSED;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class LostItemServiceImpl implements LostItemService {

  private final LostItemRepo lostItemRepo;
  private final IdCreateService idCreateService;
  private final TimeService timeService;
  private ReturnItemService returnItemService;

  @Autowired
  public LostItemServiceImpl(LostItemRepo lostItemRepo,
                             IdCreateService idCreateService,
                             TimeService timeService,
                             ReturnItemService returnItemService) {
    this.lostItemRepo = lostItemRepo;
    this.idCreateService = idCreateService;
    this.timeService = timeService;
    this.returnItemService = returnItemService;
  }

  @Override
  public LostItem create(LostItemCreator lostItemCreator, String createUser) {
    if (!isComplete(lostItemCreator)) {
      throw new MissPropertyException();
    }
    PageRequest firstResult = new PageRequest(0, 1);
    Long baseId = findLatestId(firstResult);
    LostItem lostItem = buildItem(lostItemCreator, createUser, baseId);
    return lostItemRepo.save(lostItem);
  }

  private Long findLatestId(PageRequest firstResult) {
    List<Long> ids = lostItemRepo.findLatestId(firstResult);
    return ids.size() == 0 ? 0L : ids.get(0);
  }

  private boolean isComplete(LostItemCreator creator) {
    if (creator == null) {
      return false;
    } else if (creator.getTitle() == null) {
      return false;
    } else if (creator.getItemName() == null) {
      return false;
    } else if (creator.getLostTime() == 0L) {
      return false;
    } else if (creator.getDescription() == null) {
      return false;
    }
    return true;
  }

  private LostItem buildItem(LostItemCreator creator, String user, Long baseId) {
    LostItem lostItem = new LostItem();
    lostItem.setId(idCreateService.create(baseId));
    lostItem.setTitle(creator.getTitle());
    lostItem.setOwner(user);
    lostItem.setItemName(creator.getItemName());
    lostItem.setCreateTime(timeService.getCurrentTime());
    lostItem.setLostTime(creator.getLostTime());
    lostItem.setDescription(creator.getDescription());
    lostItem.setState(ItemState.ENABLE.getValue());
    return lostItem;
  }

  @Override
  public List<LostItemPageItem> loadPage(int page, int listSize, ItemSort sort) {
    PageRequest pageRequest = new PageRequest(page - 1, listSize, DESC, sort.getColumnName());
    List<LostItem> lostItems = lostItemRepo.findAll(pageRequest).getContent();
    return lostItems.stream()
        .map(LostItemPageItem::new)
        .collect(Collectors.toList());
  }

  @Override
  public LostItem loadOne(long itemId) {
    return lostItemRepo.findById(itemId);
  }

  @Override
  public LostItem close(long itemId) {
    LostItem lostItem = lostItemRepo.findById(itemId);
    if (closeCheck(itemId, lostItem)) {
      return null;
    }
    lostItem.setState(CLOSED.getValue());
    return lostItemRepo.save(lostItem);
  }

  private boolean closeCheck(long itemId, LostItem lostItem) {
    if (lostItem == null) {
      return true;
    }
    if (lostItem.getState().equals(CLOSED.getValue())) {
      throw new CloseItemException("item closed");
    }
    if (returnItemService.hasUnreadItem(itemId)) {
      throw new CloseItemException("has unread item");
    }
    return false;
  }

  @Override
  public LostItem update(LostItemCreator updater, long itemId, String updateUser) {
    LostItem existItem = lostItemRepo.findById(itemId);
    if (existItem == null) {
      return null;
    }
    if (!existItem.getOwner().equals(updateUser)) {
      throw new UnauthorizedException("user is not owner of item");
    }
    updateExistItem(updater, existItem);
    return lostItemRepo.save(existItem);
  }

  private void updateExistItem(LostItemCreator updater, LostItem existItem) {
    if (needUpdate(updater.getTitle(), existItem.getTitle())) {
      existItem.setTitle(updater.getTitle());
    }
    if (needUpdate(updater.getItemName(), existItem.getItemName())) {
      existItem.setItemName(updater.getItemName());
    }
    if (needUpdate(updater.getLostTime(), existItem.getLostTime())) {
      existItem.setLostTime(updater.getLostTime());
    }
    if (needUpdate(updater.getDescription(), existItem.getDescription())) {
      existItem.setDescription(updater.getDescription());
    }
  }

  private <T> boolean needUpdate(T newProp, T oldProp) {
    return newProp != null && !newProp.equals(0L) && !newProp.equals(oldProp);
  }

}
