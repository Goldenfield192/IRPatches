package com.goldenfield192.irpatches.mixins.model.part;

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
import com.goldenfield192.irpatches.common.StateChangeManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import util.Matrix4;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1.Add TV modifier for widgets to allow more complex functions
 * 2.Let LABEL be translatable
 * @see com.goldenfield192.irpatches.mixins.registry.MixinEntityRollingStockDefinition
 */
@Mixin(value = Control.class)
public abstract class MixinControl{
    @Final
    @Shadow(remap = false)
    public String label;

    @Shadow(remap = false)
    @Final
    public boolean toggle;

    @Unique
    public StateChangeManager.ToggleType toggleType;

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

    @Inject(method = "<init>", at = @At("TAIL"), remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onControlLoad(ModelComponent part, ModelState state, double internal_model_scale, Map<String, DataBlock> widgetConfig, CallbackInfo ci, String rotpat, String name, DataBlock config, Predicate<String> hasKey, DataBlock rotBlock, DataBlock tl, DataBlock scale, DataBlock var13, Iterator var14, String var15, Matcher var16) {
        tex_variant = config.getValue("TV").asString(part.modelIDs.stream().map(group -> {
            Matcher matcher = Pattern.compile("_TV_([^_]+)").matcher(group);
            return matcher.find() ? matcher.group(1).replaceAll("\\^", " ") : null;
        }).filter(Objects::nonNull).findFirst().orElse(null));

//        if (tex_variant != null) {
//            System.out.println("TV: " + tex_variant);
//        }

        if(toggle){
            this.toggleType =
                    hasKey.test("TOGGLE_SMOOTH") ? StateChangeManager.ToggleType.SMOOTH :
                        hasKey.test("TOGGLE_SMOOTHIN") ? StateChangeManager.ToggleType.SMOOTHIN :
                            hasKey.test("TOGGLE.SMOOTHOUT") ? StateChangeManager.ToggleType.SMOOTHOUT :
                                StateChangeManager.ToggleType.LINEAR;
        }
    }

    @Inject(method = "postRender", at = @At(value = "INVOKE"
            , target = "Lcam72cam/mod/render/GlobalRender;drawText(Ljava/lang/String;Lcam72cam/mod/render/opengl/RenderState;Lcam72cam/mod/math/Vec3d;FF)V")
            , locals = LocalCapture.CAPTURE_FAILSOFT
            , cancellable = true
            , remap = false)
    public void labelTranslator(EntityMoveableRollingStock stock, RenderState state, float partialTicks, CallbackInfo ci, boolean isPressed, Matrix4 m, Vec3d pos, String labelstate, float percent, String str) {
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
}
