/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

/**
 *
 * @author Antoni Podlasiak
 */
import com.smartcampus.model.SensorReading;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.ErrorMessage;
import com.smartcampus.exception.SensorUnavailableException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private String sensorId;
    private static Map<String, List<SensorReading>> readings = new HashMap<>();
    
    public SensorReadingResource(String sensorId){
        this.sensorId = sensorId;
    }
    
    @GET
    @Path("/{readingId}")
    public Response getReading(@PathParam("readingId") String readingId){
        Sensor sensor = SensorResource.sensors.get(sensorId);
        if(sensor == null){
            ErrorMessage error = new ErrorMessage("Sensor with ID: "+sensorId+" not found", 404, "docs/sensors");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        List<SensorReading> sensorReadings = readings.get(sensorId); 
        if(sensorReadings == null){
            ErrorMessage error = new ErrorMessage("Reading with ID: "+readingId+" not found", 404, "docs/readings");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        for(SensorReading  r  : sensorReadings){
            if(r.getId().equals(readingId)){
                return Response.ok(r).build();
            }
        }
        ErrorMessage error = new ErrorMessage("Reading with ID: "+readingId+" not found", 404, "docs/readings");
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
    
    @GET
    public Response getAllReadings(){
        Sensor sensor = SensorResource.sensors.get(sensorId);
        if(sensor == null){
            ErrorMessage error = new ErrorMessage("Sensor with ID: "+sensorId+" not found", 404, "docs/sensors");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        List<SensorReading> sensorReadings = readings.get(sensorId);
        if (sensorReadings == null) {
            return Response.ok(new ArrayList<>()).build();
        }
        return Response.ok(sensorReadings).build();
    }
    
    @POST
    public Response addReading(SensorReading reading){
        Sensor sensor = SensorResource.sensors.get(sensorId);
        if(sensor == null){
            ErrorMessage error = new ErrorMessage("Sensor with ID: "+sensorId+" not found", 404, "docs/readings");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        if(reading.getId() == null || reading.getId().isEmpty()){
            ErrorMessage error = new ErrorMessage("Reading ID must be provided", 400, "docs/readings");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        if(reading.getTimestamp() == 0){
            ErrorMessage error = new ErrorMessage("Timestamp must be provided", 400, "docs/readings");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        if("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())){
            throw new SensorUnavailableException("Sensor with ID: "+sensor.getId()+" is currently under maintenace");
        }
        readings.putIfAbsent(sensorId, new ArrayList<>());
        readings.get(sensorId).add(reading);
        sensor.setCurrentValue(reading.getValue());
        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}
