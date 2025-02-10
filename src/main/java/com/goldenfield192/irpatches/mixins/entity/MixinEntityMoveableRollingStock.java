package com.goldenfield192.irpatches.mixins.entity;

import cam72cam.immersiverailroading.entity.EntityMoveableRollingStock;
import cam72cam.mod.entity.Entity;
import cam72cam.mod.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Predicate;

@Deprecated
@Mixin(EntityMoveableRollingStock.class)
public class MixinEntityMoveableRollingStock {
//    @ModifyArgs(method = "onTick",
//                at = @At(value = "INVOKE", target = "Lcam72cam/mod/world/World;getEntities(Ljava/util/function/Predicate;Ljava/lang/Class;)Ljava/util/List;", ordinal = 0),
//                remap = false)
//    public void mixin(Args args){
//        Predicate<Entity> predicate = args.get(0);
//        predicate = predicate.negate();
//        predicate = predicate.or(entity -> (entity.asPlayer() != null && entity.asPlayer().internal.isSpectator()));
//        predicate = predicate.negate();
//        args.set(0, predicate);
//    }
//
//    @Redirect(method = "onTick",
//                at = @At(value = "INVOKE", target = "Lcam72cam/mod/entity/Entity;setVelocity(Lcam72cam/mod/math/Vec3d;)V", ordinal = 1),
//                remap = false)
//    public void mixinVelocity(Entity instance, Vec3d motion){
//        System.out.println(instance);
//    }

//    @Inject(method = "onTick",
//            at = @At(value = "INVOKE_ASSIGN", target = "Lcam72cam/mod/world/World;getEntities(Ljava/util/function/Predicate;Ljava/lang/Class;)Ljava/util/List;", ordinal = 0),
//            remap = false,
//            locals = LocalCapture.CAPTURE_FAILSOFT)
//    public void mixinLocal(CallbackInfo ci, TickPos currentPos, Vec3d prevPos, Vec3i var3, TileRailBase var4, double prevPosX, double prevPosY, double prevPosZ, List entitiesWithin){
//        ((List<Entity>)entitiesWithin).forEach(entity -> System.out.println(entity));
//        System.out.println();
//    }
}
