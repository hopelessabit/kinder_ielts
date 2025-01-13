package com.kinder.kinder_ielts.util;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

public class TimeUtil {
    public static OffsetTime convert(Time time){
        LocalTime localTime = time.getTime();
        ZoneOffset zoneOffset = ZoneOffset.of(time.getOffset());
        return OffsetTime.of(localTime, zoneOffset);
    }
}
