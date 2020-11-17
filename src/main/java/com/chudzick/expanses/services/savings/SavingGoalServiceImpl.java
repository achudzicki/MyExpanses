package com.chudzick.expanses.services.savings;

import com.chudzick.expanses.beans.notifications.MainPageNotificationBean;
import com.chudzick.expanses.beans.savings.SavingGoalBean;
import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.savings.*;
import com.chudzick.expanses.domain.savings.dto.SavingGoalDto;
import com.chudzick.expanses.domain.savings.dto.SavingGoalView;
import com.chudzick.expanses.domain.savings.dto.SavingPaymentDto;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.InvitationNotFoundException;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.factories.savings.SavingGoalStaticFactory;
import com.chudzick.expanses.factories.savings.SavingPaymentStaticFactory;
import com.chudzick.expanses.repositories.SavingGoalRepository;
import com.chudzick.expanses.repositories.SavingGoalRequestRepository;
import com.chudzick.expanses.repositories.SavingPaymentRepository;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.users.UserService;
import com.chudzick.expanses.util.ListsUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SavingGoalServiceImpl implements SavingGoalService {

    @Autowired
    private SavingGoalRepository savingGoalRepository;

    @Autowired
    private SavingPaymentRepository savingPaymentRepository;

    @Autowired
    private SavingGoalRequestRepository requestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private MainPageNotificationBean mainPageNotificationBean;

    @Override
    @Transactional
    public List<SavingGoal> findAllUserSavingGoals() {
        AppUser appUser = userService.getCurrentLogInUser();
        return savingGoalRepository.findAllByAppUsers(appUser);
    }

    @Override
    public SavingGoal addNewGoal(SavingGoalDto savingGoalDto) {
        AppUser appUser = userService.getCurrentLogInUser();
        SavingGoal savingGoal = SavingGoalStaticFactory.createNewFromDto(savingGoalDto, appUser);
        return savingGoalRepository.save(savingGoal);
    }

    @Override
    @Transactional
    public SavingGoalBean prepareGoalBean() {
        AppUser currentUser = userService.getCurrentLogInUser();
        BigDecimal savingSum = BigDecimal.ZERO;
        BigDecimal savingPaymentSum = BigDecimal.ZERO;
        BigDecimal actualSavings = BigDecimal.ZERO;
        SavingGoalBean savingGoalBean = new SavingGoalBean();
        final ActualTransactionStatsFactory statsFactory = new ActualTransactionStatsFactory();
        List<SavingGoal> userSavingGoals = findAllUserSavingGoals();
        List<Cycle> allUserCycles = cycleService.findAllUserCycles();
        List<SavingGoalView> savingGoalViews = new LinkedList<>();
        List<SavingGoalView> closedSavingGoalViews = new LinkedList<>();

        for (Cycle cycle : allUserCycles) {
            if (!cycle.isActive()) {
                List<UserTransactions> allTransactions = ListsUnion.union(cycle.getConstantTransactions(), cycle.getCycleTransactions());
                ActualTransactionStats stats = statsFactory.fromTransactionList(allTransactions, Optional.of(cycle));
                actualSavings = actualSavings.add(stats.getBalance().add(cycle.getSaveGoal()));
            }
        }
        for (SavingGoal saving : userSavingGoals) {
            BigDecimal userPaymentsSum = BigDecimal.ZERO;
            savingSum = savingSum.add(saving.getCurrentlySaved());
            for (SavingPayment payment : saving.getSavingPayments()) {
                if (payment.getSavingPaymentType().equals(SavingPaymentType.ADD)) {
                    if (payment.getAppUser().getId().equals(currentUser.getId())) {
                        userPaymentsSum = userPaymentsSum.add(payment.getAmount());
                        savingPaymentSum = savingPaymentSum.add(payment.getAmount());
                    }
                } else {
                    if (payment.getAppUser().getId().equals(currentUser.getId())) {
                        userPaymentsSum = userPaymentsSum.subtract(payment.getAmount());
                        savingPaymentSum = savingPaymentSum.subtract(payment.getAmount());
                    }
                }
            }

            LocalDate now = LocalDate.now();
            if ((saving.getDateFrom().isBefore(now) || saving.getDateFrom().isEqual(now))
                    && (saving.getDateTo().isAfter(now) || saving.getDateTo().isEqual(now))) {
                savingGoalViews.add(new SavingGoalView(saving, userPaymentsSum));
            } else {
                closedSavingGoalViews.add(new SavingGoalView(saving, userPaymentsSum));
            }
        }

        savingGoalBean.setSavingSum(savingSum);
        savingGoalBean.setSavingToAllocate(actualSavings.subtract(savingPaymentSum));
        savingGoalBean.setUserSavingGoals(savingGoalViews);
        savingGoalBean.setClosedSavingsGoals(closedSavingGoalViews);
        savingGoalBean.setGoalRequests(requestRepository.findAllByRequestOwnerAndInvitationStatus(currentUser, InvitationStatus.PENDING));
        return savingGoalBean;
    }

    @Override
    @Transactional
    public void addPaymentToGoal(Long goalId, SavingPaymentDto savingPaymentDto) {
        AppUser appUser = userService.getCurrentLogInUser();
        SavingGoal goal = savingGoalRepository.getOne(goalId);
        setUpPayment(savingPaymentDto, goal);
        Optional<SavingPayment> paymentOptional = SavingPaymentStaticFactory.createFromDto(savingPaymentDto, appUser, goal);
        if (!paymentOptional.isPresent()) {
            return;
        }
        SavingPayment payment = savingPaymentRepository.save(paymentOptional.get());
        goal.getSavingPayments().add(payment);
        savingGoalRepository.save(goal);
    }

    @Override
    public SavingGoal finById(long goalId) {
        return savingGoalRepository.getOne(goalId);
    }

    @Override
    public void deleteGoalById(Long goalId) {
        SavingGoal goalToDelete = savingGoalRepository.getOne(goalId);
        List<SavingGoalRequest> goalInvitations = requestRepository.findAllBySavingGoal(goalToDelete);
        goalInvitations.forEach(invitation -> requestRepository.deleteById(invitation.getId()));
        savingGoalRepository.delete(goalToDelete);
    }

    @Override
    public List<AppUser> findAvailableUsers(long goalId) {
        List<AppUser> allUsers = userService.findAll();
        SavingGoal savingGoal = savingGoalRepository.getOne(goalId);
        HashSet<Long> goalUsersIds = new HashSet<>();

        savingGoal.getAppUsers().forEach(user -> goalUsersIds.add(user.getId()));
        return allUsers.stream().filter(user -> !goalUsersIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void inviteUser(long goalId, long userId) {
        AppUser current = userService.getCurrentLogInUser();
        AppUser invitedUser = userService.findUserById(userId);
        SavingGoal goal = finById(goalId);

        SavingGoalRequest savingGoalRequest = new SavingGoalRequest();
        savingGoalRequest.setRequestOwner(invitedUser);
        savingGoalRequest.setSavingGoal(goal);
        savingGoalRequest.setSavingGoalOwner(current);
        savingGoalRequest.setRequestDate(LocalDate.now());
        savingGoalRequest.setInvitationStatus(InvitationStatus.PENDING);
        requestRepository.save(savingGoalRequest);
    }

    @Override
    public void acceptInvitation(long invitationId) throws InvitationNotFoundException {
        SavingGoalRequest invitation = requestRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(ApplicationActions.ACCEPT_INVITATION));
        AppUser currentUser = userService.getCurrentLogInUser();

        if (!invitation.getRequestOwner().getId().equals(currentUser.getId())) {
            throw new InvitationNotFoundException(ApplicationActions.ACCEPT_INVITATION, "Zaproszenie nie należy do użytkownika");
        }

        invitation.setInvitationStatus(InvitationStatus.ACCEPTED);
        SavingGoal goal = savingGoalRepository.getOne(invitation.getSavingGoal().getId());
        goal.getAppUsers().add(currentUser);

        requestRepository.save(invitation);
        savingGoalRepository.save(goal);
        mainPageNotificationBean.resetBean();
    }

    @Override
    public void rejectInvitation(long invitationId) throws InvitationNotFoundException {
        SavingGoalRequest invitation = requestRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(ApplicationActions.ACCEPT_INVITATION));

        AppUser currentUser = userService.getCurrentLogInUser();

        if (!invitation.getRequestOwner().getId().equals(currentUser.getId())) {
            throw new InvitationNotFoundException(ApplicationActions.ACCEPT_INVITATION, "Zaproszenie nie należy do użytkownika");
        }

        invitation.setInvitationStatus(InvitationStatus.DECLINED);
        requestRepository.save(invitation);
        mainPageNotificationBean.resetBean();
    }

    @Override
    public List<SavingGoalRequest> findUserSavingGoalRequests() {
        AppUser appUser = userService.getCurrentLogInUser();
        return requestRepository.findAllByRequestOwnerAndInvitationStatus(appUser, InvitationStatus.PENDING);
    }

    private void setUpPayment(SavingPaymentDto savingPaymentDto, SavingGoal goal) {
        if (savingPaymentDto.getType().equals(SavingPaymentType.ADD)) {
            if (savingPaymentDto.getAmount().add(goal.getCurrentlySaved()).compareTo(goal.getGoal()) > 0) {
                savingPaymentDto.setAmount(goal.getGoal().subtract(goal.getCurrentlySaved()));
            }
        } else {
            if (goal.getCurrentlySaved().subtract(savingPaymentDto.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
                savingPaymentDto.setAmount(goal.getCurrentlySaved());
            }
        }
    }
}
