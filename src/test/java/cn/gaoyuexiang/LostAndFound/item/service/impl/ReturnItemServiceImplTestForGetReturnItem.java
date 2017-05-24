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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReturnItemServiceImplTestForGetReturnItem {

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
  public void should_return_item_when_found_item() throws Exception {
    ReturnItem returnItem = new ReturnItem();
    when(returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId))
        .thenReturn(returnItem);
    ReturnItem item = returnItemService.getReturnItem(username, lostItemId);
    assertThat(item, is(returnItem));
  }

  @Test
  public void should_return_null_when_item_not_found() throws Exception {
    when(returnItemRepo.findByReturnUserAndLostItemId(username, lostItemId))
        .thenReturn(null);
    assertNull(returnItemService.getReturnItem(username, lostItemId));
  }
}