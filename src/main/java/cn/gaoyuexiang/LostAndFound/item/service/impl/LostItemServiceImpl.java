package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LostItemServiceImpl implements LostItemService {

  private final LostItemRepo lostItemRepo;
  private final IdCreateService idCreateService;
  private final TimeService timeService;

  @Autowired
  public LostItemServiceImpl(LostItemRepo lostItemRepo,
                             IdCreateService idCreateService,
                             TimeService timeService) {
    this.lostItemRepo = lostItemRepo;
    this.idCreateService = idCreateService;
    this.timeService = timeService;
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
  public List<LostItem> loadPage(int page, int listSize, String sort) {
    return null;
  }

  @Override
  public LostItem loadOne(long itemId) {
    return null;
  }

  @Override
  public LostItem close(long itemId) {
    return null;
  }
}
