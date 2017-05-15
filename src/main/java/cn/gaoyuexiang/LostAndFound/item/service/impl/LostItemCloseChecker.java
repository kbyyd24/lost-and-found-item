package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.CloseChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.LOST_ITEM_NOT_EXIST;

@Service
public class LostItemCloseChecker implements CloseChecker {

  private LostItemRepo lostItemRepo;

  @Autowired
  public LostItemCloseChecker(LostItemRepo lostItemRepo) {
    this.lostItemRepo = lostItemRepo;
  }

  @Override
  public boolean isClosed(long itemId) {
    LostItem lostItem = lostItemRepo.findById(itemId);
    if (lostItem == null) {
      throw new NotFoundException(LOST_ITEM_NOT_EXIST.getReason());
    }
    return lostItem.getState().equals(ItemState.CLOSED.getValue());
  }
}
