# Overview
The Golden Spike which was released in IR 1.5.0 allows you to plan, move and shape the track with the [Track Blueprint](immersiverailroading:wiki/en_us/track_blueprint.md)'s blueprint mode.

For a detailed walkthrough of Golden Spike usage, check out [Cam's 1.5.0 release video](https://www.youtube.com/watch?v=dbg2fjBU2p4) on YouTube.

# Usage
After placing a blueprint, you can right-click on it with a Golden Spike to bind the item to it. The actual function varies from type to type:
## Straight & Slope
Use the bound Golden Spike on the ground will set the track length to the larger absolute difference between the x or z coordinates of the Golden Rail Spike and the track placement position, rounded to the nearest whole number and then increased by 1.

Note that you can't change the height of a Slope. If you want to do so, use Custom Curve instead.

## Turn
Use the bound Golden Spike on the ground will set the radius of the turn to the result of following algorithm:
* Take the average of the x-coordinate difference and z-coordinate difference between the Golden Rail Spike and the track placement position;
* Then increased by 1
* Then multiplied by 90 and devide by the turn's degree
* Then rounded to the nearest whole number as the result.

## Custom Curve
The Custom Curve is a Bézier curve whose position and direction of the first control point are defined by th blueprint.

Use the bound Golden Spike on the ground will set the second control point of the curve to its clicking position, whose direction is based on your looking direction.

## Switch
Use the bound Golden Spike on the ground will set the second control point of the turning section like Custom Curve, and the length of the straight section will be set to the closest distance where the other end does not coincide with the turning section then plus 3.

## Turntable
[OBJECT PROMISE]