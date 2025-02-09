package com.goldenfield192.irpatches;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import zone.rong.mixinbooter.ILateMixinLoader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IRPLoadingPlugin implements ILateMixinLoader{
    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.irpatches.json");
    }
}
