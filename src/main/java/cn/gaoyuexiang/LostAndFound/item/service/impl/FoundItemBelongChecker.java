package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.repository.FoundItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.BelongChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;

import static cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason.FOUND_ITEM_NOT_FOUND;

@Service
public class FoundItemBelongChecker implements BelongChecker {

  private FoundItemRepo foundItemRepo;

  @Autowired
  public FoundItemBelongChecker(FoundItemRepo foundItemRepo) {
    this.foundItemRepo = foundItemRepo;
  }

  @Override
  public boolean isBelong(long itemId, String username) {
    FoundItem foundItem = foundItemRepo.findById(itemId);
    if (foundItem == null) {
      throw new NotFoundException(FOUND_ITEM_NOT_FOUND.getReason());
    }
    return foundItem.getOwner().equals(username);
  }
}
