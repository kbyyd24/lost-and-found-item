package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ClaimItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClaimItemServiceImplTestForCreate {

  @Autowired
  private ClaimItemService claimItemService;

  @MockBean
  private ClaimItemRepo claimItemRepo;

  @MockBean
  private IdCreateService idCreateService;

  @MockBean
  private TimeService timeService;

  private long foundItemId;
  private String createUser;
  private ClaimItemCreator creator;

  @Before
  public void setUp() throws Exception {
    foundItemId = 1L;
    createUser = "createUser";
    creator = new ClaimItemCreator();
    creator.setContact("contact");
    creator.setReason("reason");
  }

  @Test
  public void should_return_new_claimItem_when_item_not_exist() throws Exception {
    ClaimItem expectItem = new ClaimItem();
    long latestId = 1L;
    Long id = 1L;
    Long time = 1234L;
    when(claimItemRepo.findByClaimUserAndFoundItemId(createUser, foundItemId))
        .thenReturn(null);
    when(claimItemRepo.findLatestId()).thenReturn(Collections.singletonList(latestId));
    when(idCreateService.create(latestId)).thenReturn(id);
    when(timeService.getCurrentTime()).thenReturn(time);
    when(claimItemRepo.save(any(ClaimItem.class))).thenReturn(expectItem);
    ClaimItem claimItem = claimItemService.create(foundItemId, createUser, creator);
    assertThat(claimItem, is(expectItem));
    verify(claimItemRepo).findByClaimUserAndFoundItemId(createUser, foundItemId);
    verify(claimItemRepo).findLatestId();
    verify(idCreateService).create(latestId);
    verify(timeService).getCurrentTime();
    verify(claimItemRepo).save(any(ClaimItem.class));
  }

  @Test
  public void should_return_updated_claimItem_when_item_exist() throws Exception {
    ClaimItem existItem = new ClaimItem();
    existItem.setState(ItemState.ENABLE.getValue());
    when(claimItemRepo.findByClaimUserAndFoundItemId(createUser, foundItemId))
        .thenReturn(existItem);
    when(claimItemRepo.save(existItem)).thenReturn(existItem);
    ClaimItem claimItem = claimItemService.create(foundItemId, createUser, creator);
    assertThat(claimItem, is(existItem));
    assertNotNull(claimItem.getReason());
  }

  @Test(expected = MissPropertyException.class)
  public void should_throw_MissPropertyException_when_creator_miss_property() throws Exception {
    creator.setReason(null);
    claimItemService.create(foundItemId, createUser, creator);
  }

  @Test(expected = UpdateItemException.class)
  public void should_throw_UpdateItemException_when_exist_item_state_is_accepted() throws Exception {
    ClaimItem closedItem = new ClaimItem();
    closedItem.setState(ItemState.ACCEPTED.getValue());
    when(claimItemRepo.findByClaimUserAndFoundItemId(createUser, foundItemId))
        .thenReturn(closedItem);
    claimItemService.create(foundItemId, createUser, creator);
  }
}