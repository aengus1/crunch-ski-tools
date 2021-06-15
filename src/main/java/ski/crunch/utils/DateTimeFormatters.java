package ski.crunch.utils;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;


import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DateTimeFormatters {

  public static final DateTimeFormatter ISO_LOCAL_DATE_TIME_NO_NANO =
      new DateTimeFormatterBuilder()
          .parseCaseInsensitive()
          .append(ISO_LOCAL_DATE)
          .appendLiteral('T')
          .appendValue(HOUR_OF_DAY, 2)
          .appendLiteral(':')
          .appendValue(MINUTE_OF_HOUR, 2)
          .appendLiteral(':')
          .appendValue(SECOND_OF_MINUTE, 2)
          .toFormatter(Locale.ENGLISH);

  public static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FILE = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(ISO_LOCAL_DATE)
      .appendLiteral('T')
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral('-')
      .appendValue(MINUTE_OF_HOUR, 2)
      .appendLiteral('-')
      .appendValue(SECOND_OF_MINUTE, 2)
      .toFormatter(Locale.ENGLISH);
}
