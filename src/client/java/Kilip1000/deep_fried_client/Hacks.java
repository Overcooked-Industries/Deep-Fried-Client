package Kilip1000.deep_fried_client;

import Kilip1000.deep_fried_client.screens.MainHackScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Hacks {
    public static boolean fly = false;
    public static boolean no_gravity = false;
    public static boolean invisibility_bypass = false;
    public static boolean no_fall = false;
    public static boolean free_cam = false;

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
                fly = !fly;
                state = fly;
            }
            case NO_GRAVITY -> {
                no_gravity = !no_gravity;
                state = no_gravity;
            }
            case INVISIBILITY_BYPASS -> {
                invisibility_bypass = !invisibility_bypass;
                state = invisibility_bypass;
            }
            case NO_FALL -> {
                no_fall = !no_fall;
                state = no_fall;
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