#!/bin/bash

declare -a delays=("0" "25" "50")
repeat=30
time=900
inputFile="./data/distribution_normal.csv"

for delay in "${delays[@]}"
do
	echo "Running delay: $delay"

	mkdir -p results/process/trafficlight/
	mkdir -p results/process/trafficlight/delay_$delay

	for i in $(seq 1 $repeat)
	do
		seed=$RANDOM
		processLogFile=results/process/trafficlight/delay_$delay/events_$seed
		processVehsFile=results/process/trafficlight/delay_$delay/vehs_$seed
		echo -e "ProcessOriented\t$delay\tseed $seed\t$i/$repeat"
		java -jar ProcessOriented.jar -input $inputFile -log $processLogFile -vehs $processVehsFile -time $time -delay $delay -seed $seed
	done
done