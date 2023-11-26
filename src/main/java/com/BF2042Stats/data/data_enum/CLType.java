package com.BF2042Stats.data.data_enum;

public enum CLType {
    AssaultRifles("\u7a81\u51fb\u6b65\u67aa"),
    BoltAction("\u72d9\u51fb\u6b65\u67aa"),
    DMR("\u5c04\u624b\u6b65\u67aa"),
    LMG("\u5927\u673a\u67aa"),
    PDW("\u51b2\u950b\u67aa"),
    Shotguns("\u591a\u529f\u80fd"),
    Railguns("\u7535\u78c1\u67aa"),
    LeverActionCarbines("\u53cc\u55b7"),
    Crossbows("\u9a7d"),
    Sidearm("\u624b\u67aa"),
    Miscellaneous("\u6742\u9879"),
    Primary("Primary"),
    Helicopter("\u76f4\u5347\u673a"),
    Plane("\u56fa\u5b9a\u7ffc"),
    InWorld("\u4e16\u754c\u8f7d\u5177"),
    Land("\u9646\u8f7d"),
    Amphibious("\u4e24\u6816");
    private final String s;
    CLType(String s) {
        this.s = s;
    }
    public String getS() {
        return s;
    }
}
