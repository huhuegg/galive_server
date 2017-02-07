package com.galive.logic.dao;

import java.util.Set;

import com.galive.logic.model.Room;

public interface RoomDao {

    Set<String> findAllRooms();

    Room findBySid(String sid);

    Room findByRemoteClient(String sid);

    Room findByOwner(String sid);

    Room findByMember(String sid);

    Room save(Room room);

    void removeMember(String memberSid);

    void delete(Room room);
}
 