package com.BF2042Stats.data.data_enum;

public enum StateCenter {
    A1("\u51fb\u6740\u6570","kills"),//击杀数
    A2("\u6b7b\u4ea1\u6570","deaths"),//死亡数
    A1_1("\u771f\u5b9e\u51fb\u6740","human"),//真实击杀
    A1_2("\u8fd1\u6218\u51fb\u6740","melee"),//近战击杀
    A3("KD","killDeath"),//kd
    A3_1("\u771f\u5b9eKD","infantryKillDeath"),//真实kd
    A4("KPM","killsPerMinute"),//kpm
    A4_1("\u771f\u5b9eKPM","inkpm"),//真实kpm
    A5("\u7206\u5934\u6570","headShots"),//爆头数
    A6("\u7206\u5934\u7387","headshots"),//爆头率
    A7("\u547d\u4e2d\u7387","accuracy"),//正确率
    A8("\u80dc\u7387","winPercent"),//胜率
    A9("\u6bcf\u5c40\u4f24\u5bb3","damagePerMatch"),//每局伤害
    A10("\u6bcf\u5c40\u51fb\u6740","killsPerMatch"),//每局击杀
    A11("\u7ef4\u4fee\u6570","repairs"),//维修数
    A12("\u6551\u63f4\u6570","revives"),//救援数
    A13("\u6700\u4f73\u5c0f\u961f","bestSquad"),
    A14("\u51fb\u6740\u52a9\u653b","killAssists"),
    A15("\u8f7d\u5177\u6467\u6bc1","vehiclesDestroyed"),
    A16("\u6807\u8bb0\u654c\u4eba","enemiesSpotted");
    private final String s1,s2;
    StateCenter(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }
}
