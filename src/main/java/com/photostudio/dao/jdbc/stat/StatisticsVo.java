package com.photostudio.dao.jdbc.stat;

import lombok.Data;

@Data
public class StatisticsVo {
    private long openConnectionTime;
    private long closeConnectionTime;
    private int preparedStatementsCount;
    private long preparedStatementsSpentTime;
    private int sqlExecutedCount;
    private long sqlExecutedTime;

    public void addExecutedTime(long time) {
        sqlExecutedCount++;
        sqlExecutedTime += time;
    }
}
