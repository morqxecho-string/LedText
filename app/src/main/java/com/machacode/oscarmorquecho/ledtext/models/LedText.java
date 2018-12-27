package com.machacode.oscarmorquecho.ledtext.models;

import com.machacode.oscarmorquecho.ledtext.utils.ScrollTextView;

public class LedText{
    private String text;
    private int speed;
    private boolean direction;
    private int size;
    private int colorBackgroundColor;
    private int colorText;
    private ScrollTextView tvTextDisplayed;
    private float STEP_TEXT_SIZE = 10;

    public LedText(ScrollTextView tvTextDisplayed, String text, int speed, boolean direction, int size, int colorBackgroundColor, int colorText) {
        this.tvTextDisplayed = tvTextDisplayed;
        this.text = text;
        this.speed = speed;
        this.direction = direction;
        this.size = size;
        this.colorBackgroundColor = colorBackgroundColor;
        this.colorText = colorText;

        this.tvTextDisplayed.setText(this.text);
        this.tvTextDisplayed.setTextSize(this.size);
        this.tvTextDisplayed.startScroll();
    }

    public boolean stopMarquee(){
        if(!this.tvTextDisplayed.isPaused()){
            this.tvTextDisplayed.pauseScroll();
            return true;
        }else{
            this.tvTextDisplayed.resumeScroll();
            return false;
        }
    }

    public boolean resumeMarquee(){
        if(this.tvTextDisplayed.isPaused()){
            this.tvTextDisplayed.resumeScroll();
            return true;
        }else{
            return false;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.tvTextDisplayed.setText(this.text);
        this.tvTextDisplayed.startScroll();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        this.tvTextDisplayed.setRndDuration(speed);
        this.tvTextDisplayed.startScroll();
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

    public void setSize(String action) {
        this.tvTextDisplayed.setTextSize(action.equals("enlarge") ? (this.size += STEP_TEXT_SIZE) : (this.size -= STEP_TEXT_SIZE));
    }

    public int getColorBackgroundColor() {
        return colorBackgroundColor;
    }

    public void setColorBackgroundColor(int colorBackgroundColor) {
        this.colorBackgroundColor = colorBackgroundColor;
        this.tvTextDisplayed.setBackgroundColor(this.colorBackgroundColor);
    }

    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
        this.tvTextDisplayed.setTextColor(this.colorText);
    }
}