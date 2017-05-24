package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.ItemApplication;
import cn.gaoyuexiang.LostAndFound.item.exception.UnauthorizedException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class)
public class LostItemServiceImplTestForUpdate {

  @Autowired
  private LostItemService lostItemService;

  @MockBean
  private LostItemRepo lostItemRepo;

  private String username;
  private LostItemCreator updater;
  private long id;
  private LostItem existedItem;

  @Before
  public void setUp() throws Exception {
    username = "username";
    id = 123L;
    String title = "title";
    String itemName = "itemName";
    long lostTime = 24L;
    String description = "description";
    String picture1 = "picture1";
    String picture2 = "picture2";
    List<String> pictures = Arrays.asList(picture1, picture2);
    updater = new LostItemCreator(title, itemName, lostTime, description);
    existedItem = new LostItem();
    existedItem.setId(id);
    existedItem.setTitle(title);
    existedItem.setItemName("item name");
    existedItem.setLostTime(12L);
  }

  @Test
  public void should_return_updated_item_when_update_success() throws Exception {
    existedItem.setOwner(username);
    when(lostItemRepo.findById(id)).thenReturn(existedItem);
    when(lostItemRepo.save(existedItem)).thenReturn(existedItem);
    LostItem updatedItem = lostItemService.update(updater, id, username);
    assertThat(updatedItem, is(existedItem));
  }

  @Test
  public void should_return_null_when_item_not_found() throws Exception {
    when(lostItemRepo.findById(id)).thenReturn(null);
    LostItem nullItem = lostItemService.update(updater, id, username);
    assertNull(nullItem);
  }

  @Test(expected = UnauthorizedException.class)
  public void should_throw_UnauthorizedException_when_user_do_not_have_the_item() throws Exception {
    existedItem.setOwner("");
    when(lostItemRepo.findById(id)).thenReturn(existedItem);
    lostItemService.update(updater, id, username);
  }
}