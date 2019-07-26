package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstantTransactionRepository extends JpaRepository<ConstantTransaction, Long> {

    List<ConstantTransaction> findAllByAppUser(AppUser appUser);
}
