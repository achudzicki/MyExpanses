package com.chudzick.expanses.domain.savings;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;

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
}
