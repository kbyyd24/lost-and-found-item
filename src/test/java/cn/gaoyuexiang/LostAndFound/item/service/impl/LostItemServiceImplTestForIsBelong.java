package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.interfaces.BelongChecker;
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
public class LostItemServiceImplTestForIsBelong {

  @Autowired
  private BelongChecker belongChecker;

  @MockBean
  private LostItemRepo lostItemRepo;

  private String username;
  private long id;
  private LostItem lostItem;

  @Before
  public void setUp() throws Exception {
    username = "username";
    id = 123L;
    lostItem = new LostItem();
  }

  @Test
  public void should_return_true_when_item_belong_to_user() throws Exception {
    lostItem.setOwner(username);
    when(lostItemRepo.findById(id)).thenReturn(lostItem);
    boolean belong = belongChecker.isBelong(id, username);
    assertTrue(belong);
  }

  @Test
  public void should_return_false_when_item_not_belong_to_user() throws Exception {
    lostItem.setOwner("");
    when(lostItemRepo.findById(id)).thenReturn(lostItem);
    boolean belong = belongChecker.isBelong(id, username);
    assertFalse(belong);
  }

  @Test(expected = NotFoundException.class)
  public void should_throw_NotFoundException_when_item_not_found() throws Exception {
    when(lostItemRepo.findById(id)).thenReturn(null);
    belongChecker.isBelong(id, username);
  }
}