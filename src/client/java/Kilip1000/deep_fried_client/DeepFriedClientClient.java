package Kilip1000.deep_fried_client;

import Kilip1000.deep_fried_client.screens.MainHackScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static Kilip1000.deep_fried_client.DeepFriedClient.LOGGER;

public class DeepFriedClientClient implements ClientModInitializer {
    private static final List<KeyInformation> keyInformations = new ArrayList<>();
    private static final KeyInformation KEY_OPEN = register_keybind("open", false, () -> Minecraft.getInstance().setScreen(new MainHackScreen(Component.empty())));
    public static Minecraft MC;

    public static boolean hack_fly = false;
    public static boolean hack_no_gravity = false;

    /**
     * @param use_mouse {@code false} to register a keyboard input, {@code true} to register a mouse input.
     */
    public static KeyInformation register_keybind(String id, boolean use_mouse, KeyResponse response) {
        KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("deep_fried_client", "keybinds"));
        KeyMapping mapping = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                id + ".deep_fried_client",
                use_mouse ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                CATEGORY
        ));
        var keyMap = new KeyInformation(mapping, response);
        keyInformations.add(keyMap);
        return keyMap;
    }

    @Override
    public void onInitializeClient() {
        MC = Minecraft.getInstance();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (MC.player == null) return;
            Vec3 motion = MC.player.getDeltaMovement();

            for (var mapping : keyInformations) {
                if (mapping.keyMappings.isDown()) {
                    mapping.keyResponses.respond();
                }
            }

            if (MC.options.keyJump.isDown() && hack_fly && MC.player.gameMode() != GameType.CREATIVE && MC.player.gameMode() != GameType.SPECTATOR) {
                PlayerMovementUtils.setMotion(motion.x, 1, motion.z);
            }

            if (MC.options.keyShift.isDown() && hack_fly && MC.player.gameMode() != GameType.CREATIVE && MC.player.gameMode() != GameType.SPECTATOR) {
                PlayerMovementUtils.setMotion(motion.x, -1, motion.z);
            }

            if (hack_no_gravity || (hack_fly && !(MC.options.keyShift.isDown() || MC.options.keyJump.isDown()))) {
                PlayerMovementUtils.applyMotion(0, -motion.y, 0);
            }

            //DEBUG
            for (var p: MC.level.players()){
                if (p.getUUID() != MC.player.getUUID()){
                    LOGGER.info(p.getPlainTextName() + ": " + p.position().toString());
                }
            }
        });

    }

    @FunctionalInterface
    public interface KeyResponse {
        void respond();
    }

    private record KeyInformation(KeyMapping keyMappings, KeyResponse keyResponses) {
    }
}