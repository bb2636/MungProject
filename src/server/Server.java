package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
  // 필드 생성
  private List<Room> rooms = new ArrayList<>();
  private List<OwnerHandler> owner = new ArrayList<>();
  private ExecutorService pool = Executors.newFixedThreadPool(10); // 클라이언트 요청을 병렬처리하는 스레드 풀.

  //01.훈련 방 생성
  public static void main(String[] args) {
    new Server().startServer();
  }
  public void startServer(){
    int port=5000;
    try(ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("서버가 포트 " + port + "에서 실행 중...");
      while (true){
        Socket ownerSocket = serverSocket.accept(); //오너 연결 후 소켓 생성
        OwnerHandler ownerHandler = new OwnerHandler();
        // 오너 연결을 처리할 OwnerHandler 객체 생성.
        // this 는 현재 서버 객체를 OwnerHandler 에 전달하여 클라이언트 핸들러가 서버의 상태에 접근할 수 있도록 합니다.
        owner.add(ownerHandler);
        pool.execute(ownerHandler); // OwnerHandler 클래스에서 override
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      pool.shutdown();
    }
  }
  //02.방 리스트 제공
  public synchronized void listRooms(PrintWriter out){
    for (Room room : rooms){ // 방 리스트에 저장된 Room 객체들을 하나씩 가져와 room 변수에 저장하고 반복 실행.
      out.println("방 이름 : " + room.getName());
    }
  }


  //03.클라이언트 접속 처리

  //04.채팅 가능

  //05.훈련 명령어 전달

  //06.훈련결과반환

  //07.클라이언트 퇴장처리


}