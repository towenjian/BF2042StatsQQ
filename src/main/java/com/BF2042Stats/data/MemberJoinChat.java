package com.BF2042Stats.data;

import net.mamoe.mirai.event.events.MemberJoinRequestEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class MemberJoinChat {
    private static final int MAX_SIZE = 20;
    private static final Map<String, MemberJoinRequestEvent> JOIN_CHAT_REQUESTS = new LinkedHashMap<String,MemberJoinRequestEvent>(){
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, MemberJoinRequestEvent> eldest) {
            return size() > MAX_SIZE;
        }
    };
    public static void addRequest(String key, MemberJoinRequestEvent event) {
        JOIN_CHAT_REQUESTS.put(key, event);
    }
    public static boolean hasRequest(String key){
        return JOIN_CHAT_REQUESTS.containsKey(key);
    }
    public static MemberJoinRequestEvent getRequest(String key){
        return  JOIN_CHAT_REQUESTS.get(key);
    }
    public static void removeRequest(String key){
         JOIN_CHAT_REQUESTS.remove(key);
    }
    public static void removeRequest_id(long id){
        JOIN_CHAT_REQUESTS.values().removeIf(event -> event.getFromId() == id);
    }
}
