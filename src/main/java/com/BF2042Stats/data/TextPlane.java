package com.BF2042Stats.data;

import java.awt.*;

public class TextPlane {
    private final Graphics2D graphics2D;
    private final Font font;
    private final String s;

    public TextPlane(Graphics2D graphics2D, Font font, String s) {
        this.graphics2D = graphics2D;
        this.font = font;
        this.s = s;
    }
    public void show(int x,int y){
        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);
        String[] ss = s.split("");
        for (int i = 0; i < ss.length; i++) {
            graphics2D.drawString(ss[i],x,y+i*font.getSize());
        }
    }
}
