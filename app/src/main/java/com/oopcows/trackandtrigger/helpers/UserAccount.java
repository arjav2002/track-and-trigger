package com.oopcows.trackandtrigger.helpers;

public class UserAccount {

    private String username;
    private String password;
    private String gmailId;
    private String phno;

    public UserAccount(String username, String password, String gmailId, String phno) {
        this.username = username;
        this.password = password;
        this.gmailId = gmailId;
        this.phno = phno;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGmailId() {
        return gmailId;
    }

    public String getPhno() {
        return phno;
    }

}
