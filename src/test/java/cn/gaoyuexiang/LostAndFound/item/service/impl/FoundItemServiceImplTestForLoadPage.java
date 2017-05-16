package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.repository.FoundItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoundItemServiceImplTestForLoadPage {

  @Autowired
  private FoundItemService foundItemService;

  @MockBean
  private FoundItemRepo foundItemRepo;

  private int page;
  private int size;
  private ItemSort sort;
  private Pageable requestPage;

  @Before
  public void setUp() throws Exception {
    page = 1;
    size = 8;
    sort = ItemSort.CREATE_TIME;
    requestPage = new PageRequest(page - 1, size, DESC, sort.getColumnName());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void should_return_list_when_found_items() throws Exception {
    List<FoundItem> foundItems = Collections.singletonList(new FoundItem());
    Page mockPage = mock(Page.class);
    when(mockPage.getContent()).thenReturn(foundItems);
    when(foundItemRepo.findAll(eq(requestPage))).thenReturn(mockPage);
    List<FoundItemPageItem> items = foundItemService.loadPage(page, size, sort);
    assertThat(items.size(), is(1));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void should_return_empty_list_when_page_out_of_bound() throws Exception {
    Page mockPage = mock(Page.class);
    when(mockPage.getContent()).thenReturn(Collections.emptyList());
    when(foundItemRepo.findAll(eq(requestPage))).thenReturn(mockPage);
    List<FoundItemPageItem> items = foundItemService.loadPage(page, size, sort);
    assertThat(items.size(), is(0));
  }
}