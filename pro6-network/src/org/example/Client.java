package org.example;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) {

//      if(args.length!=1) {
//         System.out.println("USAGE: java TcpIpMultichatClient 대화명");
//         System.exit(0);
//      }

        try {
            String serverIp = "127.0.0.1";
            // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, 5445); 					//ip주소, 포트번호로 소켓 생성 -> 서버 연결 요청
            System.out.println("서버에 연결되었습니다.");
            Thread sender   = new Thread(new ClientSender(socket, "client2")); 	//clientSender객체 생성, 생성된 소켓과 이름 전달 -> 객체를 sender 스레드로 사용
            Thread receiver = new Thread(new ClientReceiver(socket)); 		//clientReceiver객체 생성

            sender.start(); 	//sender 스레드 시작 : 메시지 전송 역할
            receiver.start(); 	//receiver스레드 시작 : 메시지 수신 역할

        } catch(ConnectException ce) {
            ce.printStackTrace();
        } catch(Exception e) {}
    } // main



    static class ClientSender extends Thread { //client가 서버로 메시지를 전송하는 thread

        Socket socket; 		//소켓 : 서버와의 연결 나타냄
        DataOutputStream out; //서버로 데이터를 전송하기 위해 사용
        String name; 			//client의 이름 변수

        ClientSender(Socket socket, String name) {

            this.socket = socket;

            try {
                out = new DataOutputStream(socket.getOutputStream()); //소켓의 출력스트림 생성 -> out변수에 할당 : 서버로 데이터 전송 가능
                this.name = name;
            } catch(Exception e) {}
        }



        public void run() {

            Scanner scanner = new Scanner(System.in); //사용자의 입력을 읽기 위한 scanner객체 생성

            try {
                if(out != null) { 		//출력스트림이 null이 아닌 경우에만
                    out.writeUTF(name);  //client의 이름을 서버로 전송
                }

                while(out!=null) {
                    out.writeUTF("["+name+"]"+scanner.nextLine()); //입력한 메시지를 client의 이름과 함께 서버로 전송
                }
            } catch(IOException e) {}

        }
    } // ClientSender

    static class ClientReceiver extends Thread {

        Socket socket; 		//서버와의 연결 나타냄
        DataInputStream in;   //서버로 부터 데이터를 읽기 위한 입력스트림

        ClientReceiver(Socket socket) {

            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
            } catch(IOException e) {}
        }

        public void run() {

            while(in!=null) {

                try {
                    System.out.println(in.readUTF()); //서버로 수신한 메시지를 콘솔에 출력
                } catch(IOException e) {}
            }
        } // run
    } // ClientReceiver
} // class