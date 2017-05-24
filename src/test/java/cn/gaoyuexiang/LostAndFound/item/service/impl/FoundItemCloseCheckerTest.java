package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemState;
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
public class FoundItemCloseCheckerTest {

  @Autowired
  private FoundItemCloseChecker closeChecker;

  @MockBean
  private FoundItemRepo foundItemRepo;

  private long itemId;
  private FoundItem foundItem;

  @Before
  public void setUp() throws Exception {
    itemId = 123L;
    foundItem = new FoundItem();
  }

  @Test
  public void should_return_true_when_item_closed() throws Exception {
    foundItem.setState(ItemState.CLOSED.getValue());
    when(foundItemRepo.findById(itemId)).thenReturn(foundItem);
    assertTrue(closeChecker.isClosed(itemId));
  }

  @Test
  public void should_return_false_when_item_not_closed() throws Exception {
    foundItem.setState(ItemState.ENABLE.getValue());
    when(foundItemRepo.findById(itemId)).thenReturn(foundItem);
    assertFalse(closeChecker.isClosed(itemId));
  }

  @Test(expected = NotFoundException.class)
  public void should_throw_NotFoundException_when_item_not_found() throws Exception {
    when(foundItemRepo.findById(itemId)).thenReturn(null);
    closeChecker.isClosed(itemId);
  }
}