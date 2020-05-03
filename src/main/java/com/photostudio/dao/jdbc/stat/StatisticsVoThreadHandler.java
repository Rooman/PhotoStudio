package com.photostudio.dao.jdbc.stat;

public class StatisticsVoThreadHandler {
    private static final ThreadLocal<StatisticsVo> STATISTICS_VO_THREAD_LOCAL = new ThreadLocal<>();

    static StatisticsVo createStatisticVo() {
        StatisticsVo statisticsVo = new StatisticsVo();
        STATISTICS_VO_THREAD_LOCAL.set(statisticsVo);
        return statisticsVo;
    }

    static StatisticsVo getCurrentStatisticVo() {
        return STATISTICS_VO_THREAD_LOCAL.get();
    }

    static void clearStatisticVo() {
        STATISTICS_VO_THREAD_LOCAL.remove();
    }
}
