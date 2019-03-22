package com.github.dskprt.remotemc.mixins;

import com.github.dskprt.remotemc.RemoteMC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Shadow
    public Minecraft mc;

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void chatMessage(String msg, boolean addToChat, CallbackInfo callbackInfo) {
        if(msg.startsWith("rmc$")) {
            if(msg.equals("rmc$start")) {
                RemoteMC.INSTANCE.getHttpProcessor().start();

                mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent("{text:\"§0[§fremoteMC§0] §fStarted the server. You can now go to 127.0.0.1:1234 in your browser\"}"));

                callbackInfo.cancel();
            } else if(msg.equals("rmc$stop")) {
                RemoteMC.INSTANCE.getHttpProcessor().stop();

                mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent("{text:\"§0[§fremoteMC§0] §fStopped the server.\"}"));

                callbackInfo.cancel();
            } else {
                mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent("{text:\"§0[§fremoteMC§0] §fUse §0rmc$start §fand §0rmc$stop §fto start or stop the server.\"}"));

                callbackInfo.cancel();
            }
        }
    }
}
