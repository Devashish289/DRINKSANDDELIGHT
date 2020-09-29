package com.capgemini.drinksanddelight.controller;
import com.capgemini.drinksanddelight.dto.CreateDistributorRequest;
import com.capgemini.drinksanddelight.dto.CreateProductOrderRequest;
import com.capgemini.drinksanddelight.dto.CreateProductRequest;
import com.capgemini.drinksanddelight.dto.ProductOrderDetails;
import com.capgemini.drinksanddelight.dto.ProductStockDetails;
import com.capgemini.drinksanddelight.dto.RawMaterialStockDto;
import com.capgemini.drinksanddelight.dto.SupplierDto;
import com.capgemini.drinksanddelight.entities.DistributorEntity;
import com.capgemini.drinksanddelight.entities.ProductOrderEntity;
import com.capgemini.drinksanddelight.entities.ProductStockEntity;
import com.capgemini.drinksanddelight.entities.RawMaterialOrder;
import com.capgemini.drinksanddelight.entities.RawMaterialStockEntity;
import com.capgemini.drinksanddelight.entities.Supplier;
import com.capgemini.drinksanddelight.service.DrinksAndDelightService;
import com.capgemini.drinksanddelight.util.DistributorUtil;
import com.capgemini.drinksanddelight.util.ProductOrderUtil;
import com.capgemini.drinksanddelight.util.ProductStockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/products")
public class DrinksAndDelightController {

    @Autowired
    private DrinksAndDelightService drinksAndDelightService;
    
    @GetMapping("/liststock")
    public ResponseEntity<List<ProductStockEntity>> getProductStockList() {
        List<ProductStockEntity> list = drinksAndDelightService.fetchAllStocks();
        return new ResponseEntity<List<ProductStockEntity>>(list, HttpStatus.OK);
    }
    
    @PostMapping("/addstock")
    public ResponseEntity<ProductStockDetails> addStock(@RequestBody @Valid CreateProductRequest requestData) {
        ProductStockEntity entity = ProductStockUtil.toEntity(requestData);
        entity = drinksAndDelightService.save(entity);
        ProductStockDetails details = ProductStockUtil.toDetails(entity);
        ResponseEntity<ProductStockDetails> response = new ResponseEntity<>(details, HttpStatus.OK);
        return response;
    }
    
    @GetMapping("/viewStockById/{no}")
    public ProductStockEntity viewStock(@PathVariable(value="no") String stockId) 
    	{
    		return drinksAndDelightService.viewStock(stockId);
    	}

	 @PatchMapping("/updatestock")
	 public ProductStockEntity updateOrders(@RequestBody ProductStockEntity productstockdetails) {
			return drinksAndDelightService.updateStock(productstockdetails);		
			}
	 
	 @DeleteMapping(value="/delete/{stockId}")
	    public boolean cancelOrder(@PathVariable("stockId") String stockId) {
		  drinksAndDelightService.cancelStock(stockId);
		  return true;
	   }

//Distributors-->
	   @GetMapping("listdist")
	    public List<DistributorEntity> getDistributors() {
	        List<DistributorEntity> list = drinksAndDelightService.fetchAllDistributors();
	        System.out.println("hi");
	        return list;
	    }

	    @PostMapping("/adddist")
	    public ResponseEntity<DistributorEntity> addDistributor(@RequestBody CreateDistributorRequest requestData) {
	        DistributorEntity distributordetails = DistributorUtil.convertToDistributor(requestData);
	        distributordetails = drinksAndDelightService.saveDistributor(distributordetails);
	        ResponseEntity<DistributorEntity> response = new ResponseEntity<>(distributordetails, HttpStatus.OK);
	        return response;

	    }
	   
	    
//Raw material Stock
		@PostMapping("/addraw") // will add RawMaterialStock imp details.
		public ResponseEntity<RawMaterialStockEntity> addStock(@RequestBody @Valid RawMaterialStockDto dto) {
			RawMaterialStockEntity stock1 = convert(dto);
			stock1 = drinksAndDelightService.addStock(stock1);
			return new ResponseEntity<>(stock1, HttpStatus.OK);
		}

		public RawMaterialStockEntity convert(RawMaterialStockDto dto) {
			RawMaterialStockEntity stock = new RawMaterialStockEntity();
			stock.setOrderId(dto.getOrderId());
			stock.setName(dto.getName());
			stock.setPrice_per_unit(dto.getPrice_per_unit());
			stock.setPrice(dto.getPrice());
			stock.setQuantityUnit(dto.getQuantityUnit());
			stock.setWarehouseId(dto.getWarehouseId());
			stock.setDeliveryDate(dto.getDeliveryDate());
			stock.setQuantityValue(dto.getQuantityValue());

			Date manufacturingDate = dto.getManuDate();
			if (drinksAndDelightService.validateManufacturingDate((java.sql.Date) manufacturingDate))
				stock.setManuDate(dto.getManuDate());
			else
				System.err.println("Manufacturing Date not in range");
			Date expirydate = dto.getExpiryDate();
			if (drinksAndDelightService.validateExpiryDate((java.sql.Date) expirydate))
				stock.setExpiryDate(expirydate);
			else
				System.out.println("Expiry Date not in range");

			stock.setProcessDate(dto.getProcessDate());
			stock.setQualityCheck(dto.getQualityCheck());
			return stock;
		}

		
		//Supplier
		@PostMapping("/addSupplier") // will add Supplier details.
		public ResponseEntity<Supplier> addSupplier(@RequestBody @Valid SupplierDto dto) {
			Supplier supplier = convertSupplier(dto);
			supplier = drinksAndDelightService.addSupplier(supplier);
			return new ResponseEntity<>(supplier, HttpStatus.OK);
		}

		public static Supplier convertSupplier(SupplierDto dto) {
			Supplier supplier = new Supplier();
			supplier.setSupplierName(dto.getSupplierName());
			supplier.setSupplierAddress(dto.getSupplierAddress());
			supplier.setSupplierPhoneNo(dto.getSupplierPhoneNo());
			return supplier;
		}

		@GetMapping("/get/{id}") // will fetch RawMaterialStovck details through id.
		public ResponseEntity<RawMaterialStockEntity> fetchStock(@PathVariable("id") String id) {
			RawMaterialStockEntity stock = drinksAndDelightService.trackRawMaterialOrder(id);
			return new ResponseEntity<>(stock, HttpStatus.OK);
		}

		// will fetch supplier details on the basis of id.
		@GetMapping("/getS/{supplierId}")
		public ResponseEntity<Supplier> fetchSupplier(@PathVariable("supplierId") @Min(1) int supplierId) {
			Supplier supplier = drinksAndDelightService.fetchSupplierById(supplierId);
			return new ResponseEntity<>(supplier, HttpStatus.OK);
		}

		// will fetch details of all RawMaterialStocks.
		@GetMapping("/getStocks")
		public ResponseEntity<List<RawMaterialStockEntity>> fetchAllRawMaterialStock() {
			List<RawMaterialStockEntity> stocks = drinksAndDelightService.fetchAllStock();
			return new ResponseEntity<>(stocks, HttpStatus.OK);
		}

		// will fetch details of all Suppliers.
		@GetMapping("/getSuppliers")
		public ResponseEntity<List<Supplier>> fetchAllSuppliers() {
			List<Supplier> suppliers = drinksAndDelightService.fetchAllSuppliers();
			return new ResponseEntity<>(suppliers, HttpStatus.OK);

		}

		// will update RawMaterialStock process date.
		@PutMapping("/update/{id}/{date}")
		public ResponseEntity<String> updateStock(@RequestBody Map<String, String> map) throws ParseException {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String id = map.get("orderId");
			String date = map.get("processDate");
			Date requiredate = format.parse(date);
			String message = drinksAndDelightService.updateRawMaterialStock(id, (java.sql.Date) requiredate);
			return new ResponseEntity<>(message, HttpStatus.OK);

		}
		
		
		//ProductOrder-->  
	    @GetMapping("/orderlist")
	    public ResponseEntity<List<CreateProductOrderRequest>> fetchAll() {
	        List<ProductOrderEntity>  orders = drinksAndDelightService.fetchAll();
	        List<CreateProductOrderRequest> list =ProductOrderUtil.toOrderEntityDto(orders);
	        ResponseEntity<List<CreateProductOrderRequest>> response = new ResponseEntity<>(list, HttpStatus.OK);
	        return response;
	    }
	    

	    @PostMapping("/addorder")
	    public ResponseEntity<ProductOrderDetails> addProduct(@RequestBody CreateProductOrderRequest requestData) {
	        ProductOrderEntity order = ProductOrderUtil.toOrderEntity(requestData);
	        order = drinksAndDelightService.save(order);
	        ProductOrderDetails details=ProductOrderUtil.toOrderDetails(order);
	        ResponseEntity<ProductOrderDetails> response = new ResponseEntity<>(details, HttpStatus.OK);
	        return response;
	    }


	    @GetMapping("/getorder/{id}")
	    public ResponseEntity<ProductOrderDetails> trackOrder(@PathVariable("id") String id)  {
	        ProductOrderEntity order = drinksAndDelightService.trackOrder(id);
	        ProductOrderDetails details=ProductOrderUtil.toOrderDetails(order);
	        return new ResponseEntity<ProductOrderDetails>(details, HttpStatus.OK);
	    }
	    
	    @DeleteMapping("/deleteorder/{orderId}")
	    public ResponseEntity<Boolean> deleteOrder(@PathVariable("OrderId") String orderId) {
	        boolean result = drinksAndDelightService.deleteOrder(orderId);
	        ResponseEntity respose = new ResponseEntity(result, HttpStatus.OK);
	        return respose;
	        
	    }
	    
	    
	    //Raw Material Order--> 
	  
	 @RequestMapping(value="/orders",method=RequestMethod.POST) 
		public RawMaterialOrder addRawMaterialOrder(@RequestBody RawMaterialOrder rawMaterialOrder) {
			return drinksAndDelightService.addRawMaterialOrder(rawMaterialOrder);
		}
	 
	 
	 @GetMapping(value="/orders/list")
		public List<RawMaterialOrder> listOfOrders(){
			return drinksAndDelightService.displayRawMaterialOrder();
		}
	 
	 
	 
	 @DeleteMapping(value="/orders/{orderId}")
	    public boolean cancelOrder(@PathVariable("orderId") int orderId) {
		 drinksAndDelightService.cancelOrder(orderId);
		  return true;
	}
	 
	 
	 @GetMapping(value="/orders/{orderId}")
		public RawMaterialOrder getRawMaterialOrder(@PathVariable("orderId") int orderId){
		 RawMaterialOrder rawMaterialOrder=drinksAndDelightService.getRawMaterialOrder(orderId);
	           return rawMaterialOrder;
		}
	 
	

	 @PutMapping(value="/orders")
	 public RawMaterialOrder updateOrders(@RequestBody RawMaterialOrder rawMaterialOrder) {
			return drinksAndDelightService.updateRawMaterialOrder(rawMaterialOrder);		
			}
}