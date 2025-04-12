package org.common.models.DBModels;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

import static org.common.interaction.PasswordHasher.encryptThisString;

public class UserData implements Serializable {
    public int id;
    public String loginOrRegister;
    public String login;
    private String password;
    public static int SALT_LENGTH = 9;
    public String salt;
    public String hashedPassword;

    public UserData(String loginOrRegister, String login, String password) {
        this.loginOrRegister = Objects.equals(loginOrRegister, "1") ? "login" : "register";
        this.login = login;
        this.password = password;
        this.salt = generateSalt();
        this.hashedPassword = encryptThisString(password);
    }
    public UserData(String login, String password) {
        this.login = login;
        this.password = password;
        this.salt = generateSalt();
        this.hashedPassword = encryptThisString(password);
    }
    public UserData(int id, String login, String hashedPassword, String salt){
        this.id = id;
        this.login = login;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.password = "";
    }
    public UserData(){}
    public int get_id(){
        return this.id;
    }
    public void set_id(int id){
        this.id = id;
    }


    public String getHashedPassword() {
        return hashedPassword;
    }
    public String getSalt() {
        return salt;
    }
    private String generateSalt() {
        int start = 40;
        int end = 127;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(SALT_LENGTH);
        for (int i = 0; i < SALT_LENGTH; i++) {
            int randomLimitedInt = start + (int)
                    (random.nextFloat() * (end - start + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

}
