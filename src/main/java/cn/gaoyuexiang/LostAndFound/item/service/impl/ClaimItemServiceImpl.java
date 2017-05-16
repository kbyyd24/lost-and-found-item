package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ClaimItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.CLAIM_ITEM_NOT_FOUND;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class ClaimItemServiceImpl implements ClaimItemService {

  private ClaimItemRepo claimItemRepo;
  private IdCreateService idCreateService;
  private TimeService timeService;

  private Map<ActionType, ItemState> actionStateMap;

  @Autowired
  public ClaimItemServiceImpl(ClaimItemRepo claimItemRepo,
                              IdCreateService idCreateService,
                              TimeService timeService) {
    this.claimItemRepo = claimItemRepo;
    this.idCreateService = idCreateService;
    this.timeService = timeService;
    buildActionStateMap();
  }

  private void buildActionStateMap() {
    this.actionStateMap = new HashMap<>(4);
    actionStateMap.put(ActionType.ACCEPT, ItemState.ACCEPTED);
    actionStateMap.put(ActionType.CANCEL, ItemState.CANCELED);
    actionStateMap.put(ActionType.REJECT, ItemState.REJECTED);
  }

  @Override
  public boolean hasUnreadItem(long foundItemId) {
    return claimItemRepo.findByFoundItemIdAndState(foundItemId, ItemState.UNREAD.getValue()) != null;
  }

  @Override
  public List<ClaimItemPageItem> loadPage(long foundItemId, int page, int size, ItemSort sort) {
    PageRequest pageRequest = new PageRequest(page - 1, size, DESC, sort.getColumnName());
    List<ClaimItem> claimItems =
        claimItemRepo.findAllByFoundItemId(foundItemId, pageRequest).getContent();
    return claimItems.stream()
        .map(this::buildClaimItemPageItem)
        .collect(toList());
  }

  @Override
  public ClaimItem loadOne(long foundItemId, String claimUser) {
    return claimItemRepo.findByClaimUserAndFoundItemId(claimUser, foundItemId);
  }

  @Override
  public ClaimItem create(long foundItemId, String createUser, ClaimItemCreator creator) {
    checkCompletion(creator);
    ClaimItem existItem = claimItemRepo.findByClaimUserAndFoundItemId(createUser, foundItemId);
    if (existItem == null) {
      existItem = buildClaimItem(foundItemId, createUser, creator);
    } else if (existItem.getState().equals(ItemState.ACCEPTED.getValue())) {
      throw new UpdateItemException("item accepted");
    } else {
      updateClaimItem(creator, existItem);
    }
    return claimItemRepo.save(existItem);
  }

  @Override
  public ClaimItem delete(long foundItemId, String claimUser, ActionType action) {
    ClaimItem claimItem = claimItemRepo.findByClaimUserAndFoundItemId(claimUser, foundItemId);
    if (claimItem == null) {
      throw new NotFoundException(CLAIM_ITEM_NOT_FOUND.getReason());
    }
    if (!claimItem.getState().equals(ItemState.UNREAD.getValue())) {
      throw new UpdateItemException("item state is " + claimItem.getState());
    }
    claimItem.setState(actionStateMap.get(action).getValue());
    return claimItemRepo.save(claimItem);
  }

  private void checkCompletion(ClaimItemCreator creator) {
    if (creator == null || creator.getContact() == null || creator.getReason() == null) {
      throw new MissPropertyException();
    }
  }

  private void updateClaimItem(ClaimItemCreator creator, ClaimItem existItem) {
    existItem.setContact(creator.getContact());
    existItem.setReason(creator.getReason());
    existItem.setState(ItemState.UNREAD.getValue());
  }

  private ClaimItem buildClaimItem(long foundItemId, String createUser, ClaimItemCreator creator) {
    Long latestId = findLatestId();
    long newId = idCreateService.create(latestId);
    long createTime = timeService.getCurrentTime();
    return new ClaimItem(newId, createUser, createTime, creator.getReason(),
        creator.getContact(), ItemState.UNREAD.getValue(), foundItemId);
  }

  private Long findLatestId() {
    List<Long> ids = claimItemRepo.findLatestId();
    return ids.get(0);
  }

  private ClaimItemPageItem buildClaimItemPageItem(ClaimItem claimItem) {
    ClaimItemPageItem pageItem = new ClaimItemPageItem();
    pageItem.setClaimUser(claimItem.getClaimUser());
    pageItem.setApplyTime(claimItem.getApplyTime());
    pageItem.setState(claimItem.getState());
    return pageItem;
  }
}
