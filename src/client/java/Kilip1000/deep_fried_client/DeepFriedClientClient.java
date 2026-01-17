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
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class DeepFriedClientClient implements ClientModInitializer {
    private static final List<KeyInformation> keyInformations = new ArrayList<>();
    private static final KeyInformation KEY_OPEN = register_keybind("open", false, () -> Minecraft.getInstance().setScreen(new MainHackScreen(Component.empty())));
    public static Minecraft MC;
    public static boolean fly_hack = false;

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
    public String toString() {
        return super.toString();
    }

    @Override
    public void onInitializeClient() {
        MC = Minecraft.getInstance();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (var mapping : keyInformations) {
                if (mapping.keyMappings.isDown()) {
                    mapping.keyResponses.respond();
                }
            }
            //ClientPlayNetworking.send(new ServerboundMovePlayerPacket.Pos(new Vec3(pos.x, pos.y, pos.z), true, false))
        });

    }

    @FunctionalInterface
    public interface KeyResponse {
        void respond();
    }

    private record KeyInformation(KeyMapping keyMappings, KeyResponse keyResponses) {
    }
}