package cn.gaoyuexiang.LostAndFound.item.repository;

import cn.gaoyuexiang.LostAndFound.item.model.entity.FoundItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface FoundItemRepo extends PagingAndSortingRepository<FoundItem, Long> {

  FoundItem findById(long id);

  FoundItem findByIdAndOwner(long id, String owner);

  @Query("select foundItem.id from FoundItem foundItem order by foundItem.createTime desc")
  List<Long> findLatestId();
}
