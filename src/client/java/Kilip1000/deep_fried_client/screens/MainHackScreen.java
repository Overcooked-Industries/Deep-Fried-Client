package Kilip1000.deep_fried_client.screens;

import Kilip1000.deep_fried_client.DeepFriedClientClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import static Kilip1000.deep_fried_client.DeepFriedClientClient.MC;

public class MainHackScreen extends Screen {
    public MainHackScreen(Component title) {
        super(title);
    }

    public String colored_bool_text(boolean org_bool) {
        if (org_bool) {
            return "§aON";
        } else {
            return "§4OFF";
        }
    }

    @FunctionalInterface
    public interface ButtonResponse {
        void respond();
    }

    public void reload(){
        Minecraft.getInstance().setScreen(this);
    }

    public void add_button(String title, ButtonResponse func, int pos_x, int pos_y, int size_x, int size_y) {
        Button buttonWidget = Button.builder(Component.nullToEmpty(title), (btn) -> func.respond()).bounds(pos_x, pos_y, size_x, size_y).build();
        this.addRenderableWidget(buttonWidget);
    }

    public void add_label(GuiGraphics context, String title, int pos_x, int pos_y) {
        context.drawString(this.font, title, pos_x, pos_y - this.font.lineHeight - 10, 0xFFFFFFFF, true);
    }

    @Override
    protected void init() {
        add_button("Close", () -> {
            Minecraft.getInstance().setScreen(null);
        }, 270, 350, 100, 20);
        add_button("Fly Hack: " + colored_bool_text(DeepFriedClientClient.fly_hack), () -> {
            DeepFriedClientClient.fly_hack = !DeepFriedClientClient.fly_hack;
            reload();

            LocalPlayer player = MC.player;
            if(player == null) return;

            player.getAbilities().flying = false;

            player.setDeltaMovement(0, 0, 0);
            Vec3 velocity = player.getDeltaMovement();

            player.setDeltaMovement(velocity.x, 5, velocity.z);

            }, 40, 75, 150, 20);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        add_label(context, "Deep Fried Client", 40, 40);
        super.render(context, mouseX, mouseY, delta);
    }
}