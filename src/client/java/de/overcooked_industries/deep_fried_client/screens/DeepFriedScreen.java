package de.overcooked_industries.deep_fried_client.screens;

import de.overcooked_industries.deep_fried_client.Hacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class DeepFriedScreen extends Screen {
    public DeepFriedScreen() {
        super(Component.empty());
    }

    public void reload() {
        Minecraft.getInstance().setScreen(this);
    }

    public void addButton(String title, Runnable onPress, int pos_x, int pos_y, int size_x, int size_y) {
        Button buttonWidget = Button.builder(Component.nullToEmpty(title), btn -> onPress.run()).bounds(pos_x, pos_y, size_x, size_y).build();
        this.addRenderableWidget(buttonWidget);
    }

    public void addSmartButton(String title, Hacks.Hack hack, int offset, boolean show_message) {
        addButton(title + MainHackScreen.colored_bool_text(hack.active), () -> {
            hack.toggle(show_message);
            reload();
        }, 40, 75 + offset * 25, 150, 20);
    }

    public void addSlider(String title, int pos_x, int pos_y, int size_x, int size_y) {
        Slider sliderWidget = new Slider(
                pos_x,
                pos_y,
                size_x,
                size_y,
                title,
                1
        );
        this.addRenderableWidget(sliderWidget);
    }

    public void add_label(GuiGraphics context, String title, int pos_x, int pos_y) {
        context.drawString(this.font, title, pos_x, pos_y - this.font.lineHeight - 10, 0xFFFFFFFF, true);
    }

    public static String colored_bool_text(boolean org_bool) {
        return org_bool ? "§aON" : "§4OFF";
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        add_label(context, "", 40, 40);
        super.render(context, mouseX, mouseY, delta);
    }
}
