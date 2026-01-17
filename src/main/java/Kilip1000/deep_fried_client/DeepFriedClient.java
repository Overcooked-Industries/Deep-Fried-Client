package Kilip1000.deep_fried_client;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeepFriedClient implements ModInitializer {
    public static final String MOD_ID = "deep_fried_client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Deep Fried Client loaded.");
    }
}