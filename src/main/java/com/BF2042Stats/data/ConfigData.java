package com.BF2042Stats.data;

import com.BF2042Stats.BF2042StatsV1;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigData {
    private static JavaPlugin javaPlugin;
    private static long qqBot;
    private static String user;
    private static List<String> groupList = new ArrayList<>();
    private static File data;
    private static Map<String,String> qq_gameID = new HashMap<>();
    private static String welcomeMessage,menuMessage,RequestIssue;
    private static boolean openWelcome = false,openRequestValidation = false,cx = true,vh = true,wp = true,kd = true,kpm = true,cl = true;
    private static final ConfigData configData = new ConfigData();

    private ConfigData() {
        ConfigData.javaPlugin = BF2042StatsV1.getJP();
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ConfigData getInstance(){
        return configData;
    }
    private static void init() throws IOException {
        File config = new File(javaPlugin.getConfigFolder(), "config.yml");
        if (!config.exists()){
            Files.copy(javaPlugin.getResourceAsStream("config.yml"), config.toPath());
            config = new File(javaPlugin.getConfigFolder(), "config.yml");
        }
        Yaml yaml = new Yaml();
//        FileReader configReader = new FileReader(config,StandardCharsets.UTF_8);
        InputStreamReader configReader = new InputStreamReader(Files.newInputStream(config.toPath()),StandardCharsets.UTF_8);
        Map<String,Object> map = yaml.load(configReader);
        groupList = (List<String>) map.get("qqGroup");
        System.out.println(groupList);
        user = (String) map.get("user");
        System.out.println(user);
        qqBot = (long) map.get("qqBot");
        welcomeMessage = (String) map.get("welcome");
        openWelcome = (boolean) map.get("openWelcome");
        openRequestValidation = (boolean) map.get("openRequestValidation");
        cx = (boolean) map.get("cx");
        cl = (boolean) map.get("cl");
        vh = (boolean) map.get("vh");
        wp = (boolean) map.get("wp");
        kd = (boolean) map.get("kd");
        kpm = (boolean) map.get("kpm");
        menuMessage = (String) map.get("menuMessage");
        RequestIssue = (String) map.get("RequestIssue");
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

    /**
     *
     * @param u qq用户的id
     * @return true则是管理员账号，false则相反
     */
    public static boolean isAdmin(long u){
        return (String.valueOf(user)).equals(String.valueOf(u));
    }

    public static long getQqBot() {
        return qqBot;
    }

    public static List<String> getGroupList() {
        return groupList;
    }
    public static String getWelcomeMessage(){
        if (welcomeMessage==null) return "null";
        return welcomeMessage;
    }
    public static boolean isOpenWelcome(){
        return openWelcome;
    }
    public static String getMenuMessage(){
        return menuMessage;
    }
    public static void reload() throws IOException {
        init();
    }
    public void sout(){
        System.out.println("load");
    }

    public static JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public static boolean isCx() {
        return cx;
    }

    public static boolean isVh() {
        return vh;
    }

    public static boolean isWp() {
        return wp;
    }

    public static boolean isKd() {
        return kd;
    }

    public static boolean isKpm() {
        return kpm;
    }

    public static boolean isCl() {
        return cl;
    }

    public static void setCx(boolean cx) {
        ConfigData.cx = cx;
    }

    public static void setVh(boolean vh) {
        ConfigData.vh = vh;
    }

    public static void setWp(boolean wp) {
        ConfigData.wp = wp;
    }

    public static void setKd(boolean kd) {
        ConfigData.kd = kd;
    }

    public static void setKpm(boolean kpm) {
        ConfigData.kpm = kpm;
    }

    public static void setCl(boolean cl) {
        ConfigData.cl = cl;
    }
    public static void setw(){

    }
}
