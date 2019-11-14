package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConstantTransactionRepository extends JpaRepository<ConstantTransaction, Long> {

    List<ConstantTransaction> findAllByAppUserAndCyclesOrderByIdDesc(AppUser appUser, Cycle cycle);

    List<ConstantTransaction> findAllByAppUserAndActiveOrderByIdDesc(AppUser appUser, boolean active);

    int countAllByTransactionGroupId(long groupId);

    List<ConstantTransaction> findAllByTransactionGroupId(long groupId);

    Optional<ConstantTransaction> findById(long id);

    List<ConstantTransaction> findAllByAppUser(AppUser appUser);
}
