# Overview
![](immersiverailroading:wiki/images/augments/detector.png)
The Detector Augment is displayed as red block under rail and is used to check the locomotive/stock overhead's properties.

# Usage
The Detector Augment has 5 modes that can be toggled by holding a redstone torch/redstone dust/piston and right-clicking.
* Stock: Outputs a signal of 15 if there's a stock on the detector, otherwise outputs no signal.
* Speed: Outputs a signal equal to the minimum value of (speed(kph) divided by 10) rounded down and 15.
* Number of passengers: Outputs a signal with strength equal to the minimum value of the number of passengers on the stock and 15.
* Freight Cargo Fullness: If target locomotive/stock has inventory, outputs a signal with strength proportional to the ratio of non-empty item slots to its total item slots, otherwise outputs a signal of 0.
* Liquid Cargo Fullness: If target locomotive/stock has tank, outputs a signal with strength proportional to the ratio of current liquid amount to its max amount of liquid, otherwise outputs a signal of 0.

# Example
A Detector Augment set in Stock mode:
![stock](immersiverailroading:wiki/images/augments/detector_stock.png)

Another Detector Augment set in Passenger mode with 7 villagers on board:
![passenger](immersiverailroading:wiki/images/augments/detector_passenger.png)

Note that when the output redstone signal is n, there are n+1 redstone lamps on.