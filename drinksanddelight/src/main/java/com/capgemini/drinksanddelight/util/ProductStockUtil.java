package com.capgemini.drinksanddelight.util;

import com.capgemini.drinksanddelight.dto.CreateProductRequest;
import com.capgemini.drinksanddelight.dto.ProductStockDetails;
import com.capgemini.drinksanddelight.entities.ProductStockEntity;

import java.time.LocalDate;

public class ProductStockUtil {


    public static ProductStockDetails toDetails(ProductStockEntity entity){
        ProductStockDetails details=new ProductStockDetails();
        details.setStockId(entity.getStockId());
        String expiryDateText=DateUtil.toString(entity.getExpiryDate());
        details.setExpiryDate(expiryDateText);
        String manufactureDateText=DateUtil.toString(entity.getManufactureDate());
        details.setManufactureDate(manufactureDateText);
        details.setName(entity.getName());
        details.setQuantityUnit(entity.getQuantityUnit());
        details.setPricePerUnit(entity.getPricePerUnit());
        details.setQuantityValue(entity.getQuantityValue());
        details.setWarehouseId(entity.getWarehouseId());
        details.setSupplierId(entity.getSupplierId());
        details.setQualityCheck(entity.getQualityCheck());
        return details;
    }


    public static ProductStockEntity toEntity(CreateProductRequest requestData) {
        ProductStockEntity stockdetails = new ProductStockEntity();
        stockdetails.setStockId(requestData.getStockId());
        stockdetails.setName(requestData.getName());
        stockdetails.setPricePerUnit(requestData.getPricePerUnit());
        stockdetails.setQuantityUnit(requestData.getQuantityUnit());
        stockdetails.setQuantityValue(requestData.getQuantityValue());
        stockdetails.setSupplierId(requestData.getSupplierId());
        String expiryDateText = requestData.getExpiryDate();
        LocalDate expiryDate = DateUtil.toLocalDate(expiryDateText);
        stockdetails.setExpiryDate(expiryDate);
        stockdetails.setWarehouseId(requestData.getWarehouseId());
        String manufactureDateText = requestData.getManufactureDate();
        LocalDate manufactureDate = DateUtil.toLocalDate(manufactureDateText);
        stockdetails.setManufactureDate(manufactureDate);
        stockdetails.setQualityCheck(requestData.getQualityCheck());
        return stockdetails;
    }

}
