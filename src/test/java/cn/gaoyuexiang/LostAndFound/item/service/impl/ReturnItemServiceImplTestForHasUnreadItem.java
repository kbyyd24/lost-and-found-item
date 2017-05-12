package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ReturnItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
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
public class ReturnItemServiceImplTestForHasUnreadItem {

  @Autowired
  private ReturnItemService returnItemService;

  @MockBean
  private ReturnItemRepo returnItemRepo;
  private long lostItemId;
  private String state;

  @Before
  public void setUp() throws Exception {
    lostItemId = 123L;
    state = "unread";
  }

  @Test
  public void should_return_true_when_has_unread_return_item() throws Exception {
    ReturnItem returnItem = new ReturnItem();
    returnItem.setState(state);
    when(returnItemRepo.findByLostItemIdAndState(eq(lostItemId), eq(state)))
        .thenReturn(returnItem);
    boolean hasOne = returnItemService.hasUnreadItem(lostItemId);
    assertTrue(hasOne);
  }

  @Test
  public void should_return_false_when_does_not_have_unread_return_item() throws Exception {
    when(returnItemRepo.findByLostItemIdAndState(eq(lostItemId), eq(state)))
        .thenReturn(null);
    boolean hasNo = returnItemService.hasUnreadItem(lostItemId);
    assertFalse(hasNo);
  }
}