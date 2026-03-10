/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

/**
 *
 * @author 44743
 */
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class DiscoveryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DiscoveryResponce getDiscovery(){
        return new DiscoveryResponce("v1","w1984268@westminster.ac.uk","/api/v1/rooms","/api/v1/sensors");
    }
    
    static class DiscoveryResponce{
        private String version;
        private String contact;
        private String room;
        private String sensor;
        
        public DiscoveryResponce(String version, String contact, String room, String sensor){
            this.version = version;
            this.contact = contact;
            this.room = room;
            this.sensor = sensor;
        }
        
        public void setVersion(String version){
            this.version = version;
        }
        
        public void setContact(String contact){
            this.contact = contact;
        }
        
        public void setRoom(String room){
            this.room = room;
        }
        
        public void setSensor(String sensor){
            this.sensor = sensor;
        }
        
        public String getVersion(){
            return version;
        }
        
        public String getContact(){
            return contact;
        }
        
        public String getRoom(){
            return room;
        }
        
        public String getSensor(){
            return sensor;
        }
        
        
        
    }
    
}
