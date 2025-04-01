# Read Me First
This is a simple aggregator/matching engine for FX. Unlike equities FX matching will be on more parameters.
Although the design/data structures follows the equity model, the logic is customized for FX products.

# Getting Started
This is a basic spring boot project. Order publishers and aggregation/matching are from the same process.
There is a rest endpoint, exposed in swagger ui. (http://localhost:8080/swagger-ui/index.html).
Checking out the project into an IDE like eclipse/Intellij should be enough to get started

# Design principles
Simple event source model is used. Events are published/subscribed over channels.
Clients publish order events over order intake or through rest api endpoints and subscribe for executions.
Matching engine subscribed for order/match requests and responds with executions.
Use IPC as messaging medium since everything is contained in a single JVM.
Can be extended to use udp multicast to support multiple microservices with an event sequencer with 
support of passive/hot replicated instances for failovers. 

ConcurrentSkipListMap and ConcurrentLinkedDeque is used for storing orders. 
They are readily available for quick implementations. Need high performing custom tree data structure with linked orders
for scaling purposes.

# Tech Stack
JDK17, Spring Boot - fast application buildup
Aeron - for messaging between services. IPC is used here, but can be expanded to udp multicast or other custom message delivery strategies. 
Simple Binary Encoding - encoding/decoding messages, helps in off heap memory model and reduced GC
Swagger-ui - exposes rest api endpoints in a nice GUI
Mokito - for unit tests

