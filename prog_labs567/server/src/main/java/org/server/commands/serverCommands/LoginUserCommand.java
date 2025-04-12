package org.server.commands.serverCommands;

import org.common.interaction.Response;
import org.common.interaction.UserAuthStatus;
import org.common.models.DBModels.UserData;
import org.server.App;
import org.server.utility.managers.DBInteraction.DBManager;
import org.server.utility.managers.DBInteraction.Users;

import java.sql.SQLException;

public class LoginUserCommand extends AuthorizationCommand {
    public LoginUserCommand(DBManager dbManager) {
        super("login", dbManager);
    }

    @Override
    public Response execute(UserData userData) {
        if (!Users.isUserExists(userData.login))
            return new Response(UserAuthStatus.NO_SUCH_LOGIN, "пользователя с логином " + userData.login + " еще не существует(");
        if (DBManager.loginUser(userData, userData.getHashedPassword())) {
            try {
                userData.set_id(Integer.parseInt(dbManager.getUserIDByLogin(userData.login)));
                App.logger.info("пользователь: " + userData.login + " успешно авторизовался");
                dbManager.selectUsers();
                return new Response(UserAuthStatus.OK, "пользователь: " + userData.login + " успешно авторизовался");
            } catch (SQLException e) {
                return new Response(UserAuthStatus.ERROR, "ошибка авторзации. повторите попытку");
            }
        }
        return new Response(UserAuthStatus.WRONG_PASSWORD, "пароли не совпадают(\nповторите попытку авторизации");
    }
}
