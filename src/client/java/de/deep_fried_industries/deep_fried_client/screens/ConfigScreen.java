package de.deep_fried_industries.deep_fried_client.screens;

import de.deep_fried_industries.deep_fried_client.Hacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    public ConfigScreen() {
        super(Component.empty());
    }

    public void reload() {
        Minecraft.getInstance().setScreen(this);
    }

    public void addButton(String title, ButtonResponse func, int pos_x, int pos_y, int size_x, int size_y) {
        Button buttonWidget = Button.builder(Component.nullToEmpty(title), (btn) -> {
            func.respond();
        }).bounds(pos_x, pos_y, size_x, size_y).build();
        this.addRenderableWidget(buttonWidget);
    }

    public void add_label(GuiGraphics context, String title, int pos_x, int pos_y) {
        context.drawString(this.font, title, pos_x, pos_y - this.font.lineHeight - 10, 0xFFFFFFFF, true);
    }

    public void addSmartButton(String title, boolean hack, ButtonResponse func, int offset) {
        Button buttonWidget = Button.builder(Component.nullToEmpty(title + MainHackScreen.colored_bool_text(hack)), (btn) -> {
            func.respond();
            reload();
        }).bounds(40, 75 + offset * 25, 150, 20).build();
        this.addRenderableWidget(buttonWidget);
    }

    @Override
    protected void init() {
        addButton("Back", () -> Minecraft.getInstance().setScreen(new MainHackScreen()), 270, 300, 100, 20);

        ButtonResponse toggleFly =                 () -> Hacks.fly = !Hacks.fly;
        ButtonResponse toggleNoGravity =           () -> Hacks.no_gravity = !Hacks.no_gravity;
        ButtonResponse toggleInvisibilityBypass =  () -> Hacks.invisibility_bypass = !Hacks.invisibility_bypass;
        ButtonResponse toggleNoFall =              () -> Hacks.no_fall = !Hacks.no_fall;

        addSmartButton("Fly: ",                 Hacks.fly,                   toggleFly,                 0);
        addSmartButton("Zero Gravity: ",        Hacks.no_gravity,            toggleNoGravity,           1);
        addSmartButton("Invisibility-Bypass: ", Hacks.invisibility_bypass,   toggleInvisibilityBypass,  2);
        addSmartButton("No Fall: ",             Hacks.no_fall,               toggleNoFall,              3);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        add_label(context, "Deep Fried Client", 40, 40);
        super.render(context, mouseX, mouseY, delta);
    }

    @FunctionalInterface
    public interface ButtonResponse {
        void respond();
    }
}