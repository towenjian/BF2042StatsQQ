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
    private static File data,config,customResults;
    private static Map<String,String> qq_gameID = new HashMap<>(),customResultsMap = new HashMap<>();
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
        customResults = new File(javaPlugin.getConfigFolder(),"customResults.yml");
        if (!config.exists()){
            Files.copy(javaPlugin.getResourceAsStream("config.yml"), config.toPath());
            config = new File(javaPlugin.getConfigFolder(), "config.yml");
        }
        if (!customResults.exists()){
            customResults.createNewFile();
            customResults = new File(javaPlugin.getConfigFolder(), "customResults.yml");
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
        Yaml customResultsYml = new Yaml();
        InputStreamReader customResultsReader = new InputStreamReader(Files.newInputStream(customResults.toPath()),StandardCharsets.UTF_8);
        customResultsMap = customResultsYml.load(customResultsReader);
    }

    /**
     * 设置玩家的单独鉴定结果,输入null则会清除数据
     * @param name 玩家ID
     * @param s 设置的鉴定结果
     */
    public static void setCustomResultsMap(String name,String s){
        if (customResultsMap == null) customResultsMap = new HashMap<>();
        if (s.equals("null")){
            removeCustomResultsMap(name);
        }else customResultsMap.put(name.toLowerCase(),s);
    }

    /**
     * 获取玩家的特制鉴定结果
     * @param name 玩家名字
     * @return 返回玩家的特制鉴定结果
     */
    public static String getCusTomResult(String name){
        if (customResultsMap==null) return null;
        return customResultsMap.getOrDefault(name.toLowerCase(), null);
    }

    /**
     * 返回玩家的鉴定结果map
     * @return 返回玩家的鉴定结果map
     */
    public static Map<String,String> getCustomResultsMap(){
        return customResultsMap;
    }

    /**
     * 移除玩家的鉴定结果
     * @param name 玩家名字
     */
    public static void removeCustomResultsMap(String name){
        customResultsMap.remove(name.toLowerCase());
    }

    /**
     * 设置绑定玩家与ID及平台
     * @param qq qq号
     * @param ID_Pt 类型为"id#pt"自行拼接
     * @return 设置完成返回true反之亦然
     */
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
    public static Map<String,String> getQq_gameID(){
        return qq_gameID;
    }

    /**
     * 检查是否是允许的群聊列表
     * @param groupID 传入检查的群聊id
     * @return true则为是允许的列表，false则相反
     */
    public static boolean isGroupList(long groupID){
       return groupList.contains(String.valueOf(groupID));
    }

    /**
     * 检查该qq号是否绑定
     * @param qq qq号
     * @return true则为已经绑定false则相反
     */
    public static boolean isBD(long qq){
        if (qq_gameID==null) return false;
        return qq_gameID.containsKey(String.valueOf(qq));
    }

    /**
     * 检查该玩家ID是否已经绑定
     * @param name 玩家ID
     * @return true则为已经绑定false则相反
     */
    public static boolean isBD(String name){
        if (qq_gameID == null) return false;
        return qq_gameID.values().stream().map(s -> s.split("#")[0].toLowerCase()).collect(Collectors.toList()).contains(name.toLowerCase());
    }

    /**
     * 清楚绑定的玩家数据
     * @param qq 需要清楚的qq号
     */
    public static void removeBD(long qq){
        qq_gameID.remove(String.valueOf(qq));
    }
    public static void removeBD(String qq){
        qq_gameID.remove(qq);
    }

    /**
     * 返回绑定的玩家数据
     * @param qq qq号
     * @return 返回绑定的数据格式为"id#pt" 自行使用spilt分割
     */
    public static String GameID(long qq){
        return qq_gameID.getOrDefault(String.valueOf(qq), null);
    }

    /**
     * 获取管理员账号
     * @return 返回管理员号码
     */
    public static String getUser() {
        return user;
    }

    /**
     * 改变admin账号
     * @param num 管理员的账号
     */
    public static void changeAdmin(String num){
        if (num==null) return;
        if (num.length()<6) return;
        user = num;
        map.put("user",user);
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

    /**
     * 获取欢迎消息
     * @return 返回入群欢迎字符串
     */
    public static String getWelcomeMessage(){
        if (welcomeMessage==null) return "null";
        return welcomeMessage;
    }

    /**
     * 设置入群欢迎字符串
     * @param message 入群欢迎的字符串
     */
    public static void setWelcomeMessage(String message){
        welcomeMessage = message;
        map.put("welcome",welcomeMessage);
    }
    public static boolean isOpenWelcome(){
        return openWelcome;
    }
    public static String getMenuMessage(){
        return menuMessage;
    }
    public static void setMenuMessage(String message){
        menuMessage = message;
        map.put("menuMessage",menuMessage);
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
        map.put("PrivateChatSearch",privateChatSearch);
    }

    public static void setCx(boolean cx) {
        ConfigData.cx = cx;
        map.put("cx",cx);
    }

    public static void setVh(boolean vh) {
        ConfigData.vh = vh;
        map.put("vh",vh);
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

    public static boolean isOpenRequestValidation() {
        return openRequestValidation;
    }

    public static void setOpenRequestValidation(boolean openRequestValidation) {
        ConfigData.openRequestValidation = openRequestValidation;
        map.put("openRequestValidation",openRequestValidation);
    }

    public static void setOpenWelcome(boolean openWelcome) {
        ConfigData.openWelcome = openWelcome;
        map.put("openWelcome",openWelcome);
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

    /**
     * 插件关闭时保存配置文件
     * @throws IOException 异常
     */
    public static void stop() throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(config.toPath()),StandardCharsets.UTF_8);
        DumperOptions options = new DumperOptions();
        map.put("qqGroup",groupList);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        yaml.dump(map,writer);
        System.out.println("配置文件已保存");
        OutputStreamWriter writer1 = new OutputStreamWriter(Files.newOutputStream(customResults.toPath()),StandardCharsets.UTF_8);
        Yaml yaml1 = new Yaml();
        yaml1.dump(customResultsMap,writer1);

        if (qq_gameID==null) qq_gameID = new HashMap<>();
        FileWriter dataWriter = new FileWriter(data);
        Yaml yaml2 = new Yaml();
        yaml2.dump(qq_gameID, dataWriter);
    }

    /**
     * 增加允许的群聊
     * @param num 群聊号
     */
    public static void addGroupList(String num){
        if (num==null) return;
        groupList.add(num);
    }

    /**
     * 移除群聊号
     * @param num 需要被移除的群聊
     * @return 成功移除则会返回true反之亦然
     */
    public static boolean removeGroupList(String num){
        return groupList.remove(num);
    }
}
