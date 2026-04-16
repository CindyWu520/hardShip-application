package com.assignment.hardship.mapper;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.entity.Customer;
import com.assignment.hardship.entity.Hardship;
import com.assignment.hardship.entity.HardshipHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HardshipMapper {
    @Mapping(target = "id", ignore = true)
    Customer buildCustomer(HardshipRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Hardship buildHardship(HardshipRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hardship", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    HardshipHistory buildHardshipHistory(HardshipRequest hardshipRequest);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "name")
    @Mapping(source = "customer.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "customer.income", target = "income")
    @Mapping(source = "customer.expenses", target = "expenses")
    @Mapping(source = "id", target = "hardshipId")
    HardshipResponse buildResponse(Hardship hardship);
}
