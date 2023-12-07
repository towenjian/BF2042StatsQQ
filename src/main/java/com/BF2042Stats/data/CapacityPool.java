package com.BF2042Stats.data;

import java.lang.reflect.Array;
import java.util.*;

public class CapacityPool {
    public static final int PLAYER_BD = 1;
    public static final int PLAYER_NBD = 0;
    private static final Map<String,PlayerData> gameID_PlayerData_tempPlayer = new HashMap<>();//临时玩家数据（只允许10个，并且只有6小时缓存时间）
    private static final Map<String,PlayerData> gameID_PlayerData_Group = new LinkedHashMap<>();//群内绑定玩家数据（数量不做限制，仅24小时限制）
    private static final Map<String,String> user_gameID = new HashMap<>();

    /**
     * 新增player玩家类
     * @param playerData 增加的player类
     * @param isBD 是否绑定
     */
    public static void addPlayerData(PlayerData playerData,int isBD){
        switch (isBD){
            case PLAYER_BD:
                gameID_PlayerData_Group.put(playerData.getName().toLowerCase(), playerData);
                gameID_PlayerData_Group.get(playerData.getName().toLowerCase()).setTime(24);
                break;
            case PLAYER_NBD:
                if (gameID_PlayerData_tempPlayer.size()>=ConfigData.getTempPlayer()){
                    List<String> name = new ArrayList<>( gameID_PlayerData_tempPlayer.keySet());
                    gameID_PlayerData_tempPlayer.remove(name.get(ConfigData.getTempPlayer()).toLowerCase()).removeTimer();
                }
                gameID_PlayerData_tempPlayer.put(playerData.getName().toLowerCase(), playerData);
                gameID_PlayerData_tempPlayer.get(playerData.getName().toLowerCase()).setTime(2);
        }
    }

    /**
     * 寻找玩家
     * @param name 玩家的名字
     * @param isBD 是否绑定
     * @return true则是含有该玩家的数据类，false则是没有该玩家的数据类
     */
    public static boolean findPlayerData(String name,int isBD){
        if (isBD==1) return gameID_PlayerData_Group.containsKey(name.toLowerCase());
        if (isBD==0) return gameID_PlayerData_tempPlayer.containsKey(name.toLowerCase());
        return false;
    }

    /**
     * 获取玩家数据
     * @param name 玩家名字
     * @param isBD 是否绑定
     * @return 返回玩家的数据类
     */
    public static PlayerData getPlayerData(String name,int isBD){
        if (isBD==1) return gameID_PlayerData_Group.get(name.toLowerCase());
        if (isBD==0) return gameID_PlayerData_tempPlayer.get(name.toLowerCase());
        return null;
    }
    public static void removePlayerData(String name){
        if (gameID_PlayerData_Group.containsKey(name.toLowerCase())) {
            gameID_PlayerData_Group.remove(name.toLowerCase()).removeTimer();
        }
        if (gameID_PlayerData_tempPlayer.containsKey(name.toLowerCase())){
            gameID_PlayerData_tempPlayer.remove(name.toLowerCase()).removeTimer();
        }
        System.out.println("玩家:"+name+"的数据已经被移除");
    }
    public static int getPlayerBd(){
        return gameID_PlayerData_Group.size();
    }
    public static int getPlayerNbd(){
        return gameID_PlayerData_tempPlayer.size();
    }
    public static Set<String> getPlayerIDBd(){
        return gameID_PlayerData_Group.keySet();
    }
    public static Set<String> getPlayerIDNBd(){
        return gameID_PlayerData_tempPlayer.keySet();
    }
    public static Map<String,Integer> getPlayerName_Time_y(){
        Map<String,Integer> map = new LinkedHashMap<>();
        for (String s : getPlayerIDBd()){
            map.put(s.toLowerCase(),gameID_PlayerData_Group.get(s).getTime());
        }
        return map;
    }
    public static Map<String,Integer> getPlayerName_Time_n(){
        Map<String,Integer> map = new LinkedHashMap<>();
        for (String s : getPlayerIDNBd()){
            map.put(s.toLowerCase(),gameID_PlayerData_tempPlayer.get(s).getTime());
        }
        return map;
    }
}
