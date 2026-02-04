package de.overcooked_industries.deep_fried_client;

import de.overcooked_industries.deep_fried_client.screens.MainHackScreen;
import net.minecraft.network.chat.Component;

import static de.overcooked_industries.deep_fried_client.DeepFriedClientClient.MC;

public class Hacks {
    public static int global_cooldown = 0;

    public enum Hack {
        FLY("Fly"),
        NO_GRAVITY("Zero Gravity"),
        INVISIBILITY_BYPASS("Invisibility Bypass"),
        NO_FALL("No Fall");

        final String name;
        public boolean active = false;

        public void toggle(boolean show_message){
            if (global_cooldown != 0) {
                global_cooldown = 2;
                return;
            }
            global_cooldown = 2;
            active = !active;

            if (!show_message) return;

            assert MC.player != null;
            MC.player.displayClientMessage(Component.literal("§l" + name + "§r§7 was toggled to " + MainHackScreen.colored_bool_text(active) + "§r§7!"), false);
        }

        Hack(String name) {
            this.name = name;
        }
    }
}