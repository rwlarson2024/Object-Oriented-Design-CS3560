package Scheduler;

import java.time.LocalDateTime;

public class TimeUtils {
    public static LocalDateTime roundToNearest15Minutes(LocalDateTime dateTime) {
        int minute = dateTime.getMinute();
        int newMinute;

        if (minute % 15 < 7.5) {
            newMinute = minute - (minute % 15);
        } else {
            newMinute = minute + (15 - minute % 15);
        }

        if (newMinute == 60) {
            dateTime = dateTime.plusHours(1);
            newMinute = 0;
        }

        return dateTime.withMinute(newMinute).withSecond(0).withNano(0);
    }
}
