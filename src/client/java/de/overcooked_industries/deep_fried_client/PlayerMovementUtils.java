package de.overcooked_industries.deep_fried_client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

import static de.overcooked_industries.deep_fried_client.DeepFriedClientClient.MC;

public class PlayerMovementUtils {

    public static void setMotion(double x, double y, double z) {
        LocalPlayer player = MC.player;
        if (player == null) return;

        player.getAbilities().flying = false;
        player.setDeltaMovement(x, y, z);
    }

    public static void applyMotion(double dx, double dy, double dz) {
        LocalPlayer player = MC.player;
        if (player == null) return;

        Vec3 velocity = player.getDeltaMovement();
        setMotion(velocity.x + dx, velocity.y + dy, velocity.z + dz);
    }
}
