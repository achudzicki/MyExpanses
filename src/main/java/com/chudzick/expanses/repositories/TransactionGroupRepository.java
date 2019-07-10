package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionGroupRepository extends JpaRepository<TransactionGroup, Long> {

    List<TransactionGroup> findAllByAppUser(AppUser appUser);

    Optional<TransactionGroup> findByIdAndAppUser(long groupId, AppUser appUser);
}
