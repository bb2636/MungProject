package server;

import client.Owner;
import admin.Admin;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
      out.println("방 이름 : " + room.getName()); // 각 방 이름들 출력
    }
  }
  //03.클라이언트 접속 처리
  public synchronized void joinRoom(OwnerHandler owner, String roomName){
    for (Room room : rooms){
      if (room.getName().equals(roomName)){ // 방 이름이 요청된 roomName 과 일치하는지 확인.
        room.addOwner(owner);
        return; // 이미 존재하는 방이면 오너를 추가하고 메서드 종료.
      }
    }
    Map<String, String> ownerinfo = new HashMap<>();
    ownerinfo.put("A", "소형");
    ownerinfo.put("B", "중형");
    ownerinfo.put("C", "대형");
    Room newRoom =
        new Room(roomName, rooms.size() + 1, new Admin("Trainer", "대형견"));
    newRoom.addOwner(owner);
    rooms.add(newRoom);
  }

  //04.채팅 가능

  //05.훈련 명령어 전달

  //06.훈련결과반환

  //07.클라이언트 퇴장처리


}