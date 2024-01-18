package com.BF2042Stats.data;

import com.BF2042Stats.BF2042StatsV1;
import net.mamoe.mirai.utils.MiraiLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImgData {
    private static final Map<String, BufferedImage> weaponImg = new HashMap<>();
    private static final Map<String, BufferedImage> clImg = new HashMap<>();
    private static final Map<String,BufferedImage> vehImg = new HashMap<>();
    private static final MiraiLogger logger = BF2042StatsV1.getLoggerS();
    private static final String WP = "wp",VH = "veh",CL = "cl";
    private static final File weaponFolder = new File(BF2042StatsV1.getJP().getDataFolder()+"/wp");
    private static final File clFolder = new File(BF2042StatsV1.getJP().getDataFolder()+"/cl");
    private static final File vehFolder = new File(BF2042StatsV1.getJP().getDataFolder()+"/veh");
    private static final ImgData imgData = new ImgData();


    private ImgData(){
        logger.info("正在实例化所有图片");
        initImg(weaponFolder.listFiles(),weaponImg);
        initImg(clFolder.listFiles(),clImg);
        initImg(vehFolder.listFiles(),vehImg);
    }
    public  static ImgData getInstance(){
        return  imgData;
    }
    public BufferedImage getImg(String type,String name){
        switch (type){
            case  WP:
                return weaponImg.get(name);
            case  CL:
                return clImg.get(name);
            case  VH:
                return vehImg.get(name);
            default:
                return null;
        }
    }
    public void setImg(String type,String name,BufferedImage img){
        switch (type){
            case  WP:
                weaponImg.put(name,img);
                break;
            case  CL:
                clImg.put(name,img);
                break;
            case  VH:
                vehImg.put(name,img);
                break;
            default:
                logger.warning("图片类型错误");
                break;
        }
    }
    private void initImg(File[] files,Map<String,BufferedImage> map){
        if (files==null) {
            logger.warning("文件夹缺失");
        }
        for (File file : files) {
            try {
                map.put(file.getName(),ImageIO.read(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void start(){
        logger.info("加载完毕");
    }
}
