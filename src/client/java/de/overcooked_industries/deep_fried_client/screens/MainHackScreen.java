package de.overcooked_industries.deep_fried_client.screens;

import de.overcooked_industries.deep_fried_client.Hacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MainHackScreen extends Screen {
    public MainHackScreen() {
        super(Component.empty());
    }

    public static String colored_bool_text(boolean org_bool) {
        if (org_bool) {
            return "§aON";
        } else {
            return "§4OFF";
        }
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
        Button buttonWidget = Button.builder(Component.nullToEmpty(title + colored_bool_text(hack)), (btn) -> {
            func.respond();
            reload();
        }).bounds(40, 75 + offset * 25, 150, 20).build();
        this.addRenderableWidget(buttonWidget);
    }

    @Override
    protected void init() {
        addButton("Close", () -> Minecraft.getInstance().setScreen(null), 270, 300, 100, 20);
        addButton("Config",  () -> Minecraft.getInstance().setScreen(new ConfigScreen()), 500, 300, 20, 20);

        ButtonResponse toggleFly =                 () -> Hacks.fly = !Hacks.fly;
        ButtonResponse toggleNoGravity =           () -> Hacks.no_gravity = !Hacks.no_gravity;
        ButtonResponse toggleInvisibilityBypass =  () -> Hacks.invisibility_bypass = !Hacks.invisibility_bypass;
        ButtonResponse toggleNoFall =              () -> Hacks.no_fall = !Hacks.no_fall;
        ButtonResponse toggleFreecam =              () -> Hacks.freecam = !Hacks.freecam;

        addSmartButton("Fly: ",                 Hacks.fly,                   () -> Hacks.toggleHack(Hacks.Hack.FLY, false),                 0);
        addSmartButton("Zero Gravity: ",        Hacks.no_gravity,            () -> Hacks.toggleHack(Hacks.Hack.NO_GRAVITY, false),          1);
        addSmartButton("Invisibility-Bypass: ", Hacks.invisibility_bypass,   () -> Hacks.toggleHack(Hacks.Hack.INVISIBILITY_BYPASS, false), 2);
        addSmartButton("No Fall: ",             Hacks.no_fall,               () -> Hacks.toggleHack(Hacks.Hack.NO_FALL, false),             3);
        addSmartButton("Freecam: ",             Hacks.freecam,               () -> Hacks.toggleHack(Hacks.Hack.FREECAM, false),             4);
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