/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

/**
 *
 * @author Antoni Podlasiak
 */

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Path("/rooms")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorRoomResource {
    private static Map<String, Room> rooms = new HashMap<>();
    
    @GET
    public List<Room> getAllRooms(){
        return new ArrayList<>(rooms.values());
    }
    
    @POST
    public Room createRoom(Room room){
        rooms.put(room.getId(), room);
        return room;
    }
    
    @GET
    @Path("/{roomId}")
    public Room getRoom(@PathParam("roomId") String roomId){
        return rooms.get(roomId);
    }
} 