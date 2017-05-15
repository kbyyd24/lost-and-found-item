package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ActionType;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
import cn.gaoyuexiang.LostAndFound.item.exception.UpdateItemException;
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

import javax.ws.rs.NotFoundException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClaimItemServiceImplTestForDelete {

  @Autowired
  private ClaimItemService claimItemService;

  @MockBean
  private ClaimItemRepo claimItemRepo;

  private String claimUser;
  private long foundItemId;

  @Before
  public void setUp() throws Exception {
    claimUser = "username";
    foundItemId = 123L;
  }

  @Test
  public void should_change_item_state_when_state_is_unread() throws Exception {
    ClaimItem expectItem = new ClaimItem();
    expectItem.setState(ItemState.UNREAD.getValue());
    when(claimItemRepo.findByClaimUserAndFoundItemId(claimUser, foundItemId))
        .thenReturn(expectItem);
    when(claimItemRepo.save(expectItem)).thenReturn(expectItem);
    ClaimItem claimItem = claimItemService.delete(foundItemId, claimUser, ActionType.ACCEPT);
    assertThat(claimItem, is(expectItem));
  }

  @Test(expected = UpdateItemException.class)
  public void should_throw_UpdateItemException_when_state_not_unread() throws Exception {
    ClaimItem claimItem = new ClaimItem();
    claimItem.setState(ItemState.ACCEPTED.getValue());
    when(claimItemRepo.findByClaimUserAndFoundItemId(claimUser, foundItemId))
        .thenReturn(claimItem);
    claimItemService.delete(foundItemId, claimUser, ActionType.ACCEPT);
  }

  @Test(expected = NotFoundException.class)
  public void should_throw_NotFoundException_when_item_not_found() throws Exception {
    when(claimItemRepo.findByClaimUserAndFoundItemId(claimUser, foundItemId))
        .thenReturn(null);
    claimItemService.delete(foundItemId, claimUser, ActionType.ACCEPT);
  }
}