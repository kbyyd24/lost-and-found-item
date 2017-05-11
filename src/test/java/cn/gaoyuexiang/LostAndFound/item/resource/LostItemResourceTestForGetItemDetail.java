package cn.gaoyuexiang.LostAndFound.item.resource;

import cn.gaoyuexiang.LostAndFound.item.model.dto.Message;
import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import cn.gaoyuexiang.LostAndFound.item.service.LostItemService;
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
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class LostItemResourceTestForGetItemDetail {

  @Autowired
  private TestRestTemplate restTemplate;
  
  @MockBean
  private LostItemService lostItemService;

  private long id;
  private String path;

  @Before
  public void setUp() throws Exception {
    id = 3L;
    path = "/item/lost/" + id;
  }

  @Test
  public void should_response_200_when_lost_item_is_exist() throws Exception {
    LostItem lostItem = new LostItem();
    given(lostItemService.loadOne(id)).willReturn(lostItem);
    ResponseEntity<LostItem> entity = restTemplate.getForEntity(path, LostItem.class);
    assertThat(entity.getStatusCode(), is(OK));
    assertThat(entity.getBody(), is(lostItem));
  }

  @Test
  public void should_response_404_when_lost_item_not_found() throws Exception {
    given(lostItemService.loadOne(id)).willReturn(null);
    ResponseEntity<Message> entity = restTemplate.getForEntity(path, Message.class);
    assertThat(entity.getStatusCode(), is(NOT_FOUND));
  }
}