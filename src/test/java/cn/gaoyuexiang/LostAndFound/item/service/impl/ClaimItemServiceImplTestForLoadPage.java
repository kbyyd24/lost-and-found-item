package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.ClaimItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import cn.gaoyuexiang.LostAndFound.item.repository.ClaimItemRepo;
import cn.gaoyuexiang.LostAndFound.item.service.ClaimItemService;
import org.junit.Before;
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
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClaimItemServiceImplTestForLoadPage {

  @Autowired
  private ClaimItemService claimItemService;

  @MockBean
  private ClaimItemRepo claimItemRepo;

  private int page;
  private int size;
  private ItemSort sort;
  private PageRequest pageRequest;
  private long foundItemId;

  @Before
  public void setUp() throws Exception {
    foundItemId = 13L;
    page = 1;
    size = 8;
    sort = ItemSort.CREATE_TIME;
    pageRequest = new PageRequest(page, size, DESC, sort.getColumnName());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void should_return_items_when_has_claimItem() throws Exception {
    Page mockPage = mock(Page.class);
    when(mockPage.getContent()).thenReturn(Collections.singletonList(new ClaimItem()));
    when(claimItemRepo.findAllByFoundItemId(eq(foundItemId), eq(pageRequest)))
        .thenReturn(mockPage);
    List<ClaimItemPageItem> items =
        claimItemService.loadPage(foundItemId, page, size, sort);
    assertThat(items.size(), is(1));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void should_return_empty_list_when_can_not_find_anything() throws Exception {
    Page mockPage = mock(Page.class);
    when(mockPage.getContent()).thenReturn(Collections.emptyList());
    when(claimItemRepo.findAllByFoundItemId(eq(foundItemId), eq(pageRequest)))
        .thenReturn(mockPage);
    List<ClaimItemPageItem> items =
        claimItemService.loadPage(foundItemId, page, size, sort);
    assertThat(items.size(), is(0));
  }
}