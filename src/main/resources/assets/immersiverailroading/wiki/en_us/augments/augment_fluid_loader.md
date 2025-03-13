# Overview
![](immersiverailroading:wiki/images/augments/fluid_loader.png)
The Fluid Loader Augment is displayed as blue block under rail and is used to interface with the tank of the locomotive/stock overhead.

# Usage
The Fluid Loader Augment will accept fluid from any connected pipe(like IE) and store it in a small internal buffer tank then dumps into the stock overhead.

By right-clicking with a piston in your hand, you can change pushpull status of this augment. This augment will only work if pushpull is enabled.

Besides, this augment also has 4 redstone modes that can be toggled by holding a redstone torch/redstone dust and right-clicking.
* Enabled: The augment is enabled, regardless of redstone signal.
* Required: The augment is enabled if redstone signal is greater than 0, otherwise it is disabled.
* Inverted: The augment is enabled if redstone signal equals to 0, otherwise it is disabled.
* Disabled: The augment is disabled, regardless of redstone signal.

# Example
A Fluid Loader Augment which is feeding water from a pipe to the tank of the stock:
![](immersiverailroading:wiki/images/augments/fluid_loader_example.png)

# Relevant Pages
* [Fluid Unloader](immersiverailroading:wiki/en_us/augments/augment_fluid_unloader.md)