package com.coolspy3.hypixelapi;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Command {
    
    public static final String regex = "/linkhypixelapi " + HypixelAPI.uuidRegex + "( .*)?";
    public static final Pattern pattern = Pattern.compile(regex);
    private HypixelAPI mod;

    public Command(HypixelAPI mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void register(ClientChatEvent event) {
        String msg = event.getMessage();
        if(msg.matches("/linkhypixelapi( .*)?")) {
            event.setCanceled(true);
            Minecraft.getInstance().gui.getChat().addRecentChat(event.getMessage());
            Matcher matcher = pattern.matcher(msg);
            if(msg.matches("/linkhypixelapi new(.*)?")) {
                HypixelAPI.sendMessage(TextFormatting.AQUA + "Generating New API Key...");
                mod.awaitAPIKey = true;
                Minecraft.getInstance().player.chat("/api new");
            } else if(matcher.matches()) {
                try {
                    String apiKey = matcher.group(1);
                    HypixelAPI.sendMessage(TextFormatting.AQUA + "API key set to: \"" + apiKey + "\"");
                    APIConfig.getInstance().apiKey = apiKey;
                    APIConfig.save();
                } catch(IOException e) {
                    e.printStackTrace(System.err);
                }
            } else if(msg.matches("/linkhypixelapi ?")) {
                HypixelAPI.sendMessage(TextFormatting.YELLOW + "WARNING: This will overwrite any existing API keys");
                HypixelAPI.sendMessage(TextFormatting.AQUA + "Type \"/linkhypixelapi new\" to continue");
                HypixelAPI.sendMessage(TextFormatting.AQUA + "Or type \"/linkhypixelapi <API key>\" to link an existing API key");
            } else {
                HypixelAPI.sendMessage(TextFormatting.RED + "Invalid API key: \"" + msg.substring("/linkhypixelapi ".length()) + "\"");
            }
        }
    }

}
