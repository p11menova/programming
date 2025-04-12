package org.client.utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ConnectorWithServer {

    public String host;
    public int port;
    private SocketChannel socketChannel;

    public ConnectorWithServer(String host, int port) {
        this.host = host;
        this.port = port;

    }

    public void connectToServer() throws IOException{
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(this.host, this.port));

    }

    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }


    public ObjectOutputStream getServerWriter() throws IOException {
        try {
            return new ObjectOutputStream(socketChannel.socket().getOutputStream());
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public ObjectInputStream getServerReader() throws IOException {
        try {
            return new ObjectInputStream(socketChannel.socket().getInputStream());
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
