package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import cn.gaoyuexiang.LostAndFound.item.service.FoundItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FoundItemResourceTestForLoadOne {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private FoundItemService foundItemService;

  private long itemId;
  private String path;

  @Before
  public void setUp() throws Exception {
    itemId = 123L;
    path = "/item/found/" + itemId;
  }

  @Test
  public void should_response_200_when_item_found() throws Exception {
    FoundItem foundItem = new FoundItem();
    given(foundItemService.loadOne(eq(itemId))).willReturn(foundItem);
    ResponseEntity<FoundItem> entity = restTemplate.getForEntity(path, FoundItem.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(foundItem));
  }

  @Test
  public void should_response_404_when_item_not_found() throws Exception {
    given(foundItemService.loadOne(eq(itemId))).willReturn(null);
    ResponseEntity<Message> entity = restTemplate.getForEntity(path, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}