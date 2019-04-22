#!/bin/bash

declare -a distributes=("distribution_less_1" "distribution_less_2" "distribution_more")
repeat=100
time=900

for input in "${distributes[@]}"
do
	inputFile=./data/$input.csv
	echo "$inputFiles"

	mkdir -p results/event/
	mkdir -p results/event/$input

	mkdir -p results/process/
	mkdir -p results/process/$input

	for i in $(seq 1 $repeat)
	do
		seed=$RANDOM
		eventLogFile=results/event/$input/events_$seed
		eventVehsFile=results/event/$input/vehs_$seed
		processLogFile=results/process/$input/events_$seed
		processVehsFile=results/process/$input/vehs_$seed
		echo -e "EventOriented\t$input\tseed $seed\t$i/$repeat"
		java -jar EventOriented.jar -input $inputFile -log $eventLogFile -vehs $eventVehsFile -time $time -seed $seed
		echo -e "ProcessOriented\t$input\tseed $seed\t$i/$repeat"
		java -jar ProcessOriented.jar -input $inputFile -log $processLogFile -vehs $processVehsFile -time $time -seed $seed
	done
done