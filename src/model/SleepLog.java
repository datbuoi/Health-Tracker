package model;

import java.util.Date;

public class SleepLog {
    private int id;
    private int userId;
    private Date sleepTime;
    private Date wakeTime;
    private String quality;
    private String note;

    public SleepLog() {}

    public SleepLog(int userId, Date sleepTime, Date wakeTime, String quality, String note) {
        this.userId = userId;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.quality = quality;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getSleepTime() { return sleepTime; }
    public void setSleepTime(Date sleepTime) { this.sleepTime = sleepTime; }

    public Date getWakeTime() { return wakeTime; }
    public void setWakeTime(Date wakeTime) { this.wakeTime = wakeTime; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public double getDurationHours() {
        if (sleepTime == null || wakeTime == null) return 0;
        long diff = wakeTime.getTime() - sleepTime.getTime();
        return diff / (1000.0 * 60 * 60);
    }
}
