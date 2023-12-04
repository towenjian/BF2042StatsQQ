package com.BF2042Stats.data.data_enum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Classes {
    Mackay("\u9ea6\u51ef","Mackay"),
    Angel("\u5929\u4f7f","Angel"),
    Falck("\u6cd5\u5170\u514b","Falck"),
    Paik("\u767d\u667a\u79c0","Paik"),
    Sundance("\u65e5\u821e","Sundance"),
    Dozer("\u63a8\u571f\u673a","Dozer"),
    Rao("\u62c9\u5965","Rao"),
    Lis("\u8389\u4e1d","Lis"),
    Irish("\u7231\u5c14\u5170\u4f6c","Irish"),
    Crawford("\u514b\u52b3\u798f\u5fb7","Crawford"),
    Boris("\u9c8d\u91cc\u65af","Boris"),
    Zain("\u624e\u56e0","Zain"),
    Casper("\u5361\u65af\u5e15","Casper"),
    Blasco("\u5e03\u62c9\u65af\u79d1","Blasco");
    private final String cName,s1;
    public static List<String> list = new ArrayList<>(Arrays.stream(Classes.values()).map(classes -> classes.s1).collect(Collectors.toList()));
    Classes(String cName,String s1) {
        this.cName = cName;
        this.s1 = s1;
    }
    public String getcName() {
        return cName;
    }
    public static boolean isCl(String s1){
        return list.contains(s1);
    }
}
