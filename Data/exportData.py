# -*- coding: utf-8 -*-
"""
@Author      : 满目皆星河
@Project     : Data
@File        : exportData.py
@Time        : 2020/12/23 22:45
@Description : 
"""

import datetime
import pymysql
import os


client = pymysql.connect("39.106.111.245", "hanzhuo", "000403", "qweather")
cursor = client.cursor()

date = (datetime.datetime.now() - datetime.timedelta(days=0)).strftime("%Y-%m-%d")
cursor.execute("select * from qweather.weather where `time`='{}'".format(date))
data = cursor.fetchall()

if not os.path.exists("weatherData"):
    os.mkdir("weatherData")
filename = 'weatherData/{}.csv'.format(date)
with open(filename, mode='w', encoding='utf-8') as f:
    for item in data:
        for i in item:
            f.write(str(i))
            if i != item[-1]:
                f.write(",")
        f.write("\n")
