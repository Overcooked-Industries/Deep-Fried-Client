package Kilip1000.deep_fried_client;

import Kilip1000.deep_fried_client.screens.MainHackScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Hacks {
    public static boolean hack_fly = false;
    public static boolean hack_no_gravity = false;
    public static boolean hack_invisibility_bypass = false;
    public static boolean hack_no_fall = false;

    public static int hack_cooldown = 0;

    public static void toggleHack(Hack hack, boolean show_message){
        if (hack_cooldown != 0) {
            hack_cooldown = 2;
            return;
        }
        hack_cooldown = 2;

        boolean state = false;
        switch (hack) {
            case FLY -> {
                hack_fly = !hack_fly;
                state = hack_fly;
            }
            case NO_GRAVITY -> {
                hack_no_gravity = !hack_no_gravity;
                state = hack_no_gravity;
            }
            case INVISIBILITY_BYPASS -> {
                hack_invisibility_bypass = !hack_invisibility_bypass;
                state = hack_invisibility_bypass;
            }
            case NO_FALL -> {
                hack_no_fall = !hack_no_fall;
                state = hack_no_fall;
            }
        }

        assert Minecraft.getInstance().player != null;

        if(show_message) Minecraft.getInstance().player.displayClientMessage(Component.literal("§l" + hack.name + "§r§7 was toggled to " + MainHackScreen.colored_bool_text(state) + "§r§7!"), false);
    }

    public static void toggleHack(Hack hack){
        toggleHack(hack, true);
    }

    public enum Hack{
        FLY("Fly"),
        NO_GRAVITY("Zero Gravity"),
        INVISIBILITY_BYPASS("Invisibility Bypass"),
        NO_FALL("No Fall");

        final String name;

        Hack(String name) {
            this.name = name;
        }
    }
}