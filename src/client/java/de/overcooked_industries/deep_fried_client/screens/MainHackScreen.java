package de.overcooked_industries.deep_fried_client.screens;

import de.overcooked_industries.deep_fried_client.Hacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MainHackScreen extends DeepFriedScreen {
    public MainHackScreen() {
        super();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        add_label(context, "§fDeep Fried Client", 40, 40);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        addButton("Close", () -> Minecraft.getInstance().setScreen(null), 270, 300, 100, 20);
        addButton("Config",  () -> Minecraft.getInstance().setScreen(new ConfigScreen()), 500, 300, 20, 20);

        ButtonResponse toggleFly =                 () -> Hacks.fly = !Hacks.fly;
        ButtonResponse toggleNoGravity =           () -> Hacks.no_gravity = !Hacks.no_gravity;
        ButtonResponse toggleInvisibilityBypass =  () -> Hacks.invisibility_bypass = !Hacks.invisibility_bypass;
        ButtonResponse toggleNoFall =              () -> Hacks.no_fall = !Hacks.no_fall;

        addSmartButton("Fly: ",                 Hacks.fly,                   () -> Hacks.toggleHack(Hacks.Hack.FLY, false),                 0);
        addSmartButton("Zero Gravity: ",        Hacks.no_gravity,            () -> Hacks.toggleHack(Hacks.Hack.NO_GRAVITY, false),          1);
        addSmartButton("Invisibility-Bypass: ", Hacks.invisibility_bypass,   () -> Hacks.toggleHack(Hacks.Hack.INVISIBILITY_BYPASS, false), 2);
        addSmartButton("No Fall: ",             Hacks.no_fall,               () -> Hacks.toggleHack(Hacks.Hack.NO_FALL, false),             3);
    }
}