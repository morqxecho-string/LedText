package com.machacode.oscarmorquecho.ledtext.models;

public class Font{
    private int id;
    private String fontName;
    private String fontPath;
    private int TYPE_FONT;

    public Font(int id, String fontName, String fontPath, int TYPE_FONT) {
        this.id = id;
        this.fontName = fontName;
        this.fontPath = fontPath;
        this.TYPE_FONT = TYPE_FONT;
    }

    public int getId() {
        return id;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    public int getTYPE_FONT() {
        return TYPE_FONT;
    }

    public void setTYPE_FONT(int TYPE_FONT) {
        this.TYPE_FONT = TYPE_FONT;
    }
}