package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.enums.ItemSort;
import cn.gaoyuexiang.LostAndFound.item.enums.NotFoundReason;
import cn.gaoyuexiang.LostAndFound.item.model.dto.LostItemPageItem;
import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LostItemResourceTestForGetLostItemList {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private LostItemService lostItemService;

  @Test
  public void should_response_200_when_page_is_valid() throws Exception {
    LostItemPageItem pageItem = new LostItemPageItem();
    List<LostItemPageItem> itemList = Collections.singletonList(pageItem);
    given(lostItemService.loadPage(1, 8, ItemSort.CREATE_TIME))
        .willReturn(itemList);
    ResponseEntity<List> entity = restTemplate.getForEntity("/item/lost", List.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  @Ignore
  public void should_response_404_when_page_out_of_bound() throws Exception {
    given(lostItemService.loadPage(1, 8, ItemSort.CREATE_TIME))
        .willReturn(Collections.emptyList());
    ResponseEntity<Message> entity = restTemplate.getForEntity("/item/lost", Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    assertThat(entity.getBody().getMsg(), is(NotFoundReason.PAGE_OUT_OF_BOUND.getReason()));
  }
}