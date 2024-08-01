package com.example.myapplication.Model;

import java.io.Serializable;

public class LanguageModel implements Serializable {

    private boolean isChecked = false;
    private final int languageflag;
    private String languageCode;
    private String languageName;

    public LanguageModel(int languageflag, String languageCode, String languageName) {
        this.languageflag = languageflag;
        this.languageCode = languageCode;
        this.languageName = languageName;
    }

    @Override
    public String toString() {
        return "LanguageModel{" +
                "isChecked=" + isChecked +
                ", languageflag=" + languageflag +
                ", languageCode='" + languageCode + '\'' +
                ", languageName='" + languageName + '\'' +
                '}';
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getLanguageflag() {
        return languageflag;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}