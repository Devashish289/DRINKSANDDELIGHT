package com.capgemini.drinksanddelight.dao;

import com.capgemini.drinksanddelight.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISupplierDao extends JpaRepository<Supplier, Integer> {
	

}
