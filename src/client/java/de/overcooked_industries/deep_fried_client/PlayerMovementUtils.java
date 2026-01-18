package de.overcooked_industries.deep_fried_client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

import static de.overcooked_industries.deep_fried_client.DeepFriedClientClient.MC;

public class PlayerMovementUtils {

    public static void setDeltaMovement(double x, double y, double z) {
        LocalPlayer player = MC.player;
        if (player == null) return;

        player.getAbilities().flying = false;
        player.setDeltaMovement(x, y, z);
    }

    public static void setDeltaMovement(Vec3 deltaMovement) {
        LocalPlayer player = MC.player;
        if (player == null) return;

        player.getAbilities().flying = false;
        player.setDeltaMovement(deltaMovement);
    }

    public static void applyMotion(double dx, double dy, double dz) {
        LocalPlayer player = MC.player;
        if (player == null) return;

        Vec3 velocity = player.getDeltaMovement();
        setDeltaMovement(velocity.x + dx, velocity.y + dy, velocity.z + dz);
    }
}
