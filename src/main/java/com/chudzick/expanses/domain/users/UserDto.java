package com.chudzick.expanses.domain.users;

import com.chudzick.expanses.validators.register.PasswordMatches;
import com.chudzick.expanses.validators.register.ValidEmail;
import com.chudzick.expanses.validators.register.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserDto {

    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    private String lastName;
    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    private String name;
    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    @Size(min = 6,max = 20,message = "{form.validation.wrong.login.length}" )
    private String login;
    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    @ValidPassword
    private String password;
    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    private String repeatedPassword;
    private String gender;
    @NotEmpty(message = "{form.validation.not.empty}")
    @NotNull
    @ValidEmail
    private String email;

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

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
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
}
