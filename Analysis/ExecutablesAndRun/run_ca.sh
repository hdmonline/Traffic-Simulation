#!/bin/bash

declare -a distributes=("distribution_more_2_ca" "distribution_more_1_ca" "distribution_normal_ca" "distribution_less_1_ca" "distribution_less_2_ca")
repeat=100
time=900

for input in "${distributes[@]}"
do
	inputFile=./data/$input.csv
	echo "Running: $input"

	mkdir -p results/ca/
	mkdir -p results/ca/$input

	for i in $(seq 1 $repeat)
	do
		seed=$RANDOM
		caLogFile=results/ca/$input/events_$seed
		caVehsFile=results/ca/$input/vehs_$seed
		echo -e "CellularAutomata\t$input\tseed $seed\t$i/$repeat"
		java -jar CellularAutomata.jar -input $inputFile -log $caLogFile -vehs $caVehsFile -time $time -seed $seed
	done
done