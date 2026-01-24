package de.overcooked_industries.deep_fried_client.screens;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class Slider extends AbstractSliderButton {
    String text;

    public Slider(int x, int y, int width, int height, String text, double value) {
        super(x, y, width, height, Component.literal(""), value);
        this.text = text;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format("%s: %.2f", text, this.value)));
    }

    @Override
    protected void applyValue() {
        double value = this.value;
    }
}
