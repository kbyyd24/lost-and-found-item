package cn.gaoyuexiang.LostAndFound.item.repository;

import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimItemRepo extends PagingAndSortingRepository<ClaimItem, Long> {

  Page<ClaimItem> findAllByFoundItemId(long foundItemId, Pageable pageable);

  ClaimItem findByClaimUserAndFoundItemId(String claimUser, long foundItemId);

  ClaimItem findByFoundItemIdAndState(long foundItemId, String state);

  @Query("select claimItem.id from ClaimItem claimItem order by claimItem.id desc")
  List<Long> findLatestId();
}
