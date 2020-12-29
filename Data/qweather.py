# -*- coding: utf-8 -*-
"""
@Author      : 满目皆星河
@Project     : Data
@File        : qweather.py
@Time        : 2020/12/21 21:37
@Description : 
"""

import pymysql
import requests
import datetime
import pandas as pd

# 关闭多余连接
requests = requests.session()
requests.keep_alive = False

# 初始化MySQL连接
client = pymysql.connect("39.106.111.245", "hanzhuo", "000403", "qweather")
cursor = client.cursor()
sql = """INSERT INTO `qweather`.`weather` (`ID`, `region`, `name`, `time`, `aqi`, `level`, `category`, `primary`, `pm10`, `pm2p5`, `no2`, `so2`, `co`, `o3`, `tempMin`, `tempMax`, `text`, `windScale`, `windSpeed`, `humidity`, `precip`, `pressure`) VALUES ('{0}', '{1}', '{2}', '{3}', '{4}', '{5}', '{6}', '{7}', '{8}', '{9}', '{10}', '{11}', '{12}', '{13}', '{14}', '{15}', '{16}', '{17}', '{18}', '{19}', '{20}', '{21}');"""

table = pd.read_csv("LocationList/China-City-List-latest.csv", encoding="gbk")


def history():
    """
        :param: None
        :Desc: 获取历史天气数据，只获取一次，后续只需要监控当天天气即可
    """

    # api_list = ["weather", "air"]
    api = "https://datasetapi.qweather.com/v7/historical/{0}?key=eb13ce2341114e848f0a682dfc453b2b&location={1}&date={2}"

    # 获取前10天的日期
    for day in range(4, 11):
        # 获取不同的地区ID
        for i in range(0, len(table)):
            # 获取市级数据
            if table.iloc[i]["城市名称"] == table.iloc[i]["二级行政区划(Adm2)"]:
                region = table.iloc[i]["Location_ID"]
                city = table.iloc[i]["城市名称"]
                # 格式化日期
                int_date = (datetime.datetime.now() - datetime.timedelta(days=day))
                date = int_date.strftime("%Y%m%d")
                # 获取天气数据
                weather_data = requests.get(api.format("weather", region, date)).json()
                # 获取空气质量, 以十二时为准
                air_data = requests.get(api.format("air", region, date)).json()["airHourly"][12]

                cursor.execute(
                    sql.format(str(region) + "_" + int_date.strftime("%Y-%m-%d"), region, city,
                               int_date.strftime("%Y-%m-%d"), air_data["aqi"], air_data["level"],
                               air_data["category"], air_data["primary"], air_data["pm10"],
                               air_data["pm2p5"], air_data["no2"], air_data["so2"], air_data["co"],
                               air_data["o3"], weather_data["weatherDaily"]["tempMin"],
                               weather_data["weatherDaily"]["tempMax"],
                               weather_data["weatherHourly"][12]["text"],
                               weather_data["weatherHourly"][12]["windScale"],
                               weather_data["weatherHourly"][12]["windSpeed"], weather_data["weatherDaily"]["humidity"],
                               weather_data["weatherDaily"]["precip"], weather_data["weatherDaily"]["pressure"]))
                client.commit()


def now():
    """
        :param: None
        :Desc: 监控当天天气
    """

    int_date = (datetime.datetime.now() - datetime.timedelta(days=0))
    date = int_date.strftime("%Y-%m-%d")
    weather_url = "https://devapi.qweather.com/v7/weather/3d?location={0}&key=7f7e08cd8f794bb6a34ff556775ec0a4"
    air_api = "https://devapi.qweather.com/v7/air/now?location={0}&key=7f7e08cd8f794bb6a34ff556775ec0a4"
    # 获取不同的地区ID
    for i in range(0, len(table)):
        # 获取市级数据
        if table.iloc[i]["城市名称"] == table.iloc[i]["二级行政区划(Adm2)"]:
            region = table.iloc[i]["Location_ID"]
            weather_data = requests.get(weather_url.format(region)).json()
            air_data = requests.get(air_api.format(region)).json()
            city = table.iloc[i]["城市名称"]
            try:
                windScaleDay = weather_data["daily"][0]["windScaleDay"].split("-")[-1]
            except:
                windScaleDay = weather_data["daily"][0]["windScaleDay"]
            cursor.execute(sql.format(str(region) + "_" + date, region, city,
                           date, air_data["now"]["aqi"], air_data["now"]["level"],
                           air_data["now"]["category"], air_data["now"]["primary"], air_data["now"]["pm10"],
                           air_data["now"]["pm2p5"], air_data["now"]["no2"], air_data["now"]["so2"],
                           air_data["now"]["co"],
                           air_data["now"]["o3"], weather_data["daily"][0]["tempMin"],
                           weather_data["daily"][0]["tempMax"],
                           weather_data["daily"][0]["textDay"],
                           windScaleDay,
                           weather_data["daily"][0]["windSpeedDay"],
                           weather_data["daily"][0]["humidity"],
                           weather_data["daily"][0]["precip"], weather_data["daily"][0]["pressure"]))
            client.commit()


if __name__ == '__main__':
    try:
        cursor.execute("""CREATE TABLE `weather` (
          `ID` varchar(75) NOT NULL,
          `region` int DEFAULT NULL,
          `name` varchar(45) DEFAULT NULL,
          `time` varchar(45) DEFAULT NULL,
          `aqi` varchar(45) DEFAULT NULL,
          `level` varchar(45) DEFAULT NULL,
          `category` varchar(45) DEFAULT NULL,
          `primary` varchar(45) DEFAULT NULL,
          `pm10` varchar(45) DEFAULT NULL,
          `pm2p5` varchar(45) DEFAULT NULL,
          `no2` varchar(45) DEFAULT NULL,
          `so2` varchar(45) DEFAULT NULL,
          `co` varchar(45) DEFAULT NULL,
          `o3` varchar(45) DEFAULT NULL,
          `tempMin` varchar(45) DEFAULT NULL,
          `tempMax` varchar(45) DEFAULT NULL,
          `text` varchar(45) DEFAULT NULL,
          `windScale` varchar(45) DEFAULT NULL,
          `windSpeed` varchar(45) DEFAULT NULL,
          `humidity` varchar(45) DEFAULT NULL,
          `precip` varchar(45) DEFAULT NULL,
          `pressure` varchar(45) DEFAULT NULL,
          PRIMARY KEY (`ID`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8""")
        history()
    except:
        pass
    try:
        now()
    except:
        client.close()
