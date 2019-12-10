package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.savings.InvitationStatus;
import com.chudzick.expanses.domain.savings.SavingGoalRequest;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingGoalRequestRepository extends JpaRepository<SavingGoalRequest, Long> {

    List<SavingGoalRequest> findAllByRequestOwnerAndInvitationStatus(AppUser appUser, InvitationStatus invitationStatus);
}
