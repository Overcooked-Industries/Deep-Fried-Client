package de.overcooked_industries.deep_fried_client;

import de.overcooked_industries.deep_fried_client.screens.MainHackScreen;
import static de.overcooked_industries.deep_fried_client.Hacks.Hack.*;
import static de.overcooked_industries.deep_fried_client.Hacks.Hack;
import com.mojang.blaze3d.platform.InputConstants;
import de.overcooked_industries.deep_fried_client.screens.Slider;
import net.fabricmc.api.ClientModInitializer;
import static de.overcooked_industries.deep_fried_client.DeepFriedClient.LOGGER;
import net.minecraft.client.KeyMapping.Category;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class DeepFriedClientClient implements ClientModInitializer {
    public static final List<KeyInformation> keyInformation = new ArrayList<>();
    public static Category CATEGORY = KeyMapping.Category.register(DeepFriedClient.id("keybinds"));
    public static Minecraft MC;

    /**
     * @param use_mouse {@code false} to register a keyboard input, {@code true} to register a mouse input.
     */
    public static void registerKeybind(String id, boolean use_mouse, KeyResponse response, int default_key) {
        KeyMapping mapping = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                id + ".deep_fried_client",
                use_mouse ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM,
                default_key,
                CATEGORY
            )
        );
        var keyMap = new KeyInformation(mapping, response);
        keyInformation.add(keyMap);
    }

    public static void registerHackKeybind(String id, Hack hack) {
        registerKeybind("hack_" + id, false, () -> Hacks.toggleHack(hack), -1);
    }

    public Vec2 getVecFromYaw(double yaw){
        return new Vec2((float) -Math.sin(yaw * (Math.PI / 180)), (float) Math.cos(yaw * (Math.PI / 180)));
    }

    @Override
    public void onInitializeClient() {
        MC = Minecraft.getInstance();
        registerKeybind("open", false, () -> Minecraft.getInstance().setScreen(new MainHackScreen()), GLFW.GLFW_KEY_RIGHT_SHIFT);

        registerHackKeybind("fly", FLY);
        registerHackKeybind("no_gravity", NO_GRAVITY);
        registerHackKeybind("invisibility_bypass", INVISIBILITY_BYPASS);
        registerHackKeybind("no_fall", NO_FALL);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (Hacks.hack_cooldown > 0) Hacks.hack_cooldown -= 1;

            var player = MC.player;
            if (player == null) return;
            Vec3 motion = player.getDeltaMovement();

            for (var mapping : keyInformation) {
                if (mapping.keyMappings.isDown()) {
                    mapping.keyResponses.respond();
                }
            }


            boolean jump = MC.options.keyJump.isDown();
            boolean shift = MC.options.keyShift.isDown();

            var movement = new boolean[]{
                    MC.options.keyUp.isDown(),
                    MC.options.keyLeft.isDown(),
                    MC.options.keyDown.isDown(),
                    MC.options.keyRight.isDown()
            };

            GameType gameMode = player.gameMode();
            boolean isCreative = gameMode == GameType.CREATIVE;
            boolean isSpectator = gameMode == GameType.SPECTATOR;
            boolean inValidGameMode = !(isCreative || isSpectator);

            if (Hacks.fly && inValidGameMode){
                assert MC.player != null;
                Vec3 movement_motion = new Vec3(0, 0, 0);
                Vec2 rotation = MC.player.getRotationVector();

                if (jump) {movement_motion = movement_motion.add(new Vec3(0, 1, 0));}
                if (shift) {movement_motion = movement_motion.add(new Vec3(0, -1, 0));}

                if (movement[0]){
                    movement_motion = movement_motion.add(new Vec3(getVecFromYaw(rotation.y).x, 0, getVecFromYaw(rotation.y).y));
                }
                if (movement[1]){
                    movement_motion = movement_motion.add(new Vec3(getVecFromYaw(rotation.y - 90).x, 0, getVecFromYaw(rotation.y - 90).y));
                }
                if (movement[2]){
                    movement_motion = movement_motion.add(new Vec3(getVecFromYaw(rotation.y + 180).x, 0, getVecFromYaw(rotation.y + 180).y));
                }
                if (movement[3]) {
                    movement_motion = movement_motion.add(new Vec3(getVecFromYaw(rotation.y + 90).x, 0, getVecFromYaw(rotation.y + 90).y));
                }

                PlayerMovementUtils.setMotion(movement_motion.x, movement_motion.y, movement_motion.z);
            }

            if (Hacks.no_fall) player.connection.send(new ServerboundMovePlayerPacket.StatusOnly(true, MC.player.horizontalCollision));
            if (Hacks.no_gravity) PlayerMovementUtils.applyMotion(0, -motion.y, 0);
        });

    }

    @FunctionalInterface
    public interface KeyResponse {
        void respond();
    }

    public record KeyInformation(KeyMapping keyMappings, KeyResponse keyResponses) {}

}