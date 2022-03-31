package com.ecnu.counseling.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeUtils {

    private final static ZoneId defaultZoneId = ZoneId.of("Asia/Shanghai");

    /**
     * LocalDateTime -> 时间戳 毫秒
     *
     * @param localDateTime
     * @return
     */
    public static Long getTimeStampMilli(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(defaultZoneId).toInstant();
        long timeStamp = instant.toEpochMilli();
        return timeStamp;
    }


    /**
     * 时间戳 毫秒 -> LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, defaultZoneId);
    }


    /**
     * 时间戳 秒 -> LocalDateTime
     *
     * @param second
     * @return
     */
    public static LocalDateTime getDateTimeOfSecond(long second) {
        return getDateTimeOfTimestamp(second * 1000L);
    }


}
