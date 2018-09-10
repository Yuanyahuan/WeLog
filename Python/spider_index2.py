# -*- coding:utf8 -*-
import requests
from lxml import etree
import json
import os

useragent={'user-agent':'mozilla/5.0'}

# 获取url对应的源码页面
def getHtml(url):
    try:
        html = requests.get(url, timeout=30, headers=useragent)
        html.raise_for_status()
        html.encoding = html.apparent_encoding
        return html.text
    except:
        return ''

#解析html内容，通过xpath获取需要的内容
def getParseHtml(html):
    cont=[]
    try:
        selector = etree.HTML(html)
        times = selector.xpath('//div[@class="resultList"]/div[@class="jobList"]/ul/li[@class="l1"]/span[@class="e2"]/text()')
        companys = selector.xpath('//div[@class="resultList"]/div[@class="jobList"]/ul/li[@class="l1"]/span[@class="e3 cutWord"]/a/text()')
        prices = selector.xpath('//div[@class="resultList"]/div[@class="jobList"]/ul/li[@class="l2"]/span[@class="e2"]/text()')
        type = selector.xpath('//div[@class="resultList"]/div[@class="jobList"]/ul/li[@class="l2"]/span[@class="e3"]/em[2]/text()')
        peopnums = selector.xpath('//div[@class="resultList"]/div[@class="jobList"]/ul/li[@class="l2"]/span[@class="e3"]/em[3]/text()')
        for i in range(len(times)):
            dic = {}
            dic["time"]=times[i]
            dic["company"]=companys[i]
            dic["price"]=prices[i]
            dic["type"] = type[i]
            dic["num"] = peopnums[i]
            cont.append(dic)
        return cont
    except:
        return ''

#把内容输出到文件中(列表)
def saveContent(cont):
    for co in cont:
        with open('D:/download/a.json', 'a') as outfile:
            print(co)
            json.dump(co, outfile, ensure_ascii=False)
            outfile.write('\n')
            outfile.close()

#获取请求的地址
def geturl(word,num,city):
    urls = []
    url = r'http://www.chinahr.com/sou/?orderField=relate&keyword={word}&city={city}&page={page}'
    for x in range(1, num + 1):
        # word为搜索关键词，num为想获取的页面数量
        u = url.format(word=word, city=city,page=x)
        u = url.format(word=word, city=city,page=x)
        urls.append(u)
    return urls

def main():
    num = int(input('输入需要获取的page数：'))
    word = input("请输入需要查询的关键字：")
    urls = geturl(word,num,"34,398")
    for url in urls:
        html =getHtml(url)
        cont = getParseHtml(html)
        saveContent(cont)
main()
