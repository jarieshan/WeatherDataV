# -*- coding: utf-8 -*-
"""
@Author      : 满目皆星河
@Project     : qweather.py
@File        : importXY.py
@Time        : 2020/12/26 23:42
@Description : 
"""

import pandas as pd
import pymysql


client = pymysql.connect("39.106.111.245", "hanzhuo", "000403", "qweather")
cursor = client.cursor()

table = pd.read_csv("LocationList/China-City-List-latest.csv", encoding="gbk")
sql = "INSERT INTO `qweather`.`citylocation` (`name`, `Longitude`, `Latitude`) VALUES ('{0}', '{1}', '{2}');"
# 获取不同的地区ID
for i in range(0, len(table)):
    # 获取市级数据
    if table.iloc[i]["城市名称"] == table.iloc[i]["二级行政区划(Adm2)"]:
        name = table.iloc[i]["城市名称"]
        Longitude = table.iloc[i]["Longitude"]
        Latitude = table.iloc[i]["Latitude"]
        cursor.execute(sql.format(name, Longitude, Latitude))
client.commit()
