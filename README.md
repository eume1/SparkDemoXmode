# SparkDemoXmode

Location data is generated using: https://github.com/cahilltr/XMode-Interview-Data-Generator

The task is to Locate users within 50Meters of certain Points of Interest POIs without using a cartesian join.

The approach taken is to broadcast the poi data(very small data set) and then use the 'haversine equation' to calculate the distance between lat & long pairs of the POIs and cell users. and then filter based on distance.
