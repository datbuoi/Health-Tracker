package model;

import java.util.Date;

public class WeightLog {
    private int id;
    private int userId;
    private float weightKg;
    private float bmi;
    private Date logDate;
    private String note;

    public WeightLog() {}

    public WeightLog(int userId, float weightKg, float bmi, Date logDate, String note) {
        this.userId = userId;
        this.weightKg = weightKg;
        this.bmi = bmi;
        this.logDate = logDate;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public float getWeightKg() { return weightKg; }
    public void setWeightKg(float weightKg) { this.weightKg = weightKg; }

    public float getBmi() { return bmi; }
    public void setBmi(float bmi) { this.bmi = bmi; }

    public Date getLogDate() { return logDate; }
    public void setLogDate(Date logDate) { this.logDate = logDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getBmiCategory() {
        if (bmi < 18.5) return "Gầy";
        else if (bmi < 25.0) return "Bình thường";
        else if (bmi < 30.0) return "Thừa cân";
        else return "Béo phì";
    }
}
