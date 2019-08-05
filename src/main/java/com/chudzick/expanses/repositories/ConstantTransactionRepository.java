package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstantTransactionRepository extends JpaRepository<ConstantTransaction, Long> {

    List<ConstantTransaction> findAllByAppUserAndCyclesOrderByIdDesc(AppUser appUser, Cycle cycle);

    List<ConstantTransaction> findAllByAppUserAndActiveOrderByIdDesc(AppUser appUser, boolean active);
}
