package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.ItemApplication;
import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import cn.gaoyuexiang.LostAndFound.item.service.TimeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class)
public class LostItemServiceImplTestForCreate {

  @Autowired
  private LostItemService lostItemService;

  @MockBean
  private LostItemRepo lostItemRepo;

  @MockBean
  private IdCreateService idCreateService;

  @MockBean
  private TimeService timeService;

  private String title;
  private String itemName;
  private long beginTime;
  private long endTime;
  private String description;
  private List<String> pictures;
  private String username;

  @Before
  public void setUp() throws Exception {
    title = "title";
    itemName = "itemName";
    beginTime = 123;
    endTime = 147;
    description = "description";
    pictures = Arrays.asList("pic1", "pic2");
    username = "username";

  }

  @Test
  public void should_return_LostItem_when_create_success() throws Exception {
    LostItemCreator lostItemCreator = new LostItemCreator(
        title, itemName, beginTime, endTime, description, pictures);
    Long id = 0L;
    Long time = 123L;
    LostItem expectItem = new LostItem();
    when(lostItemRepo.findLatestId(any(PageRequest.class))).thenReturn(Collections.singletonList(id));
    when(idCreateService.create(id)).thenReturn(id);
    when(timeService.getCurrentTime()).thenReturn(time);
    when(lostItemRepo.save(any(LostItem.class))).thenReturn(expectItem);

    LostItem lostItem = lostItemService.create(lostItemCreator, username);

    assertThat(expectItem, is(lostItem));
  }

  @Test(expected = MissPropertyException.class)
  public void should_throw_MissPropertyException_when_lostItemCreator_miss_any_property() throws Exception {
    LostItemCreator lostItemCreator = new LostItemCreator();
    lostItemService.create(lostItemCreator, username);
  }
}