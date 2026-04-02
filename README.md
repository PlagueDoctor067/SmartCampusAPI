w1984268

#Part 1: Service Architecture & Setup
##1. Project & Application Configuration
In JAX-RS, resource classes use a per request lifecycle, this means that a new instance is created for each incoming request rather than being treated as a single instance. In this implemintation the shared data is stored using static in memory classe Room, Sensor and reading if it was store in instance variables it would be reinstated and the data would be lost per request. However, because the static HashMap and ArrayList are shared across multiple Recorces, concurrent requstes can modify them simutaniesly which can lead to inconsistant updatas or data curruption. Therefore, when using shared in-memory maps and lists in a per-request lifecycle, thread safety must be ensured through synchronization or by using concurrent collections to prevent data loss and maintain consistency.
##2.The ”Discovery” Endpoint
