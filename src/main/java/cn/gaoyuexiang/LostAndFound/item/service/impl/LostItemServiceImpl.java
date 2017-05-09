package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LostItemServiceImpl implements LostItemService {

  private final LostItemRepo lostItemRepo;
  private final IdCreateService idCreateService;
  private final TimeService timeService;
  private Map<ItemSort, String> sortPropMap;

  @Autowired
  public LostItemServiceImpl(LostItemRepo lostItemRepo,
                             IdCreateService idCreateService,
                             TimeService timeService) {
    this.lostItemRepo = lostItemRepo;
    this.idCreateService = idCreateService;
    this.timeService = timeService;
    sortPropMap = new HashMap<>();
    sortPropMap.put(ItemSort.CREATE_TIME, "create_time");
    sortPropMap.put(ItemSort.END_TIME, "end_time");
    sortPropMap.put(ItemSort.BEGIN_TIME, "begin_time");
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
    PageRequest pageRequest = new PageRequest(page, listSize);
    Sort orders = new Sort(Sort.Direction.DESC, this.sortPropMap.get(sort));
    List<LostItem> lostItems = lostItemRepo.findAll(orders, pageRequest);
    return lostItems.stream()
        .map(LostItemPageItem::new)
        .collect(Collectors.toList());
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
