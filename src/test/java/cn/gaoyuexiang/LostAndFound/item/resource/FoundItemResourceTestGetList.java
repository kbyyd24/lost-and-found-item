package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.model.dto.FoundItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import cn.gaoyuexiang.LostAndFound.item.service.impl.FoundItemServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FoundItemResourceTestGetList {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private FoundItemServiceImpl foundItemService;

  private int page;
  private int size;
  private String sortColumn;
  private String path;
  private String query;

  @Before
  public void setUp() throws Exception {
    page = 1;
    size = 8;
    sortColumn = "create_time";
    path = "/item/found";
    query = String.format("?page=%d&listSize=%d&sort=%s", page, size, sortColumn);
  }

  @Test
  public void should_response_200_when_load_page_success() throws Exception {
    given(foundItemService.loadPage(eq(page), eq(size), eq(ItemSort.CREATE_TIME)))
        .willReturn(Collections.singletonList(new FoundItemPageItem()));
    ResponseEntity<List> entity = restTemplate.getForEntity(path, List.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  public void should_response_404_when_page_out_of_bound() throws Exception {
    given(foundItemService.loadPage(eq(page), eq(size), eq(ItemSort.CREATE_TIME)))
        .willReturn(Collections.emptyList());
    ResponseEntity<Message> entity = restTemplate.getForEntity(path, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}