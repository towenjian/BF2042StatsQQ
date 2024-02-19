package com.BF2042Stats.data;

import net.mamoe.mirai.contact.Member;

import java.util.ArrayList;
import java.util.List;

public class Permissions {
    private static final int LEVEL_BOT_ADMIN = 3;
    private static final int LEVEL_MEMBER = 0;
    private static final int LEVEL_ADMINISTRATOR = 1;
    private static final int LEVEL_OWNER = 2;

    private static String userId;
    private static List<String> qqGroups = new ArrayList<>();
    private final List<Integer> allowLevel = new ArrayList<>();
    public Permissions(){}
    public static void initPermissions(String userId,List<String> qqGroups){
        Permissions.userId = userId;
        Permissions.qqGroups = qqGroups;
    }
    /**
     *
     * @param qqId 发送者的QQ号
     * @return 监测是否是机器人管理员
     */
    public boolean isAdmin(long qqId){
        return String.valueOf(qqId).equals(userId);
    }
    /**
     *
     * @param groupID 消息来源的群聊
     * @return 如果是允许的群聊则返回true
     */
    public static boolean allowGroup(long groupID){
        return qqGroups.contains(String.valueOf(groupID));
    }
    public String getUserId(){
        return userId;
    }
    public List<String> getQqGroups(){
         return qqGroups;
    }
    private void addAllowLevel(int level){
        allowLevel.add(level);
    }
    public boolean isAllow(Member member){
        if (!allowGroup(member.getGroup().getId())) return false;
        return isAdmin(member.getId())?allowLevel.contains(LEVEL_BOT_ADMIN):allowLevel.contains(member.getPermission().getLevel());
    }
    public boolean notAllow(Member member){
        if (!allowGroup(member.getGroup().getId())) return false;
        return !(isAdmin(member.getId())?allowLevel.contains(LEVEL_BOT_ADMIN):allowLevel.contains(member.getPermission().getLevel()));
    }
    private void init(){}
    public static class Builder{
        private Permissions permissions = new Permissions();
        public Builder allowBotAdmin(){
            permissions.addAllowLevel(LEVEL_BOT_ADMIN);
            return this;
        }
        public Builder allowMember(){
            permissions.addAllowLevel(LEVEL_MEMBER);
            return this;
        }
        public Builder allowAdministrator(){
            permissions.addAllowLevel(LEVEL_ADMINISTRATOR);
            return this;
        }
        public Builder allowOwner(){
            permissions.addAllowLevel(LEVEL_OWNER);
            return this;
        }
        public Builder allowAll(){
            permissions.addAllowLevel(LEVEL_BOT_ADMIN);
            permissions.addAllowLevel(LEVEL_ADMINISTRATOR);
            permissions.addAllowLevel(LEVEL_MEMBER);
            permissions.addAllowLevel(LEVEL_OWNER);
            return this;
        }

        /**
         * 排除普通群员
         * @return
         */
        public Builder ExcludeMember(){
            permissions.addAllowLevel(LEVEL_BOT_ADMIN);
            permissions.addAllowLevel(LEVEL_ADMINISTRATOR);
            permissions.addAllowLevel(LEVEL_OWNER);
            return this;
        }
        public Permissions build(){
             return permissions;
        }
    }
}
