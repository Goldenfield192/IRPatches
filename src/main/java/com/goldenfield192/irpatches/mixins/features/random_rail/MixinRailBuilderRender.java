package com.goldenfield192.irpatches.mixins.features.random_rail;

import cam72cam.immersiverailroading.library.TrackItems;
import cam72cam.immersiverailroading.model.TrackModel;
import cam72cam.immersiverailroading.render.ExpireableMap;
import cam72cam.immersiverailroading.render.rail.RailBuilderRender;
import cam72cam.immersiverailroading.track.BuilderBase;
import cam72cam.immersiverailroading.util.RailInfo;
import cam72cam.mod.MinecraftClient;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.render.opengl.VBO;
import com.goldenfield192.irpatches.accessor.ITrackModelAccessor;
import com.goldenfield192.irpatches.util.ExtraTrackDefinition;
import com.goldenfield192.irpatches.util.TrackRoll;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import util.Matrix4;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(RailBuilderRender.class)
public class MixinRailBuilderRender {
    @Unique
    private static final ExpireableMap<String, Set<VBO>> multiTrackCache = new ExpireableMap<String, Set<VBO>>() {
        @Override
        public void onRemove(String key, Set<VBO> value) {
            value.forEach(VBO::free);
        }
    };

    @Inject(method = "renderRailBuilder", at = @At(value = "INVOKE", target = "Lcam72cam/immersiverailroading/render/ExpireableMap;get(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false, cancellable = true)
    private static void inject0(RailInfo info, List<BuilderBase.VecYawPitch> renderData, RenderState state, CallbackInfo ci){
        TrackModel surface = info.getTrackModel();

        if(!((ITrackModelAccessor)surface).hasExtraDefinition()){
            return;
        }

        Set<VBO> cached = multiTrackCache.get(info.uniqueID);

        if(cached == null) {
            ExtraTrackDefinition.ExtraTrackModel extraModel = ((ITrackModelAccessor) surface).getExtra();
            cached = new HashSet<>();
            Random random = new Random(info.uniqueID.hashCode());
            int[] ints = new int[renderData.size()];
            for (int i = 0; i < renderData.size(); i++) {
                Pair<Integer, TrackModel> pair = extraModel.getModelForNumber(random.nextInt(extraModel.getTotalNumber()));
                ints[i] = pair.getLeft();
            }
            for (Map.Entry<Integer, TrackModel> entry : extraModel.getRefer().entrySet()) {
                OBJRender.Builder builder = entry.getValue().binder().builder();
                for (int i = 0; i < renderData.size(); i++) {
                    if (entry.getKey() == ints[i]) {
                        applyMatrix(renderData.get(i), entry.getValue(), builder, info);
                    }
                }
                cached.add(builder.build());
            }
            multiTrackCache.put(info.uniqueID, cached);
        }

        MinecraftClient.startProfiler("irpMultiTrackModel");
        for(VBO single : cached){
            try (VBO.Binding vbo = single.bind(state, info.settings.type == TrackItems.TURNTABLE)) {
                vbo.draw();
            }
        }
        MinecraftClient.endProfiler();

        ci.cancel();
    }

    @Unique
    private static void applyMatrix(BuilderBase.VecYawPitch piece, TrackModel model, OBJRender.Builder builder, RailInfo info){
        Matrix4 m = new Matrix4();
        m.translate(piece.x, piece.y, piece.z);
        m.rotate(Math.toRadians(piece.getYaw()), 0, 1, 0);
        m.rotate(Math.toRadians(piece.getPitch()), 1, 0, 0);
        TrackRoll.applyRollToMatrix(m, info, piece);
        m.rotate(Math.toRadians(-90), 0, 1, 0);

        if (piece.getLength() != -1) {
            m.scale(piece.getLength() / info.settings.gauge.scale(), 1, 1);
        }
        double scale = info.settings.gauge.scale();
        m.scale(scale, scale, scale);

        if (!piece.getGroups().isEmpty()) {
            List<String> groups = model.groups().stream()
                                       .filter(group -> piece.getGroups().stream().anyMatch(group::contains))
                                       .collect(Collectors.toList());
            builder.draw(groups, m);
        } else {
            builder.draw(m);
        }
    }
}
