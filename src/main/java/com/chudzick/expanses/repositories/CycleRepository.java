package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {
}
