package com.galive.logic.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.galive.logic.dao.RoomDao;
import com.galive.logic.dao.RoomDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Room;
import com.galive.logic.network.socket.ChannelManager;
import sun.rmi.runtime.Log;

public class RoomServiceImpl extends BaseService implements RoomService {

    private RoomDao roomDao = new RoomDaoImpl();

    static {
        ExecutorService thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            RoomDao roomDao = new RoomDaoImpl();
            while (true) {
                Set<String> rooms = roomDao.findAllRooms();
                if (rooms != null) {
                    for (String id : rooms) {
                        Room room = roomDao.findBySid(id);
                        if (room != null) {
                            boolean allOffline = true;
                            for (String member : room.getMembers()) {
                                if (ChannelManager.getInstance().isOnline(member)) {
                                    allOffline = false;
                                    break;
                                }
                            }

                            if (allOffline) {
                                roomDao.delete(room);
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(1000 * 60 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Room saveOrUpdateRoom(Room room) throws LogicException {
        room = roomDao.save(room);
        return room;
    }

    @Override
    public Room findRoom(FindRoomBy by, String byId) throws LogicException {
        Room room = null;
        switch (by) {
            case id:
                room = roomDao.findBySid(byId);
                break;
            case Owner:
                room = roomDao.findByOwner(byId);
                break;
            case Member:
                room = roomDao.findByMember(byId);
                break;
            case RemoteClient:
                room = roomDao.findByRemoteClient(byId);
                break;
        }
        return room;
    }

    @Override
    public Room createRoom(String accountSid) throws LogicException {
        checkInRoom(accountSid, false);
        Room room = new Room();
        room.setOwnerSid(accountSid);
        room.getMembers().add(accountSid);
        room = roomDao.save(room);
        return room;
    }

    @Override
    public Room joinRoom(String roomSid, String accountSid) throws LogicException {
        checkInRoom(accountSid, true);
        Room room = roomDao.findBySid(roomSid);
        if (room == null) {
            throw makeLogicException("房间不存在");
        }
        room.getMembers().add(accountSid);
        room = roomDao.save(room);
        return room;
    }

    @Override
    public Room leaveRoom(String accountSid) throws LogicException {
        Room room = roomDao.findByMember(accountSid);
        if (room != null) {
            if (room.getOwnerSid().equals(accountSid)) {
                throw makeLogicException("房主无法离开房间");
            }
            room.getMembers().remove(accountSid);
            roomDao.save(room);
            roomDao.removeMember(accountSid);
            if (room.getMembers().isEmpty()) {
                roomDao.delete(room);
            }
        }
        return room;
    }

    @Override
    public Room destroyRoom(String accountSid) throws LogicException {
        Room room = roomDao.findByOwner(accountSid);
        if (room != null) {
            roomDao.delete(room);
        }
        return room;
    }

    @Override
    public Room updateRoomExtraInfo(Room room, Map<String, Object> extraInfo) throws LogicException {
        if (extraInfo != null) {
            room.setExtraInfo(extraInfo);
            room = roomDao.save(room);
        }
        return room;
    }

    @Override
    public Room bindPCClient(String accountSid, String pcClientId) throws LogicException {
        Room room = roomDao.findByOwner(accountSid);
        if (room == null) {
            throw new LogicException("未在房间中");
        }
        room.setRemoteClientId(pcClientId);
        room.setRemotePublishUrl(RemoteClientService.PUBLISH_URL);
        room.setRemotePullUrl(RemoteClientService.PULL_URL);
        roomDao.save(room);
        return room;
    }


    // 检查是否在房间中
    private void checkInRoom(String accountSid, boolean join) throws LogicException {
        Room room = roomDao.findByOwner(accountSid);
        if (room == null) {
            room = roomDao.findByMember(accountSid);
        }
        if (room != null) {
            if (join) {
                if (room.getMembers().contains(accountSid) && !room.getOwnerSid().equals(accountSid)) {
                    return;
                }
            }
            throw makeLogicException("已在房间中");
        }
    }


}
