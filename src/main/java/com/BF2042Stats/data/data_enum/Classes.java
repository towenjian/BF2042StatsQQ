package com.BF2042Stats.data.data_enum;

public enum Classes {
    Mackay("\u9ea6\u51ef"),
    Angel("\u5929\u4f7f"),
    Falck("\u6cd5\u5170\u514b"),
    Paik("\u767d\u667a\u79c0"),
    Sundance("\u65e5\u821e"),
    Dozer("\u63a8\u571f\u673a"),
    Rao("\u62c9\u5965"),
    Lis("\u8389\u4e1d"),
    Irish("\u7231\u5c14\u5170\u4f6c"),
    Crawford("\u514b\u52b3\u798f\u5fb7"),
    Boris("\u9c8d\u91cc\u65af"),
    Zain("\u624e\u56e0"),
    Casper("\u5361\u65af\u5e15"),
    Blasco("\u5e03\u62c9\u65af\u79d1");
    private final String cName;
    Classes(String cName) {
        this.cName = cName;
    }
    public String getcName() {
        return cName;
    }
}
