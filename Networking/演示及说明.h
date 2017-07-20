

//演讲内容简书博客地址
http://www.jianshu.com/p/74fb837e63f2



//演示内容
//简单的http演示
http://v.juhe.cn/WNXG/city?key=59a90ae6541d99e9f0db1abc032dd567
wireshark
http.host contains juhe

//arp
sudo arp -d 10.252.68.118
sudo tcpdump  -i en0  -ent '(src 10.252.71.172 and dst 10.252.68.118) or (src 10.252.68.118 and dst 10.252.71.172)'

//dns演示
//清空本地dns
sudo killall -HUP mDNSResponder
ping www.baidu.com 查看


//路由演示
netstat -r

traceroute www.58.com

//ip切片
sudo tcpdump -ntv icmp
ping www.58.com -s 1473


//http请求演示
http://v.juhe.cn/WNXG/city?key=59a90ae6541d99e9f0db1abc032dd567

http.host contains v.juhe.cn
