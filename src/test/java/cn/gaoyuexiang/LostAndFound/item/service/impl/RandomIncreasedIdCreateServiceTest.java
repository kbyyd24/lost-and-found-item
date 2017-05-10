package cn.gaoyuexiang.LostAndFound.item.service.impl;

import cn.gaoyuexiang.LostAndFound.item.ItemApplication;
import cn.gaoyuexiang.LostAndFound.item.service.IdCreateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class)
public class RandomIncreasedIdCreateServiceTest {

  @Autowired
  private IdCreateService idCreateService;

  @Value("${lost-and-found.id.initialBase}")
  private long initialBase;

  @Test
  public void should_return_initial_base_when_base_less_than_initial_base() throws Exception {
    assertThat(idCreateService.create(initialBase - 1), is(initialBase));
  }

  @Test
  public void should_return_id_bigger_than_base_when_base_is_bigger_than_initial_base() throws Exception {
    assertTrue(idCreateService.create(initialBase) > initialBase);
  }
}