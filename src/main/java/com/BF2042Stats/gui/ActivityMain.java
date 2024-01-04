package com.BF2042Stats.gui;

import com.BF2042Stats.BF2042StatsV1;
import com.BF2042Stats.gui.activity.ConfigActivity;
import net.coobird.thumbnailator.Thumbnails;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ActivityMain{
    private static final JavaPlugin javaPlugin = BF2042StatsV1.getJP();
    private static final ActivityMain activityMain = new ActivityMain();
    private Font font;
    private ConfigActivity configActivity;
    private ActivityMain() {
        InputStream fontFile = getClass().getClassLoader().getResourceAsStream("AL.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(40f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ActivityMain getInstance(){
        return activityMain;
    }
    public void show() throws IOException {
        JFrame jFrame = new JFrame("配置文件");
        jFrame.setSize(1600,1000);
        configActivity = new ConfigActivity(jFrame);
        jFrame.setContentPane(configActivity.getRootPanel());
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.setVisible(true);
    }
    public void setFun(String c,boolean b){
        configActivity.setUse(c,b);
    }
    public Font getFont(){
        return font;
    }
}
