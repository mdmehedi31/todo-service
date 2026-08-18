package com.todom.library.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;

public class DateTimeUtil {

    private static final Logger log= LoggerFactory.getLogger(DateTimeUtil.class);

    public static final Integer SUBSTRACT_VALUE_FOR_GOOGLE_CALENDAR=10;
    // Converting local date time to UTC time
    public static Instant convertLocalDateTimeToUTCTime(LocalDateTime dateTime, String userTimeZone) {

        ZoneId userZoneId = ZoneId.of(userTimeZone);
        ZonedDateTime utcZoneDateTime=dateTime.atZone(userZoneId);
        log.info("Converted to UTC time value is : {}",utcZoneDateTime);
        return utcZoneDateTime.toInstant();
    }

    // Convert local date to UTC time
    public static Instant convertLocaldateTodInstant(LocalDate dateTime) {
        ZoneId zoneId = ZoneId.of("UTC");
        Instant utcZoneDateTime=dateTime.atStartOfDay(zoneId).toInstant();
        log.info("Converted to Local date to UTC time value is : {}",utcZoneDateTime);
        return utcZoneDateTime;
    }

    //Converting utc time to local date time
    public static LocalDateTime convertUTCTimeToLocalDateTime(Instant instant, String userTimeZone) {
        ZoneId userZoneId = ZoneId.of(userTimeZone);
        ZonedDateTime utcZoneDateTime=instant.atZone(userZoneId);
        log.debug("Converted to Local date time value is : {}",utcZoneDateTime);
        return utcZoneDateTime.toLocalDateTime();
    }

    //Converting utc time to local date
    public static LocalDate convertUTCTimeToLocalDate(Instant instant, String userTimeZone) {
        ZoneId userZoneId = ZoneId.of(userTimeZone);
        ZonedDateTime utcZoneDateTime=instant.atZone(userZoneId);
        log.debug("Converted to Local date time value is : {}",utcZoneDateTime);
        return utcZoneDateTime.toLocalDate();
    }


    public static LocalDateTime convertEpochMilliToLocalDateTimeWithUserTimeZone(long epochMilli, String timeZoneId) {

        Instant instant = Instant.ofEpochMilli(epochMilli);
        ZoneId zoneId =null;
        if(timeZoneId != null) {
            zoneId= ZoneId.of(timeZoneId);
        }else{
            zoneId= ZoneId.systemDefault();
        }

        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        return zonedDateTime.toLocalDateTime();
    }

}
