package cn.gaoyuexiang.LostAndFound.item.repository;

import cn.gaoyuexiang.LostAndFound.item.model.entity.LostItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostItemRepo extends PagingAndSortingRepository<LostItem, Long> {

  @Query("select lostItem from LostItem")
  List<LostItem> findAll(Sort sort, Pageable pageable);

  LostItem findById(long id);

}
