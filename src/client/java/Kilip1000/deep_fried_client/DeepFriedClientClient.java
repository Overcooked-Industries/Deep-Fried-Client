package Kilip1000.deep_fried_client;

import Kilip1000.deep_fried_client.screens.MainHackScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping.Category;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class DeepFriedClientClient implements ClientModInitializer {
    private static final List<KeyInformation> keyInformation = new ArrayList<>();
    public static Category CATEGORY = KeyMapping.Category.register(DeepFriedClient.id("keybinds"));
    public static Minecraft MC;

    /**
     * @param use_mouse {@code false} to register a keyboard input, {@code true} to register a mouse input.
     */
    public static KeyInformation registerKeybind(String id, boolean use_mouse, KeyResponse response, int default_key) {
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
        return keyMap;
    }

    @Override
    public void onInitializeClient() {
        MC = Minecraft.getInstance();
        registerKeybind("open", false, () -> Minecraft.getInstance().setScreen(new MainHackScreen()), GLFW.GLFW_KEY_RIGHT_SHIFT);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (MC.player == null) return;
            Vec3 motion = MC.player.getDeltaMovement();

            for (var mapping : keyInformation) {
                if (mapping.keyMappings.isDown()) {
                    mapping.keyResponses.respond();
                }
            }

            boolean jump = MC.options.keyJump.isDown();
            boolean shift = MC.options.keyShift.isDown();

            GameType gameMode = MC.player.gameMode();
            boolean isCreative = gameMode == GameType.CREATIVE;
            boolean isSpectator = gameMode == GameType.SPECTATOR;
            boolean inValidGameMode = !(isCreative || isSpectator);

            if (ActiveHacks.hack_fly && jump && inValidGameMode) {
                PlayerMovementUtils.setMotion(motion.x, 1, motion.z);
            }

            if (ActiveHacks.hack_fly && shift && inValidGameMode) {
                PlayerMovementUtils.setMotion(motion.x, -1, motion.z);
            }

            if (ActiveHacks.hack_no_gravity || (ActiveHacks.hack_fly && !(shift || jump))) {
                PlayerMovementUtils.applyMotion(0, -motion.y, 0);
            }
        });

    }

    @FunctionalInterface
    public interface KeyResponse {
        void respond();
    }

    public record KeyInformation(KeyMapping keyMappings, KeyResponse keyResponses) {}
}