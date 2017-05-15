package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ClaimItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class ClaimItemServiceImpl implements ClaimItemService {

  private ClaimItemRepo claimItemRepo;

  @Autowired
  public ClaimItemServiceImpl(ClaimItemRepo claimItemRepo) {
    this.claimItemRepo = claimItemRepo;
  }

  @Override
  public boolean hasUnreadItem(long foundItemId) {
    return claimItemRepo.findByFoundItemIdAndState(foundItemId, ItemState.UNREAD.getValue()) != null;
  }

  @Override
  public List<ClaimItemPageItem> loadPage(long foundItemId, int page, int size, ItemSort sort) {
    PageRequest pageRequest = new PageRequest(page, size, DESC, sort.getColumnName());
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
    return null;
  }

  @Override
  public ClaimItem delete(long foundItemId, String claimUser, ActionType action) {
    return null;
  }

  private ClaimItemPageItem buildClaimItemPageItem(ClaimItem claimItem) {
    ClaimItemPageItem pageItem = new ClaimItemPageItem();
    pageItem.setClaimUser(claimItem.getClaimUser());
    pageItem.setApplyTime(claimItem.getApplyTime());
    pageItem.setState(claimItem.getState());
    return pageItem;
  }
}
