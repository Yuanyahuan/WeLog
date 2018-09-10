import pymysql

# 连接数据库
conn=pymysql.connect("localhost","root","root","python")

# 使用 cursor() 方法创建一个游标对象 cursor
cur = conn.cursor()

# 使用 execute()  方法执行 SQL 查询
cur.execute("SELECT VERSION()")

# 使用 fetchone() 方法获取单条数据.
data = cur.fetchone()

print("Database version : %s " % data)

# 关闭数据库连接
conn.close()