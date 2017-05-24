package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
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

import javax.ws.rs.NotFoundException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReturnItemServiceImplTestForDelete {

  @Autowired
  private ReturnItemService returnItemService;

  @MockBean
  private ReturnItemRepo returnItemRepo;

  private String username;
  private long lostItemId;

  @Before
  public void setUp() throws Exception {
    username = "username";
    lostItemId = 123L;
  }

  @Test
  public void should_change_item_state_when_state_is_unread() throws Exception {
    ReturnItem returnItem = new ReturnItem();
    returnItem.setState(ItemState.UNREAD.getValue());
    when(returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId))
        .thenReturn(returnItem);
    when(returnItemRepo.save(returnItem)).thenReturn(returnItem);
    ReturnItem resultItem = returnItemService.delete(username, lostItemId, ActionType.CANCEL);
    assertThat(resultItem.getState(), is(ItemState.CANCELED.getValue()));
  }

  @Test(expected = UpdateItemException.class)
  public void should_throw_UpdateItemException_when_item_is_not_unread() throws Exception {
    ReturnItem returnItem = new ReturnItem();
    returnItem.setState(ItemState.CANCELED.getValue());
    when(returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId))
        .thenReturn(returnItem);
    returnItemService.delete(username, lostItemId, ActionType.REJECT);
  }

  @Test(expected = NotFoundException.class)
  public void should_throw_NotFoundException_when_returnIte_not_found() throws Exception {
    when(returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId))
        .thenReturn(null);
    returnItemService.delete(username, lostItemId, ActionType.CANCEL);
  }
}