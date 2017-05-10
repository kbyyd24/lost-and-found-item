package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.ItemApplication;
import cn.gaoyuexiang.LostAndFound.item.exception.CloseItemException;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.repository.ReturnItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static cn.gaoyuexiang.LostAndFound.item.enums.ItemState.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class)
public class LostItemServiceImplTestForClose {

  @Autowired
  private LostItemService lostItemService;
  
  @MockBean
  private LostItemRepo lostItemRepo;

  @MockBean
  private ReturnItemService returnItemService;
  @Test
  public void should_close_item_when_item_cloud_be_closed() throws Exception {
    long lostItemId = 3L;
    LostItem lostItem = new LostItem();
    lostItem.setState(ENABLE.getValue());
    when(lostItemRepo.findById(lostItemId)).thenReturn(lostItem);
    when(returnItemService.hasUnreadItem(lostItemId))
        .thenReturn(false);
    when(lostItemRepo.save(lostItem)).thenReturn(lostItem);
    LostItem resultItem = lostItemService.close(lostItemId);
    assertThat(resultItem, is(lostItem));
  }

  @Test
  public void should_return_null_when_lost_item_not_found() throws Exception {
    long lostItemId = 3L;
    when(lostItemRepo.findById(lostItemId)).thenReturn(null);
    assertNull(lostItemService.close(lostItemId));
  }

  @Test(expected = CloseItemException.class)
  public void should_throw_CloseItemException_when_item_is_closed() throws Exception {
    long lostItemId = 3L;
    LostItem lostItem = new LostItem();
    lostItem.setState(CLOSED.getValue());
    when(lostItemRepo.findById(lostItemId)).thenReturn(lostItem);
    lostItemService.close(lostItemId);
  }

  @Test(expected = CloseItemException.class)
  public void should_throw_CloseItemException_when_lost_item_has_unread_return_item() throws Exception {
    long lostItemId = 3L;
    ReturnItem returnItem = new ReturnItem();
    LostItem lostItem = new LostItem();
    lostItem.setState(ENABLE.getValue());
    when(lostItemRepo.findById(lostItemId)).thenReturn(lostItem);
    when(returnItemService.hasUnreadItem(lostItemId))
        .thenReturn(true);
    lostItemService.close(lostItemId);
  }
}