# Overview
![Loader](immersiverailroading:wiki/images/augments/item_loader.png)
The Item Loader Augment is used to insert items into the inventory of the locomotive/stock overhead.

# Usage
The item augments do not pull from adjacent inventories, nor do they push to adjacent inventories.

By right-clicking with a piston in your hand, you can change pushpull status of this augment. This augment will only work if pushpull is enabled.

Besides, this augment also has 4 redstone modes that can be toggled by holding a redstone torch/redstone dust and right-clicking.
* Enabled: The augment is enabled, regardless of redstone signal.
* Required: The augment is enabled if redstone signal is greater than 0, otherwise it is disabled.
* Inverted: The augment is enabled if redstone signal equals to 0, otherwise it is disabled.
* Disabled: The augment is disabled, regardless of redstone signal.

# Example
An Item Loader Augment which is feeding items from a hopper to the inventory of the stock:
![Loader](immersiverailroading:wiki/images/augments/item_loader_example.png)

# Relevant Pages
* [Item Unloader](immersiverailroading:wiki/en_us/augments/augment_item_unloader.md)