package com.chudzick.expanses.domain.settings.dto;

import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.validators.register.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserProfileSettingsDto {
    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    private String lastName;
    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    private String name;
    @NotEmpty(message = "{form.validation.not.empty}")
    @NotNull
    @ValidEmail
    private String email;

    public static UserProfileSettingsDto fromAppUser(AppUser currentUser) {
        UserProfileSettingsDto userProfileSettingsDto = new UserProfileSettingsDto();
        userProfileSettingsDto.setEmail(currentUser.getEmail());
        userProfileSettingsDto.setLastName(currentUser.getLastName());
        userProfileSettingsDto.setName(currentUser.getName());
        return userProfileSettingsDto;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
