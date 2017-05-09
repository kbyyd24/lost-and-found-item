package cn.gaoyuexiang.LostAndFound.item.repository;

import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnItemRepo extends PagingAndSortingRepository<ReturnItem, Long> {

  List<ReturnItem> findAllByLostItemId(long lostItemId, Sort sort);

  ReturnItem findByReturnUserAndLostItemId(String returnUser, long lostItemId);

}
