package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.exception.MissPropertyException;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemCreator;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.repository.FoundItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
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
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoundItemServiceImplTestForCreate {

  @Autowired
  private FoundItemService foundItemService;

  @MockBean
  private FoundItemRepo foundItemRepo;

  @MockBean
  private IdCreateService idCreateService;

  @MockBean
  private TimeService timeService;

  private String createUser;
  private FoundItemCreator creator;

  @Before
  public void setUp() throws Exception {
    createUser = "createUser";
    creator = new FoundItemCreator("title", "itemName", 123L, "description");
  }

  @Test
  public void should_return_foundItem_when_create_success() throws Exception {
    long baseId = 1L;
    long createTime = 1L;
    FoundItem foundItem = new FoundItem();
    when(foundItemRepo.findLatestId()).thenReturn(Collections.singletonList(baseId));
    when(idCreateService.create(baseId)).thenReturn(baseId);
    when(timeService.getCurrentTime()).thenReturn(createTime);
    when(foundItemRepo.save(any(FoundItem.class))).thenReturn(foundItem);
    FoundItem resultItem = foundItemService.create(creator, createUser);
    assertThat(resultItem, is(foundItem));
  }

  @Test(expected = MissPropertyException.class)
  public void should_throw_MissPropertyException_when_foundItemCreator_miss_property() throws Exception {
    creator.setDescription(null);
    foundItemService.create(creator, createUser);
  }
}