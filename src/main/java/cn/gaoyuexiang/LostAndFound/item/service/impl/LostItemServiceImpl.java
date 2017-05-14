package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
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
import cn.gaoyuexiang.LostAndFound.item.service.interfaces.BelongChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static cn.gaoyuexiang.LostAndFound.item.enums.ItemState.CLOSED;
import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.LOST_ITEM_NOT_EXIST;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class LostItemServiceImpl implements LostItemService, BelongChecker {

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
    long baseId = lostItemRepo.findLatestId();
    LostItem lostItem = buildItem(lostItemCreator, createUser, baseId);
    return lostItemRepo.save(lostItem);
  }

  private boolean isComplete(LostItemCreator creator) {
    if (creator == null) {
      return false;
    } else if (creator.getTitle() == null) {
      return false;
    } else if (creator.getItemName() == null) {
      return false;
    } else if (creator.getBeginTime() == 0L) {
      return false;
    } else if (creator.getEndTime() == 0L) {
      return false;
    } else if (creator.getDescription() == null) {
      return false;
    } else if (creator.getPictures() == null) {
      return false;
    }
    return true;
  }

  private LostItem buildItem(LostItemCreator creator, String user, long baseId) {
    LostItem lostItem = new LostItem();
    lostItem.setId(idCreateService.create(baseId));
    lostItem.setTitle(creator.getTitle());
    lostItem.setOwner(user);
    lostItem.setItemName(creator.getItemName());
    lostItem.setCreateTime(timeService.getCurrentTime());
    lostItem.setBeginTime(creator.getBeginTime());
    lostItem.setEndTime(creator.getEndTime());
    lostItem.setDescription(creator.getDescription());
    lostItem.setPictures(creator.getPictures());
    return lostItem;
  }

  @Override
  public List<LostItemPageItem> loadPage(int page, int listSize, ItemSort sort) {
    PageRequest pageRequest = new PageRequest(page, listSize, DESC, sort.getColumnName());
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

  @Override
  public boolean isBelong(long itemId, String username) {
    LostItem lostItem = lostItemRepo.findById(itemId);
    if (lostItem == null) {
      throw new NotFoundException(LOST_ITEM_NOT_EXIST.getReason());
    }
    return lostItem.getOwner().equals(username);
  }

  @Override
  public boolean isClosed(long itemId) {
    LostItem lostItem = lostItemRepo.findById(itemId);
    if (lostItem == null) {
      throw new NotFoundException(LOST_ITEM_NOT_EXIST.getReason());
    }
    return lostItem.getState().equals(ItemState.CLOSED.getValue());
  }

  private void updateExistItem(LostItemCreator updater, LostItem existItem) {
    if (needUpdate(updater.getTitle(), existItem.getTitle())) {
      existItem.setTitle(updater.getTitle());
    }
    if (needUpdate(updater.getItemName(), existItem.getItemName())) {
      existItem.setItemName(updater.getItemName());
    }
    if (needUpdate(updater.getBeginTime(), existItem.getBeginTime())) {
      existItem.setBeginTime(updater.getBeginTime());
    }
    if (needUpdate(updater.getEndTime(), existItem.getEndTime())) {
      existItem.setEndTime(updater.getEndTime());
    }
    if (needUpdate(updater.getDescription(), existItem.getDescription())) {
      existItem.setDescription(updater.getDescription());
    }
    if (needUpdate(updater.getPictures(), existItem.getPictures())) {
      existItem.setPictures(updater.getPictures());
    }
  }

  private <T> boolean needUpdate(T newProp, T oldProp) {
    return newProp != null && !newProp.equals(0L) && !newProp.equals(oldProp);
  }

}
