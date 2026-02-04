package de.overcooked_industries.deep_fried_client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import static de.overcooked_industries.deep_fried_client.Hacks.Hack.*;

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
        addButton("Config", () -> Minecraft.getInstance().setScreen(new ConfigScreen()), 500, 300, 20, 20);

        addSmartButton("Fly: ", FLY, 0, false);
        addSmartButton("Zero Gravity: ", NO_GRAVITY, 1, false);
        addSmartButton("Invisibility-Bypass: ", INVISIBILITY_BYPASS, 2, false);
        addSmartButton("No Fall: ", NO_FALL, 3, false);
    }
}