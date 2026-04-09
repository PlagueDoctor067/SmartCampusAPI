w1984268

#Part 1: Service Architecture & Setup

##1. Project & Application Configuration

In JAX-RS, resource classes use a per request lifecycle, this means that a new instance is created for each incoming request rather than being treated as a single instance. In this implemintation the shared data is stored using static in memory classe Room, Sensor and reading if it was store in instance variables it would be reinstated and the data would be lost per request. However, because the static HashMap and ArrayList are shared across multiple Recorces, concurrent requstes can modify them simutaniesly which can lead to inconsistant updatas or data curruption. Therefore, when using shared in-memory maps and lists in a per-request lifecycle, thread safety must be ensured through synchronization or by using concurrent collections to prevent data loss and maintain consistency.

##2.The ”Discovery” Endpoint

Hypermedia is considerd a hallmark of advanced RESTful designe beause it transformes the API from a static endpoint into a dynamic, navigable system where the server guides the client's next posible action. This is beneficial for the client as it provides a self-descoverable, live map of the API which reduces the relients on static documentation which can become quickly outdated.

#Part 2: Room Management

##1.Room Resource Implementation

Returning only room IDs uses less bandwith as the payload will be smaller. However it will require the client to send multiple requests to get the full room detail which will increase the client-side processing and latency. Returtning a full room object will use more bandwidth but it will provide all the detail for the room in one request. This reduces the requests and simplefies the client logic.

##2. Room Deletion & Safety Logic

Yes the DELETE operation is idempotent because repeted requests reuslte in the same final system state. The first DELETE request will remove the room data if the room exist and return a success. If the client mistakently sent the same DELETE request when the room is already deleted the method will return a 404 response without changeing anything. Althought the respone differs, no addition side effects happend, making the operation remain idempotent.

#Part 3: Sensor Operations & Linking 

##1. Sensor Resource & Integrity

Using the @Consumes(MediaType.APPLICATION_JSON) will restrict the POST method to only accept requests with the content type application/json. If the client tries to send a request with a different format such as text/plain or application/xml JAX-RS will treat this a an unknown format and return a 415 response error. This will prevent the method from executing and ensure only the correct format of json will be inputted.

##2. Filtered Retrieval & Search

Using @QueryParam for filtering is generally superior because queary paramiters are used for filtering and searching within a collective without changing the recource identity. The base path /sensors will not change and still represent the same collection while the query will narrow down a specific result. For example, using a path like /sensors/type/CO2 which will treat the filter as a diffrent resource structure which reduces fexebility and makes it harder to retrieve multiple results.

#Part 4: Deep Nesting with Sub - Resources

##1. The Sub-Resource Locator Pattern

The sub-resource locater pattern improves API architecture by delegating responsebilitys for nested resources to sperate classes which helps manage complexity and improves maintainability. Instead of having to define every nessted path in one large controller, the parent resource will handle the main entity of the request and pass the rest forward to the dedecated sub-resource class. This seperation keeps each class focused on a specific part of the API. This makes the code easier to read, test and extend. It also reduces duplication and allow multiple developers to work on diffrent resources independently. Using sub-resource locater will stop controler classes from becoming too big. This leads to the API to being cleaner and more scalable.

#Part 5: Advanced Error Handling, Exception Mapping & Logging

##2. Dependency Validation 
