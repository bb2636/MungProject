package server;

import admin.Admin;
import client.ClientHandler;

import java.util.ArrayList;
import java.util.List;
//01.훈련 방 생성,
public class Room {
  private int id; // 방의 고유값
  private String name; // 방의 이름
  private Admin trainer; // 훈련사
  private List<OwnerHandler> owners = new ArrayList<>();

  public Room(String name,int id, Admin trainer){
    this.name = name;
    this.id = id;
    this.trainer = trainer;
  }
  public String getName() { // 방의 이름 가져오기
    return name;
  }
  public synchronized void addOwner(OwnerHandler owner){
    owners.add(owner);
  }
  public synchronized void removeClient(OwnerHandler owner) {
    owners.remove(owner);
  }
  public synchronized void broadcastMessage(String message) {
    for (OwnerHandler owner : owners) {
      owner.sendMessage(message);
    }
  }
}