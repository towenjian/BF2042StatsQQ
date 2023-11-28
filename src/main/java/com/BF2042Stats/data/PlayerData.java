package com.BF2042Stats.data;

import com.BF2042Stats.data.data_enum.CLType;
import com.BF2042Stats.data.data_enum.Classes;
import com.BF2042Stats.data.data_enum.ClassesType;
import com.BF2042Stats.data.data_enum.StateCenter;
import com.BF2042Stats.data.data_interface.GetType;
import com.BF2042Stats.listen.Command;
import com.BF2042Stats.textenum.PlayerDataText;
import com.BF2042Stats.textenum.PostionEnum;
import com.BF2042Stats.textenum.TextData;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class PlayerData {
    private final String name,platform;
    private JSONObject jsonObject;
    private JSONArray classes,weapons,veh,kd_array,kpm_array,kd_in_array,headshots_array,wp_group_array,veh_group_array;
    private final JavaPlugin javaPlugin;
    private final Font mc_font;
    private final Command command;
    private final long userID;
    private final Timer timer = new Timer();
    private boolean isTime = true;
    private boolean isOK_Graphs=false;
    private boolean isUpData = false;
    private Thread veh_T,wp_T,thread,imgTread,thread_Graphs,kd_T,kpm_T,st_T,cl_T;
    private GroupMessage groupMessage;
    private final Bot bot;
    private int time=2;
    private int TimeMember=0;
    private final int type;
    private int getThread_frequency=0;
    private int getThread_Graphs=0;
    private final Map<Integer,String> Mapping_table = new LinkedHashMap<>();
    private final List<Integer> list_origin = new ArrayList<>();

    public PlayerData(GroupMessage groupMessage,String name,String platform,int type) {
        this.type = type;
        this.bot = groupMessage.getBot();
        this.groupMessage = groupMessage;
        this.name = name;
        this.platform = platform;
        this.javaPlugin = groupMessage.getJavaPlugin();
        this.command = groupMessage.getCommand();
        this.userID = groupMessage.getUser();
        Get();
        InputStream fontFile = getClass().getClassLoader().getResourceAsStream("AL.ttf");
        try {
            mc_font = Font.createFont(Font.TRUETYPE_FONT,fontFile).deriveFont(40f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void Get(){
        thread = new Thread(this::Get_origin);
        thread.start();
        if (!isOK_Graphs) {
            thread_Graphs = new Thread(this::Get_Graphs);
            thread_Graphs.start();
        }
    }
    private void TextMessage(){
        String replyMessage = "游戏ID:"+jsonObject.getString("userName")+"\n" +
                "游戏时长:" +jsonObject.getString("time")+"h"+"\n"+
                "MVP数:"+jsonObject.getString("mvp")+"\n" +
                "鸡杀数:"+jsonObject.getString("kills")+"\n" +
                "真实击杀数:" +jsonObject.getString("human")+"\n" +
                "近战击杀:"+jsonObject.getString("melee")+"\n"+
                "死亡数:"+jsonObject.getString("deaths")+"\n" +
                "胜利场数:"+jsonObject.getString("wins")+"\n" +
                "失败场数:"+jsonObject.getString("loses")+"\n" +
                "KD:"+jsonObject.getString("killDeath")+"\n" +
                "真实KD:"+jsonObject.getString("infantryKillDeath")+"\n" +
                "KPM:"+jsonObject.getString("killsPerMinute")+"\n" +
                "真实KPM:" +jsonObject.getString("inkpm")+"\n"+
                "分钟伤害:"+jsonObject.getString("damagePerMinute")+"\n" +
                "场均击杀:"+jsonObject.getString("killsPerMatch")+"\n" +
                "场均伤害:"+jsonObject.getString("damagePerMatch")+"\n" +
                "爆头数:"+jsonObject.getString("headShots")+"\n" +
                "胜率:"+jsonObject.getString("winPercent")+"\n" +
                "爆头率:"+jsonObject.getString("headshots")+"\n" +
                "更多数据请前往软件观看...";
        groupMessage.sendGroupMessage(replyMessage);
    }
    public void CX(GroupMessage groupMessage){
        if (isUpData){
            groupMessage.sendGroupMessage("当前正在执行拼接操作，请等待拼接完成");
            return;
        }
        File file = new File(javaPlugin.getDataFolder(), name+".png");
        if (file.exists()){
            if (file.delete()) {
                System.out.println(name+"的cx图片被删除");
            }
        }
        imgTread = new Thread(() -> {
            try {
                ImgMessage(groupMessage);
            } catch (IOException e) {
                try {
                    ImgMessage(groupMessage);
                } catch (IOException ex) {
                    groupMessage.sendGroupMessage("生成失败");
                    System.out.println(ex.getCause().toString());
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        });
        imgTread.start();
    }
    private void ImgMessage(GroupMessage groupMessage) throws IOException {
        this.groupMessage = groupMessage;
        System.out.println("IMG");
        TextMessage();
        groupMessage.sendGroupMessage("正在生成你的基本数据图，等待中。。。");
        boolean is_guer = false,isPro = false;
        if (Double.valueOf(jsonObject.getString("headshots").replace("%", ""))>50) is_guer = true;
        if (jsonObject.getDouble("killDeath")>2&&jsonObject.getDouble("killsPerMinute")>1) isPro=true;
        //test
        //end
        //wp
        //
        Thumbnails.of(new BufferedImage(1920, 3240, BufferedImage.TYPE_INT_ARGB))
                .size(1920,3240)
                .watermark(PostionEnum.Base.getPosition(), Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=mp&size=mw1920")).width(1920).asBufferedImage(),1f)
                .watermark((enclosingWidth, enclosingHeight, width, height, insetLeft, insetRight, insetTop, insetBottom) -> new Point(0,0), Thumbnails.of(getClass().getClassLoader().getResourceAsStream("base2.png")).size(1920, 3240).asBufferedImage(), 1f)
                .watermark(PostionEnum.TX.getPosition(), Thumbnails.of(new URL(isNull(jsonObject.getString("avatar")))).size(165,165).asBufferedImage(),1f)
                .watermark(PostionEnum.CL.getPosition(), Thumbnails.of(new URL(isNull(classes.getJSONObject(0).getJSONObject("avatarImages").getString("us")))).height(165).asBufferedImage(),1f)
                .watermark(PostionEnum.P1.getPosition(), Thumbnails.of(getClass().getClassLoader().getResourceAsStream(is_guer?"h.png":(isPro?"pro.png":"ss.png"))).size(165,165).asBufferedImage(), 1f)
                .watermark(PostionEnum.P2.getPosition(), getWpDataPie(800,800), 1f)
                .watermark(PostionEnum.P3.getPosition(), getVehDataPie(800,800),1f)
                .toFile(new File(javaPlugin.getDataFolder(), name+".png"));
        File img_file = new File(javaPlugin.getDataFolder(), name+".png");
        BufferedImage image = ImageIO.read(img_file);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.black);
        g2d.setFont(mc_font);
        //头像那一类
        g2d.drawString("\u73a9\u5bb6:"+name, 190, 65);
        g2d.drawString("\u65f6\u957f:"+jsonObject.getString("time")+"h",190,120);
        g2d.drawString("mvps:"+jsonObject.getString("mvp"),190,175);
        //end
        //鉴定
        g2d.drawString("\u4e00\u773c\u9876\u771f\uff0c\u9274\u5b9a\u4e3a:", is_guer?1305:1365, 85);
        g2d.setColor(Color.RED);
        g2d.drawString(is_guer?"\u6211\u4e0d\u5230\u554a\uff1fcpu\u70e7\u4e86":(isPro?"\u5367\u69fd\uff0cpro\u54e5":"\u6211\u662f\u85af\u85af\uff0c\u522b\u635e\u4e86\uff01"), 1565, 125);
        g2d.setColor(Color.BLACK);
        //end
        //专家
        g2d.drawString("KD:"+classes.getJSONObject(0).getString("killDeath"),810,60);
        g2d.drawString("KPM:"+classes.getJSONObject(0).getString("kpm"),810,100);
        g2d.drawString("\u51fb\u6740\u6570:"+classes.getJSONObject(0).getString("kills"),810,140);
        g2d.drawString("\u6b7b\u4ea1\u6570:"+classes.getJSONObject(0).getString("deaths"),810,180);
        //end
        //中间状态栏
        g2d.setFont(mc_font.deriveFont(30f));
        StateCenter[] temp_st = StateCenter.values();
        int temp =0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                g2d.drawString(temp_st[temp].getS1()+":"+jsonObject.getString(temp_st[temp].getS2()),100+i*360,240+j*40);
                temp++;
            }
        }
        //end
        //武器和载具图
        g2d.setColor(Color.WHITE);
        g2d.setFont(mc_font.deriveFont(20f));
        for (int i = 0; i < 18; i++) {
            writeVhImg(g2d, 950, 1200+i*110, veh.getJSONObject(i));
            writeWpImg(g2d, 35, 1200+i*110, weapons.getJSONObject(i));
        }
        g2d.dispose();
        ImageIO.write(image, "png", img_file);
        System.out.println("over");
        ExternalResource resource = ExternalResource.create(img_file);
        Image image1 = groupMessage.getGroup().uploadImage(resource);
        resource.close();
        groupMessage.getGroup().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(groupMessage.getMessages()))
                .append(image1)
                .build());
        img_file.delete();
        File file1 = new File(javaPlugin.getDataFolder(), name+".png");
        file1.delete();
        img_file.delete();
    }
    private JSONArray JsonSort(JSONArray jsonArray){
        int n = jsonArray.size();
        for (int i = 1; i < n; i++) {
            JSONObject key = jsonArray.getJSONObject(i);
            int j = i - 1;
            while (j >= 0 && key.getInteger("kills") > jsonArray.getJSONObject(j).getInteger("kills")) {
                jsonArray.set(j + 1, jsonArray.getJSONObject(j));
                j--;
            }
            jsonArray.set(j + 1, key);
        }
        return jsonArray;
    }
    private String isNull(String s){
        if (s.isEmpty()){
            return "https://moe.jitsu.top/img/?sort=1080p&size=sq256";
        }else {
            return s;
        }
    }//处理图像链接为空的问题
    public void WPImg(GroupMessage groupMessage){
        if (isUpData){
            groupMessage.sendGroupMessage("当前正在执行拼接操作，请等待拼接完成");
            return;
        }
        File file = new File(javaPlugin.getDataFolder(), name+"-wp.png");
        if (file.exists()){
            if (file.delete()) {
                System.out.println(name+"的wp图片被删除");
            }
        }
        wp_T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WP(groupMessage);
                } catch (IOException e) {
                    try {
                        WP(groupMessage);
                    } catch (IOException ex) {
                        groupMessage.sendGroupMessage("生成失败,请联系管理员查找错误");
                        System.out.println(ex.getCause().toString());
                        throw new RuntimeException(ex);
                    }
                    throw new RuntimeException(e);
                }
            }
        });
        wp_T.start();
    }
    private void WP(GroupMessage groupMessage) throws IOException {
        System.out.println("WP");
        this.groupMessage = groupMessage;
        TextMessage();
        groupMessage.sendGroupMessage("正在生成你的武器数据图，等待中");
        if (jsonObject.isEmpty()) {
            groupMessage.sendGroupMessage("数据暂未被拉取成功，请等待cx程序输出文字数据后在使用此命令");
            return;
        }
        Map<String,Double> map = new HashMap<>();
        for (int i = 0; i < wp_group_array.size(); i++) {
            JSONObject jsonObject1 = wp_group_array.getJSONObject(i);
            if (jsonObject1.getDouble("kills")!=0) {
                map.put(CLType.valueOf(jsonObject1.getString("groupName").replace(" ", "").replace("-", "")).getS(), jsonObject1.getDouble("kills"));
            }
        }
        //test
        DefaultPieDataset dataset = new DefaultPieDataset<>();
        for(String s : map.keySet()){
            if (s.equals("Primary")) continue;
            dataset.setValue(s,map.get(s)/map.get("Primary"));
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "\u51fb\u6740\u5360\u6bd4",
                dataset);
        TextTitle title = chart.getTitle();
        title.setFont(mc_font);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(mc_font);//标签
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}\u5360\u6bd4{2}"));
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(mc_font);
        BufferedImage pie_img = Thumbnails.of(chart.createBufferedImage(1200,1200)).size(500,500).asBufferedImage();
        //end
        //鉴定
        boolean is_guer = false,isPro = false;
        if (Double.valueOf(jsonObject.getString("headshots").replace("%", ""))>50) is_guer = true;
        if (jsonObject.getDouble("killDeath")>2&&jsonObject.getDouble("killsPerMinute")>1) isPro=true;
        //
        Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920"))
                .size(1920,1080)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(0,0);
                    }
                }, Thumbnails.of(getClass().getClassLoader().getResourceAsStream("WP.png")).size(1920, 1080).asBufferedImage(), 1f)
                .watermark(PostionEnum.WP2.getPosition(), Thumbnails.of(new URL(isNull(jsonObject.getString("avatar")))).size(200,200).asBufferedImage(),1f)
                .watermark(PostionEnum.WP3.getPosition(), Thumbnails.of(getClass().getClassLoader().getResourceAsStream(is_guer?"h.png":(isPro?"pro.png":"ss.png"))).size(200,200).asBufferedImage(),1f)
                .watermark(PostionEnum.WP1.getPosition(), Thumbnails.of(pie_img).size(400, 400).asBufferedImage(), 1f)
                .toFile(new File(javaPlugin.getDataFolder(), name+"-wp.png"));
        File img_file = new File(javaPlugin.getDataFolder(), name+"-wp.png");
        BufferedImage image = ImageIO.read(img_file);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.black);
        g2d.setFont(mc_font);
        //头像那一类
        double temp = (map.get("Primary")/ jsonObject.getDouble("kills"))*100;
        BigDecimal b = new BigDecimal(temp);
        temp = b.setScale(2,RoundingMode.HALF_UP).doubleValue();
        g2d.setFont(mc_font.deriveFont(40f));
        g2d.drawString("\u73a9\u5bb6:"+name, 1488, 49);
        g2d.drawString("\u65f6\u957f:"+jsonObject.getString("time")+"h",1488,99);
        g2d.drawString("mvps:"+jsonObject.getString("mvp"),1488,149);
        g2d.drawString("\u67aa\u68b0\u51fb\u6740\u5360\u6bd4:"+temp+"%",1488,199);
        //end
        //鉴定
        g2d.drawString("\u4e00\u773c\u9876\u771f\uff0c\u9274\u5b9a\u4e3a:", is_guer?1409:1489, 299);
        g2d.setColor(Color.RED);
        g2d.drawString(is_guer?"\u6211\u4e0d\u5230\u554a\uff1fcpu\u70e7\u4e86":(isPro?"\u5367\u69fd\uff0cpro\u54e5":"\u6211\u662f\u85af\u85af\uff0c\u522b\u635e\u4e86\uff01"), 1559, 339);
        g2d.setColor(Color.WHITE);
        g2d.setFont(mc_font.deriveFont(20f));
        int t = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (t>9&&t<14) {
                    t++;
                    continue;
                }
                writeWpImg(g2d, 15+i*1000, 15+j*100, weapons.getJSONObject(t>13?t-4:t));
                t++;
            }
        }g2d.dispose();
        ImageIO.write(image, "png", img_file);
        System.out.println("over");
        ExternalResource resource = ExternalResource.create(img_file);
        Image image_temp = groupMessage.getGroup().uploadImage(resource);
        resource.close();
        groupMessage.getGroup().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(groupMessage.getMessages()))
                .append(image_temp)
                .build());
        img_file.delete();
        File file2 = new File(javaPlugin.getDataFolder(), name+"-wp.png");
        file2.delete();
    }
    public void VEHImg(GroupMessage groupMessage){
        if (isUpData){
            groupMessage.sendGroupMessage("当前正在执行拼接操作，请等待拼接完成");
            return;
        }
        File file = new File(javaPlugin.getDataFolder(), name+"-veh.png");
        if (file.exists()){
            if (file.delete()) {
                System.out.println(name+"的veh图片被删除");
            }
        }
        veh_T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    VEH(groupMessage);
                } catch (IOException e) {
                    try {
                        VEH(groupMessage);
                    } catch (IOException ex) {
                        groupMessage.sendGroupMessage("生成失败,请联系管理员查找错误");
                        System.out.println(ex.getCause().toString());
                        throw new RuntimeException(ex);
                    }
                    throw new RuntimeException(e);
                }
            }
        });
        veh_T.start();
    }
    private void VEH(GroupMessage groupMessage) throws IOException {
        System.out.println("VEH");
        this.groupMessage = groupMessage;
        TextMessage();
        groupMessage.sendGroupMessage("正在生成你的载具数据图，等待中");
        if (jsonObject.isEmpty()) {
            groupMessage.sendGroupMessage("数据暂未被拉取成功，请等待cx程序输出文字数据后在使用此命令");
            return;
        }
        Map<String,Double> map = new HashMap<>();
        double allKills = 0;
        for (int i = 0; i < veh_group_array.size(); i++) {
            JSONObject jsonObject1 = veh_group_array.getJSONObject(i);
            if (jsonObject1.getString("groupName").equals("Air")||jsonObject1.getString("groupName").equals("Sur")) continue;
            if (jsonObject1.getDouble("kills")!=0) {
                map.put(CLType.valueOf(jsonObject1.getString("groupName").replace("-", "")).getS(), jsonObject1.getDouble("kills"));
                allKills += jsonObject1.getDouble("kills");
            }
        }
        //test
        DefaultPieDataset dataset = new DefaultPieDataset<>();
        for(String s : map.keySet()){
            dataset.setValue(s,map.get(s)/allKills);
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "\u51fb\u6740\u5360\u6bd4",
                dataset);
        TextTitle title = chart.getTitle();
        title.setFont(mc_font);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(mc_font);//标签
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}\u5360\u6bd4{2}"));
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(mc_font);
        BufferedImage pie_img = Thumbnails.of(chart.createBufferedImage(1200,1200)).size(500,500).asBufferedImage();
        //end
        // time
        String str = jsonObject.getString("timePlayed");
        String time1 = "null";
        if (str.indexOf("days")!=-1){
            int D = Integer.parseInt(str.substring(0,str.indexOf(" days,")));
            int H = Integer.parseInt(str.substring(str.indexOf(", ")+2,str.indexOf(":")));
            String m = str.substring(str.indexOf(":")+1);
            int M = Integer.parseInt(m.substring(0,m.indexOf(":")));
            time1=String.valueOf(M>=30?D*24+H+1:D*24+H);
        } else if (str.indexOf("day" )!=-1) {
            int D = Integer.parseInt(str.substring(0,str.indexOf(" day,")));
            int H = Integer.parseInt(str.substring(str.indexOf(", ")+2,str.indexOf(":")));
            String m = str.substring(str.indexOf(":")+1);
            int M = Integer.parseInt(m.substring(0,m.indexOf(":")));
            time1=String.valueOf(M>=30?D*24+H+1:D*24+H);
        } else {
            int H =Integer.parseInt(str.substring(0,str.indexOf(":")));
            String m = str.substring(str.indexOf(":")+1);
            int M = Integer.parseInt(m.substring(0,m.indexOf(":")));
            time1=String.valueOf(M>=30?H+1:H);
        }
        //end
        //鉴定
        boolean is_guer = false,isPro = false;
        if (Double.valueOf(jsonObject.getString("headshots").replace("%", ""))>50) is_guer = true;
        if (jsonObject.getDouble("killDeath")>2&&jsonObject.getDouble("killsPerMinute")>1) isPro=true;
        //
        Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920"))
                .size(1920,1080)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(0,0);
                    }
                }, Thumbnails.of(getClass().getClassLoader().getResourceAsStream("vh.png")).size(1920, 1080).asBufferedImage(), 1f)
                .watermark(PostionEnum.VH2.getPosition(), Thumbnails.of(new URL(isNull(jsonObject.getString("avatar")))).size(200,200).asBufferedImage(),1f)
                .watermark(PostionEnum.VH3.getPosition(), Thumbnails.of(getClass().getClassLoader().getResourceAsStream(is_guer?"h.png":(isPro?"pro.png":"ss.png"))).size(200,200).asBufferedImage(),1f)
                .watermark(PostionEnum.VH1.getPosition(), Thumbnails.of(pie_img).size(400, 400).asBufferedImage(), 1f)
                .toFile(new File(javaPlugin.getDataFolder(), name+"-vh.png"));
        File img_file = new File(javaPlugin.getDataFolder(), name+"-vh.png");
        BufferedImage image = ImageIO.read(img_file);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.black);
        g2d.setFont(mc_font);
        //头像那一类
        double temp = (allKills/ jsonObject.getDouble("kills"))*100;
        BigDecimal b = new BigDecimal(temp);
        temp = b.setScale(2,RoundingMode.HALF_UP).doubleValue();
        g2d.setFont(mc_font.deriveFont(40f));
        g2d.drawString("\u73a9\u5bb6:"+name, 1565, 49);
        g2d.drawString("\u65f6\u957f:"+time1+"h",1565,99);
        g2d.drawString("mvps:"+jsonObject.getString("mvp"),1565,149);
        g2d.drawString("\u8f7d\u5177\u51fb\u6740\u6bd4:"+temp+"%",1565,199);
        //end
        //鉴定
        g2d.drawString("\u4e00\u773c\u9876\u771f\uff0c\u9274\u5b9a\u4e3a:", is_guer?1499:1565, 299);
        g2d.setColor(Color.RED);
        g2d.drawString(is_guer?"\u6211\u4e0d\u5230\u554a\uff1fcpu\u70e7\u4e86":(isPro?"\u5367\u69fd\uff0cpro\u54e5":"\u6211\u662f\u85af\u85af\uff0c\u522b\u635e\u4e86\uff01"), 1569, 349);
        g2d.setColor(Color.WHITE);
        g2d.setFont(mc_font.deriveFont(20f));
        int t = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (t>9&&t<14) {
                    t++;
                    continue;
                }
                writeVhImg(g2d, 10+i*1000, 15+j*100, veh.getJSONObject(t>13?t-4:t));
                t++;
            }
        }g2d.dispose();
        ImageIO.write(image, "png", img_file);
        System.out.println("over");
        ExternalResource resource = ExternalResource.create(img_file);
        Image image_temp = groupMessage.getGroup().uploadImage(resource);
        resource.close();
        groupMessage.getGroup().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(groupMessage.getMessages()))
                .append(image_temp)
                .build());
        img_file.delete();
        File file2 = new File(javaPlugin.getDataFolder(), name+"-vh.png");
        file2.delete();
    }
    public void CLImg(GroupMessage groupMessage){
        File file = new File(javaPlugin.getDataFolder(),name+"-cl.png");
        if (file.exists()) file.delete();
        cl_T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CL(groupMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cl_T.start();
    }
    private void CL(GroupMessage groupMessage) throws IOException {
        System.out.println("cl");
        this.groupMessage = groupMessage;
        TextMessage();
        groupMessage.sendGroupMessage("正在生成你的专家数据图，等待中");
        if (jsonObject.isEmpty()) {
            groupMessage.sendGroupMessage("数据暂未被拉取成功，请等待cx程序输出文字数据后在使用此命令");
            return;
        }
        boolean is_guer = false,isPro = false;
        if (Double.valueOf(jsonObject.getString("headshots").replace("%", ""))>50) is_guer = true;
        if (jsonObject.getDouble("killDeath")>2&&jsonObject.getDouble("killsPerMinute")>1) isPro=true;
        Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920"))
                .size(1920,1080)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(0,0);
                    }
                }, Thumbnails.of(getClass().getClassLoader().getResourceAsStream("base.png")).size(1920, 1080).asBufferedImage(), 1f)
                .watermark(PostionEnum.TX.getPosition(), Thumbnails.of(new URL(isNull(jsonObject.getString("avatar")))).size(165,165).asBufferedImage(),1f)
                .watermark(PostionEnum.CL.getPosition(), Thumbnails.of(new URL(isNull(classes.getJSONObject(0).getJSONObject("avatarImages").getString("us")))).height(165).asBufferedImage(),1f)
                .watermark(PostionEnum.P1.getPosition(), Thumbnails.of(getClass().getClassLoader().getResourceAsStream(is_guer?"h.png":(isPro?"pro.png":"ss.png"))).size(165,165).asBufferedImage(), 1f)
                .toFile(new File(javaPlugin.getDataFolder(),name+"-cl.png"));
        File file = new File(javaPlugin.getDataFolder(),name+"-cl.png");
        BufferedImage image = ImageIO.read(file);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.black);
        g2d.setFont(mc_font);
        //头像那一类
        g2d.drawString("\u73a9\u5bb6:"+name, 190, 65);
        g2d.drawString("\u65f6\u957f:"+jsonObject.getString("time")+"h",190,120);
        g2d.drawString("mvps:"+jsonObject.getString("mvp"),190,175);
        //end
        //鉴定
        g2d.drawString("\u4e00\u773c\u9876\u771f\uff0c\u9274\u5b9a\u4e3a:", is_guer?1305:1365, 85);
        g2d.setColor(Color.RED);
        g2d.drawString(is_guer?"\u6211\u4e0d\u5230\u554a\uff1fcpu\u70e7\u4e86":(isPro?"\u5367\u69fd\uff0cpro\u54e5":"\u6211\u662f\u85af\u85af\uff0c\u522b\u635e\u4e86\uff01"), 1565, 125);
        g2d.setColor(Color.BLACK);
        //end
        //专家
        g2d.drawString("KD:"+classes.getJSONObject(0).getString("killDeath"),810,60);
        g2d.drawString("KPM:"+classes.getJSONObject(0).getString("kpm"),810,100);
        g2d.drawString("\u51fb\u6740\u6570:"+classes.getJSONObject(0).getString("kills"),810,140);
        g2d.drawString("\u6b7b\u4ea1\u6570:"+classes.getJSONObject(0).getString("deaths"),810,180);
        //end
        //中间状态栏
        g2d.setFont(mc_font.deriveFont(30f));
        StateCenter[] temp_st = StateCenter.values();
        int temp =0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                g2d.drawString(temp_st[temp].getS1()+":"+jsonObject.getString(temp_st[temp].getS2()),100+i*360,240+j*40);
                temp++;
            }
        }
        //classes
        g2d.setColor(Color.WHITE);
        g2d.setFont(mc_font.deriveFont(20f));
        int t = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                writeClImg(g2d, 30+i*640, 400+j*160, classes.getJSONObject(t));
                t++;
            }
        }
        g2d.dispose();
        ImageIO.write(image,"png",file);
        ExternalResource resource = ExternalResource.create(file);
        Image image1 = groupMessage.getGroup().uploadImage(resource);
        resource.close();
        groupMessage.getGroup().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(groupMessage.getMessages()))
                .append(image1)
                .build());
        file.delete();
    }
    public void KDImg(GroupMessage groupMessage){
        if (isUpData){
            groupMessage.sendGroupMessage("当前正在执行拼接操作，请等待拼接完成");
            return;
        }
        File file = new File(javaPlugin.getDataFolder(), name+"-kd.png");
        if (file.exists()){
            if (file.delete()) {
                System.out.println(name+"的kd图片被删除");
            }
        }
        kd_T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    KD(groupMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        kd_T.start();
    }
    private void KD(GroupMessage groupMessage) throws IOException {
        this.groupMessage = groupMessage;
        groupMessage.sendGroupMessage("正在生成你的KD状态图，等待中");
        BufferedImage bufferedImage1 = Generate_Graph(kd_array, TextData.KD),bufferedImage2 = Generate_Graph(kd_in_array,TextData.InfantryKD);
        Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920"))
                .size(1920,1080)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(1720,223);
                    }
                }, Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=sq256")).size(175,175).asBufferedImage(),1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(0,0);
                    }
                }, Thumbnails.of(getClass().getClassLoader().getResourceAsStream("WP.png")).size(1920, 1080).asBufferedImage(), 1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(30,140);
                    }
                }, bufferedImage1, 1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(30,540);
                    }
                }, bufferedImage2,1f)
                .toFile(new File(javaPlugin.getDataFolder(), name+"-kd.png"));
        File file = new File(javaPlugin.getDataFolder(), name+"-kd.png");
        BufferedImage image1 = ImageIO.read(file);
        Graphics2D graphics2D = image1.createGraphics();
        graphics2D.setFont(mc_font.deriveFont(18f));
        graphics2D.drawString("ID: "+name, 1720, 440);
        int ran = new Random().nextInt(8)+1;
        TextPlane textPlane = new TextPlane(graphics2D, mc_font, PlayerDataText.valueOf("B"+ran).toString());
        textPlane.show(1790, 476);
        graphics2D.dispose();
        ImageIO.write(image1, "png", file);
        ExternalResource resource = ExternalResource.create(file);
        Image image = groupMessage.getGroup().uploadImage(resource);
        groupMessage.getGroup().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(groupMessage.getMessages()))
                .append(image)
                .build());
        file.delete();
        boolean de = file.delete();
        if (de) {
            System.out.println("已清除"+name+"的kd数据图");
        }
    }
    public void KPMImg(GroupMessage groupMessage){
        File file = new File(javaPlugin.getDataFolder(), name+"-kpm.png");
        if (file.exists()){
            if (file.delete()) {
                System.out.println(name+"的kpm图片被删除");
            }
        }
        kpm_T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    KPM(groupMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        kpm_T.start();
    }
    private void KPM(GroupMessage groupMessage) throws IOException {
        this.groupMessage = groupMessage;
        groupMessage.sendGroupMessage("正在生成你的KPM状态图，等待中");
        BufferedImage bufferedImage1 = Generate_Graph(kpm_array, TextData.KPM);
        Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920"))
                .size(1920,1080)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(1720,223);
                    }
                }, Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=sq256")).size(175,175).asBufferedImage(),1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(0,0);
                    }
                }, Thumbnails.of(getClass().getClassLoader().getResourceAsStream("WP.png")).size(1920, 1080).asBufferedImage(), 1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(30,340);
                    }
                }, bufferedImage1, 1f)
                .toFile(new File(javaPlugin.getDataFolder(), name+"-kpm.png"));
        File file = new File(javaPlugin.getDataFolder(), name+"-kpm.png");
        BufferedImage image1 = ImageIO.read(file);
        Graphics2D graphics2D = image1.createGraphics();
        graphics2D.setFont(mc_font.deriveFont(18f));
        graphics2D.drawString("ID: "+name, 1720, 440);
        int ran = new Random().nextInt(8)+1;
        TextPlane textPlane = new TextPlane(graphics2D, mc_font, PlayerDataText.valueOf("B"+ran).toString());
        textPlane.show(1790, 476);
        graphics2D.dispose();
        ImageIO.write(image1, "png", file);
        ExternalResource resource = ExternalResource.create(file);
        Image image = groupMessage.getGroup().uploadImage(resource);
        groupMessage.getGroup().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(groupMessage.getMessages()))
                .append(image)
                .build());
        boolean de = file.delete();
        if (de) {
            System.out.println("已清除"+name+"的kd数据图");
        }
    }
    public void STImg(GroupMessage groupMessage){
        File file = new File(javaPlugin.getDataFolder(), name+"-st.png");
        if (file.exists()){
            if (file.delete()) {
                System.out.println(name+"的st图片被删除");
            }
        }
        st_T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ST(groupMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        st_T.start();
    }
    /*
    * TODO
    *  把st的图片增加，加长图片高度，增加所有饼状图
    *
    * */
    private void ST(GroupMessage groupMessage) throws IOException {
        this.groupMessage = groupMessage;
        groupMessage.sendGroupMessage("正在汇总状态图，请等待");
        int height = 300;
        BufferedImage kd = Generate_Graph(kd_array, TextData.KD,height);
        BufferedImage kpm = Generate_Graph(kpm_array,TextData.KPM,height);
        BufferedImage head = Generate_Graph(headshots_array,TextData.HEADSHOTS,height);
        Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920"))
                .size(1920,1080)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(1720,223);
                    }
                }, Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=sq256")).size(175,175).asBufferedImage(),1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(0,0);
                    }
                }, Thumbnails.of(getClass().getClassLoader().getResourceAsStream("WP.png")).size(1920, 1080).asBufferedImage(), 1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(30,50);
                    }
                }, kd, 1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(30,400);
                    }
                }, head, 1f)
                .watermark(new Position() {
                    @Override
                    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                        return new Point(30,750);
                    }
                }, kpm, 1f)
                .toFile(new File(javaPlugin.getDataFolder(), name+"-st.png"));
        File file = new File(javaPlugin.getDataFolder(), name+"-st.png");
        BufferedImage image1 = ImageIO.read(file);
        Graphics2D graphics2D = image1.createGraphics();
        graphics2D.setFont(mc_font.deriveFont(18f));
        graphics2D.drawString("ID: "+name, 1720, 440);
        int ran = new Random().nextInt(8)+1;
        TextPlane textPlane = new TextPlane(graphics2D, mc_font, PlayerDataText.valueOf("B"+ran).toString());
        textPlane.show(1790, 476);
        graphics2D.dispose();
        ImageIO.write(image1, "png", file);
        ExternalResource resource = ExternalResource.create(file);
        Image image = groupMessage.getGroup().uploadImage(resource);
        groupMessage.getGroup().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(groupMessage.getMessages()))
                .append(image)
                .build());
        boolean de = file.delete();
        File del = new File( javaPlugin.getDataFolder(), name+"-st.png");
        if (de) {
            System.out.println("已清除"+name+"的st数据图");
        }
        if (del.delete()){
            System.out.println("已清除"+name+"的st数据图");
        }
    }
    public void RL(GroupMessage groupMessage){
        this.groupMessage = groupMessage;
        String reply = "玩家："+name+" 最近的一次活动时间为"+Mapping_table.get(Mapping_table.size()-1);
        groupMessage.sendGroupMessage(reply);
    }
    public void setTime(int time){
        this.time = time;
        TimeDelay();
    }
    private void TimeDelay(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                isTime=false;
                Get();
                System.out.println("用户："+userID+"的缓存数据已经被重新拉取，玩家ID为："+name);
                if (TimeMember>=time){
                    CapacityPool.removePlayerData(name);
                    System.out.println("用户："+userID+"的缓存数据已经被清除");
                }
                TimeMember++;
            }
        };
        int delay = 1000*60*60;
        timer.scheduleAtFixedRate(timerTask, delay,delay);
    }
    public void removeTimer(){
        if (thread!=null) thread.interrupt();
        if (veh_T!=null) veh_T.interrupt();
        if (wp_T!=null) wp_T.interrupt();
        if (imgTread!=null) imgTread.interrupt();
        if (kd_T!=null)kd_T.interrupt();
        if (kpm_T!=null)kpm_T.interrupt();
        if (st_T!=null) st_T.interrupt();
        ThreadInterrupt(cl_T);
        timer.cancel();
        System.out.println("该任务"+(timer.purge()==0?"已经被停止":"还在运行"));
    }
    private BufferedImage returnBufferedImage(String type,String s,int w,int h) throws IOException {
        if (s==null||s.isEmpty()) return Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920")).size(w,h).asBufferedImage();
        if (getClass().getClassLoader().getResourceAsStream(type+"/"+s.split("/")[s.split("/").length-1])==null)return Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920")).size(w,h).asBufferedImage();
        return Thumbnails.of(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(type + "/" + s.split("/")[s.split("/").length - 1]))).size(w,h).asBufferedImage();
    }
    private BufferedImage returnBufferedImage(String type,String s,int h) throws IOException {
        if (s==null||s.isEmpty()) return Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920")).height(h).asBufferedImage();
        File file = new File(javaPlugin.getDataFolder()+"/"+type,s.split("/")[s.split("/").length-1]);
        if (file.exists()) return Thumbnails.of(file).height(h).asBufferedImage();
        else Thumbnails.of(new URL(s)).height(100).toFile(new File(javaPlugin.getDataFolder()+"/"+type, s.split("/")[s.split("/").length-1]));
        return Thumbnails.of(new URL("https://moe.jitsu.top/img/?sort=1080p&size=mw1920")).height(h).asBufferedImage();
    }
    public String getName(){
        return name;
    }
    private void selectMessage(int i) throws IOException {
        switch (i){
            case GetType.CX:
                try {
                    ImgMessage(groupMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);//基本图
                }
                break;
            case GetType.WP:
                try {
                    WP(groupMessage);//武器图
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case GetType.VH:
                VEH(groupMessage);//载具图
                break;
            case GetType.KD:
                KD(groupMessage);//kd图
                break;
            case GetType.KPM:
                KPM(groupMessage);//kpm图
                break;
            case GetType.ST:
                ST(groupMessage);//所有集合图
                break;
            case GetType.RL:
                RL(groupMessage);//最近活动
                break;
            case GetType.CL:
                CL(groupMessage);
                break;
        }
    }
    private String ShortCharacters(String s){
        if(s==null) return "null";//解决空字符问题
        return s.length()<10?s:s.substring(0, 10)+"...";
    }
    private void Get_origin(){
        try {
            System.out.println("GET");
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
            String url ="https://api.gametools.network/bf2042/stats/?raw=false&format_values=true&name="+name+"&platform="+platform+"&skip_battlelog=false";
            Request request = new Request.Builder()
                    .url(url)
                    .get().build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    if (getThread_frequency<5) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                getThread_frequency++;
                                thread.interrupt();
                                thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Get_origin();
                                    }
                                });
                                thread.start();
                            }
                        });
                    }
                    if(getThread_frequency>=5) {
                        if (isTime) groupMessage.sendGroupMessage("链接超时,请检查ID与平台是否正确");
                        else System.out.println("链接超时");
                        getThread_frequency++;
                        CapacityPool.removePlayerData(name);
                    }
                    System.out.println("链接超时");
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    int code = response.code();
                    if (code!=200){
                        if (getThread_frequency<5) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    getThread_frequency++;
                                    thread.interrupt();
                                    thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Get_origin();
                                        }
                                    });
                                    thread.start();
                                }
                            });
                        }
                        if (getThread_frequency>=5) {
                            if (isTime) groupMessage.sendGroupMessage("查询失败,错误码为:" + code+"|请检查ID与平台是否正确");
                            else System.out.println("查询失败，错误码为:" + code);
                            CapacityPool.removePlayerData(name);
                            getThread_frequency++;
                        }
                        System.out.println("错误码："+code);
                        return;
                    }
                    try {
                        jsonObject = JSONObject.parseObject(response.body().string());
                    } catch (IOException e) {
                        if (getThread_frequency<5) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    getThread_frequency++;
                                    thread.interrupt();
                                    thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Get_origin();
                                        }
                                    });
                                    thread.start();
                                }
                            });
                        }
                        if (getThread_frequency>=5) {
                            if (isTime) groupMessage.sendGroupMessage("查询失败,错误码为:" + code+"|请检查ID与平台是否正确");
                            else System.out.println("查询失败，错误码为:" + code);
                            CapacityPool.removePlayerData(name);
                            getThread_frequency++;
                        }
                        throw new RuntimeException(e);
                    }
                    jsonObject.put("human", jsonObject.getJSONObject("dividedKills").get("human"));
                    jsonObject.put("melee", jsonObject.getJSONObject("dividedKills").get("melee"));
                    String str = jsonObject.getString("timePlayed");
                    String time1 = "null";
                    if (str.indexOf("days")!=-1){
                        int D = Integer.parseInt(str.substring(0,str.indexOf(" days,")));
                        int H = Integer.parseInt(str.substring(str.indexOf(", ")+2,str.indexOf(":")));
                        String m = str.substring(str.indexOf(":")+1);
                        int M = Integer.parseInt(m.substring(0,m.indexOf(":")));
                        time1=String.valueOf(M>=30?D*24+H+1:D*24+H);
                    } else if (str.indexOf("day" )!=-1) {
                        int D = Integer.parseInt(str.substring(0,str.indexOf(" day,")));
                        int H = Integer.parseInt(str.substring(str.indexOf(", ")+2,str.indexOf(":")));
                        String m = str.substring(str.indexOf(":")+1);
                        int M = Integer.parseInt(m.substring(0,m.indexOf(":")));
                        time1=String.valueOf(M>=30?D*24+H+1:D*24+H);
                    } else {
                        int H =Integer.parseInt(str.substring(0,str.indexOf(":")));
                        String m = str.substring(str.indexOf(":")+1);
                        int M = Integer.parseInt(m.substring(0,m.indexOf(":")));
                        time1=String.valueOf(M>=30?H+1:H);
                    }
                    jsonObject.put("time", time1);
                    double min = Integer.parseInt(time1)*60;
                    min = jsonObject.getDoubleValue("human")/min;
                    BigDecimal bi = new BigDecimal(min);
                    min = bi.setScale(2, RoundingMode.HALF_UP).doubleValue();
                    jsonObject.put("inkpm", min);
                    classes = JsonSort(jsonObject.getJSONArray("classes"));
                    jsonObject.remove("classes");
                    weapons = JsonSort(jsonObject.getJSONArray("weapons"));
                    jsonObject.remove("weapons");
                    veh = JsonSort(jsonObject.getJSONArray("vehicles"));
                    jsonObject.remove("vehicles");
                    wp_group_array = jsonObject.getJSONArray("weaponGroups");
                    jsonObject.remove("weaponGroups");
                    veh_group_array = jsonObject.getJSONArray("vehicleGroups");
                    jsonObject.remove("vehicleGroups");
                    getThread_frequency=5;
                    System.out.println("GET over");
                    if (isTime&&type<=4){
                        selectMessage(type);
                    }
                }
            });
        } catch (Exception e) {
            if (getThread_frequency<5) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        getThread_frequency++;
                        thread.interrupt();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Get_origin();
                            }
                        });
                        thread.start();
                    }
                });
            }
            if(getThread_frequency>=5) {
                if (isTime) groupMessage.sendGroupMessage("链接超时,请检查ID与平台是否正确");
                else System.out.println("链接超时");
                getThread_frequency++;
                CapacityPool.removePlayerData(name);
            }
            throw new RuntimeException(e);
        }
    }
    private void Get_Graphs(){
        if (isOK_Graphs) return;
        getThread_Graphs++;
        System.out.println("GET_G");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        String url = "https://api.gametools.network/bf2042/statsarray/?days=30&name="+name+"&platform="+platform+"&skip_battlelog=false";
        Request request = new Request.Builder()
                .url(url)
                .get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (getThread_Graphs>=5&&type>=4){
                    groupMessage.sendGroupMessage("数据获取失败，请检查id与平台是否正确");
                    CapacityPool.removePlayerData(name);
                }
                if (getThread_Graphs<5){
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            thread_Graphs.interrupt();
                            System.out.println(getThread_Graphs);
                            thread_Graphs = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Get_Graphs();
                                }
                            });
                            thread_Graphs.start();
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code()!=200){
                    if (getThread_Graphs>=5&&type>=4){
                        groupMessage.sendGroupMessage("数据获取失败，请检查id与平台是否正确");
                        CapacityPool.removePlayerData(name);
                    }
                    if (getThread_Graphs<5){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                thread_Graphs.interrupt();
                                System.out.println(getThread_Graphs);
                                thread_Graphs = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Get_Graphs();
                                    }
                                });
                                thread_Graphs.start();
                            }
                        });
                    }
                }
                isOK_Graphs=true;
                JSONObject data = null;
                try {
                    data = JSONObject.parseObject(response.body().string());
                } catch (IOException e) {
                    if (getThread_Graphs>=5&&type>=4){
                        groupMessage.sendGroupMessage("数据获取失败，请检查id与平台是否正确");
                        CapacityPool.removePlayerData(name);
                    }
                    if (getThread_Graphs<5){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                thread_Graphs.interrupt();
                                System.out.println(getThread_Graphs);
                                thread_Graphs = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Get_Graphs();
                                    }
                                });
                                thread_Graphs.start();
                            }
                        });
                    }
                    throw new RuntimeException(e);
                }
                JSONArray time_array = data.getJSONArray("timeStamps");
                kd_array = data.getJSONArray("killDeath");
                kd_in_array = data.getJSONArray("infantryKillDeath");
                kpm_array = data.getJSONArray("killsPerMinute");
                headshots_array = data.getJSONArray("headshots");
                Map<String,Integer> map_origin = new HashMap<>();
                for (int i = 0; i < time_array.size(); i++) {
                    map_origin.put(time_array.getString(i), i);
                }
                time_array.sort(new Comparator<Object>() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        String s1 = (String) o1,s2 = (String) o2;
                        return (int) (num(s1)-num(s2));
                    }
                });
                Map<String,Integer> map = new LinkedHashMap<>();
                for (int i = 0; i < time_array.size(); i++) {
                    map.put(time_array.getString(i), i);
                }
                List<String> list = new ArrayList<>(map.keySet());
                for (int i = 0; i < list.size(); i++) {
                    String[] s1 = list.get(i).split("T");
                    String[] s1_l = s1[0].split("-"),s1_r = s1[1].split(":");
                    Mapping_table.put(i,s1_l[0]+"/"+s1_l[1]+"/"+s1_l[2]);
                    list_origin.add(map_origin.get(list.get(i)));
                }
                if (type>4) selectMessage(type);
                System.out.println("GET_Graph over");
            }
        });
    }
    public int getTime(){
        return Math.max((time - TimeMember), 0);
    }
    public double num(String s){
        String[] temp = s.split("T");
        String[] temp_l = temp[0].split("-"),temp_r = temp[1].split(":");
        double i = Double.parseDouble(temp_l[0]+temp_l[1]+temp_l[2]+temp_r[0]+temp_r[1]);
        return i;
    }
    private BufferedImage Generate_Graph(JSONArray array, TextData textData){
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        double min = 100,max = 0;
        for (int i = 0; i < list_origin.size(); i++) {
            dataset1.addValue(array.getDoubleValue(list_origin.get(i)), textData.getS1(), Mapping_table.get(i));
            max = Math.max(max,array.getDoubleValue(i));
            min = Math.min(min,array.getDoubleValue(i));
        }
        System.out.println(max);
        System.out.println(min);
        JFreeChart chart = ChartFactory.createLineChart(
                name+textData.getS1(),
                textData.getS2(),
                textData.getS3(),
                dataset1,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.setBackgroundPaint(new Color(255,255,255,50));
        CategoryPlot plot = chart.getCategoryPlot();
        setChart(plot, max,min);
        System.out.println(name+textData.getS1()+"over");
        return chart.createBufferedImage(Math.min(list_origin.size() * 100, 1600), 300);
    }
    private BufferedImage Generate_Graph(JSONArray array, TextData textData,int height){
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        double min = 100,max = 0;
        for (int i = 0; i < list_origin.size(); i++) {
            dataset1.addValue(array.getDoubleValue(list_origin.get(i)), textData.getS1(), Mapping_table.get(i));
            max = Math.max(max,array.getDoubleValue(i));
            min = Math.min(min,array.getDoubleValue(i));
        }
        System.out.println(max);
        System.out.println(min);
        JFreeChart chart = ChartFactory.createLineChart(
                name+textData.getS1(),
                textData.getS2(),
                textData.getS3(),
                dataset1,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.setBackgroundPaint(new Color(255,255,255,50));
        CategoryPlot plot = chart.getCategoryPlot();
        setChart(plot, max,min);
        System.out.println(name+textData.getS1()+"over");
        return chart.createBufferedImage(Math.min(list_origin.size() * 100, 1600), height);
    }
    private void setChart(CategoryPlot plot,Double max,Double min){
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setRange(min,max);
        numberAxis.setTickLabelPaint(Color.BLACK);
        numberAxis.setLabelPaint(Color.BLACK);
        numberAxis.setAxisLinePaint(Color.BLACK);
        CategoryAxis axis = plot.getDomainAxis();
        axis.setTickLabelPaint(Color.BLACK);
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
//        axis.setMaximumCategoryLabelLines(0);
        axis.setLabelPaint(Color.BLACK);//days
        axis.setAxisLinePaint(Color.BLACK);
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        //

//        renderer.setSeriesPaint(0, Color.RED);  // 曲线颜色
        renderer.setSeriesPaint(0,Color.RED);
        renderer.setSeriesPaint(1,Color.GREEN);
        renderer.setSeriesShapesVisible(0, true);  // 显示标记点
        renderer.setSeriesLinesVisible(0, true);  // 显示曲线
        renderer.setDefaultLinesVisible(true);
        plot.setRenderer(renderer);
    }
    private void writeWpImg(Graphics2D g2d,int x ,int y,JSONObject jsonObject) throws IOException {
        g2d.setFont(g2d.getFont().deriveFont(25f));
        g2d.setColor(Color.WHITE);
        g2d.drawImage(returnBufferedImage("wp", jsonObject.getString("image"), 100), null, x, y);
        g2d.drawString(ShortCharacters(jsonObject.getString("weaponName")), x+210, y+35);
        g2d.drawString("\u51fb\u6740\u6570:"+jsonObject.getString("kills"), x+210, y+85);
        g2d.drawString("\u7206\u5934\u6570:"+jsonObject.getString("headshotKills"), x+420, y+35);
        g2d.drawString("\u7206\u5934\u7387:"+jsonObject.getString("headshots"), x+420, y+85);
        g2d.drawString("KPM:"+jsonObject.getString("killsPerMinute"), x+610, y+35);
        double time_temp = jsonObject.getDouble("timeEquipped");
        time_temp = time_temp/3600;
        BigDecimal b = new BigDecimal(time_temp);
        time_temp = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        g2d.drawString("\u88c5\u5907\u65f6\u957f:"+time_temp+"h", x+610, y+85);
        for (int i = 0; i < 32; i++) {
            g2d.fillRoundRect(x+i*25,y+100,20,4,2,2);
        }
    }
    private void writeVhImg(Graphics2D g2d,int x,int y,JSONObject jsonObject) throws IOException {
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(25f));
        BufferedImage bufferedImage = returnBufferedImage("veh", jsonObject.getString("image"), 75);
        int w = bufferedImage.getWidth();
        g2d.drawImage(bufferedImage, null, w<300?x+75:x, y+13);
        g2d.drawString(ShortCharacters(jsonObject.getString("vehicleName")), x+300, y+35);
        g2d.drawString("\u51fb\u6740\u6570:"+jsonObject.getString("kills"), x+300, y+85);
        g2d.drawString("KPM:"+jsonObject.getString("killsPerMinute"), x+510, y+35);
        double time_temp = jsonObject.getDouble("timeIn");
        time_temp = time_temp/3600;
        BigDecimal b = new BigDecimal(time_temp);
        time_temp = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        g2d.drawString("\u9a7e\u9a76\u65f6\u957f:"+time_temp+"h", x+510, y+85);
        g2d.drawString("\u6467\u6bc1\u8f7d\u5177:"+jsonObject.getString("vehiclesDestroyedWith"), x+750, y+35);
        g2d.drawString("\u521b\u788e\u4eba\u6570:"+jsonObject.getString("roadKills"), x+750, y+85);
        for (int i = 0; i < 36; i++) {
            g2d.fillRoundRect(x+i*25,y+100,20,4,2,2);
        }
    }
    private void writeClImg(Graphics2D g2d,int x,int y,JSONObject jsonObject) throws IOException {
        g2d.setFont(g2d.getFont().deriveFont(20f));
        g2d.setColor(Color.WHITE);
        g2d.drawImage(returnBufferedImage("cl", jsonObject.getJSONObject("avatarImages").getString("us"), 120), null, x, y);
        g2d.setColor(ClassesType.valueOf(jsonObject.getString("className").replace("-", "")).getColor());
        g2d.fillRoundRect(x+105,y,50+ClassesType.valueOf(jsonObject.getString("className").replace("-", "")).getS1().length()*25,40,10,10);
        g2d.setColor(Color.WHITE);
        g2d.drawString("\u5175\u79cd: "+ClassesType.valueOf(jsonObject.getString("className").replace("-", "")).getS1(),x+110, y+30);
        g2d.drawString("\u4e13\u5bb6\u540d: "+ Classes.valueOf(jsonObject.getString("characterName")).getcName(), x+110,y+70);
        double time = jsonObject.getDouble("secondsPlayed")/3600;
        BigDecimal b = new BigDecimal(time);
        time = b.setScale(2,RoundingMode.HALF_UP).doubleValue();
        g2d.drawString("\u65f6\u957f: "+time+"h",x+110,y+110);
        g2d.drawString("KD: "+jsonObject.getString("killDeath"),x+280,y+30);
        g2d.drawString("\u51fb\u6740\u6570: "+jsonObject.getString("kills"),x+280,y+70);
        g2d.drawString("\u6b7b\u4ea1\u6570: "+jsonObject.getString("deaths"),x+280,y+110);
        g2d.drawString("KPM: "+jsonObject.getString("kpm"), x+450,y+30);
        g2d.drawString("\u6551\u63f4\u6570: "+jsonObject.getString("revives"),x+450,y+70);
        g2d.drawString("\u52a9\u653b\u6570: "+jsonObject.getString("assists"),x+450,y+110);
    }
    private void ThreadInterrupt(Thread thread){
        if (thread!=null) thread.interrupt();
    }
    private BufferedImage getVehDataPie(int w,int h){
        Map<String,Double> map = new HashMap<>();
        double allKills = 0;
        for (int i = 0; i < veh_group_array.size(); i++) {
            JSONObject jsonObject1 = veh_group_array.getJSONObject(i);
            if (jsonObject1.getString("groupName").equals("Air")||jsonObject1.getString("groupName").equals("Sur")) continue;
            if (jsonObject1.getDouble("kills")!=0) {
                map.put(CLType.valueOf(jsonObject1.getString("groupName").replace("-", "")).getS(), jsonObject1.getDouble("kills"));
                allKills += jsonObject1.getDouble("kills");
            }
        }
        //test
        DefaultPieDataset dataset = new DefaultPieDataset<>();
        for(String s : map.keySet()){
            dataset.setValue(s,map.get(s)/allKills);
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "\u51fb\u6740\u5360\u6bd4",
                dataset);
        chart.setBorderVisible(false);
        chart.setBackgroundPaint(null);
        chart.setBackgroundImageAlpha(0.0f);
        TextTitle title = chart.getTitle();
        title.setFont(mc_font);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(mc_font);//标签
        plot.setBackgroundAlpha(0.0f);
        plot.setLabelShadowPaint(null);// 去掉阴影
        plot.setLabelOutlinePaint(null);// 去掉边框
        // 去除背景边框线
        plot.setOutlinePaint(null);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}\u5360\u6bd4{2}"));
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(mc_font);
        BufferedImage pie_img = null;
        try {
            pie_img = Thumbnails.of(chart.createBufferedImage(1200,1200)).size(w,h).asBufferedImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pie_img;
    }
    private BufferedImage getWpDataPie(int w,int h){
        Map<String,Double> map = new HashMap<>();
        double all_kills = 0;
        for (int i = 0; i < wp_group_array.size(); i++) {
            JSONObject jsonObject1 = wp_group_array.getJSONObject(i);
            if ((jsonObject1.getDouble("kills")!=0&&!(jsonObject1.getString("groupName").equals("Miscellaneous")))&&!(jsonObject1.getString("groupName").equals("Primary"))) {
                map.put(CLType.valueOf(jsonObject1.getString("groupName").replace(" ", "").replace("-", "")).getS(), jsonObject1.getDouble("kills"));
                all_kills += jsonObject1.getDouble("kills");
            }
        }
        //test
        DefaultPieDataset dataset = new DefaultPieDataset<>();
        for(String s : map.keySet()){
            dataset.setValue(s,map.get(s)/all_kills);
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "\u51fb\u6740\u5360\u6bd4",
                dataset);
        chart.setBorderVisible(false);
        chart.setBackgroundPaint(null);
        chart.setBackgroundImageAlpha(0.0f);
        TextTitle title = chart.getTitle();
        title.setFont(mc_font);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(mc_font);//标签
        plot.setBackgroundAlpha(0.0f);
        plot.setLabelShadowPaint(null);// 去掉阴影
        plot.setLabelOutlinePaint(null);// 去掉边框
        // 去除背景边框线
        plot.setOutlinePaint(null);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}\u5360\u6bd4{2}"));
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(mc_font);
        BufferedImage pie_img = null;
        try {
            pie_img = Thumbnails.of(chart.createBufferedImage(1200,1200)).size(w,h).asBufferedImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pie_img;
    }
}
