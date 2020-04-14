package com.kakacat.minitool.cleanFile;

import java.io.File;

public class FileItem {

    private File file;
    private boolean isChecked;

    public FileItem(File file,boolean isChecked) {
        this.file = file;
        this.isChecked = isChecked;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

