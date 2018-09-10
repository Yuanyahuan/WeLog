# 框架源码分析的另一种方式


### 一、平时我们看源码是怎么看的？
    
   * 看readme/doc
   * 运行并debug
   * 加注释
   * 画流程图
   * 画类图
    
举个例子：以往我们阅读一个开源库的代码都是看最新的代码。就像我们看一个三里屯的一个美女一样，好看，美丽，妆很浓，身材很好，衣服很考究，但是我们看多了以后也想自己这样打扮，但是我们并不知道如何去做，不知道从哪下手。如果我们强行去模仿，经常会东施效颦，模仿的并不像，贻笑大方。幸运的是，我们有个法宝，可以时光倒流，看到美女（代码）过去的样子，就是版本管理（git）。如果我们知道美女起床以后都干了什么，对于眼睛、衣服、脸都是怎么收拾的，我们知道了这整个过程，其实我们也就知道了她们是怎么做的，更好的情况是，有可能她们还会边化妆边自言自语，讲述自己化妆的想法，那我们也就知道该怎么化妆了。

所以对于SDWebImage这么有名的第三库，我并没有上来就看最新的代码，而是追本溯源，去看git分支上最原始的commit，试图探寻代码的更迭历程。当然目前，网上分析源码的文章都是从最新代码入手的，所以想要了解，自行网上搜索就好了。
    
### 二、新的学习方法
   
   * 	git commit history
   * git tag

   
   具体怎么做呢？如果看每一个commit，根本不现实，因为太多了。幸运的是，git一般采用tag来做milestone，所以可以以tag为结点，分别看每个版本的区别和内容迭代。cocoapods也是用tag来做版本识别的。
```
-> SDWebImage (4.0.0)
   Asynchronous image downloader with cache support with an UIImageView category.
   pod 'SDWebImage', '~> 4.0.0'
   - Homepage: https://github.com/rs/SDWebImage
   - Source:   https://github.com/rs/SDWebImage.git
   - Versions: 4.0.0, 4.0.0-beta2, 4.0.0-beta, 3.8.2, 3.8.1, 3.8.0, 3.7.6, 3.7.5, 3.7.4, 3.7.3, 3.7.2, 3.7.1, 3.7.0, 3.6, 3.5.4, 3.5.2, 3.5.1, 3.5, 3.4,
   3.3, 3.2, 3.1, 3.0, 2.7.4, 2.7, 2.6, 2.5, 2.4 [master repo]
   - Subspecs:
     - SDWebImage/Core (4.0.0)
     - SDWebImage/MapKit (4.0.0)
     - SDWebImage/GIF (4.0.0)
     - SDWebImage/WebP (4.0.0)
```

SDWebImage目前最高的tag是4.0.0，我们可以用以下tag来分析。

## 1.0/1.0.0
基本框架已经出来了
三个基本类
UIImageView+WebCache
SDWebImageManager
SDWebImageDownloader
SDImageCache

也可以讲一个下README

## 2.0/2.0.0
关键文件：
README.md
SDWebImageDownloader.m

关键词：
NSEventTrackingRunLoopMode

## 2.0到3.0的版本就不赘述了


## 92d7a01a526c19f19240827df06b87c0ddca4747


## 3.0/2012年
- Complete rewrite of the library using `GCD`, `ARC`, `NSCache` and blocks
- Drop compatibility with iOS 3 and 4

## 4.0
SDImageCache、SDImageCacheConfig-----builder模式

附：

#### 1. [SDWebImage源码下载地址](git clone --recursive https://github.com/rs/SDWebImage.git)

#### 2. SDWebImage UML图：

![UML for SDWebImage](http://7xsbfz.com1.z0.glb.clouddn.com/4.0.0-SDWebImageClassDiagram.png)

#### 3. SDWebImage 流程图:
![Sequence for SDWebImage](http://7xsbfz.com1.z0.glb.clouddn.com/4.0.0-SDWebImageSequenceDiagram.png)
 
 


