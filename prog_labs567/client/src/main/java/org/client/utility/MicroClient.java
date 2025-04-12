//package org.example.utility;
//
//import org.example.interaction.Request;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
//public class MicroClient extends Client{
//    public MicroClient(int port, String host, UserHandler userHandler) {
//        super(port, host, userHandler);
//    }
//
//    @Override
//    public boolean connectToServer() {
//        return super.connectToServer();
//    }
//
//    @Override
//    public boolean sendRequest() {
//        Request request = this.userHandler.getRequest();
//        try {
//            System.out.println(request.getCommandName());
//            socketChannel.write(ByteBuffer.wrap(request.getCommandName().getBytes()));
//            socketChannel.socket().getOutputStream().flush();
//
//
//        } catch (IOException e) {
//            System.out.println("trouble");
//            return false;
//        }
//        return true;
//    }
//    public void run() {
//        connectToServer();
//        sendRequest();
//    }
//}
