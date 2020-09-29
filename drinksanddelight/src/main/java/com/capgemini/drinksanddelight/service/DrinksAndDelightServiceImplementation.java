package com.capgemini.drinksanddelight.service;

import java.math.BigInteger;
import java.util.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;
import com.capgemini.drinksanddelight.dao.DistributorDetailsDao;
import com.capgemini.drinksanddelight.dao.IRawMaterialDao;
import com.capgemini.drinksanddelight.dao.IRawMaterialOrderDao;
import com.capgemini.drinksanddelight.dao.ISupplierDao;
import com.capgemini.drinksanddelight.dao.ProductOrderDao;
import com.capgemini.drinksanddelight.entities.DistributorEntity;
import com.capgemini.drinksanddelight.entities.ProductOrderEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capgemini.drinksanddelight.dao.ProductStockDao;
import com.capgemini.drinksanddelight.entities.ProductStockEntity;
import com.capgemini.drinksanddelight.entities.RawMaterialOrder;
import com.capgemini.drinksanddelight.entities.RawMaterialStockEntity;
import com.capgemini.drinksanddelight.entities.Supplier;
import com.capgemini.drinksanddelight.exception.InvalidArgumentException;
import com.capgemini.drinksanddelight.exception.ProductOrderNotFoundException;
import com.capgemini.drinksanddelight.exception.RawMaterialOrderNotFoundException;
import com.capgemini.drinksanddelight.exception.StockAdditionException;
import com.capgemini.drinksanddelight.exception.StockNotFoundException;
import com.capgemini.drinksanddelight.exception.SupplierNotFoundException;


@Service
@Transactional
public class DrinksAndDelightServiceImplementation implements DrinksAndDelightService {

    @Autowired
    private ProductStockDao stockDao;

    @Autowired
    private DistributorDetailsDao distributorDao;
    
    @Autowired
	private IRawMaterialDao dao;
    
    @Autowired
	private ISupplierDao dao2;
    
    @Autowired
    private ProductOrderDao productOrderDao;
    
    @Autowired
	private IRawMaterialOrderDao rawMaterialOrderDao;

    @Override
    public ProductStockEntity save(ProductStockEntity productstockdetails) {
        productstockdetails = stockDao.save(productstockdetails);
        return productstockdetails;
    }

    @Override
    public List<ProductStockEntity> fetchAllStocks() {
        List<ProductStockEntity> stocks = stockDao.findAll();
        return stocks;
    }


    @Override
    public List<DistributorEntity> fetchAllDistributors() {
        List<DistributorEntity> distributorentity = distributorDao.findAll();
        return distributorentity;

    }

    @Override
    public DistributorEntity saveDistributor(DistributorEntity entity) {
        entity = distributorDao.save(entity);
        return entity;
    }
    
    @Override
    public ProductStockEntity viewStock(String stockId) {
    	ProductStockEntity ProductDetails=null;
    	try {
    			System.out.println("see the details"+stockDao.existsById(stockId));
    			ProductDetails=stockDao.findById(stockId).get();
    		}
    	catch(Exception e) 
    		{
    			e.printStackTrace();
    		}
    	return null;
    }
    
   @Override
    public ProductStockEntity updateStock(ProductStockEntity productstockdetails){
		ProductStockEntity updatedstock= stockDao.save(productstockdetails);
		    if(updatedstock!=null)
			{
			return stockDao.save(productstockdetails);		
			}
			else
			{
			throw new ProductOrderNotFoundException("OrderId  does not exist.");
			}
   }
  
		    @Override
			public void cancelStock(String stockId) {
				stockDao.deleteById(stockId);
				
			}
		    
	

			// This method will be used to add RawMaterailStock.
			@Override
			public RawMaterialStockEntity addStock(RawMaterialStockEntity stock) {
				stock.setStockId(generateId());
				stock = dao.save(stock);
				if (stock.getStockId().isEmpty()) {
					throw new StockAdditionException("Unable to generate stock id");
				}
				return stock;
			}

			// This method will track RawMaterialStock details on the basis of orderId.
			@Override
			public RawMaterialStockEntity trackRawMaterialOrder(String orderId) {
				if (orderId.isEmpty()) {
					throw new InvalidArgumentException("Invalid Id");
				}
				RawMaterialStockEntity stock = dao.findByOrderId(orderId);
				if (stock == null) {
					throw new StockNotFoundException(" Stock Not Found");
				}
				return stock;
			}

			// This method will validate the manufacturing date according to required
			// conditions.
			// Date should not be less then current date and not greater than date after 3
			// months of current date.
			@Override
			public boolean validateManufacturingDate(Date manuDate) {
				Calendar currentDateAfter3Months = Calendar.getInstance();
				currentDateAfter3Months.add(Calendar.MONTH, 3);
				Calendar currentDate = Calendar.getInstance();
				if (manuDate.before(currentDateAfter3Months.getTime()) && manuDate.after(currentDate.getTime())) {
					return true;
				}
				throw new StockAdditionException("Manufacturing Date not in range!!");
			}

			// This method will validate the expiry date according to required conditions.
			// Date should not be less then current date and not greater than date after 3
			// months of current date.
			@Override
			public boolean validateExpiryDate(Date expiryDate) {
				Calendar currentDateAfter3Months = Calendar.getInstance();
				currentDateAfter3Months.add(Calendar.MONTH, 3);
				Calendar currentDate = Calendar.getInstance();
				if (expiryDate.before(currentDateAfter3Months.getTime()) && expiryDate.after(currentDate.getTime())) {
					return true;
				}
				throw new StockAdditionException("Expiry date not in range!!");
			}

			// This method will update RawMaterialStock on basis of its processDate.
			@Override
			public String updateRawMaterialStock(String orderId, Date date) {
				String msg;
				RawMaterialStockEntity updatedStock = trackRawMaterialOrder(orderId);

				Calendar currentDateAfter3Months = Calendar.getInstance();
				currentDateAfter3Months.add(Calendar.MONTH, 3);
				Calendar currentDate = Calendar.getInstance();

				if (date.before(currentDateAfter3Months.getTime()) && date.after(currentDate.getTime())) {
					updatedStock.setProcessDate(date);
					dao.save(updatedStock);
					msg = "Date Updated";
				} else
					msg = "Error in data updation";

				return msg;

			}

			// This method will be used to fetch all RawMaterialStock present in a list.
			@Override
			public List<RawMaterialStockEntity> fetchAllStock() {
				return dao.findAll();

			}

			public String generateId() {
				StringBuilder id = new StringBuilder();
				for (int i = 0; i < 10; i++) {
					Random random = new Random();
					int number = random.nextInt(3);
					id.append(number);
				}
				return id.toString();
			}
			
			
			@Override
			public Supplier addSupplier(Supplier supplier) {
				supplier = dao2.save(supplier);
				return supplier;
			}

			@Override
			public Supplier fetchSupplierById(int id) {
				if (id == 0) {
					throw new InvalidArgumentException("Invalid Supplier Id");
				}
				Optional<Supplier> optional = dao2.findById(id);
				if (optional.isPresent()) {
					return optional.get();
				}
				throw new SupplierNotFoundException("Supplier not found");
			}

			@Override
			public List<Supplier> fetchAllSuppliers() {
				return dao2.findAll();

			}
			

			
		    @Override
		    public ProductOrderEntity save(ProductOrderEntity productorderentity) {
		        productorderentity = productOrderDao.save(productorderentity);
		        return productorderentity;
		    }

		    @Override
		    public ProductOrderEntity placeProductOrder(String orderId, String name, String supplierid, double quantityValue,
		                                                double quantityUnit, double pricePerUnit, LocalDate expectedDeliveryDate, double totalPrice) {
		        ProductOrderEntity productOrderEntity = new ProductOrderEntity();
		        productOrderEntity.setOrderId(orderId);
		        productOrderEntity.setName(name);
		        productOrderEntity.setSupplierId(supplierid);
		        productOrderEntity.setQuantityUnit(quantityUnit);
		        productOrderEntity.setPricePerUnit(pricePerUnit);
		        productOrderEntity.setQuantityValue(quantityValue);
		        productOrderEntity.setExpectedDeliveryDate(expectedDeliveryDate);
		        productOrderEntity.setTotalPrice(totalPrice);
		        save(productOrderEntity);
		        return productOrderEntity;

		    }

		    public ProductOrderEntity updateTrackOrder(String orderId, String location, LocalDate date) {
		        ProductOrderEntity productorderentity = trackOrder(orderId);
		        //can be commented
		        productorderentity.setOrderId(orderId);
		        productorderentity.setLocation(location);
		        productorderentity.setExpectedDeliveryDate(date);
		        save(productorderentity);
		        return productorderentity;
		    }

		    @Override
		    public List<ProductOrderEntity> fetchAll() {
		        List<ProductOrderEntity> productorderentity = productOrderDao.findAll();
		        return productorderentity;
		    }

		    @Override
		    public ProductOrderEntity trackOrder(String orderId) {
		        Optional<ProductOrderEntity> optional = productOrderDao.findById(orderId);
		        if (optional.isPresent()) {
		            ProductOrderEntity order = optional.get();
		            return order;
		        }
		        throw new ProductOrderNotFoundException("product order not found for id=" + orderId);
		    }
		    
		    public boolean deleteOrder(String orderId) {
				 Optional<ProductOrderEntity> optional = productOrderDao.findById(orderId);   
			     if(optional.isPresent()) {
			    	 productOrderDao.deleteById(orderId);
					    return true;
			    }
			     throw new ProductOrderNotFoundException("product order not found for id=" + orderId);
			
				}	
		    
		    
		    //Raw Material Order...
		    
		    
			@Override
			public RawMaterialOrder addRawMaterialOrder(RawMaterialOrder rawMaterialOrder) {		
				Date date = new Date(0);
				rawMaterialOrder.setDateOfOrder(date);
				Calendar calendar=Calendar.getInstance();
			    calendar.add(Calendar.DAY_OF_MONTH,7);
			    Date deliveryDate= (Date) calendar.getTime();
			    rawMaterialOrder.setDateOfDelivery(deliveryDate);			        
				double pricePerUnit = rawMaterialOrder.getPricePerUnit();
			    double quantityValue = rawMaterialOrder.getQuantityValue();
				double totalPrice = pricePerUnit * quantityValue;
				rawMaterialOrder.setTotalPrice(totalPrice);				
				return	rawMaterialOrderDao.save( rawMaterialOrder);
					  	
			}
			
		  	
			@Override
			public RawMaterialOrder getRawMaterialOrder(int orderId) {
				return rawMaterialOrderDao.findById(orderId).get();
				
			}

		
			@Override
			public RawMaterialOrder updateRawMaterialOrder(RawMaterialOrder rawMaterialOrder) {
				RawMaterialOrder updateOrders =rawMaterialOrderDao.save(rawMaterialOrder);
				    if(updateOrders!=null)
					{
					return rawMaterialOrderDao.save(rawMaterialOrder);		
					}
					else
					{
					throw new RawMaterialOrderNotFoundException("OrderId  does not exist.");
					}
				 }	
			
			
			@Override
			public List<RawMaterialOrder> displayRawMaterialOrder() {
				 List<RawMaterialOrder> orders=  rawMaterialOrderDao.findAll();
			        return orders;
			}

		
			@Override
			public void cancelOrder(int orderId) {
				rawMaterialOrderDao.deleteById(orderId);
				
			}

		    
	}

