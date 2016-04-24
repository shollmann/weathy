package com.shollmann.weathy.helper;

public class Constants {
    public static final String EMPTY_STRING = "";
    public static final String UTF_8 = "UTF-8";
    public static String CACHE = "Cache";

    public class Time {
        public static final long TEN_SECONDS = 10;
        public static final long ONE_MINUTE = 10;
        public static final long TEN_MINUTES = 10 * ONE_MINUTE;
    }

    public class Size {
        public static final long ONE_KIBIBYTE = 1024;
        public static final long ONE_MEBIBYTE = ONE_KIBIBYTE * 1024;
        public static final long TWO_MEBIBYTES = ONE_MEBIBYTE * 2;
    }

    public class OpenWeatherApi {
        public static final String API_KEY = "c9347df1453d44f36c64fe861d46e36c";
        public static final String URL = "http://api.openweathermap.org";
        public static final String APP_ID_QUERY_PARAM = "appid";
        public static final String UNITS_QUERY_PARAM = "units";
        public static final String METRIC = "metric";
    }

    public class WeatherType {
        public static final String RAIN = "Rain";
        public static final String CLOUDS = "Clouds";
        public static final String CLEAR = "Clear";
        public static final String DRIZZLE = "Drizzle";
        public static final String THUDERSTORM = "Thunderstorm";
        public static final String HAZE = "Haze";
        public static final String SNOW = "Snow";
    }
}
