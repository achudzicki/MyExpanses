package com.chudzick.expanses.domain.users;

import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.settings.UserSettings;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "appuser")
public class AppUser {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "roles")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "appuser_role",
            joinColumns = @JoinColumn(name = "appuser_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    private String name;
    private String lastName;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    private String gender;
    private String email;

    @OneToMany(
            mappedBy = "appUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SingleTransaction> transactionList;

    @OneToMany(
            mappedBy = "appUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ConstantTransaction> constantTransactions;


    @OneToMany(
            mappedBy = "appUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TransactionGroup> transactionGroups;

    @OneToOne(
            mappedBy = "appUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private UserSettings userSettings;

    @OneToMany(
            mappedBy = "appUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Cycle> cycle;


    public List<ConstantTransaction> getConstantTransactions() {
        return constantTransactions;
    }

    public void setConstantTransactions(List<ConstantTransaction> constantTransactions) {
        this.constantTransactions = constantTransactions;
    }

    public List<Cycle> getCycle() {
        return cycle;
    }

    public void setCycle(List<Cycle> cycle) {
        this.cycle = cycle;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public List<SingleTransaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<SingleTransaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<TransactionGroup> getTransactionGroups() {
        return transactionGroups;
    }

    public void setTransactionGroups(List<TransactionGroup> transactionGroups) {
        this.transactionGroups = transactionGroups;
    }

    public Long getId() {
        return id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) &&
                Objects.equals(login, appUser.login) &&
                Objects.equals(password, appUser.password) &&
                Objects.equals(email, appUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email);
    }
}
