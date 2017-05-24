package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.BelongChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.LOST_ITEM_NOT_EXIST;

@Service
public class LostItemBelongChecker implements BelongChecker {

  private LostItemRepo lostItemRepo;

  @Autowired
  public LostItemBelongChecker(LostItemRepo lostItemRepo) {
    this.lostItemRepo = lostItemRepo;
  }

  @Override
  public boolean isBelong(long itemId, String username) {
    LostItem lostItem = lostItemRepo.findById(itemId);
    if (lostItem == null) {
      throw new NotFoundException(LOST_ITEM_NOT_EXIST.getReason());
    }
    return lostItem.getOwner().equals(username);
  }
}
