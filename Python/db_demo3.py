# -*- coding:utf8 -*-
import pymysql

conn = pymysql.connect("localhost","root","root","python",charset='utf8')

cur = conn.cursor()

username = input()

sql = "INSERT INTO t_user(userId,name) VALUES(999,'%s')" %(username)

try:
   cur.execute(sql)
   conn.commit()
except:
   conn.rollback()

conn.close()