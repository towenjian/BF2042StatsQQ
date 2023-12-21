package com.BF2042Stats.data;

import com.BF2042Stats.BF2042StatsV1;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
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
    private static File data,config;
    private static Map<String,String> qq_gameID = new HashMap<>();
    private static Map<String,Object> map = new HashMap<>();
    private static String welcomeMessage,menuMessage,RequestIssue;
    private static int tempPlayer = 10;
    private static boolean openWelcome = false,openRequestValidation = false,cx = true,vh = true,wp = true,kd = true,kill = true,cl = true,PrivateChatSearch = false,preGet = false;
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
        config = new File(javaPlugin.getConfigFolder(), "config.yml");
        if (!config.exists()){
            Files.copy(javaPlugin.getResourceAsStream("config.yml"), config.toPath());
            config = new File(javaPlugin.getConfigFolder(), "config.yml");
        }
        Yaml yaml = new Yaml();
        InputStreamReader configReader = new InputStreamReader(Files.newInputStream(config.toPath()),StandardCharsets.UTF_8);
        map = yaml.load(configReader);
        groupList = (List<String>) map.get("qqGroup");
        System.out.println(groupList);
        user = (String) map.get("user");
        System.out.println(user);
        qqBot = (long) map.get("qqBot");
        welcomeMessage = (String) map.get("welcome");
        openWelcome = (boolean) map.get("openWelcome");
        openRequestValidation = (boolean) map.get("openRequestValidation");
        tempPlayer = (Integer) map.get("tempPlayer");
        cx = (boolean) map.get("cx");
        cl = (boolean) map.get("cl");
        vh = (boolean) map.get("vh");
        wp = (boolean) map.get("wp");
        kd = (boolean) map.get("kd");
        preGet = (boolean) map.get("preGet");
        PrivateChatSearch = (boolean) map.get("PrivateChatSearch");
        kill = (boolean) map.get("kill");
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

    /**
     * 检查是否是允许的群聊列表
     * @param groupID 传入检查的群聊id
     * @return true则为是允许的列表，false则相反
     */
    public static boolean isGroupList(long groupID){
       return groupList.contains(String.valueOf(groupID));
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

    public static boolean isKill() {
        return kill;
    }

    public static boolean isCl() {
        return cl;
    }

    public static boolean isPrivateChatSearch() {
        return PrivateChatSearch;
    }

    public static void setPrivateChatSearch(boolean privateChatSearch) {
        PrivateChatSearch = privateChatSearch;
    }

    public static void setCx(boolean cx) {
        ConfigData.cx = cx;
        map.put("cx",cx);
    }

    public static void setVh(boolean vh) {
        ConfigData.vh = vh;
    }

    public static void setWp(boolean wp) {
        ConfigData.wp = wp;
        map.put("wp", wp);
    }

    public static void setKd(boolean kd) {
        ConfigData.kd = kd;
        map.put("kd",kd);
    }

    public static void setKill(boolean kill) {
        ConfigData.kill = kill;
        map.put("kill",kill);
    }

    public static void setCl(boolean cl) {
        ConfigData.cl = cl;
        map.put("cl",cl);
    }

    public static String getRequestIssue() {
        return RequestIssue;
    }

    public static int getTempPlayer() {
        return tempPlayer;
    }

    public static boolean isPreGet() {
        return preGet;
    }

    public static void setPreGet(boolean preGet) {
        ConfigData.preGet = preGet;
        map.put("preGet",preGet);
    }
    public static void stop() throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(config.toPath()),StandardCharsets.UTF_8);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        yaml.dump(map,writer);
        System.out.println("配置文件已保存");
    }
}
