package com.BF2042Stats.data.data_enum;

import com.BF2042Stats.data.data_factory.T.*;
import com.BF2042Stats.data.data_interface.InterfaceData;

public enum FactoryEnum {
    cx(new CX(),"cx"),
    bd(new BD(),"bd"),
    vh(new VH(),"vh"),
    wp(new WP(),"wp"),
    cd(new CD(),"cd"),
    list(new LIST(),"list"),
    cl(new CL(),"cl"),
    kd(new KD(),"kd"),
    kill(new Kill(),"kill"),
    st(new ST(),"st"),
    rl(new RL(),"rl"),
    reload(new Reload(),"reload"),
    config(new Config(),"config"),
    clear(new Clear(),"clear");
    private final InterfaceData interfaceData;
    private final String name;

    FactoryEnum(InterfaceData interfaceData,String name) {
        this.name = name;
        this.interfaceData = interfaceData;
    }

    public InterfaceData getInterfaceData() {
        return interfaceData;
    }
    @Override
    public String toString() {
        return name;
    }
}
