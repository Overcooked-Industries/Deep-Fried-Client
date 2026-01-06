package Kilip1000.deep_fried_client;

import Kilip1000.deep_fried_client.screens.MainHackScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class DeepFriedClientClient implements ClientModInitializer {
	@FunctionalInterface
	public interface KeyResponse {
		void respond();
	}
	private static record KeyInformation(KeyMapping keyMappings, KeyResponse keyResponses){}

	private static List<KeyInformation> keyInformations = new ArrayList<>();

	/**
	 @param use_mouse {@code false} to register a keyboard input, {@code true} to register a mouse input.
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

	public static boolean fly_hack = false;

	private static final KeyInformation KEY_OPEN = register_keybind("open", false, () -> Minecraft.getInstance().setScreen(new MainHackScreen(Component.empty())));



	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			for (var mapping: keyInformations) {
				if (mapping.keyMappings.isDown()) {
					mapping.keyResponses.respond();
				}
			}
		var pos = Minecraft.getInstance().player.position();
		var rot = Minecraft.getInstance().player.getRotationVector();
		var ground = Minecraft.getInstance().player.onGround();
		ClientPlayNetworking.send(new ServerboundMovePlayerPacket.Pos(new Vec3(pos.x, pos.y, pos.z), true, false))
		});
	}
}