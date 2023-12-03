package com.BF2042Stats.data;

import net.mamoe.mirai.contact.Group;

public class CompetencePlayer {
    public static final int  ALL_USER = 0;
    public static final int GROUP_OWNER = 2;
    public static final int GROUP_MANAGER = 1;
    private final int per;
    private long user;
    private Group group;
    private CompetencePlayer(int per){
        this.per = per;
    }
    public static CompetencePlayer create(int permissions){
        return new CompetencePlayer(permissions);
    }
    public CompetencePlayer setUser(long user){
        this.user = user;
        return this;
    }
    public CompetencePlayer setGroup(Group group){
        this.group = group;
        return this;
    }
    public boolean isAllow(){
        switch (per){
            case 0:
                return true;
        }
        return false;
    }
}
