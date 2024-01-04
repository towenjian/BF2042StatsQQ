package com.BF2042Stats;

import com.BF2042Stats.data.ConfigData;
import com.BF2042Stats.gui.ActivityMain;
import com.BF2042Stats.listen.Command;
import com.BF2042Stats.listen.ListenQqBot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;

public final class BF2042StatsV1 extends JavaPlugin {
    public static final BF2042StatsV1 INSTANCE = new BF2042StatsV1();
    private static JavaPlugin javaPlugin;

    private BF2042StatsV1() {
        super(new JvmPluginDescriptionBuilder("com.BF2042Stats.v1", "0.1.0")
                .name("BF2042StatsV1")
                .author("hhhh6448")
                .build());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        super.onLoad($this$onLoad);
        javaPlugin = this;
        ConfigData.getInstance().sout();
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
        ListenQqBot listenQqBot = new ListenQqBot(this);
        listenQqBot.BotOnline();
        Command command = new Command();
        command.GroupMessage();
        command.MemberJoin();
        command.joinGroupMessage();
        command.PrivateChat();
        command.exitGroup();
        // TODO: 2023/12/9 图形化界面
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
//        ActivityMain activityMain = ActivityMain.getInstance();
//        try {
//            activityMain.show();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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
        return javaPlugin;
    }
}