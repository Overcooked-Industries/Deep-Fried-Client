package de.overcooked_industries.deep_fried_client.screens;

import de.overcooked_industries.deep_fried_client.Hacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends DeepFriedScreen {
    public ConfigScreen() {
        super();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        add_label(context, "§7Deep Fried Client§l/§r§fConfig", 40, 40);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        addButton("Back", () -> Minecraft.getInstance().setScreen(new MainHackScreen()), 270, 300, 100, 20);

        addSlider("Flight Speed", 40, 75, 150, 20);
    }
}