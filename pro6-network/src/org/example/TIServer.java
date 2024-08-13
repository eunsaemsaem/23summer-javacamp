package org.example;

import java.net.*;
import java.io.*;
import java.util.*;

public class TIServer {

    HashMap clients; //client들의 이름과 DataOutputStream객체 매핑

    TIServer() {

        clients = new HashMap(); //hashMap초기화
        Collections.synchronizedMap(clients); //clients변수를 동기화된 map으로 만듦 (스레드 동시접근 문제 방지)
    }

    public void start() {

        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(5445); //포트번호
            System.out.println("서버가 시작되었습니다.");

            while (true) { //client의 서버 요청 받기 위해 무한루프 실행


                socket = serverSocket.accept(); //연결 수락되면 socket생성
                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");
                ServerReceiver thread = new ServerReceiver(socket); //client와 통신을 담당하는 thread
                thread.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // start()

    void sendToAll(String ss) { //서버에 연결된 모든 client에게 메세지 전송 (msg = 전송할 메시지)

        Iterator it = clients.keySet().iterator(); //client맵의 키에 대한 반복자 생성 -> 반복자 사용으로 맵의 모든 클라이언트에 접근 가능

        while(it.hasNext()) {

            try {
                DataOutputStream out = (DataOutputStream)clients.get(it.next()); //현재 client의 DataOutputStream객체 가져옴
                out.writeUTF(ss); //메세지를 client에 전송
            } catch(IOException e){}
        }
    } // sendToAll

    public static void main(String args[]) {

        new TIServer().start(); //서버 시작
    }

    class ServerReceiver extends Thread { 	//client와 통신을 처리하는 thread
        Socket socket; 						//client와의 연결을 나타내는 socket
        DataInputStream in; 				//client로부터 데이터 읽기 위해 사용되는 입력스트림
        DataOutputStream out; 				//client로 데이터를 전송하기 위해 사용되는 출력스트림

        ServerReceiver(Socket socket) {

            this.socket = socket;
            try {
                in  = new DataInputStream(socket.getInputStream());   //데이터 읽기
                out = new DataOutputStream(socket.getOutputStream()); //데이터 전송
            } catch(IOException e) {}
        }

        public void run() {

            String name = "";

            try {
                name = in.readUTF(); 	//첫번째 메시지를 읽어 name변수에 저장 : client가 자신의 이름을 서버에 전송하는 메시지
                //sendToAll("#"+name+"님이 들어오셨습니다.");

                clients.put(name, out); //client맵에 이름과 출력시트림을 매핑하여 저장
                System.out.println("현재 서버접속자 수는 "+ clients.size()+"입니다.");

                while(in!=null) { 		//client로부터 메시지를 계속해서 읽음

                    sendToAll(in.readUTF()); //메시지 전송
                }
            } catch(IOException e) {
                // ignore
            } finally {
                //sendToAll("#"+name+"님이 나가셨습니다.");
                clients.remove(name);
                System.out.println("["+socket.getInetAddress() +":"+socket.getPort()+"]"+"에서 접속을 종료하였습니다.");
                System.out.println("현재 서버접속자 수는 "+ clients.size()+"입니다.");
            } // try
        } // run
    } // ReceiverThread
} // class