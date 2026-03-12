/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

/**
 *
 * @author Antoni Podlasiak
 */

import com.smartcampus.model.Room;
import com.smartcampus.model.ErrorMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response createRoom(Room room){
        if (room.getId() == null || room.getId().isEmpty()){
            ErrorMessage error = new ErrorMessage("Room Id can not be empty", 400, "doc/rooms");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        if (rooms.containsKey(room.getId())){
            ErrorMessage error = new ErrorMessage("Room with ID already exists", 409,"doc/rooms");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }
    
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId){
        Room room = rooms.get(roomId);
        if(room == null){
            ErrorMessage error = new ErrorMessage("Room not found", 404, "doc/rooms");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(room).build();
    }
    
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId){
        Room room = rooms.get(roomId);
        if(room == null){
            ErrorMessage error = new ErrorMessage("Room not found", 404, "doc/rooms");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        rooms.remove(roomId);
        return Response.ok("Room deleted successfully").build();
    }
} 