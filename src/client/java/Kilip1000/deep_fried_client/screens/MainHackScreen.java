package Kilip1000.deep_fried_client.screens;

import Kilip1000.deep_fried_client.ActiveHacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MainHackScreen extends Screen {
    public MainHackScreen() {
        super(Component.empty());
    }

    public String colored_bool_text(boolean org_bool) {
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

        ButtonResponse toggleFly =      () -> ActiveHacks.hack_fly = !ActiveHacks.hack_fly;
        ButtonResponse toggleNoGrav =   () -> ActiveHacks.hack_no_gravity = !ActiveHacks.hack_no_gravity;
        ButtonResponse toggleNoInvis =  () -> ActiveHacks.hack_invisibility_bypass = !ActiveHacks.hack_invisibility_bypass;

        addSmartButton("Fly: ",                 ActiveHacks.hack_fly,                   toggleFly,      0);
        addSmartButton("Zero Gravity: ",        ActiveHacks.hack_no_gravity,            toggleNoGrav,   1);
        addSmartButton("Invisibility-Bypass: ", ActiveHacks.hack_invisibility_bypass,   toggleNoInvis,  2);
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