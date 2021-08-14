package com.coolspy3.hypixelapi;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.brigadier.LiteralMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("hypixelapi")
public class HypixelAPI {
    
    public static final String uuidRegex = "([a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})";
    public static final String keyRegex = "Your new API key is " + uuidRegex;
    public static final Pattern keyPattern = Pattern.compile(keyRegex);
    public boolean awaitAPIKey = false;

    public HypixelAPI() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new Command(this));
    }

    @SubscribeEvent
    public void onChatMessageRecieved(ClientChatReceivedEvent event) {
        if(awaitAPIKey) {
            String msg = event.getMessage().getString();
            Matcher matcher = keyPattern.matcher(msg);
            if(matcher.matches()) {
                try {
                    String apiKey = matcher.group(1);
                    HypixelAPI.sendMessage(TextFormatting.AQUA + "API key set to: \"" + apiKey + "\"");
                    APIConfig.getInstance().apiKey = apiKey;
                    APIConfig.save();
                } catch(IOException e) {
                    e.printStackTrace(System.err);
                }
                awaitAPIKey = false;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
        awaitAPIKey = false;
    }

    public static void sendMessage(String msg) {
        Minecraft.getInstance().player.sendMessage(TextComponentUtils.fromMessage(new LiteralMessage(msg)), Util.NIL_UUID);
    }

}
