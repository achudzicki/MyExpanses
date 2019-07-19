package com.chudzick.expanses.domain.settings;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "user_settings")
@Entity
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int cycleDays;
    private BigDecimal cycleSaveGoal;
    private boolean automaticExtension;

    @OneToOne
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;

    public boolean isAutomaticExtension() {
        return automaticExtension;
    }

    public void setAutomaticExtension(boolean automaticExtension) {
        this.automaticExtension = automaticExtension;
    }

    public Long getId() {
        return id;
    }

    public int getCycleDays() {
        return cycleDays;
    }

    public void setCycleDays(int cycleDays) {
        this.cycleDays = cycleDays;
    }

    public BigDecimal getCycleSaveGoal() {
        return cycleSaveGoal;
    }

    public void setCycleSaveGoal(BigDecimal cycleSaveGoal) {
        this.cycleSaveGoal = cycleSaveGoal;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
