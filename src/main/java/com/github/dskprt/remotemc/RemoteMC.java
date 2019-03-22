package com.github.dskprt.remotemc;

import com.github.dskprt.remotemc.server.HTTPProcessor;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public enum RemoteMC {

    INSTANCE;

    public final Logger LOGGER = LogManager.getLogger("remoteMC");

    private Minecraft mc;
    private HTTPProcessor httpProcessor;

    public void init() {
        LOGGER.info("Loaded remoteMC!");

        mc = Minecraft.getMinecraft();

        try {
            httpProcessor = new HTTPProcessor(INSTANCE);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Minecraft getMinecraft() {
        return mc;
    }

    public HTTPProcessor getHttpProcessor() {
        return httpProcessor;
    }
}
