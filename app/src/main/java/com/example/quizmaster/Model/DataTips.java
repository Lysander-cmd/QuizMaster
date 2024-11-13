package com.example.quizmaster.Model;

public class DataTips {
    private String id;
    private String judulTips;
    private String isiTips;

    // Default constructor diperlukan untuk Firebase
    public DataTips() {
    }

    public DataTips(String id, String judulTips, String isiTips) {
        this.id = id;
        this.judulTips = judulTips;
        this.isiTips = isiTips;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudulTips() {
        return judulTips;
    }

    public void setJudulTips(String judulTips) {
        this.judulTips = judulTips;
    }

    public String getIsiTips() {
        return isiTips;
    }

    public void setIsiTips(String isiTips) {
        this.isiTips = isiTips;
    }
}

