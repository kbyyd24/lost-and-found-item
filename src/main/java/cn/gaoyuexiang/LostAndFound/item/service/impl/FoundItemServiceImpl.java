package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.repository.FoundItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class FoundItemServiceImpl implements FoundItemService {

  private FoundItemRepo foundItemRepo;
  private TimeService timeService;
  private IdCreateService idCreateService;

  public FoundItemServiceImpl(FoundItemRepo foundItemRepo,
                              TimeService timeService,
                              IdCreateService idCreateService) {
    this.foundItemRepo = foundItemRepo;
    this.timeService = timeService;
    this.idCreateService = idCreateService;
  }

  @Override
  public FoundItem create(FoundItemCreator creator, String createUser) {
    if (!isComplete(creator)) {
      throw new MissPropertyException();
    }
    return foundItemRepo.save(buildItem(creator, createUser));
  }

  @Override
  public List<FoundItemPageItem> loadPage(int page, int size, ItemSort sort) {
    PageRequest pageRequest = new PageRequest(page, size, DESC, sort.getColumnName());
    List<FoundItem> items = foundItemRepo.findAll(pageRequest).getContent();
    return items.stream()
        .map(this::buildPageItem)
        .collect(toList());
  }

  @Override
  public FoundItem loadOne(long itemId) {
    return null;
  }

  @Override
  public FoundItem close(long itemId) {
    return null;
  }

  @Override
  public FoundItem update(FoundItemCreator updater, long itemId, String updateUser) {
    return null;
  }

  @Override
  public boolean isBelong(long itemId, String updateUser) {
    return false;
  }

  @Override
  public boolean isClosed(long itemId) {
    return false;
  }

  private boolean isComplete(FoundItemCreator creator) {
    return creator != null
        && creator.getTitle() != null
        && creator.getItemName() != null
        && creator.getFoundTime() != 0L
        && creator.getDescription() != null
        && creator.getPictures() != null;
  }

  private FoundItem buildItem(FoundItemCreator creator, String createUser) {
    long latestId = foundItemRepo.findLatestId();
    FoundItem foundItem = new FoundItem();
    foundItem.setId(idCreateService.create(latestId));
    foundItem.setOwner(createUser);
    foundItem.setTitle(creator.getTitle());
    foundItem.setItemName(creator.getItemName());
    foundItem.setCreateTime(timeService.getCurrentTime());
    foundItem.setFoundTime(creator.getFoundTime());
    foundItem.setDescription(creator.getDescription());
    foundItem.setPictures(creator.getPictures());
    foundItem.setState(ItemState.ENABLE.getValue());
    return foundItem;
  }

  private FoundItemPageItem buildPageItem(FoundItem foundItem) {
    FoundItemPageItem pageItem = new FoundItemPageItem();
    pageItem.setId(foundItem.getId());
    pageItem.setTitle(foundItem.getTitle());
    pageItem.setItemName(foundItem.getItemName());
    pageItem.setCreateTime(foundItem.getCreateTime());
    pageItem.setFoundTime(foundItem.getFoundTime());
    List<String> pictures = foundItem.getPictures();
    if (pictures != null && !pictures.isEmpty()) {
      pageItem.setPicture(pictures.get(0));
    }
    return pageItem;
  }
}
