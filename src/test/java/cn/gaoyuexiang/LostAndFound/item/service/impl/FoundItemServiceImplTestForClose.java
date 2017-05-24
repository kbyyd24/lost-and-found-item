package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.exception.CloseItemException;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.repository.FoundItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoundItemServiceImplTestForClose {

  @Autowired
  private FoundItemServiceImpl foundItemService;

  @MockBean
  private FoundItemRepo foundItemRepo;

  @MockBean
  private ClaimItemService claimItemService;

  private long itemId;

  @Before
  public void setUp() throws Exception {
    itemId = 123L;
  }

  @Test
  public void should_return_foundItem_when_close_success() throws Exception {
    FoundItem foundItem = new FoundItem();
    when(claimItemService.hasUnreadItem(itemId)).thenReturn(false);
    when(foundItemRepo.findById(itemId)).thenReturn(foundItem);
    when(foundItemRepo.save(foundItem)).thenReturn(foundItem);
    FoundItem closedItem = foundItemService.close(itemId);
    assertThat(closedItem, is(foundItem));
  }

  @Test
  public void should_return_null_when_item_not_found() throws Exception {
    when(claimItemService.hasUnreadItem(itemId)).thenReturn(false);
    when(foundItemRepo.findById(itemId)).thenReturn(null);
    assertNull(foundItemService.close(itemId));
  }

  @Test(expected = CloseItemException.class)
  public void should_throw_CloseItemException_when_foundItem_has_unread_claimItem() throws Exception {
    when(claimItemService.hasUnreadItem(itemId)).thenReturn(true);
    foundItemService.close(itemId);
  }
}