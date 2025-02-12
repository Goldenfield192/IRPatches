package com.goldenfield192.irpatches.mixins.entity;

import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.immersiverailroading.model.part.Control;
import cam72cam.mod.entity.CustomEntity;
import cam72cam.mod.entity.custom.IClickable;
import cam72cam.mod.entity.custom.IKillable;
import cam72cam.mod.entity.custom.ITickable;
import com.goldenfield192.irpatches.common.StateChangeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRollingStock.class)
public abstract class MixinEntityRollingStock extends CustomEntity implements ITickable, IClickable, IKillable {
    @Shadow(remap = false) public abstract float getControlPosition(Control<?> control);

    @Shadow(remap = false) public abstract void setControlPosition(Control<?> control, float val);

    @Inject(method = "onDragRelease",
            at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/immersiverailroading/entity/EntityRollingStock;setControlPressed(Lcam72cam/immersiverailroading/model/part/Control;Z)V"),
            remap = false,
            cancellable = true)
    public void mixinDrag(Control<?> control, CallbackInfo ci){
        if (control.toggle) {
            StateChangeManager.addChange((EntityRollingStock) (CustomEntity) this, control.controlGroup);
        }

        if (control.press) {
            this.setControlPosition(control, 0.0F);
        }

        ci.cancel();
    }
}
