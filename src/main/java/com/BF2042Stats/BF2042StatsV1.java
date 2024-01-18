package com.BF2042Stats;

import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.data.ImgData;
import com.BF2042Stats.gui.ActivityMain;
import com.BF2042Stats.listen.Command;
import com.BF2042Stats.listen.ListenQqBot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;

public final class BF2042StatsV1 extends JavaPlugin {
    public static final BF2042StatsV1 INSTANCE = new BF2042StatsV1();

    private BF2042StatsV1() {
        super(new JvmPluginDescriptionBuilder("com.BF2042Stats.v1", "0.1.0")
                .name("BF2042StatsV1")
                .author("hhhh6448")
                .build());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        super.onLoad($this$onLoad);
        ConfigData.getInstance().sout();
        ImgData.getInstance().start();
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
        ListenQqBot.getInstance().BotOnline();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ActivityMain activityMain = ActivityMain.getInstance();
                try {
                    activityMain.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
        try {
            ConfigData.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JavaPlugin getJP(){
        return INSTANCE;
    }
    public static MiraiLogger getLoggerS(){
        return INSTANCE.getLogger();
    }
}