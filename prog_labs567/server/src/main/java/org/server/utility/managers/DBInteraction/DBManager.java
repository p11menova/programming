package org.server.utility.managers.DBInteraction;

import org.common.models.Coordinates;
import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.DBModels.UserData;
import org.common.models.Person;
import org.common.models.Ticket;
import org.common.models.TicketType;
import org.server.App;
import org.server.models.Tickets2;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;

public class DBManager {
    private Connection connection;
    private final String host;
    private final String login;
    private final String password;
    public Statement st;
    public Tickets2 selectedTickets;
    public DBManager(String host, String login, String password){
        this.host = host;
        this.login = login;
        this.password = password;
        this.selectedTickets = new Tickets2();
    }
    public HashMap<String, String> users = new HashMap<>();
    public void connectToDB() throws SQLException{

        Properties auth = new Properties();
       // String DB_URL = "jdbc:postgresql://localhost:5432/studs";
        auth.put("user", login);
        auth.put("password", password);

        this.connection = DriverManager.getConnection(host, auth);
        System.out.println("successfully connected to DB \n --DB_HOST: "+ host + " \n --LOGIN: " + login);
        App.logger.info("successfully connected to DB \n --DB_HOST: "+ host + " \n --LOGIN: " + login);

        this.st = connection.createStatement();


        createTables();
        createTriggerForDeleting(); // триггер сохраняющий айди удаленных элементов


    }

    public void createTables() throws SQLException {
        App.logger.info("=======PREPARING WORKS WITH DATABASE=======");

        st.execute(DBCommands.createTableUsers());
        System.out.println("successfully created table Users");
        App.logger.info("successfully created table Users");

        st.execute(DBCommands.createTableTickets());
        System.out.println("successfully created table Tickets");
        App.logger.info("successfully created table Tickets");



        selectUsers();
        App.logger.info("добавлены юзеры из БД");
        selectTickets();
        App.logger.info("добавлены экземпляры Ticket из БД");
        App.logger.info("=======READY FOR DATABASE INTERACTION=======");

    }
    public void createTriggerForDeleting() throws SQLException{
        st.execute(DBCommands.createTriggerForDeleting());
    }
    public int insertIntoUsers(UserData userData) throws SQLException {
        st.execute(DBCommands.insertIntoUsers(userData));
        ResultSet rs = st.executeQuery(DBCommands.getUserIDByLogin(userData.login));
        if (rs.next()){
            return Integer.parseInt(rs.getString("id"));
        }

        return -1;
    }
    public void insertIntoTickets(TicketWithMetadata ticket) throws SQLException{
        ResultSet rs = st.executeQuery(DBCommands.getUserIDByLogin(ticket.userData.login));
        if (rs.next()){
            String user_id = rs.getString("id");
            st.execute(DBCommands.insertIntoTickets(ticket, user_id));
        }

    }
    public static boolean loginUser(UserData userData, String password) {
        return Users.checkPassword(userData, password);
    }
    public void selectUsers() throws SQLException{
        ResultSet rs = st.executeQuery(DBCommands.getUsers());
        Users.users.clear();
        while (rs.next()){
            UserData userData = new UserData(Integer.parseInt(rs.getString("id")),
                    rs.getString("login"),
                    rs.getString("hashed_password"),
                    rs.getString("salt"));
            Users.register(userData);
        }
    }
    public String getUserIDByLogin(String login) throws SQLException{
        ResultSet rs = st.executeQuery(DBCommands.getUserIDByLogin(login));
        if (!rs.next()){
            throw new SQLException();
        }
        return rs.getString("id");


    }

    public Tickets2 selectTickets() throws SQLException {
        ResultSet rs = st.executeQuery(DBCommands.getTickets());
        selectedTickets.clear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (rs.next()){

            Ticket ticket = new Ticket();
            ticket.set_id(Integer.parseInt(rs.getString("id").trim()));
            ticket.set_name(rs.getString("name"));
            Coordinates coords = new Coordinates();
            coords.set_coords(rs.getString("coordinates"));
            ticket.set_coordinates(coords);
            String price = rs.getString("price");
            ticket.set_price(price == null ? null : Long.parseLong(price));
            LocalDateTime creation_date = LocalDateTime.parse(rs.getString("creation_date"), formatter);
            ZonedDateTime zdt = creation_date.atZone(ZoneId.of( "UTC"));
            ticket.set_creationData(zdt);
            ticket.set_refundable(rs.getString("is_refundable").startsWith("t"));
            ticket.set_type(TicketType.valueOf(rs.getString("type")));
            Person p = new Person();
            p.setHeight(Double.parseDouble(rs.getString("person_height")));
            p.setBirthday(LocalDateTime.parse(rs.getString("person_birthday"), formatter));
            ticket.set_person(p);

            TicketWithMetadata ticketWithMetadata = new TicketWithMetadata(ticket);
            ticketWithMetadata.setUserData(new UserData());
            ticketWithMetadata.setUserId(Integer.parseInt(rs.getString("user_id")));
            selectedTickets.addToList(ticketWithMetadata);

        }
        return selectedTickets;
    }
    public void updateTicket(TicketWithMetadata ticket1) throws SQLException {
        st.execute(DBCommands.updateTicket(ticket1, String.valueOf(ticket1.userData.id)));
    }
    public void deleteById(int id) throws SQLException{
        st.execute(DBCommands.deleteFromTicketsById(String.valueOf(id)));
        st.execute(DBCommands.clearDeletedRows());

    }
    public String clearByUser(int id) throws SQLException{
        StringBuilder res = new StringBuilder();

        st.execute(DBCommands.clearByUser(id)); //удаляем записи
        ResultSet rs = st.executeQuery(DBCommands.getDeletedIds()); // добавляем айдишники удаленных строк в табличк deleted_rows
        while (rs.next()){
            res.append(rs.getString("id")).append(" ");
        }
        st.execute(DBCommands.clearDeletedRows()); // очищаем табличк deleted_rows
        return res.toString();
    }
}
