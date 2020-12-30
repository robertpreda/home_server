package com.example.client;

import java.net.Socket;

public class SocketHandler {
    private static Socket socket;
    private static int HEADER = 64;

    public static synchronized Socket getSocket(){
        return socket;
    }
    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
    public static synchronized int getHeader(){ return SocketHandler.HEADER;}

}
