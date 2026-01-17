package Kilip1000.deep_fried_client.mixin.client;

import Kilip1000.deep_fried_client.ActiveHacks;
import Kilip1000.deep_fried_client.DeepFriedClientClient;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z"))
    boolean alwaysVisiblePlayer(Entity instance){
        if(instance instanceof Player) return !ActiveHacks.hack_invisibility_bypass && instance.isInvisible();
        return instance.isInvisible();
    }
}
