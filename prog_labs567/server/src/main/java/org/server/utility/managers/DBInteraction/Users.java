package org.server.utility.managers.DBInteraction;

import org.common.models.DBModels.UserData;

import java.util.Comparator;
import java.util.Objects;
import java.util.TreeMap;

public class Users {
    public static TreeMap<String, UserData> users = new TreeMap<>();

    public synchronized static boolean isUserExists(String login) {
        return users.get(login) != null;
    }
    public synchronized static boolean checkPassword(UserData userData, String hashedPassword){
        UserData previousUser = users.get(userData.login);
        System.out.println(previousUser.getHashedPassword()+previousUser.getSalt()+" "+ hashedPassword+previousUser.getSalt());
        return Objects.equals(previousUser.getHashedPassword()+previousUser.getSalt(), hashedPassword+previousUser.getSalt());

    }
    public synchronized static boolean register(UserData user){
        if (users.get(user.login) == null) {
            users.put(user.login, user);
            return true;
           }
        return false;

    }
    public synchronized static UserData getById(int id){
        for (UserData userData:users.values()){
            if (userData.id == id) return userData;
        }
        return null;
    }

    public static String String() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%6s | %16s", "id", "login"));
        sb.append("\n--------------------------\n");
        users.values()
                .stream()
                .sorted(Comparator.comparingInt(UserData::get_id))
                .forEach(v -> sb.append(String.format("%6s | %16s \n", v.get_id(), v.login)));

        return sb.toString();
    }
}

