# Antoni Podlasiak w1984268

# Overview

The Smart Campus API is a RESTful web service designed for managing rooms, sensors and sensor readings within a university campus, it utilizes JSON as its data format with the base path /api/v1. This API consists of three main resource classes: rooms, sensors and sensor readings with a hierarchical relationship where rooms contain sensors while sensors contain readings. The API uses the methods GET, POST and DELETE to retrieve, create and delete data respectively. The integrity of the data is assured through not allowing sensors to be created in non-existing rooms and rooms not being able to be deleted if sensors still exist within them while using proper HTTP status codes. There is also a discovery endpoint that contains the API's metadata, it also uses custom exceptions and structured error messages. Furthermore, it contains a logger filter that record all incoming requests and outgoing responses which makes the system easier to debug, maintain and extend.

# Build and Run Instructions

1. Open the project
   Lanch NetBeans and open the SmartCampusAPI project
   
2. Configure Tomcat server
   Go to Services Server and click Add Server, choose Apache Tomcat and provide the installation directory

3. Clean and build the project
   Right-click the project and choose Clean and Build to compile the code

4. Run the project
   Right-click on the server and choose run then Right-click on the project and click run

5. Check the endpoint
   Check the discovery endpoit at http://localhost:8080/SmartCampusAPI/api/v1

# Curl Commands

## 1. GET discovery endpoint
      curl http://localhost:8080/SmartCampusAPI/api/v1

## 2. POST a room
      curl -X http://localhost:8080/SmartCampusAPI/api/v1/rooms \
      -H "Content-Type: application/json" \
      -d '{"id": "LAB-01", "name": "Computer Lab", "capacity": 30}'

## 3. GET all rooms
      curl http://localhost:8080/SmartCampusAPI/api/v1/rooms

## 4. POST a sensor
      curl -X http://localhost:8080/SmartCampusAPI/api/v1 \
      -H "Content-Type: application/json" \
      -d '{"id": "LABSEN-01", "type": "Temperature", "status": "ACTIVE", "currentValue": 14, "roomId": "LAB-01"}'

## 5. GET sensor by type
      curl http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=temperature
      
# Report

# Part 1: Service Architecture & Setup

## 1. Project & Application Configuration

In JAX-RS, resource classes use a per request lifecycle, this means that a new instance is created for each incoming request rather than being treated as a single instance. In this implemintation the shared data is stored using static in memory class Room, Sensor and reading if it was store in instance variables it would be reinstated and the data would be lost per request. However, because the static HashMap and ArrayList are shared across multiple Recorces, concurrent requstes can modify them simutaniesly which can lead to inconsistant updates or data corruption. Therefore, when using shared in-memory maps and lists in a per-request lifecycle, thread safety must be ensured through synchronization or by using concurrent collections to prevent data loss and maintain consistency.

## 2.The ”Discovery” Endpoint

Hypermedia is considerd a hallmark of advanced RESTful design because it transforms the API from a static endpoint into a dynamic, navigable system where the server guides the client's next posible action. This is beneficial for the client as it provides a self-descoverable, live map of the API which reduces the reliance on static documentation which can become quickly outdated.

# Part 2: Room Management

## 1.Room Resource Implementation

Returning only room IDs uses less bandwith as the payload will be smaller. However it will require the client to send multiple requests to get the full room detail which will increase the client-side processing and latency. Returtning a full room object will use more bandwidth but it will provide all the detail for the room in one request. This reduces the requests and simplifies the client logic.

## 2. Room Deletion & Safety Logic

Yes the DELETE operation is idempotent because repeated requests reuslt in the same final system state. The first DELETE request will remove the room data if the room exists and return a success. If the client mistakenly sent the same DELETE request when the room is already deleted the method will return a 404 response without changing anything. Although the response differs, no addition side effects happened, making the operation remain idempotent.

# Part 3: Sensor Operations & Linking 

## 1. Sensor Resource & Integrity

Using the @Consumes(MediaType.APPLICATION_JSON) will restrict the POST method to only accept requests with the content type application/json. If the client tries to send a request with a different format such as text/plain or application/xml JAX-RS will treat this as an unknown format and return a 415 response error. This will prevent the method from executing and ensure only the correct format of json will be inputted.

## 2. Filtered Retrieval & Search

Using @QueryParam for filtering is generally superior because query paramiters are used for filtering and searching within a collective without changing the resource identity. The base path /sensors will not change and still represent the same collection while the query will narrow down a specific result. For example, using a path like /sensors/type/CO2 will treat the filter as a diffrent resource structure which reduces fexebility and makes it harder to retrieve multiple results.

# Part 4: Deep Nesting with Sub - Resources

## 1. The Sub-Resource Locator Pattern

The sub-resource locater pattern improves API architecture by delegating responsibilities for nested resources to separate classes which helps manage complexity and improves maintainability. Instead of having to define every nessted path in one large controller, the parent resource will handle the main entity of the request and pass the rest forward to the dedicated sub-resource class. This seperation keeps each class focused on a specific part of the API. This makes the code easier to read, test and extend. It also reduces duplication and allow multiple developers to work on diffrent resources independently. Using sub-resource locater will stop controller classes from becoming too big. This leads to the API to being cleaner and more scalable.

# Part 5: Advanced Error Handling, Exception Mapping & Logging

## 2. Dependency Validation 

A HTTP 422 error is more accurate than a 404 error when the request is valid but it contains a logical error like a missing reference inside a valid JSON payload. A 404 error means that the requested endpoint or url does not exist, where as a 422 error means that the request was understood by the server but it can not be processed because the data is invalid. Returning a 422 error will clearly communicate to the client where the problem lies. 

## 4. The Global Safety Net 

Exposing the java stack traces to the API consumers is risky because it will reveal the internal details such as the class names, package structure, file paths and call methods. An attacker could use this information to descover vulnerabilities and make targeted attacks. Returning a generic error message instead helps to prevent information leakage and improve security.

## 5. API Request & Response Logging Filters

Using JAX-RS filters is beneficial because it will centralise the functionality into one instead of having to use a Logger.info() statement inside every single resource class. This will reduce the codes repetition, will keep classes cleaner and more readable, and will make a consistent logging for both requests and responses. Filters are also easier to maintain while manual logging will require updating everything and increase the risk of inconsistencies or missed cases. 
