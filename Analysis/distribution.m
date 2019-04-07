clear all;
close all;
fid = fopen('event_vehicles.txt');
cols = textscan(fid, '%d%f%f%d%c%d%c', 'Delimiter', ' ');
travelTime = cols{3} - cols{2};
% cut off first 10 min vehs
travelTime = travelTime(cols{2} > 600);
histogram(travelTime);
title('Traverse Time Distribution (Including Warmup)');
xlabel('Traverse Time (s)');
ylabel('Number of Vehicles');
fclose(fid);