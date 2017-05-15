package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.CloseItemException;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.repository.FoundItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import cn.gaoyuexiang.LostAndFound.item.service.BelongChecker;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.FOUND_ITEM_NOT_FOUND;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class FoundItemServiceImpl implements FoundItemService, BelongChecker {

  private FoundItemRepo foundItemRepo;
  private TimeService timeService;
  private IdCreateService idCreateService;
  private ClaimItemService claimItemService;

  public FoundItemServiceImpl(FoundItemRepo foundItemRepo,
                              TimeService timeService,
                              IdCreateService idCreateService,
                              ClaimItemService claimItemService) {
    this.foundItemRepo = foundItemRepo;
    this.timeService = timeService;
    this.idCreateService = idCreateService;
    this.claimItemService = claimItemService;
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
    return foundItemRepo.findById(itemId);
  }

  @Override
  public FoundItem close(long itemId) {
    if (claimItemService.hasUnreadItem(itemId)) {
      throw new CloseItemException("has unread claim item");
    }
    FoundItem foundItem = foundItemRepo.findById(itemId);
    return foundItem == null ? null : foundItemRepo.save(foundItem);
  }

  @Override
  public FoundItem update(FoundItemCreator updater, long itemId, String updateUser) {
    FoundItem existItem = foundItemRepo.findByIdAndOwner(itemId, updateUser);
    if (existItem == null) {
      return null;
    }
    updateItem(updater, existItem);
    return foundItemRepo.save(existItem);
  }

  @Override
  @Deprecated
  public boolean isBelong(long itemId, String username) {
    FoundItem foundItem = foundItemRepo.findById(itemId);
    if (foundItem == null) {
      throw new NotFoundException(FOUND_ITEM_NOT_FOUND.getReason());
    }
    return foundItem.getOwner().equals(username);
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

  private <T> boolean needUpdate(T newProp, T oldProp) {
    return newProp != null && !newProp.equals(0L) && !newProp.equals(oldProp);
  }

  private void updateItem(FoundItemCreator updater, FoundItem existItem) {
    if (needUpdate(updater.getTitle(), existItem.getTitle())) {
      existItem.setTitle(updater.getTitle());
    }
    if (needUpdate(updater.getItemName(), existItem.getItemName())) {
      existItem.setItemName(updater.getItemName());
    }
    if (needUpdate(updater.getFoundTime(), existItem.getFoundTime())) {
      existItem.setFoundTime(updater.getFoundTime());
    }
    if (needUpdate(updater.getDescription(), existItem.getDescription())) {
      existItem.setDescription(updater.getDescription());
    }
    if (needUpdate(updater.getPictures(), existItem.getPictures())) {
      existItem.setPictures(updater.getPictures());
    }
  }
}
