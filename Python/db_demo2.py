# -*- coding:utf8 -*-
import pymysql

conn = pymysql.connect("localhost","root","root","python",charset='utf8')

cur = conn.cursor()

sql = "select * from t_user"

cur.execute(sql)

result = cur.fetchall()

print(result)
#输出结果
for row in result:
    print(row[0])
    print(row[1])
    print(row[2], end="")
    print(row[-2])

conn.close()