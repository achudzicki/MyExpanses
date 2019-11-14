package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CycleRepository extends JpaRepository<Cycle, Long> {

    Optional<Cycle> findByAppUserAndActive(AppUser appUser, boolean active);

    List<Cycle> findAllByAppUser(AppUser appUser);

    long countCycleByAppUser(AppUser appUser);
}
