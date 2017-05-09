package cn.gaoyuexiang.LostAndFound.item.repository;

import cn.gaoyuexiang.LostAndFound.item.model.entity.ClaimItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimItemRepo extends PagingAndSortingRepository<ClaimItem, Long> {

  List<ClaimItem> findAllByFoundItemId(long foundItemId, Sort sort);

  ClaimItem findByClaimUserAndFoundItemId(String claimUser, long foundItemId);

}
