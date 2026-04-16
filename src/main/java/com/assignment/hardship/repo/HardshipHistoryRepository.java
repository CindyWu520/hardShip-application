package com.assignment.hardship.repo;

import com.assignment.hardship.entity.HardshipHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardshipHistoryRepository extends JpaRepository<HardshipHistory, Long> {
}
