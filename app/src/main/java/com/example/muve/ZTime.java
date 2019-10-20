package com.example.muve;

public class ZTime {
    public int day = 0;
    public int month = 0;
    public int year = 0;

    public int hour = 0;
    public int minute = 0;
    public int second = 0;

    public ZTime(int day, int month, int year, int hour, int minute, int second)
    {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour-4;
        this.minute = minute;
        this.second = second;
        this.fixHour();
    }

    public ZTime(){}

    public void fixHour()
    {
        if(hour < 0) {
            hour += 24;
            day--;
        }
    }

    public long toUnixTimestamp()
    {
      return ZTime.GetTimeStamp(this.year-1970+1, this.month, this.day, this.hour+4, this.minute, this.second, 0);
    }

    public static ZTime timeParser(String str)
    {
        String[] split = str.split("T");
        String[] date = split[0].split("-");
        String[] time = split[1].replaceAll("Z", "").split(":");

        int[] ret = new int[date.length + time.length];
        for(int i = 0;i < date.length;i++)
        {
            ret[i] = Integer.parseInt(date[i]);
        }
        for(int i = 0;i < time.length;i++)
        {
            ret[i+date.length] = (int)Float.parseFloat(time[i]);
        }
        ZTime ret2 = new ZTime(ret[2], ret[1], ret[0], ret[3], ret[4], ret[5]);
        return ret2;
    }

    public static long GetTimeStamp(
            int year, int month, int day,
            int hour, int minute, int second, int milliseconds)
    {
        long timestamp = DateToTicks(year, month, day)
                + TimeToTicks(hour, minute, second);

        return (timestamp + milliseconds * TicksInMillisecond) / 10000000L;
    }

    static int[] DaysToMonth365 =
            new int[] { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365 };
    static int[] DaysToMonth366 =
            new int[] { 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366 };
    static long TicksInMillisecond = 10000L;
    static long TicksInSecond = TicksInMillisecond * 1000L;

    public static boolean IsLeapYear(int year)
    {
        if ((year % 4) != 0)
            return false;

        if ((year % 100) == 0)
            return ((year % 400) == 0);

        return true;
    }

    private static long DateToTicks(int year, int month, int day)
    {
        if (((year >= 1) && (year <= 9999)) && ((month >= 1) && (month <= 12)))
        {
            int[] daysToMonth = IsLeapYear(year) ? DaysToMonth366 : DaysToMonth365;
            if ((day >= 1) && (day <= (daysToMonth[month] - daysToMonth[month - 1])))
            {
                int previousYear = year - 1;
                int daysInPreviousYears = ((((previousYear * 365) + (previousYear/4)) - (previousYear/100)) + (previousYear/400));

                int totalDays = ((daysInPreviousYears + daysToMonth[month - 1]) + day) - 1;
                return (totalDays * 0xc92a69c000L);
            }
        }
        return 0L;
    }

    private static long TimeToTicks(int hour, int minute, int second)
    {
        long totalSeconds = ((hour * 3600L) + (minute * 60L)) + second;

        return (totalSeconds * TicksInSecond);
    }
}
