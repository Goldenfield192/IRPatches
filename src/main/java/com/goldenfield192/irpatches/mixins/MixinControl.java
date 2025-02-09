package com.goldenfield192.irpatches.mixins;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.immersiverailroading.model.ModelState;
import cam72cam.immersiverailroading.model.components.ModelComponent;
import cam72cam.immersiverailroading.model.part.Control;
import cam72cam.immersiverailroading.model.part.Interactable;
import cam72cam.immersiverailroading.util.DataBlock;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.GlobalRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.text.TextColor;
import cam72cam.mod.text.TextUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import util.Matrix4;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = Control.class)
public abstract class MixinControl<T extends EntityMoveableRollingStock> extends Interactable<T>/*For generics compatibility*/ {
    @Final
    @Mutable
    @Shadow(remap = false)
    protected ModelState state;

    @Final
    @Shadow(remap = false)
    public String label;

    @Unique
    public String tex_variant;

    //Complex implement
    @ModifyVariable(method = "<init>", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/String;replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"), remap = false)
    public ModelState modState(ModelState state) {
        return state.push(builder ->
                builder.add((ModelState.GroupVisibility) (stock, group) -> {
                    if (tex_variant == null)
                        return true;
                    if (Objects.equals(null, stock.getTexture()) || Objects.equals("", stock.getTexture())) {
                        return tex_variant.equals("default");
                    } else {
                        return Objects.equals(stock.getTexture(), tex_variant);
                    }
                }));
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onControlLoad(ModelComponent part, ModelState state, double internal_model_scale, Map<String, DataBlock> widgetConfig, CallbackInfo ci) {
        tex_variant = part.modelIDs.stream().map(group -> {
            Matcher matcher = Pattern.compile("_TV_([^_]+)").matcher(group);
            return matcher.find() ? matcher.group(1).replaceAll("\\^", " ") : null;
        }).filter(Objects::nonNull).findFirst().orElse(null);

        if (tex_variant != null) {
            System.out.println("TV: " + tex_variant);
        }
    }

    @Inject(method = "postRender", at = @At(value = "INVOKE"
            , target = "Lcam72cam/mod/render/GlobalRender;drawText(Ljava/lang/String;Lcam72cam/mod/render/opengl/RenderState;Lcam72cam/mod/math/Vec3d;FF)V")
            , locals = LocalCapture.CAPTURE_FAILSOFT
            , cancellable = true
            , remap = false)
    public void labelTranslator(T stock, RenderState state, float partialTicks, CallbackInfo ci, boolean isPressed, Matrix4 m, Vec3d pos, String labelstate, float percent, String str) {
        String[] sp = stock.getDefinition().defID.replaceAll(".json", "").split("/");
        String localStr = String.format("%s:label.%s.%s.%s", ImmersiveRailroading.MODID, sp[sp.length - 2], sp[sp.length - 1], label);
        String transStr = TextUtil.translate(localStr);
        if (localStr.equals(transStr)) {
            return;
        }

        transStr = transStr + labelstate;

        if (isPressed) {
            transStr = TextColor.BOLD.wrap(transStr);
        }
        GlobalRender.drawText(transStr, state, pos, 0.2F, 180.0F - stock.getRotationYaw() - 90.0F);
        ci.cancel();
    }

    //Only for compatibility
    private MixinControl(ModelComponent part) {
        super(part);
    }
}
