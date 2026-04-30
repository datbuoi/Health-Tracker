package model;

import java.util.Date;

public class WaterLog {
    private int id;
    private int userId;
    private int amountMl;
    private Date logTime;

    public WaterLog() {}

    public WaterLog(int userId, int amountMl, Date logTime) {
        this.userId = userId;
        this.amountMl = amountMl;
        this.logTime = logTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAmountMl() { return amountMl; }
    public void setAmountMl(int amountMl) { this.amountMl = amountMl; }

    public Date getLogTime() { return logTime; }
    public void setLogTime(Date logTime) { this.logTime = logTime; }
}
