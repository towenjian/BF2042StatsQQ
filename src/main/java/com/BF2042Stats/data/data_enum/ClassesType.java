package com.BF2042Stats.data.data_enum;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ClassesType {
    Engineer("\u5de5\u7a0b\u5175",new Color( 0, 75, 80),"Engineer"),
    Support("\u652f\u63f4\u5175",new Color(0, 178, 148),"Support"),
    Antitank("\u53cd\u5766\u514b",new Color( 234, 185, 138),"Antitank"),
    Assault("\u571f\u9e21\u5175",new Color( 234, 185, 138),"Assault"),
    Medic("\u4eba\u95f4\u5929\u4f7f",new Color(0, 178, 148),"Medic"),
    Recon("\u659f\u8336\u5175",new Color(180, 160, 255),"Recon"),
    Scout("\u659f\u8336\u5458",new Color(180, 160, 255),"Scout");
    private final String s1,s2;
    private final Color color;
    public static List<String> list = new ArrayList<>( Arrays.stream(ClassesType.values()).map(classesType -> classesType.s2).collect(Collectors.toList()));

    ClassesType(String s1,Color color,String s2) {
        this.s1 = s1;
        this.color = color;
        this.s2 = s2;
    }

    public String getS1() {
        return s1;
    }

    public Color getColor() {
        return color;
    }

    public static boolean isCl(String name){
        return list.contains(name);
    }
}
