//package org.example.utility;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class MicroServer extends Server{
//
//    public MicroServer(int port) {
//        super(port);
//    }
//
//    @Override
//    public void openServerSocket(){
//        try (ServerSocket serverSocket = new ServerSocket(this.port);
//             Socket clientSocket = serverSocket.accept();) {
//            Scanner in = new Scanner(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_16));
//            while (in.hasNextLine()){
//                System.out.println(in.nextLine());
//            }
//        } catch (IOException e) {
//            System.out.println("trouble in micro server");
//        }
//
//
//    }
//}
