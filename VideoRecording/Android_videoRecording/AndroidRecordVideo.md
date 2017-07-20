# Android视频录制

### 一、视频录制的方式

   * 1、使用系统提供的Camera+MediaRecorder进行录制
   * 2、使用Camera+FFmpeg库进行视频采集与编码
   * 3、录制后生成视频缩略图
   * 4、使用FFmpeg库进行视频压缩
   
### 二、Camera+MediaRecorder录制

#### 使用Camera+MediaRecorder录制的基本流程：

   * 1、根据相机Id拿到相机对象，进行方向旋转（后置90°，前置270°）；
   * 2、设置相机参数（自动对焦、白平衡……）；
   * 3、设置相机摄像区域尺寸（★考虑兼容性，从相机支持分辨率参数中选取等于屏幕宽高的分辨率或者满足16:9的最大分辨率或者取中等分辨率）；
   * 4、给SurfaceView添加SurfaceHolder回调，作为参数设置到MediaRecorder的参数中即可开启预览；
   * 5、解锁相机对象，给MediaRecorder对象设置参数（视频源、音频源、视频质量、比特率、输出路径、预览的SurfaceView对象、最大录制时间），然后调用prepare方法代表参数设置完毕；
   * 6、调用MediaRecorder的start方法即可开始录制；
   * 7、调用stop方法即可结束录制，将MediaRecorder的回调和预览清空，将对象清空并置为空，将相机锁住；
   * 8、将相机对象资源释放，否则可能会造成相机损坏；
   * 9、生成视频缩略图，跳转到播放页；
   
#### 需要注意的几点

   * 安卓需要考虑各种硬件兼容性，所以相机分辨率只能从支持列表中去取，不能直接指定一个分辨率，部分机型会直接崩溃；
   * 给Camera设置参数时，只设置几个必要的参数即可，设置的参数过多，部分老机型不支持也会崩溃；
   * SurfaceView的宽高也需要根据相机分辨率的宽高比进行调整，否则在有虚拟导航栏的手机上会造成画面变形；
   
#### 使用Camera+MediaRecorder方式录制的优缺点

 优点：系统提供API，各版本兼容性较好，使用简单、集成容易，录制的视频很清晰，不存在音视频不同步的情况；</br>
 缺点：系统高度封装，很多参数设置受限，录制后的视频体积过大，导致后期压缩处理时间也很长；
    
### 三、Camera+MediaRecorder录制

#### 使用Camera+FFmpeg录制的基本流程：

   * 1、初始化FFmpegFrameRecorder类，加载FFmpeg编码库等，初始化音视频参数设置；
   * 2、初始化FFmpegFrameFilter帧过滤器，加载FFmpeg编码库剪切库等；
   * 3、在PreviewStart方法中设置Camera参数，为Camera设置预览缓冲方法，并实现Camera.PreviewCallback接口，这样在onPreviewFrame回调方法中可以拿到一帧的视频数据；
   * 4、将一帧数据传入帧过滤器处理，拿到处理后的帧数据，加上时间戳后写入FrameRecorder字节数组中；
   * 5、在另一个线程中使用AudioRecorder录制音频流，写入音频字节数组中；
   * 6、停止录制时，将采集线程停止，将数据从内存中，加上时间戳，传入FFmpegFrameRecorder类；
   * 7、使用FFmpeg命令将视频流和音频流合到一起，合成视频文件；

#### FFmpeg编码示意图

![Sketch Map For FFmpeg](http://img.blog.csdn.net/20170612094438591?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbWFiZWlqaWFueGk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

以下以YUV420编码方式举例：

![YUV420 Encode](http://img.blog.csdn.net/20170610121150954?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbWFiZWlqaWFueGk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

 安卓中根据采样方式不同主要有YUV4:4:4，YUV4:2:2，YUV4:2:0。
 其中YUV 4:4:4采样，每一个Y对应一组UV分量。 
 YUV 4:2:2采样，每两个Y共用一组UV分量。
 YUV 4:2:0采样，每四个Y共用一组UV分量 。
 举个例子，屏幕上有八个像素点，YUV4:4:4会有8个Y,8个U,8个V。YUV4:2:2会有8个Y,4个U,4个V。YUV4:2:0会有8个Y，2个U,2个V。
    
 可以看到Y1, Y2, Y7, Y8这些物理上相近的4个像素公用了同样的U1和V1，相似的Y3,Y4,Y9,Y10用的就是U2和V2。这里不同的颜色把这个特性刻画的非常形象，一 目了然。
 格子数目就是这一帧图像的byte数组的大小，其数组元素排放顺序就是后面那一长条的样子。
 
FFmpeg中的画面裁剪和旋转操作：

![Cut Y Data](http://img.blog.csdn.net/20170610121416597?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbWFiZWlqaWFueGk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)
![Cut U And V Data](http://img.blog.csdn.net/20170610121436713?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbWFiZWlqaWFueGk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

  剪切Y值：
  我们这里假设我们采集的视频宽是640，高是480，我们要剪切成宽是400，高是300的视频。根据上面我们能知道640*480的一帧byte数组里面将会有640*480个Y，且排在最前面，
  然后有（1/4）*640*480个V，然后有（1/4）*640*480个U,我们要剪切成400*300，自然是保留一部分数据即可。
  我们先对Y建立一个模型，既然是640*480，我们可以把它当成一行有640个Y，一共有480行,如图所示红色标注内表示640*480个Y,而黄色区域内则是我们剪切完成的Y的所有值。
  
  剪切UV值：
  接下来我们解析剪切UV数据，UV的模型和Y有点不同。之所以叫YUV4：2：0，不是因为没有V,它其实是在纵向上UV交换扫描的，比如第一行扫描U第二行就扫描V，第三行再扫描U。
  在横向上是隔一个扫描，比如第一列扫描了，第二列就不扫描，然后扫描第三列。所以U在横向和纵向上的数据都是其Y的1/2，总数量是其1/4，V也是一样的。
  320*240的区域就是我们就是我们U值或者V值的区域，200*150的区域就是我们剪切后的U值或者V值的目标区域。

然后将合成后的一帧数据旋转90°（后置摄像头录制），则得到一帧视频数据：

![Rotate A Frame Data](http://upload-images.jianshu.io/upload_images/5461213-dabf7e3a12961832.gif?imageMogr2/auto-orient/strip)

#### 将编码后的视频音频文件交给FFmpeg进行合成即可得到最终视频文件：

  以下是封装好的FFmpeg工具类拼接命令参数并执行：
  
```
   char *cmd[10];
   cmd[0] = "ffmpeg";
   cmd[1] = "-i";
   cmd[2] = new_in_filename_v;
   cmd[3] = "-i";
   cmd[4] = new_in_filename_a;
   cmd[5] = "-c:v";
   cmd[6] = "copy";
   cmd[7] = "-c:a";
   cmd[8] = "copy";
   cmd[9] = new_out_filename;
   return ffmpeg_cmd_run(10,cmd);
```
   
#### 使用Camera+FFmpeg方式录制的优缺点：

  优点：可以自己处理音视频数据，可配置，灵活性更高，可实现的效果更多，同时可以在保证清晰度的情况下保持较小的体积；</br>
  缺点：需自行编译FFmpeg库，开发C层处理方法，使用JNI调用，不易集成，对开发人员要求较高，而且FFmpeg库体积较大，对移动端APP体积有很大影响；
  
### 四、视频压缩

   视频之所以可以压缩，首先建立因为视频存在两大冗余数据：</br>
   数据冗余：空间冗余、时间冗余、结构冗余、信息熵冗余等，即图像的各像素之间存在着很强的相关性。消除这些冗余并不会导致信息损失，属于无损压缩。</br>
   视觉冗余：人眼的一些特性比如亮度辨别阈值，视觉阈值，对亮度和色度的敏感度不同，使得在编码的时候引入适量的误差，也不会被察觉出来。可以利用人眼的视觉特性，以一定的客观失真换取数据压缩。这种压缩属于有损压缩。
   
   数字视频信号的压缩正是基于上述两种条件，使得视频数据量得以极大的压缩，有利于传输和存储。一般的数字视频压缩编码方法都是混合编码，即将变换编码，运动估计和运动补偿，以及熵编码三种方式相结合来进行压缩编码。
   通常使用变换编码来消除图像的帧内冗余，用运动估计和运动补偿来去除图像的帧间冗余，用熵编码来进一步提高压缩的效率。
   
#### FFmpeg常用命令：

```
   -vf 	        可以添加滤镜，特别强大，可以旋转缩放剪切等等
   Transpose    逆时针旋转90°然后垂直翻转1:顺时针旋转90°，2:逆时针旋转90°，3:顺时针旋转90°然后水平翻转
   Crop	        剪切，其有四个参数，分别是宽度、高度、起始剪切位置的X值与Y值
   -vcodec 	    指定视频编解码器；
   -acodec      指定音频编解码器；
   vbr 	        动态码率；
   cbr 	        静态码率；
   -crf 	    视频质量等级0~51，越大质量越差，建议18~28即可，与cbr模式不兼容；
   -preset      转码速度，具体有：ultrafast、superfast、veryfast、faster、fast、medium、slow、slower、veryslow、placebo
   -i           指定输入；
   -x264opts    配置其编解码参数；
   maxrate      最大码率；
   bitrate      固定码率;
   -f           输出格式;
   -s           设置帧大小。格式为 ‘wxh’;
   -ss          指定开始时间;
   -vframes     指定多少帧;
```
   
 以下是一段Java使用FFmpeg命令压缩视频的示例：
 
 ```
  String vbr = " -vbr 4 ";
  if (compressConfig != null && compressConfig.getMode()==BaseMediaBitrateConfig.MODE.CBR) {
      vbr = "";
  }
  String cmd_transcoding = String.format("ffmpeg -i %s -c:v libx264 %s %s %s -c:a libfdk_aac %s %s",
  mMediaObject.getOutputTempVideoPath(),
  getBitrateModeCommand(compressConfig,"",false),
  getBitrateCrfSize(compressConfig, "-crf 28", false),
  getBitrateVelocity(compressConfig, "-preset:v veryfast", false), vbr,mMediaObject.getOutputTempTranscodingVideoPath());
  boolean transcodingFlag = UtilityAdapter.FFmpegRun("", cmd_transcoding) == 0;
```
  
  上面的代码指定了视频编解码器为libx264，音频编解码器为libfdkaac，配置好转码速率，结束后我们就得到了压缩好的视频了。