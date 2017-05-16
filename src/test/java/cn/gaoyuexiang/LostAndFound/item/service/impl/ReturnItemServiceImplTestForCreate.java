package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ReturnItemRepo;
import cn.gaoyuexiang.LostAndFound.item.resource.provider.MissPropertyMapper;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReturnItemServiceImplTestForCreate {

  @Autowired
  private ReturnItemService returnItemService;

  @MockBean
  private ReturnItemRepo returnItemRepo;

  @MockBean
  private IdCreateService idCreateService;

  @MockBean
  private TimeService timeService;

  private String username;
  private long lostItemId;
  private ReturnItemCreator creator;

  @Before
  public void setUp() throws Exception {
    username = "username";
    lostItemId = 123L;
    creator = new ReturnItemCreator("reason", "contact");
  }

  @Test
  public void should_create_returnItem_when_item_not_exist() throws Exception {
    long baseId = 1L;
    Long time = 123L;
    ReturnItem expectItem = new ReturnItem();
    when(returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId))
        .thenReturn(null);
    when(returnItemRepo.findLatestId()).thenReturn(Collections.singletonList(baseId));
    when(idCreateService.create(baseId)).thenReturn(baseId);
    when(timeService.getCurrentTime()).thenReturn(time);
    when(returnItemRepo.save(any(ReturnItem.class))).thenReturn(expectItem);
    ReturnItem returnItem = returnItemService.create(username, lostItemId, creator);
    assertThat(returnItem, is(expectItem));
  }

  @Test
  public void should_update_returnItem_when_item_exist() throws Exception {
    ReturnItem existItem = new ReturnItem();
    existItem.setState(ItemState.UNREAD.getValue());
    when(returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId))
        .thenReturn(existItem);
    when(returnItemRepo.save(existItem)).thenReturn(existItem);
    ReturnItem returnItem = returnItemService.create(username, lostItemId, creator);
    assertThat(returnItem, is(existItem));
  }

  @Test(expected = MissPropertyException.class)
  public void should_throw_MissPropertyException_when_creator_miss_some_property() throws Exception {
    creator = new ReturnItemCreator();
    returnItemService.create(username, lostItemId, creator);
  }

  @Test(expected = UpdateItemException.class)
  public void should_throw_UpdateItemException_when_item_state_is_closed() throws Exception {
    ReturnItem closedItem = new ReturnItem();
    closedItem.setState(ItemState.CLOSED.getValue());
    when(returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId))
        .thenReturn(closedItem);
    returnItemService.create(username, lostItemId, creator);
  }
}