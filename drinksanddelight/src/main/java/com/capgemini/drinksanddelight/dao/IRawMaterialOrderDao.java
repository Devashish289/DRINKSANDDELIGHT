package com.capgemini.drinksanddelight.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.drinksanddelight.entities.RawMaterialOrder;

@Repository
public interface IRawMaterialOrderDao extends JpaRepository<RawMaterialOrder, Integer> 
{

}
