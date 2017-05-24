package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.repository.FoundItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
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
public class FoundItemServiceImplTestForUpdate {

  @Autowired
  private FoundItemService foundItemService;

  @MockBean
  private FoundItemRepo foundItemRepo;

  private long itemId;
  private String username;
  private FoundItemCreator updater;

  @Before
  public void setUp() throws Exception {
    itemId = 12L;
    username = "username";
    updater = new FoundItemCreator("title", "itemName", 1L, "description");
  }

  @Test
  public void should_return_new_item_when_update_success() throws Exception {
    FoundItem existItem = new FoundItem();
    when(foundItemRepo.findByIdAndOwner(itemId, username))
        .thenReturn(existItem);
    when(foundItemRepo.save(existItem)).thenReturn(existItem);
    FoundItem foundItem = foundItemService.update(updater, itemId, username);
    assertThat(foundItem, is(existItem));
  }

  @Test
  public void should_return_null_when_item_not_found() throws Exception {
    when(foundItemRepo.findByIdAndOwner(itemId, username))
        .thenReturn(null);
    FoundItem nullItem = foundItemService.update(updater, itemId, username);
    assertNull(nullItem);
  }
}