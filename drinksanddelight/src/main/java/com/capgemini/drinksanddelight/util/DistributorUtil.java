package com.capgemini.drinksanddelight.util;

import com.capgemini.drinksanddelight.dto.CreateDistributorRequest;
import com.capgemini.drinksanddelight.entities.DistributorEntity;

public class DistributorUtil {


    public static DistributorEntity convertToDistributor(CreateDistributorRequest disdto) {
        DistributorEntity distributordetails = new DistributorEntity();
        distributordetails.setDistributor_Id(disdto.getDistributor_Id());
        distributordetails.setDistributor_Name(disdto.getDistributor_Name());
        distributordetails.setDistributor_Address(disdto.getDistributor_Address());
        distributordetails.setDistributor_Phn(disdto.getDistributor_Phn());
        return distributordetails;
    }
}
