package com.goldenfield192.irpatches;

import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.Collections;
import java.util.List;

/**
 * Mixin loader
 */
public class IRPEarlyMixinLoader implements IEarlyMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.irpatches.json");
    }
}
