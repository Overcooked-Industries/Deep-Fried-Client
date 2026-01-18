package Kilip1000.deep_fried_client.screens;

import Kilip1000.deep_fried_client.Hacks;
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
            reload();
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
        addButton("Close", () -> Minecraft.getInstance().setScreen(null), 270, 350, 100, 20);

        ButtonResponse toggleFly =      () -> Hacks.hack_fly = !Hacks.hack_fly;
        ButtonResponse toggleNoGrav =   () -> Hacks.hack_no_gravity = !Hacks.hack_no_gravity;
        ButtonResponse toggleNoInvis =  () -> Hacks.hack_invisibility_bypass = !Hacks.hack_invisibility_bypass;

        addSmartButton("Fly: ",                 Hacks.hack_fly,                   toggleFly,      0);
        addSmartButton("Zero Gravity: ",        Hacks.hack_no_gravity,            toggleNoGrav,   1);
        addSmartButton("Invisibility-Bypass: ", Hacks.hack_invisibility_bypass,   toggleNoInvis,  2);
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