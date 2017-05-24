package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ReturnItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ReturnItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ReturnItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static cn.gaoyuexiang.LostAndFound.item.enums.ItemSort.CREATE_TIME;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReturnItemServiceImplTestForGetItems {

  @Autowired
  private ReturnItemService returnItemService;

  @MockBean
  private ReturnItemRepo returnItemRepo;

  private long itemId;
  private int page;
  private int listSize;
  private ItemSort sort;
  private PageRequest pageRequest;

  @Before
  public void setUp() throws Exception {
    itemId = 123L;
    page = 1;
    listSize = 8;
    sort = CREATE_TIME;
    pageRequest = new PageRequest(page - 1, listSize, DESC, "createTime");
  }

  @Test
  public void should_get_list_when_args_are_valid() throws Exception {
    ReturnItem returnItem = new ReturnItem();
    when(returnItemRepo.findAllByLostItemId(eq(itemId), eq(pageRequest)))
        .thenReturn(Collections.singletonList(returnItem));
    List<ReturnItemPageItem> items = returnItemService.getReturnItemPageItems(itemId, page, listSize, sort);
    assertThat(items.size(), is(1));
  }

  @Test
  public void should_return_empty_list_when_page_out_of_bound() throws Exception {
    when(returnItemRepo.findAllByLostItemId(eq(itemId), eq(pageRequest)))
        .thenReturn(Collections.emptyList());
    List<ReturnItemPageItem> items = returnItemService.getReturnItemPageItems(itemId, page, listSize, sort);
    assertThat(items.size(), is(0));
  }
}