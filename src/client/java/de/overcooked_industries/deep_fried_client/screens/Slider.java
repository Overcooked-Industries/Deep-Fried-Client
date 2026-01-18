package de.overcooked_industries.deep_fried_client.screens;

import de.overcooked_industries.deep_fried_client.DeepFriedClientClient;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class Slider extends AbstractSliderButton {
    String text = "";

    public Slider(int x, int y, int width, int height, String message, double value) {
        super(x, y, width, height, Component.literal(message), value);
        String text = message;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(text + ": " + String.format("%.2f", this.value)));
    }

    @Override
    protected void applyValue() {
        double value = this.value;
    }
}
