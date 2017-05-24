package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.ItemApplication;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class)
public class LostItemServiceImplTestForLoadOne {

  @Autowired
  private LostItemService lostItemService;

  @MockBean
  private LostItemRepo lostItemRepo;

  @Test
  public void should_return_LostItem_when_given_an_id() throws Exception {
    long id = 1L;
    when(lostItemRepo.findById(id)).thenReturn(null);
    lostItemService.loadOne(id);
    verify(lostItemRepo).findById(id);
  }
}