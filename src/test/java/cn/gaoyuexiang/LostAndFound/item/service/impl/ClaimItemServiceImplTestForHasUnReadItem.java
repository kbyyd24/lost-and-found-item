package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ClaimItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClaimItemServiceImplTestForHasUnReadItem {

  @Autowired
  private ClaimItemService claimItemService;

  @MockBean
  private ClaimItemRepo claimItemRepo;

  private long foundItemId;
  private String state;

  @Before
  public void setUp() throws Exception {
    foundItemId = 123L;
    state = "unread";
  }

  @Test
  public void should_return_true_when_foundItem_has_unread_item() throws Exception {
    ClaimItem claimItem = new ClaimItem();
    when(claimItemRepo.findByFoundItemIdAndState(eq(foundItemId), eq(state))).thenReturn(claimItem);
    assertTrue(claimItemService.hasUnreadItem(foundItemId));
  }

  @Test
  public void should_return_false_when_foundItem_not_has_unread_item() throws Exception {
    when(claimItemRepo.findByFoundItemIdAndState(eq(foundItemId), eq(state))).thenReturn(null);
    assertFalse(claimItemService.hasUnreadItem(foundItemId));
  }
}