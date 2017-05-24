package cn.gaoyuexiang.LostAndFound.item.repository;

import cn.gaoyuexiang.LostAndFound.item.model.entity.ReturnItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnItemRepo extends PagingAndSortingRepository<ReturnItem, Long> {

  List<ReturnItem> findAllByLostItemId(long lostItemId, Pageable pageable);

  List<ReturnItem> findAllByLostItemIdAndState(long lostItemId, String state);

  ReturnItem findByReturnUserAndLostItemId(String returnUser, long lostItemId);

  ReturnItem findByLostItemIdAndState(long lostItemId, String state);

  @Query("select returnItem.id from ReturnItem returnItem order by returnItem.createTime desc")
  List<Long> findLatestId();
}
