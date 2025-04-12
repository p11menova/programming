package org.client.utility;

import org.common.interaction.*;
import org.client.exceptions.WrongFileRightException;
import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.DBModels.UserData;
import org.common.models.Ticket;

import java.io.*;
import java.net.ConnectException;

public class Client {
    public ConnectorWithServer connector;
    public ObjectOutputStream serverWriter;
    public ObjectInputStream serverReader;
    private final Console console;
    private UserData currentUser;
    boolean authUser = false; // авторизован ли юзер
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;
    public static int RECONNECTION_TIMEOUT = 2 * 1000;
    public int reconnectionAttempts;
    public ClientStatus clientStatus = ClientStatus.ACTIVE;

    public Client(String host, int port, Console console) {
        this.connector = new ConnectorWithServer(host, port);
        this.console = console;
    }

    public boolean setConnection() {
        try {
            if (this.reconnectionAttempts > 0) console.println("переподключение к серверу...");

            connector.connectToServer();
            console.println("подключение к серверу установлено!");

            connectDataChannels();
            console.println("обмен данными разрешен!");

            this.reconnectionAttempts = 0;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void connectDataChannels() throws IOException {
        this.serverWriter = connector.getServerWriter();
        this.serverReader = connector.getServerReader();

    }

    public void disconnectToServer() {
        try {
            connector.getSocketChannel().socket().close();
            serverReader.close();
            serverWriter.close();
        } catch (IOException e) {
            console.println("произошла ошибка при отключении от сервера(\nпринудительное завершение работы...");
            System.exit(1);
        }
    }

    public Status processServerResponse() throws IOException, ClassNotFoundException {
        Response serverResponse = (Response) serverReader.readObject();
        if (serverResponse.getResponseStatus() == ResponseStatus.EXIT) {
            clientStatus = ClientStatus.EXIT;
            console.println(serverResponse.getResponseBody());
            return ResponseStatus.EXIT;
        }
        if (serverResponse.getResponseStatus() == ResponseStatus.ERROR ||( serverResponse.getResponseStatus() == ResponseStatus.STOP_SCRIPT && console.consoleMode == ConsoleMode.INTERACTIVE)) {
            console.print_error(serverResponse.getResponseBody());
        } else {console.println(serverResponse.getResponseBody());}

        if (serverResponse.getResponseStatus() == ResponseStatus.SCRIPT) {
            try {
                console.setScriptMode(serverResponse.getResponseBody());
            } catch (FileNotFoundException e) {
                console.print_error("файл с таким названием не найден..");
            } catch (WrongFileRightException e) {
                console.print_error("файл не имеет прав на чтение");
            } catch (RuntimeException e) {
                console.print_error(e.getMessage());
                disconnectToServer();
                System.exit(1);
            }
        }
        if (serverResponse.getResponseStatus() == ResponseStatus.STOP_SCRIPT && console.consoleMode == ConsoleMode.SCRIPT) {
            console.print_error(serverResponse.getResponseBody());
            console.println("ОБРАБОТКА СКРИПТА БУДЕТ ЗАВЕРШЕНА");
            console.denyFileMode();
        }

        if (serverResponse.getResponseStatus() == ResponseStatus.OBJECT) {
            Ticket ticket = console.makeNewTicket(Integer.parseInt(console.request.getCommandStringArg().trim()));
            sendTicketObjectToServer(ticket);
            processServerResponse();}
        if (serverResponse.getResponseStatus() == UserAuthStatus.NO_SUCH_LOGIN){
            authUser = false;
        }

        return serverResponse.getResponseStatus();
    }

    public void sendTicketObjectToServer(Ticket ticket) throws IOException {
        TicketWithMetadata ticketWithMetadata = new TicketWithMetadata(ticket);
        ticketWithMetadata.setUserData(currentUser);
        serverWriter.writeObject(ticketWithMetadata);
        serverWriter.flush();

    }

    public boolean authUser() throws IOException, ClassNotFoundException {
        UserAuthStatus userAuthStatus = null;
        while (userAuthStatus != UserAuthStatus.OK){
            currentUser = console.getUserData();
            sendRequestToServer(new Request(currentUser.loginOrRegister, currentUser));
            userAuthStatus = (UserAuthStatus) processServerResponse();}
        return true;
    }

    public boolean sendRequestToServer(Request request) throws IOException {
        request.setUserData(currentUser);
        serverWriter.writeObject(request);
        serverWriter.flush();

        return true;

    }

    public boolean startClientServerInteraction() throws IOException, ClassNotFoundException {
        try{
        Request request = console.getRequest();
        request.setUserData(currentUser);
        return sendRequestToServer(request) && (processServerResponse() != ResponseStatus.EXIT);}
        catch (IOException e) {
            if (!connector.getSocketChannel().socket().getKeepAlive())
                throw new ConnectException(); // упал на моменте соединения с сервером
            throw new IOException();
        }

    }

    public void go() {
        boolean processingStatus = true;

        while (processingStatus) {
            try {
                if (connector.getSocketChannel()==null) setConnection() ;
                if (!authUser) authUser = authUser();
                processingStatus = startClientServerInteraction();
                if (clientStatus == ClientStatus.EXIT) break;
            } catch (IOException e) {
                if (reconnectionAttempts == 0) console.println("разрыв соединения с сервером(");
                reconnectionAttempts++;
                while (!setConnection()) {
                    if (reconnectionAttempts > MAX_RECONNECTION_ATTEMPTS) {
                        console.print_error("превышен лимит попыток подключения.\ncервер временно не доступен.(..\nповторите попытку позже..");
                        break;
                    }
                    console.println("повторная попытка подключения через " + RECONNECTION_TIMEOUT / 1000 + "c");
                    try {
                        Thread.sleep(RECONNECTION_TIMEOUT);
                    } catch (InterruptedException ex) {
                        continue;
                    }
                    reconnectionAttempts++;
                }

//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//                System.out.println(e.getClass());
//                console.print_error("ошибка при передаче данных на сервер..");
            } catch (ClassNotFoundException e) {
                console.print_error("ошибка при десериализации( такого типа данных нет(");
            }
            if (!processingStatus) disconnectToServer();
        }
    }
}

