//package org.example.utility;
//
//import org.example.interaction.Request;
//
//import java.util.Scanner;
//
//
//public class UserHandler {
//    private Scanner userScanner;
//    private Request request;
//    private boolean requestIsReady=false;
//    public UserHandler(Scanner userScanner){
//        this.userScanner = userScanner;
//    }
//    public void runInteractiveMode(){
//        if (userScanner.hasNextLine()) {
//            String in_data = this.userScanner.nextLine().trim() + " ";
//            if (in_data.equals(" ")){
//                console.println("пустой ввод :(( для получения информации о возможных командах введите 'help'");
//                }
//            else {
//                String[] in_data_splited = in_data.split(" ", 2);
//                String currentCommandName = in_data_splited[0];
//                String currentCommandArgs = in_data_splited[1];
//                this.request = new Request(currentCommandName, currentCommandArgs);
//                this.requestIsReady = true;
//            }
//        }
//    }
//    public Request getRequest(){
//         while (!this.requestIsReady)
//             runInteractiveMode();
//         return this.request;
//    }
//
//
//}
//
//
