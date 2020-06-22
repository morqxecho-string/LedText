package com.machacode.oscarmorquecho.ledtext.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class LedTextRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String text;
    private int speed;
    private boolean direction;
    private int size;
    private int colorBackgroundColor;
    private int colorText;
    private Date created_at;

    public LedTextRealm(){}

    public LedTextRealm(int id, String text, int speed, boolean direction, int size, int colorBackgroundColor, int colorText, Date created_at) {
        this.id = id;
        this.text = text;
        this.speed = speed;
        this.direction = direction;
        this.size = size;
        this.colorBackgroundColor = colorBackgroundColor;
        this.colorText = colorText;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getColorBackgroundColor() {
        return colorBackgroundColor;
    }

    public String getColorBackgroundColorToString() {
        return String.valueOf(colorBackgroundColor);
    }

    public void setColorBackgroundColor(int colorBackgroundColor) {
        this.colorBackgroundColor = colorBackgroundColor;
    }

    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
