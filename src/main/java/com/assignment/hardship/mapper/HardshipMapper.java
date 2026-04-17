package com.assignment.hardship.mapper;

import com.assignment.hardship.dto.HardshipRequest;
import com.assignment.hardship.dto.HardshipResponse;
import com.assignment.hardship.dto.HardshipSummaryResponse;
import com.assignment.hardship.entity.Customer;
import com.assignment.hardship.entity.Hardship;
import com.assignment.hardship.entity.HardshipHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HardshipMapper {
    @Mapping(target = "id", ignore = true)
    Customer buildCustomer(HardshipRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Hardship buildHardship(HardshipRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hardship", ignore = true)
    @Mapping(target = "changedAt", ignore = true)
    HardshipHistory buildHardshipHistory(HardshipRequest request);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "name")
    @Mapping(source = "customer.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "customer.income", target = "income")
    @Mapping(source = "customer.expenses", target = "expenses")
    @Mapping(source = "id", target = "hardshipId")
    HardshipResponse buildResponse(Hardship hardship);

    @Mapping(target = "id", ignore = true)
    void updateCustomer(HardshipRequest request, @MappingTarget Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateHardship(HardshipRequest request, @MappingTarget Hardship hardship);

    @Mapping(source = "id", target = "hardshipId")
    @Mapping(source = "customer.name", target = "name")
    HardshipSummaryResponse buildSummaryResponse(Hardship hardship);
}
