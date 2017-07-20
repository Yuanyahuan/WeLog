#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <netdb.h>
#include <unistd.h>
#define BUFFSIZE 4096
#define TEXT_BUFFSIZE 1024
#define PORT 80

void getServiceData() {
    char host[BUFFSIZE],
    GET[BUFFSIZE], request[BUFFSIZE],
    text[BUFFSIZE],
    lastText[BUFFSIZE],
    totalText[BUFFSIZE * 2];
    int socketid, connectid, res, recvid, flag = 1,lastTextLen;
    struct hostent *purl = NULL;
    struct sockaddr_in sockinfo;
    
    memset(host, 0, BUFFSIZE);
    memset(GET, 0, BUFFSIZE);
    
    // strcpy(GET, "/WNXG/city?key=59a90ae6541d99e9f0db1abc032dd567");
    //strcpy(host, "v.juhe.cn");
    
    strcpy(host, "v.juhe.cn");
    strcpy(GET, "/nba/all_team_info.php?=&key=56bee95190701f294862790e95e32c49");
    socketid = socket(AF_INET, SOCK_STREAM, 0);
    if (socketid == -1) {
        printf("创建socket连接失败\n");
        exit(1);
    }
    printf("-> 创建socket连接成功\n");
    
    // 函数说明：gethostbyname()会返回一个hostent结构，参数name可以为一个主机名或IPv4/IPv6的IP地址。
    // hostent结构说明如下：
    // struct  hostent
    // {
    //     char *h_name;           /*正式的主机名称*/
    //     char **h_aliases;        /* 指向主机名称的其他别名 */
    //     int h_addrtype;          /* 地址的型态， 通常是AF_INET */
    //     int h_length;             /*地址的长度*/
    //     char **h_addr_list;     /* 从域名服务器取得该主机的所有地址 */
    // };
    // 返回值 ：成功返回hostent结构指针，失败则返回NULL指针
    printf("-> 开始获取地址\n");
    purl = gethostbyname(host);
    printf("-> %p",purl);
    // 设置连接信息结构
    memset(&sockinfo, 0, sizeof(struct sockaddr_in));
    sockinfo.sin_family = AF_INET;
    sockinfo.sin_addr.s_addr = *((unsigned long *)purl->h_addr_list[0]);
    sockinfo.sin_port = htons(PORT);
    printf("-> 拷贝开始\n");
    // 构造http请求
    memset(request, 0, BUFFSIZE);
    strcat(request, "GET ");
    strcat(request, GET);
    strcat(request, " HTTP/1.1\r\n");
    // 以上为http请求行信息
    strcat(request, "HOST: ");
    strcat(request, host);
    strcat(request, "\r\n");
    strcat(request, "User-Agent: ");
    strcat(request, "Mozilla");
    strcat(request, "\r\n");
    strcat(request, "Accept: ");
    strcat(request, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    strcat(request, "\r\n");
    strcat(request, "Accept-Language: ");
    strcat(request, "zh-cn");
    strcat(request, "\r\n");
    strcat(request, "Connection: ");
    strcat(request, "keep-alive");
    strcat(request, "\r\n");
    //这个回车换行折腾了我一上午
    strcat(request, "\r\n");
    
    // 连接到远端服务器
    connectid = connect(socketid, (struct sockaddr*)&sockinfo, sizeof(sockinfo));
    if (connectid == -1) {
        printf("连接远端服务器失败\n");
        exit(1);
    }
    printf("-> 连接到远端服务器成功\n");
    
    // 向服务器发送GET请求
    res = send(socketid, request, strlen(request), 0);
    if (res == -1) {
        printf("向服务器发送GET请求失败\n");
        exit(1);
    }
    printf("-> HTTP请求报文如下\n--------HTTP Request--------\n%s\n", request);
    
    printf("-> HTTP应答包报文\n--------HTTP response--------\n");
    // 接受服务器的响应
    while (flag) {
        memset(text, 0, TEXT_BUFFSIZE);
        int bufflen = recv(socketid, text, TEXT_BUFFSIZE, 0);
        
        if (bufflen < 0) {
            printf("接收数据流出错\n");
            close(socketid);
            exit(1);
        }
        if (bufflen > 0) {
            printf("%s",text);
            //结束标志
            memcpy(totalText,lastText,lastTextLen);
            memcpy(totalText+lastTextLen,text,bufflen);
            
            if (strstr(totalText,"\r\n0\r\n\r\n")) {
                flag = 0;
            }

            
        }
        
        memset(lastText,0,TEXT_BUFFSIZE);
        memcpy(lastText,text,bufflen);
        lastTextLen = bufflen;

    }
    close(socketid);
    printf("\nsocket关闭\n");
}

int main(int argc, char *argv[])
{

	getServiceData();
	return 0;
}
