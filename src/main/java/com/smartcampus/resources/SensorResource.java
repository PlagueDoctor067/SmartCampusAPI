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
import com.smartcampus.model.Sensor;
import com.smartcampus.model.ErrorMessage;
import java.util.ArrayList;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Path("/sensors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorResource {
    public static Map<String, Sensor> sensors = new HashMap<>();
    
    @GET
    public Response getSensor(@QueryParam("type") String type){
        if(type == null){
            return Response.ok(new ArrayList<>(sensors.values())).build();
        }
        List<Sensor> filteredSensors = new ArrayList<>();
        
        for(Sensor sensor : sensors.values()){
            if(sensor.getType().equalsIgnoreCase(type)){
                filteredSensors.add(sensor);
            }
        }
        return Response.ok(filteredSensors).build();
    }
    
    @POST
    public Response createSensor(Sensor sensor){
        Room room = SensorRoomResource.rooms.get(sensor.getRoomId());
        if(room == null){
            ErrorMessage error = new ErrorMessage("Room with ID:"+sensor.getRoomId()+" does not exist",422,"doc/sensors");
            return Response.status(422).entity(error).build();
        }
        sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }
    
}
