
## JVM之内存管理

在1.11版本中发现的和JVM相关的问题：

![error](https://note.youdao.com/yws/public/resource/f2ff12836b349122d42ecf5440d78458/xmlnote/580373DAB3714142AE00519A22386C5A/2583)

可能出现的问题：

## 1、WRedisUtil.class没有部署到服务器

排除

## 2、WRedisUtil.class没有加载到jvm中

排除
通过-verbose:class看到WRedisUtil只加载了一份实例
只有Load class的过程，并没有Unloading的过程

## 3、tomcat中加载WRedisUtil的ClassLoader和使用的ClassLoader不一样

排除
class的唯一性是ClassLoader+package+className来唯一决定的
使用System.out.println("ClassLoaderText类的加载器的名称:"+WRedisUtil.class.getClassLoader().getClass().getName());打印出ClassLoader信息，发现一致

## 4、WRedisUtil.class是jar包冲突导致加载顺序不一致

排除：因为WRedisUtil是app的web项目中的一个工具类，不是在jar包中的，但WRedisUtil的实现依赖的jedis的jar包。
但事后证明就是这个问题
项目原来有一份jedis-2.4.1.jar，后来又引入了wredis-jedis-2.1.0.jar。但发现只是jar包名称改变了，实际的类路径都没有变化，太坑了。虽然jar包名称不同，但内部实际的class确实完全一样的：

![wredis_jedis](https://note.youdao.com/yws/public/resource/f2ff12836b349122d42ecf5440d78458/xmlnote/5C748CB3A4544638A6B5FFDA9694216A/2617)

![jedis](https://note.youdao.com/yws/public/resource/f2ff12836b349122d42ecf5440d78458/xmlnote/3FFA532DBD2C44F2A835C81DFA03E10E/2619)

## 5、之所以jetty是成功的，但68的tomcat是失败的原因

这个不仅和jetty、tomcat容器是相关的，还和windows和linux操作系统对于jar包的加载顺序不一致造成的也是相关的
linux如何加载，windows如何加载？
linux机器（失败）
8355 scanByURLClassLoader:/opt/web/huangye_app_shangjiatong/webapps/WEB-INF/lib/jedis.jar
8541 scanByURLClassLoader:/opt/web/huangye_app_shangjiatong/webapps/WEB-INF/lib/wredis-jedis.jar
windows机器（正常）

## 6、iwork的jar包冲突检测

结果并不准确，实际上答出来的ftp的包中，netty并不冲突，只有一个jar

![solution](https://note.youdao.com/web/#/file/recent/note/6D7D017AECC24F79A5AC311046D7D911/f2ff12836b349122d42ecf5440d7845)

java.lang.NoSuchMehodError
java.lang.NoClassDefFoundError
这类问题十有八九都是jar包冲突引起