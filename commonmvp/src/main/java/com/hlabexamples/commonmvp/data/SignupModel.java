package com.hlabexamples.commonmvp.data;

/**
 * Created in BindingConstraintMVP-Demo on 11/01/17.
 */

public class SignupModel {
    private String username, password, confirmPassword;

    public SignupModel() {
    }

    public SignupModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
