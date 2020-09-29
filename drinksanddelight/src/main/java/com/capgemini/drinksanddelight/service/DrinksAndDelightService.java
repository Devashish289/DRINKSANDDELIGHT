package com.capgemini.drinksanddelight.service;

import java.math.BigInteger;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import com.capgemini.drinksanddelight.entities.DistributorEntity;
import com.capgemini.drinksanddelight.entities.ProductOrderEntity;
import com.capgemini.drinksanddelight.entities.ProductStockEntity;
import com.capgemini.drinksanddelight.entities.RawMaterialOrder;
import com.capgemini.drinksanddelight.entities.RawMaterialStockEntity;
import com.capgemini.drinksanddelight.entities.Supplier;

public interface DrinksAndDelightService {
	//ProductStock..>
	ProductStockEntity save(ProductStockEntity productstockdetails);
    List<ProductStockEntity> fetchAllStocks();
	List<DistributorEntity> fetchAllDistributors();
	DistributorEntity saveDistributor(DistributorEntity entity);
	public ProductStockEntity viewStock(String stockId);
	ProductStockEntity updateStock(ProductStockEntity productstockdetails);
	public void cancelStock(String stockId);
	
	//RawMaterialStock..
	RawMaterialStockEntity addStock(RawMaterialStockEntity stock);
	RawMaterialStockEntity trackRawMaterialOrder(String orderId);
	boolean validateManufacturingDate(Date manuDate);
	boolean validateExpiryDate(Date expiryDate);
	List<RawMaterialStockEntity> fetchAllStock();
	String updateRawMaterialStock(String orderId, Date date);
	
	//Supplier..
	Supplier addSupplier(Supplier supplier);
	Supplier fetchSupplierById(int id);
	List<Supplier> fetchAllSuppliers();
	
	//ProductOrder...
	 ProductOrderEntity save(ProductOrderEntity productorderentity);
	 List<ProductOrderEntity> fetchAll();
     ProductOrderEntity placeProductOrder(String orderId, String name, String supplierid, double quantityValue,
	                                         double quantityUnit, double pricePerUnit, LocalDate expectedDeliveryDate, double totalPrice);
	    
     ProductOrderEntity updateTrackOrder(String orderId, String location, LocalDate date);
     ProductOrderEntity trackOrder(String orderId) ;	    
	 boolean deleteOrder(String orderId);
	
	 //RawMaterialOrder...
	public RawMaterialOrder addRawMaterialOrder(RawMaterialOrder rawMaterialOrder);
	public RawMaterialOrder getRawMaterialOrder(int orderId);
	public void cancelOrder(int orderId);
	public RawMaterialOrder updateRawMaterialOrder(RawMaterialOrder rawMaterialOrder);
	public List<RawMaterialOrder> displayRawMaterialOrder();
	
}

