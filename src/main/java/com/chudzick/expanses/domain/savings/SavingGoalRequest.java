package com.chudzick.expanses.domain.savings;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity
@Table(name = "saving_goal_request")
public class SavingGoalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "saving_goal_id")
    private SavingGoal savingGoal;
    @OneToOne
    @JoinColumn(name = "inviting_id")
    private AppUser savingGoalOwner;
    @OneToOne
    @JoinColumn(name = "invited_id")
    private AppUser requestOwner;

    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;


    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SavingGoal getSavingGoal() {
        return savingGoal;
    }

    public void setSavingGoal(SavingGoal savingGoal) {
        this.savingGoal = savingGoal;
    }

    public AppUser getSavingGoalOwner() {
        return savingGoalOwner;
    }

    public void setSavingGoalOwner(AppUser savingGoalOwner) {
        this.savingGoalOwner = savingGoalOwner;
    }

    public AppUser getRequestOwner() {
        return requestOwner;
    }

    public void setRequestOwner(AppUser requestOwner) {
        this.requestOwner = requestOwner;
    }

    public long getDaysAgo() {
        return DAYS.between(requestDate,LocalDate.now());
    }
}
