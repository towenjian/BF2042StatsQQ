package com.BF2042Stats.gui;

import com.BF2042Stats.BF2042StatsV1;
import com.BF2042Stats.gui.activity.ConfigActivity;
import net.coobird.thumbnailator.Thumbnails;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

import javax.swing.*;
import java.io.IOException;

public class ActivityMain{
    private static final JavaPlugin javaPlugin = BF2042StatsV1.getJP();
    private static final ActivityMain activityMain = new ActivityMain();
    private final ConfigActivity configActivity = new ConfigActivity();
    private ActivityMain() {
    }
    public static ActivityMain getInstance(){
        return activityMain;
    }
    public void show() throws IOException {
        JFrame jFrame = new JFrame("配置文件");
        jFrame.setSize(1600,1000);
        jFrame.setContentPane(configActivity.getRootPanel());
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.setVisible(true);
    }
    public void setFun(String c,boolean b){
        configActivity.setUse(c,b);
    }

}
