package de.deep_fried_client;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeepFriedClient implements ModInitializer {
    public static final String MOD_ID = "deep_fried_client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Deep Fried Client loaded.");
    }

    public static Identifier id(String id)
    {
        return Identifier.fromNamespaceAndPath(MOD_ID, id);
    }
}