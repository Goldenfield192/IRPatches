## 1.3.6(IN DEV)
* Fix CargoItem not rolling

## 1.3.5
* Fix server crash

## 1.3.4
* Fix independent throttle not working

## 1.3.3
* Add real track bumpiness
* Make advanced camera's key bindings configurable
* Advanced camera can be toggled off
* Fix stock particle emitter offset
* Fix [issue #1526](https://github.com/TeamOpenIndustry/ImmersiveRailroading/issues/1526)
* Revert feature "Track exchanger now change all the segments on long tracks" added in 1.3.1

## 1.3.2
* Revert Door Actuator changes, now it work like ordinary augment when control group is not specified
* Fix a conflict with poizzy's build

## 1.3.1
* Fix locomotive missing chuff sound
* Stocks now roll when placing
* Track exchanger now change all the segments on long tracks

## 1.3.0
* Add stocks roll
* Change track rolling center to the center of rails
* Fix headlight issues

## 1.2.5
* Add real tilting rail(Stocks don't tilt for now)
* Add server side check for track length
* Fix NPE when clicking on rail introduced in 1.2.3
* Add configuration for onboard camera to switch between collision mode(/→IRPConfig→OnboardCameraCollideWithBlock)

## 1.2.4
* Headlight can be disabled on forward/reverse in json
* Headlight's texture render can be disabled

## 1.2.3
* Configurable maxTrackPlacementLength(1000 to 10000) and maxTrackRenderDistance(256 to 8192)
* Override Door Actuator to configurable control group changer

## 1.2.2
* Add better third-person camera when onboard

## 1.2.1
* Fix Track Blueprint GUI component overlapping issue
* Fix Golden Spike issue with smooth Custom Curve
* Change Golden Spike behaviour with Straight/Slope/Turn, now it uses projection length

## 1.2.0
* Add MarkDown-based document module

## 1.1.1
* Add support for custom fuel type outside config

## 1.1.0
* Add `left_first` to adjust valve gears
* Add fuel filter for diesel locomotives

## 1.0.0
* Translatable component label
* `TV` modifier for component
* Fix Boiler Machine incorrect render offset