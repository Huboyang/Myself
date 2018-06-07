package com.hby.myselfproject.Service;

import com.hby.myselfproject.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * service层 逻辑层 逻辑操作
 */
public interface ShopService{

    void insert(Shop s);

}
