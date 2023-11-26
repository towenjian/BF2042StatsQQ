package com.BF2042Stats.textenum;

import net.coobird.thumbnailator.geometry.Position;

import java.awt.*;

public enum PostionEnum {
    Base(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(0,0);
        }}),
    Avatar(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(30,30);
        }
    }),
    Classer(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(1040, 30);
        }
    }),
    WP1(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(886,9);
        }
    }),
    WP2(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(1286,9);
        }
    }),
    WP3(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(1288,209);
        }
    }),
    WP4(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(40,900);
        }
    }),
    VH1(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(960,10);
        }
    }),
    VH2(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(1360,10);
        }
    }),
    VH3(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(1360,210);
        }
    }),
    VEH4(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(1000, 900);
        }
    }),
    TX(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(22,22);
        }
    }),
    CL(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(665,22);
        }
    }),
    P1(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(1200, 22);
        }
    }),
    P2(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(22, 379);
        }
    }),
    P3(new Position() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
            return new Point(1096, 379);
        }
    });
    private final Position position;
    PostionEnum(Position position) {
        this.position = position;
    }
    public Position getPosition(){
        return position;
    }
}
