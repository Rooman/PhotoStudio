package com.photostudio.dao.jdbc.stat;

import lombok.Data;

@Data
public class StatisticsVo {
    private long openConnectionTime;
    private long closeConnectionTime;
    private int preparedStatementsCount;
    private long preparedStatementsSpentTime;
}
