The source code for this project contains 3 main models:
The CellularAutomata, EventOriented and ProcessOriented folders are the source code folders.
The 3 executables (.jar files) are in the Executables folder.

Running the codes:
java -jar <Model>.jar -input <input_distribution_path> -log <output_log_path> -vehs <output_vehicles_path> -time <simulation_time_in_second> [-delay <traffic_light_cycle_delay_in_second>] [-seed <seed>]

Options:
-input
	the input distribution file path
-log 				
	the log output file path. This file includes all the events that are handled during the simulation. If the model is CA, then this contains all the vehicles infomation for every time step.
-vehs 				
	the vehicle output file path. The file includes all the information of finished vehicles during the simulation.
-time 				
	the simulation time
-delay(optional) 
	the traffice cycle delay (offset) between each intersection. i.e. the signals of intersection 2 will be misaligned with the signals of intersection 1 by <delay> seconds
-seed(optional)
	the seed for random generator

Example:
	java -jar ProcessOriented.jar -input /data/distribution_normal.csv -log /results/events_123.csv -vehs /results/vehs_123.csv -time 900 -delay 50 -seed 123
