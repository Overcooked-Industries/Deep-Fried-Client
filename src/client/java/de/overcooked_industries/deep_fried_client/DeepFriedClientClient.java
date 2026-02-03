package de.overcooked_industries.deep_fried_client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.overcooked_industries.deep_fried_client.screens.MainHackScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static de.overcooked_industries.deep_fried_client.Hacks.*;
import static de.overcooked_industries.deep_fried_client.Hacks.Hack.*;

public class DeepFriedClientClient implements ClientModInitializer {
    public static final List<KeyInformation> keyInformation = new ArrayList<>();
    public static Category CATEGORY = KeyMapping.Category.register(id("keybinds"));
    public static final String MOD_ID = "deep_fried_client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Minecraft MC;

    /**
     * @param use_mouse {@code false} to register a keyboard input, {@code true} to register a mouse input.
     */
    public static void registerKeybind(String id, boolean use_mouse, Runnable response, int default_key) {
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

    public Vec2 getVecFromYaw(double yaw) {
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
            var player = MC.player;
            if (player == null) return;

            if (hack_cooldown > 0) hack_cooldown -= 1;

            for (var mapping : keyInformation) {
                if (mapping.keyMappings.isDown()) {
                    mapping.keyResponses.run();
                }
            }

            var options = MC.options;

            boolean jump = options.keyJump.isDown();
            boolean shift = options.keyShift.isDown();

            var up = options.keyUp.isDown();
            var left = options.keyLeft.isDown();
            var down = options.keyDown.isDown();
            var right = options.keyRight.isDown();

            GameType gameMode = player.gameMode();
            boolean isCreative = gameMode == GameType.CREATIVE;
            boolean isSpectator = gameMode == GameType.SPECTATOR;
            boolean inValidGameMode = !(isCreative || isSpectator);

            if (fly && inValidGameMode) {
                Vec3 movement_motion = new Vec3(0, 0, 0);
                Vec2 rotation = player.getRotationVector();

                if (jump) movement_motion = movement_motion.add(new Vec3(0, 1, 0));
                if (shift) movement_motion = movement_motion.add(new Vec3(0, -1, 0));

                if (up) movement_motion = movement_motion.add(new Vec3(getVecFromYaw(rotation.y).x, 0, getVecFromYaw(rotation.y).y));
                if (down) movement_motion = movement_motion.add(new Vec3(getVecFromYaw(rotation.y + 180).x, 0, getVecFromYaw(rotation.y + 180).y));
                if (left) movement_motion = movement_motion.add(new Vec3(getVecFromYaw(rotation.y - 90).x, 0, getVecFromYaw(rotation.y - 90).y));
                if (right) movement_motion = movement_motion.add(new Vec3(getVecFromYaw(rotation.y + 90).x, 0, getVecFromYaw(rotation.y + 90).y));

                PlayerMovementUtils.setDeltaMovement(movement_motion);
            }

            Vec3 motion = player.getDeltaMovement();
            if (no_fall)
                player.connection.send(new ServerboundMovePlayerPacket.StatusOnly(true, MC.player.horizontalCollision));
            if (no_gravity) PlayerMovementUtils.applyMotion(0, -motion.y, 0);
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext) ->
                dispatcher.register(
                        ClientCommandManager.literal("cgive")
                        .then(ClientCommandManager.argument("item", ItemArgument.item(commandBuildContext))
                                .then(ClientCommandManager.argument("count", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            var player = ctx.getSource().getPlayer();
                                            var item = ItemArgument.getItem(ctx, "item").getItem();
                                            int count = IntegerArgumentType.getInteger(ctx, "count");

                                            player.getInventory().add(new ItemStack(item, count));
                                            return 1;
                                        })
                                ))
        ));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext) ->
                dispatcher.register(
                        ClientCommandManager.literal("lastdeath")
                                                .executes(ctx -> {
                                                    var player = ctx.getSource().getPlayer();
                                                    var location = player.getLastDeathLocation();
                                                    ctx.getSource().sendFeedback(Component.literal(location.<String>map(GlobalPos::toString).orElse("Last death location unknown")));
                                                    return 0;
                                                }
                )
        ));
        LOGGER.info("Deep Fried Client loaded.");
    }

    public record KeyInformation(KeyMapping keyMappings, Runnable keyResponses) {}

    public static Identifier id(String id) {
        return Identifier.fromNamespaceAndPath(MOD_ID, id);
    }
}