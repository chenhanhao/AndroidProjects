package com.kakacat.minitool.main;

public class MainItem {
    private int titleId;
    private int iconId;
    private int noteId;

    public MainItem(int titleId, int iconId, int noteId) {
        this.titleId = titleId;
        this.iconId = iconId;
        this.noteId = noteId;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
