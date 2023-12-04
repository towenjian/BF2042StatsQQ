package com.BF2042Stats.textenum;

public enum TextData {
    KD("-KD","Days","KD"),
    InfantryKD("-infantryKillDeath","Days","KD"),
    KPM("-Kill","Days","kpm"),
    HEADSHOTS("-headshots","Days","%");
    private final String s1,s2,s3;
    TextData(String s1, String s2, String s3) {
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
    }

    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }

    public String getS3() {
        return s3;
    }
}
