package org.server.commands.serverCommands;

import org.common.interaction.Response;
import org.common.interaction.UserAuthStatus;
import org.common.models.DBModels.UserData;
import org.server.App;
import org.server.utility.managers.DBInteraction.DBManager;
import org.server.utility.managers.DBInteraction.Users;

import java.sql.SQLException;

public class RegisterUserCommand extends AuthorizationCommand {

    public RegisterUserCommand(DBManager dbManager) {
        super("register", dbManager);
    }

    @Override
    public Response execute(UserData userData) {
        if (!Users.register(userData)) return new Response(UserAuthStatus.ALREADY_EXISTS, "пользователь с логином: " + userData.login +" уже существует(" );
        try {
            System.out.println("inserting into users id="+userData.id);
            int id = this.dbManager.insertIntoUsers(userData); //добавляем + получаем ид нового юзера
            if (id > -1) {
                userData.set_id(id);
                Users.register(userData);
            } else throw new SQLException();

            App.logger.info("successfully inserted user" + userData.login +"into users");
        } catch (SQLException e) {
            return new Response(UserAuthStatus.ERROR, "не удалось добавить юзера в БД");
        }
        App.logger.info("пользователь: " + userData.login +" успешно зарегистрирован!");
        return new Response(UserAuthStatus.OK, "пользователь: " + userData.login +" успешно зарегистрирован!");
    }
}
