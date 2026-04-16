package com.assignment.hardship.repo;

import com.assignment.hardship.entity.Hardship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface HardshipRepository extends JpaRepository<Hardship, Long> {
}
