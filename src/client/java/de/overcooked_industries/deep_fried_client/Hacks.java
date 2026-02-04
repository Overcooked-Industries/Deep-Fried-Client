package de.overcooked_industries.deep_fried_client;

import de.overcooked_industries.deep_fried_client.screens.MainHackScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Hacks {
    public static boolean fly = false;
    public static boolean no_gravity = false;
    public static boolean invisibility_bypass = false;
    public static boolean no_fall = false;

    public static int hack_cooldown = 0;

    @Deprecated(forRemoval = true)
    public static void toggleHack(Hack hack, boolean show_message) {
        hack.toggle(show_message);
    }

    public static void toggleHack(Hack hack) {
        toggleHack(hack, true);
    }

    public enum Hack {
        FLY("Fly"),
        NO_GRAVITY("Zero Gravity"),
        INVISIBILITY_BYPASS("Invisibility Bypass"),
        NO_FALL("No Fall");

        final String name;
        boolean active = false;

        public void toggle(boolean show_message){
            if (hack_cooldown != 0) {
                hack_cooldown = 2;
                return;
            }
            hack_cooldown = 2;

            boolean state = false;
            active = !active;

            assert Minecraft.getInstance().player != null;
            if (show_message) Minecraft.getInstance().player.displayClientMessage(Component.literal("§l" + name + "§r§7 was toggled to " + MainHackScreen.colored_bool_text(state) + "§r§7!"), false);
        }

        Hack(String name) {
            this.name = name;
        }
    }
}