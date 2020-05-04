package com.photostudio.dao.jdbc.stat;

import lombok.extern.slf4j.Slf4j;

import java.util.StringJoiner;

@Slf4j
public class StatisticsLogger {
    public static void logStatistics(StatisticsVo statisticsVo) {
        StringJoiner stringJoiner = new StringJoiner("\n\t", "{\n\t", "}\n");
        stringJoiner.add(String.format("%d nanoseconds spent acquiring JDBC connection", statisticsVo.getOpenConnectionTime()));
        stringJoiner.add(String.format("%d nanoseconds spent releasing JDBC connection", statisticsVo.getCloseConnectionTime()));
        stringJoiner.add(String.format("%d nanoseconds spent preparing %d JDBC statements",
                statisticsVo.getPreparedStatementsSpentTime(),
                statisticsVo.getPreparedStatementsCount()));
        stringJoiner.add(String.format("%d nanoseconds spent executing %d JDBC statements",
                statisticsVo.getSqlExecutedTime(),
                statisticsVo.getSqlExecutedCount()));
        log.info(stringJoiner.toString());
    }
}
