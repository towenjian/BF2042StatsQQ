package com.BF2042Stats.data;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigData {
    private static JavaPlugin javaPlugin;
    private static long qqBot;
    private static String user;
    private static List<String> groupList = new ArrayList<>();
    private static File data;
    private static Map<String,String> qq_gameID = new HashMap<>();

    public ConfigData(JavaPlugin javaPlugin) {
        ConfigData.javaPlugin = javaPlugin;
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException {
        File config = new File(javaPlugin.getConfigFolder(), "config.yml");
        if (!config.exists()){
            Files.copy(javaPlugin.getResourceAsStream("config.yml"), config.toPath());
            config = new File(javaPlugin.getConfigFolder(), "config.yml");
        }
        Yaml yaml = new Yaml();
        FileReader configReader = new FileReader(config);
        Map<String,Object> map = yaml.load(configReader);
        groupList = (List<String>) map.get("qqGroup");
        System.out.println(groupList);
        user = (String) map.get("user");
        System.out.println(user);
        qqBot = (long) map.get("qqBot");
        System.out.println(qqBot);
        data = new File(javaPlugin.getDataFolder(),"data.yml");
        if (!data.exists()) {
            data.createNewFile();
            data = new File(javaPlugin.getDataFolder(), "data.yml");
        }
        FileReader dataReader = new FileReader(data);
        Yaml dataYaml = new Yaml();
        qq_gameID = dataYaml.load(dataReader);
        if (qq_gameID==null) qq_gameID = new HashMap<>();
    }

    public static boolean setGameID(String qq,String ID_Pt){
        try {
            if (qq_gameID==null) qq_gameID = new HashMap<>();
            qq_gameID.put(qq, ID_Pt);
            FileWriter dataWriter = new FileWriter(data);
            Yaml yaml = new Yaml();
            yaml.dump(qq_gameID, dataWriter);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public static boolean isBD(long qq){
        if (qq_gameID==null) return false;
        return qq_gameID.containsKey(String.valueOf(qq));
    }
    public static boolean isBD(String name){
        if (qq_gameID == null) return false;
        return qq_gameID.values().stream().map(s -> s.split("#")[0].toLowerCase()).collect(Collectors.toList()).contains(name.toLowerCase());
    }
    public static String GameID(long qq){
        return qq_gameID.getOrDefault(String.valueOf(qq), null);
    }
    public static String getUser() {
        return user;
    }

    public static long getQqBot() {
        return qqBot;
    }

    public static List<String> getGroupList() {
        return groupList;
    }
}
