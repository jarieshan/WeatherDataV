package com.myblog.datav.selectData;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class selectData {


    private static JdbcTemplate jdbcTemplate;

    public selectData(JdbcTemplate jdbcTemplate) {
        selectData.jdbcTemplate = jdbcTemplate;
    }


    public static Object SJZTodayInfo(){
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String sql = "SELECT tempMin, tempMax, aqi, text FROM qweather.weather where name=\"石家庄\" and time=\"2020-12-20\"";
//        String sql = str + date;
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);

        return map;
    }


    public static Object areaEchartsData(){

        String sql = "SELECT name, avg_aqi as value FROM qweather.weatheranaly;";

        List<Map<String, Object>> data = new ArrayList<>();    // 初始化用于存储数据的列表
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> temp: list) {
            temp.replace("value", new Double(String.valueOf(temp.get("value"))).intValue());
            data.add(temp);
        }

        return data;
    }

    public static Object areaEchartsMap(){

        String sql = "SELECT * FROM qweather.citylocation;";

        Map<String, Object> data = new HashMap<>();    // 初始化用于存储数据的列表
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> temp: list) {

            List<Double> array = new ArrayList<>();

            array.add(new Double(String.valueOf(temp.get("Longitude"))));
            array.add(new Double(String.valueOf(temp.get("Latitude"))));
            data.put(String.valueOf(temp.get("name")), array);

        }
        return data;
    }

    public static Object avgAQITop10() {
        String sql = "SELECT name, avg_aqi FROM qweather.weatheranaly ORDER BY avg_aqi DESC limit 10;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<List<String>> data = new ArrayList<>();

        List<String> listTemp = new ArrayList<>();
        listTemp.add("score");
        listTemp.add("AQI");
        listTemp.add("name");
        data.add(listTemp);

        int i = 10;
        for (Map<String, Object> temp: list) {
            listTemp = new ArrayList<>();
            listTemp.add(String.valueOf(i*10));
            listTemp.add(String.valueOf(temp.get("avg_aqi")));
            listTemp.add(String.valueOf(temp.get("name")));
            data.add(1, listTemp);
            i -=  1;
        }

        return data;
    }

    public static Object SJZTodayPollution(){
        //        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String sql = "SELECT pm2p5, pm10, no2, so2, co, o3 FROM qweather.weather where name=\"石家庄\" and time=\"2020-12-20\"";
//        String sql = str + date;
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        List<Object> data = new ArrayList<>();


        for (String key:map.keySet()) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("name", key);
            temp.put("value", map.get(key));
            data.add(temp);
        }

        return data;
    }

    public static Object temp() {
        String sql = "SELECT time, tempMin, tempMax FROM qweather.weather WHERE name=\"石家庄\" ORDER BY time DESC LIMIT 15;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Map<String, Object> data = new HashMap<>();
        List<Object> tempMin = new ArrayList<>();
        List<Object> tempMax = new ArrayList<>();
        List<Object> time = new ArrayList<>();

        for (Map<String, Object> temp: list){
            tempMin.add(0, temp.get("tempMin"));
            tempMax.add(0, temp.get("tempMax"));
            time.add(0, String.valueOf(temp.get("time")).split("-")[2]);
        }

        data.put("tempMin", tempMin);
        data.put("tempMax", tempMax);
        data.put("date", time);
        return data;
    }

    public static Object sumRain(){
        String sql = "SELECT name, count(*) as sumRain FROM qweather.weather WHERE text LIKE \"%雨%\" GROUP BY name ORDER BY sumRain DESC LIMIT 8;\n";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<Object> name = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        for (Map<String, Object> temp: list){
            name.add(temp.get("name"));
            value.add(temp.get("sumRain"));
        }

        map.put("name", name);
        map.put("sumRain", value);
        return map;
    }
}
