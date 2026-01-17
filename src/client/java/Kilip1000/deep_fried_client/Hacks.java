package Kilip1000.deep_fried_client;

import Kilip1000.deep_fried_client.screens.MainHackScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Hacks {
    public static boolean hack_fly = false;
    public static boolean hack_no_gravity = false;
    public static boolean hack_invisibility_bypass = false;

    //value classes would be fun for toggles, I'm sad
    public static void toggleHack(Hack hack){
        switch (hack) {
            case FLY -> hack_fly = !hack_fly;
            case NO_GRAVITY -> hack_no_gravity = !hack_no_gravity;
            case INVISIBILITY_BYPASS -> hack_invisibility_bypass = !hack_invisibility_bypass;
        }
        boolean state = false;
        switch (hack) {
            case FLY -> state = hack_fly;
            case NO_GRAVITY -> state = hack_no_gravity;
            case INVISIBILITY_BYPASS -> state = hack_invisibility_bypass;
        }

        Minecraft.getInstance().player.displayClientMessage(Component.literal(hack.name + " was toggled to " + MainHackScreen.colored_bool_text(state) + "§r!"), false);
    }

    static enum Hack{
        FLY("Fly"),
        NO_GRAVITY("Zero Gravity"),
        INVISIBILITY_BYPASS("Invisibility Bypass");

        String name;

        Hack(String name) {
            this.name = name;
        }
    }
}