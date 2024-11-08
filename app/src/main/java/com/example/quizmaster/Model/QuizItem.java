package com.example.quizmaster.Model;

import com.example.quizmaster.R;

import java.util.ArrayList;

public class QuizItem {
    private String id;
    private String judulKuis;      // Sesuai dengan key di Firebase
    private String mataPelajaran;  // Sesuai dengan key di Firebase
    private String waktu;          // Sesuai dengan key di Firebase
    private String level;          // Sesuai dengan key di Firebase
    private String pertanyaan;     // Sesuai dengan key di Firebase
    private String jawaban;        // Sesuai dengan key di Firebase
    private ArrayList<String> opsi; // Sesuai dengan key di Firebase
    private int imageResource;     // Untuk tampilan UI

    // Constructor kosong diperlukan untuk Firebase
    public QuizItem() {
    }

    // Constructor lengkap
    public QuizItem(String id, String judulKuis, String mataPelajaran, String waktu,
                    String level, String pertanyaan, String jawaban,
                    ArrayList<String> opsi, int imageResource) {
        this.id = id;
        this.judulKuis = judulKuis;
        this.mataPelajaran = mataPelajaran;
        this.waktu = waktu;
        this.level = level;
        this.pertanyaan = pertanyaan;
        this.jawaban = jawaban;
        this.opsi = opsi;
        this.imageResource = R.drawable.quiz;
    }

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJudulKuis() { return judulKuis; }
    public void setJudulKuis(String judulKuis) { this.judulKuis = judulKuis; }

    public String getMataPelajaran() { return mataPelajaran; }
    public void setMataPelajaran(String mataPelajaran) { this.mataPelajaran = mataPelajaran; }

    public String getWaktu() { return waktu; }
    public void setWaktu(String waktu) { this.waktu = waktu; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getPertanyaan() { return pertanyaan; }
    public void setPertanyaan(String pertanyaan) { this.pertanyaan = pertanyaan; }

    public String getJawaban() { return jawaban; }
    public void setJawaban(String jawaban) { this.jawaban = jawaban; }

    public ArrayList<String> getOpsi() { return opsi; }
    public void setOpsi(ArrayList<String> opsi) { this.opsi = opsi; }

    public int getImageResource() { return imageResource; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }

    // Helper methods untuk kompabilitas dengan adapter yang ada
    public String getTitle() { return judulKuis; }
    public String getSubject() { return mataPelajaran; }
    public String getDuration() { return waktu; }
    public String getDifficulty() { return level; }
    public String getQuestion() { return pertanyaan; }
    public String getAnswer() { return jawaban; }
}
