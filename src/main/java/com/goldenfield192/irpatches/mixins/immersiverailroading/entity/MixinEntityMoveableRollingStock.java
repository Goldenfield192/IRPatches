package com.goldenfield192.irpatches.mixins.immersiverailroading.entity;

@Deprecated
//@Mixin(EntityMoveableRollingStock.class)
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
