package com.jucceed.minitool.main;

public class Item {
    private int title;
    private int icon;
    private int note;

    public Item(int title, int icon, int note) {
        this.title = title;
        this.icon = icon;
        this.note = note;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }
}
