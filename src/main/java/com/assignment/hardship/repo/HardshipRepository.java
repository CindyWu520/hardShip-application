package com.assignment.hardship.repo;

import com.assignment.hardship.entity.Hardship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardshipRepository extends JpaRepository<Hardship, Long> {
    boolean existsByCustomerName(String name); // count() from hardship where hardship.customer.name := name
}
