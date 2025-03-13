You can place tracks by using the Track Blueprint.

# Overview
The Track Blueprint is used to lay down rails for rolling stock to travel on. 

It can be made from a piece of paper and 6 steel ingots:
![craft](immersiverailroading:wiki/images/track/track_crafting.png)

If Immersive Engineering is not installed, use iron ingots instead of steel ingots.

# Usage
With the blueprint in hand, right-click in midair to open the GUI and change its settings:
![UI](immersiverailroading:wiki/images/track/track_gui.png)

Right-click a block to place the rail bed, which requires Track Segments from the [Track Roller](immersiverailroading:wiki/en_us/machines/track_roller.md) and Treated Wood Planks.

If Immersive Engineering is not installed, use normal planks instead of Treated Wood Planks.

## Settings
### Gauge
This setting determines what gauge the track should be built at.

At the moment, you can use the following gauges in your game:
[gauge_provider]

### Types
This setting determines what shape your track will take. Current options are:
* [Straight](immersiverailroading:wiki/en_us/tracks/straight.md)
* [Slope](immersiverailroading:wiki/en_us/tracks/slope.md)
* [Turn](immersiverailroading:wiki/en_us/tracks/turn.md)
* [Switch](immersiverailroading:wiki/en_us/tracks/switch.md)
* [Turntable](immersiverailroading:wiki/en_us/tracks/turntable.md)
* [Custom Curve](immersiverailroading:wiki/en_us/tracks/custom_curves.md)
You can view the definition of other settings in their dedicated page.

### Track Style
This setting determines what type your track will be.

At the moment, you can use the following tracks in your game:
[track_provider]

### Rail Bed
This setting determines what visual rail bed should be placed between the rail ties, which is useful for debugging when you want to figure out where the track blocks will actually be placed.

If you right-click on track in the world with the Track Blueprint in your offhand, the blueprint will mimic the rail bed of the track you clicked on.

Here is an example of brick rail bed:
![Brick Rail Bed](immersiverailroading:wiki/images/track3.png)

### Rail Bed Fill
This setting is useful for building bridges.  It auto-places blocks from your inventory under the rail bed as it builds the tracks.

If you shift-right-click on track in the world with the Track Blueprint in your offhand, the blueprint will mimic the rail bed fill of the track you clicked on.

Here is an example of yellow concrete track bed fill:
![Bed Fill](immersiverailroading:wiki/images/track4.png)

> Using CraftTweaker, or similar, you can modify the tie and rail bed candidate materials by adding them to corresponding OreDictionary.
> Blocks added to "irTie" OreDictionary will allow blocks other than Treated Wood(or regular planks if Immersive Engineering is not installed) to be consumed as ties when placing track.
> Blocks added to "railBed" OreDictionary will add the block to the list of Rail Bed and Rail Bed Fill.

### Position
This setting determines how the track should be locked to the rail bed.
* Fixed: Locks the track directly to the rail bed, no flexibility at all
* Pixels: Allows free placement of the track (rounded to the nearest 16th of a block)
* Pixels Locked: Allows free placement of the track forward and backward, but locks side to side motion
* Smooth: Allows free placement of the track
* Smooth Locked: Allows free placement of the track forward and backward, but locks side to side motion

### Grade Crossing
Extends the rail bed out to the sides, to have the appearance of a level crossing; such as those used at intersections of roads and railroads tracks in real life.

### Place Blueprint
You can place down a blueprint which renders the track in-world.  This allows you to plan, move and shape the track as you go.

Right-clicking on a placed blueprint will allow you to change the settings without having to replace it.

Shift \+ right-click on a placed blueprint will allow you to change its direction based on your looking direction, and you can also shift its position if it is in any mode except Fixed.

Breaking the block will remove the blueprint.  Shift \+ left-click the block will attempt to place the blueprint.

See the [Golden Spike](immersiverailroading:wiki/en_us/tracks/golden_spike.md) for more information.