# Traffic-Simulation
This project is to assess the average travel time for vehicles to traverse a portion of Peachtree Street, the corridor from 10th to 14th street, in midtown Atlanta.

Three different simulation model are developed for the road network.

## Event-oriented queueing model
The Peachtree corridor is modeled as a queueing network where each intersection is modeled as a server, and vehicles must queue while waiting to enter
the intersection.

## Cellular automata model
In the event-oriented model, the traffic flow is driven by sequence of events. In the simulation engine, we have a priority queue data structure, i.e., future event list (FEL) to hold all unprocessed events. In each simulation loop, the event with the smallest time stamp would be removed and handled. Each event can change the state variables and schedule new events.

## Process-oriented queueing model
This model is similar to the event-oriented queueing model described above. In this model, the traffic flow is driven by pausing and resuming each vehicle thread. In the thread of the scheduler, there are two list. One is the event list (FEL) implemented in a priority queue, the other is the waiting vehicle list which is implemented bya a LinkedList. Even though this model is implemented by multi-thread programming, there is only one thread running at a time, either a vehicle thread or the scheduler thread.
