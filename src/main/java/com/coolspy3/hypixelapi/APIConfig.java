package com.coolspy3.hypixelapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.coolspy3.util.ModUtil;
import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

public class APIConfig {
    
    public String apiKey = null;

    // Base Config Code

    private static final File cfgFile = Minecraft.getInstance().gameDirectory.toPath().resolve("hypixelapi.cfg.json").toFile();
    private static APIConfig INSTANCE = new APIConfig();
    
    public static APIConfig getInstance() {
        return INSTANCE;
    }

    public String getAPIKey() throws IOException {
        if(apiKey == null) {
            load();
        }
        return INSTANCE.apiKey;
    }

    public static String requireAPI() throws IOException {
        String apiKey = getInstance().getAPIKey();
        if (apiKey == null) {
            ModUtil.sendMessage(TextFormatting.RED
                + "Hypixel API is not linked!");
            ModUtil.sendMessage(TextFormatting.RED
                + "Ensure that the Hypixel API mod is installed and run \"/linkhypixelapi\"");
        }
        return apiKey;
    }

    public static void load() throws IOException {
        if(!cfgFile.exists()) {
            return;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(cfgFile))) {
            String data = "", line;
            while((line = reader.readLine()) != null) {
                data += line;
                data += "\n";
            }
            data = data.substring(0, data.length()-1);
            Gson gson = new Gson();
            INSTANCE = gson.fromJson(data, APIConfig.class);
        }
    }

    public static void save() throws IOException {
        cfgFile.createNewFile();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(cfgFile))) {
            Gson gson = new Gson();
            writer.write(gson.toJson(getInstance()));
        }
    }

}