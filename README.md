# Read Me First
This is a simple aggregator/matching engine for FX. Unlike equities FX matching has more parameters involved.
Although the design/data structures follows the equity model, the matching workflow is different for FX products.

# Getting Started
This is a basic spring boot project. Order publishers and aggregation/matching are from the same process.
There is a rest endpoint, exposed in swagger ui. (http://localhost:8080/swagger-ui/index.html).
Checking out the project into an IDE like eclipse/Intellij should be enough to get started

# Design principles
1. Simple event source model is used. Events are published/subscribed over messaging channels.
2. Clients publish order events over order intake or through rest api endpoints and subscribe for executions.
3. Matching engine subscribes for order/match requests and responds with executions.
4. Use IPC as messaging medium since everything is contained in a single JVM.
5. Can be extended to use udp multicast to support multiple microservices with an event sequencer.
6. Can support passive/hot replicated instances for failovers.
7. ConcurrentSkipListMap and ConcurrentLinkedDeque are used as matching data strutures. 
8. They are readily available for quick reference implementations. Need high performing custom tree data structure with linked orders
for scaling purposes.

# Tech Stack
1. JDK17, Spring Boot - quick application buildup
2. Aeron - for messaging between services. IPC is used, but can be easily replaced with udp multicast or other custom message delivery strategies. 
3. Simple Binary Encoding - for encoding/decoding messages, uses off heap memory and reduces GC
4. Swagger-ui - exposes rest api endpoints in a nice web GUI
5. Mokito - for unit tests

# Workflow:
1. ClientOrderPublisher - publishes order/match request messages originating from file or rest api endpoint
2. ClientExecListener - listens for all execution/aggregation messages from the engine
3. OrdersListener - Entry point for engine, decodes messages and passes to OrderBookManager
4. OrderBookManager - maintains configured number of order books based on ccy pair
5. OrderBook - provides aggregation/matching functionality for specific ccy pair. orders are grouped by valuedates
6. ExecutionsPublisher - publishes all messages emitted by engine to client listeners
7. MatchingClientController - rest api endpoints and swagger ui
8. items 3-7 can be grouped together and started as multiple instances for scaling up with universe of ccy pairs.
