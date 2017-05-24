package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.ItemApplication;
import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.repository.LostItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class)
public class LostItemServiceImplTestForLoadPage {
  
  @Autowired
  private LostItemService lostItemService;

  @MockBean
  private LostItemRepo lostItemRepo;

  @Test
  public void should_load_page_when_sort_is_create_time() throws Exception {
    String orderBy = "createTime";
    check(orderBy, ItemSort.CREATE_TIME);
  }

  @Test
  public void should_load_page_when_sort_is_end_time() throws Exception {
    String orderBy = "endTime";
    check(orderBy, ItemSort.END_TIME);
  }

  @Test
  public void should_load_page_when_sort_is_begin_time() throws Exception {
    String orderBy = "beginTime";
    check(orderBy, ItemSort.BEGIN_TIME);
  }

  @SuppressWarnings("unchecked")
  private void check(String orderBy, ItemSort orderByArg) {
    int page = 1;
    int listSize = 8;
    PageRequest pageRequest = new PageRequest(page - 1, listSize, DESC, orderBy);
    LostItem lostItem = new LostItem();
    Page mockPage = mock(Page.class);
    when(mockPage.getContent()).thenReturn(Collections.singletonList(lostItem));
    when(lostItemRepo.findAll(eq(pageRequest)))
        .thenReturn(mockPage);
    List<LostItemPageItem> lostItemPageItems = lostItemService.loadPage(page, listSize, orderByArg);
    LostItemPageItem lostItemPageItem = new LostItemPageItem();
    assertThat(lostItemPageItems.get(0), is(lostItemPageItem));
  }
}