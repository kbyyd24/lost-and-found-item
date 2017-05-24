package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.repository.FoundItemRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoundItemBelongCheckerTest {

  @Autowired
  private FoundItemBelongChecker belongChecker;

  @MockBean
  private FoundItemRepo foundItemRepo;

  private String username;
  private long itemId;

  @Before
  public void setUp() throws Exception {
    username = "username";
    itemId = 123L;
  }

  @Test
  public void should_return_true_when_item_belong_to_user() throws Exception {
    FoundItem foundItem = new FoundItem();
    foundItem.setOwner(username);
    when(foundItemRepo.findById(itemId)).thenReturn(foundItem);
    assertTrue(belongChecker.isBelong(itemId, username));
  }

  @Test
  public void should_return_false_when_item_not_belong_to_user() throws Exception {
    FoundItem foundItem = new FoundItem();
    foundItem.setOwner("");
    when(foundItemRepo.findById(itemId)).thenReturn(foundItem);
    assertFalse(belongChecker.isBelong(itemId, username));
  }

  @Test(expected = NotFoundException.class)
  public void should_throw_NotFoundException_when_item_not_found() throws Exception {
    when(foundItemRepo.findById(itemId)).thenReturn(null);
    belongChecker.isBelong(itemId, username);
  }
}