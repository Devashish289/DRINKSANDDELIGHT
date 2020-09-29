package com.capgemini.drinksanddelight.dao;

import com.capgemini.drinksanddelight.entities.RawMaterialStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRawMaterialDao extends JpaRepository<RawMaterialStockEntity,String> {

	RawMaterialStockEntity findByOrderId(String orderId);

}
