package me.KP56.CodeBlock.Nodes;

import java.awt.*;
import java.io.Serializable;

public abstract class Node implements Serializable {

    private static final long serialVersionUID = 4957615803328834250L;

    private double x;
    private double y;
    private double width;
    private double height;

    public abstract String getName();
    public abstract Color getColor();

    protected Node(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected Node(double x, double y) {
        this(x, y, 160, 70);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void resize(double width, double height) {
        this.width = width;
        this.height = height;
    }
}
