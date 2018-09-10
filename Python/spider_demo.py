#使用requests 库实现爬虫 单个网页
# -*- coding:utf8 -*-
import requests
from lxml import etree

html = requests.get('http://ishare.58corp.com/index.php/node/cshow/6')

#print(html.text)

selector = etree.HTML(html.text)

auth = selector.xpath('//dt[@class="user-arc-author"]/p/a/text()')

print(auth)

titles = selector.xpath('//dd[@class="user-arc-title ell media-list-title"]/a/em/text()')

print(titles)

tags = selector.xpath('//dd[@class="user-arc-popular media-list-collec"]/span[@class="label-circle radius5"]/a/text()')
print(tags)

contents = selector.xpath('//dd[@class="user-arc-desc media-list-summary"]/a/text()')

#print(contents[1])

