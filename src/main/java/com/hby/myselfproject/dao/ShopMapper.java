package com.hby.myselfproject.dao;


import com.hby.myselfproject.entity.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Dao层
 */
@Repository
public interface ShopMapper extends CrudRepository<Shop,Integer> {

}
