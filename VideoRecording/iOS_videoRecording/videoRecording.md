#iOS视频录制.
![some](https://github.com/suifengqjn/demoimages/blob/master/视频录制/1.png?raw=true)

##UIImagePickerController
**UIImagePickerController。这是一个封装了完整视频捕获管线和相机 UI 的 view controller。这种方式只能设置一些简单参数，自定义程度不高,只能自定义界面上的操作按钮，还有视频的画质等。**

#AVFoundation/AVFoundation
![some](http://7xsbfz.com1.z0.glb.clouddn.com/tmp3654bad1.png)

![some](http://7xsbfz.com1.z0.glb.clouddn.com/tmp6dbd314f.png)

##录制mov格式

- **需要初始化的对象：** 
1.AVCaptureSession：负责输入和输出设备之间的数据传递。2.AVCaptureMovieFileOutput：视频输出流。3.AVCaptureDevice：主要用来获取iPhone一些关于相机设备的相关属性，摄像头，音频输入设备。4.AVCaptureVideoPreviewLayer：图像预览层，实时显示捕获的图像。5.视频播放器（自定义）。

###初始化
``` OC
//初始化会话，用来结合输入输出
    self.session = [[AVCaptureSession alloc] init];
    //设置分辨率 (设备支持的最高分辨率)
    if ([self.session canSetSessionPreset:AVCaptureSessionPreset640x480]) {
        self.session.sessionPreset = AVCaptureSessionPreset640x480;
    }
    //取得后置摄像头
    AVCaptureDevice *captureDevice = [self getCameraDeviceWithPosition:AVCaptureDevicePositionBack];
    //添加一个音频输入设备
    AVCaptureDevice *audioCaptureDevice=[[AVCaptureDevice devicesWithMediaType:AVMediaTypeAudio] firstObject];

    self.captureDeviceInput = [[AVCaptureDeviceInput alloc] initWithDevice:captureDevice error:&error];
    
    //添加音频
    AVCaptureDeviceInput *audioCaptureDeviceInput=[[AVCaptureDeviceInput alloc]initWithDevice:audioCaptureDevice error:&error];
      
    //输出对象
    self.captureMovieFileOutput = [[AVCaptureMovieFileOutput alloc] init];//视频输出
    //将输入设备添加到会话
    if ([self.session canAddInput:self.captureDeviceInput]) {
        [self.session addInput:self.captureDeviceInput];
        [self.session addInput:audioCaptureDeviceInput];
       }
    
    //将输出设备添加到会话 (刚开始 是照片为输出对象)
    if ([self.session canAddOutput:self.captureMovieFileOutput]) {
        [self.session addOutput:self.captureMovieFileOutput];
    }
    
    //创建视频预览层，用于实时展示摄像头状态
    self.previewLayer = [[AVCaptureVideoPreviewLayer alloc] initWithSession:self.session];。。。。
    [self.bgView.layer addSublayer:self.previewLayer];
    
    [self addNotificationToCaptureDevice:captureDevice];
   ... oc'''
```
###开始录制
``` OC
 //根据设备输出获得连接
 AVCaptureConnection *connection = [self.captureMovieFileOutput connectionWithMediaType:AVMediaTypeAudio];
        //根据连接取得设备输出的数据
        [self.captureMovieFileOutput startRecordingToOutputFileURL:fileUrl recordingDelegate:self];
。。。。。。。
    
```
###视频输出代理
``` OC
-(void)captureOutput:(AVCaptureFileOutput *)captureOutput didStartRecordingToOutputFileAtURL:(NSURL *)fileURL fromConnections:(NSArray *)connections{
}

//视频录制完成
-(void)captureOutput:(AVCaptureFileOutput *)captureOutput didFinishRecordingToOutputFileAtURL:(NSURL *)outputFileURL fromConnections:(NSArray *)connections error:(NSError *)error
{
}
```
###视频转换成MP4格式导出
```
AVAssetExportSession *exportSession= [[AVAssetExportSession alloc] initWithAsset:asset presetName:AVAssetExportPresetMediumQuality];
                exportSession.shouldOptimizeForNetworkUse = YES;
                exportSession.outputURL = [NSURL fileURLWithPath:sandboxPath];
 exportSession.outputFileType = AVFileTypeMPEG4;

```

##录制MP4格式
**初始化**

```
///1. 初始化捕捉会话，数据的采集都在会话中处理
    [self setUpInit];
    ///2. 设置视频的输入输出
    [self setUpVideo];
    
    ///3. 设置音频的输入输出
    [self setUpAudio];
    
    ///4. 视频的预览层
    [self setUpPreviewLayerWithType:type];
    
    ///5. 开始采集画面
    [self.session startRunning];
    
    /// 6. 初始化writer， 用writer 把数据写入文件
    [self setUpWriter];
    - (void)setUpWriter
   {
    self.videoUrl = [[NSURL alloc] initFileURLWithPath:[self createVideoFilePath]];
    self.writeManager = [[AVAssetWriteManager alloc] initWithURL:self.videoUrl viewType:_viewType];
    self.writeManager.delegate = self; 
  }
```
**开始录制**。。。。。

**AVCaptureVideoDataOutput && AVCaptureAudioDataOutput：AVCaptureVideoDataOutput主要输出捕获的未压缩或压缩的视频捕获的帧，AVCaptureAudioDataOutput主要输出捕获的非压缩或压缩的音频样本。**
 
 **AVAssetWriter：AVAssetWriter负责将媒体数据写入到文件。AVAssetWriter对象可以规定写入媒体文件的格式（如QuickTime电影文件格式或MPEG-4文件格式）。创建AVAssetWriter对象需要传入的参数包括文件的输出路径URL和文件格式。**
 
**设置写入视频的属性** 


self.assetWriter = [AVAssetWriter assetWriterWithURL:self.videoUrl fileType:AVFileTypeMPEG4 error:nil];
    //写入视频大小
    NSInteger numPixels = self.outputSize.width * self.outputSize.height;
    //每像素比特
    CGFloat bitsPerPixel = 4.0;
    //10.1相当于AVCaptureSessionPresetHigh，数值越大，显示越精细
    NSInteger bitsPerSecond = numPixels * bitsPerPixel;
    
    //视频压缩的参数
    // AVVideoAverageBitRateKey：视频尺寸*每像素比特率
    //AVVideoExpectedSourceFrameRateKey:
    //AVVideoMaxKeyFrameIntervalKey:关键帧最大间隔，1为每个都是关键帧，数值越大压缩率越高
    //AVVideoProfileLevelKey:默认选择AVVideoProfileLevelH264BaselineAutoLevel -对于压缩只需要控制比率就可以了
    
    NSDictionary *compressionProperties = @{ AVVideoAverageBitRateKey : @(bitsPerSecond),
                                             AVVideoExpectedSourceFrameRateKey : @(30),
                                             AVVideoMaxKeyFrameIntervalKey : @(30),
                                             AVVideoProfileLevelKey : AVVideoProfileLevelH264BaselineAutoLevel };
//视频属性
    NSInteger  height = 736;

    NSInteger width = 414;

  }


  //AVVideoCodecKey:编码格式，一般选h264,硬件编码
  // AVVideoScalingModeKey:填充模式
  //AVVideoWidthKey:视频宽度
  //AVVideoHeightKey:视频高度
  //AVVideoCompressionPropertiesKey:压缩参数
    self.videoCompressionSettings = @{ AVVideoCodecKey : AVVideoCodecH264,
                                       AVVideoScalingModeKey : AVVideoScalingModeResizeAspectFill,
                                       AVVideoWidthKey : @(height),
                                       AVVideoHeightKey : @(width),
                                       AVVideoCompressionPropertiesKey : compressionProperties };

    _assetWriterVideoInput = [AVAssetWriterInput assetWriterInputWithMediaType:AVMediaTypeVideo outputSettings:self.videoCompressionSettings];
    //expectsMediaDataInRealTime 必须设为yes，需要从capture session 实时获取数据
    _assetWriterVideoInput.expectsMediaDataInRealTime = YES;
    _assetWriterVideoInput.transform = CGAffineTransformMakeRotation(M_PI / 2.0);
    
    //视频比特率就是1秒传送多少比特的视频二进制数据。音频比特率就是1秒传送多少音频二进制数据量，间接控制音频质量。
    
    // 音频设置
    //AVEncoderBitRatePerChannelKey:标示每条通道的音频比特率。
   // AVSampleRateKey:音频采样率
    //kAudioFormatMPEG4AAC:音频格式
    //AVNumberOfChannelsKey：音乐通道数  
    self.audioCompressionSettings = @{ AVEncoderBitRatePerChannelKey : @(28000),
                                       AVFormatIDKey : @(kAudioFormatMPEG4AAC),
                                       AVNumberOfChannelsKey : @(1),
                                       AVSampleRateKey : @(22050) };
    
    
    _assetWriterAudioInput = [AVAssetWriterInput assetWriterInputWithMediaType:AVMediaTypeAudio outputSettings:self.audioCompressionSettings];
    _assetWriterAudioInput.expectsMediaDataInRealTime = YES;

###视频压缩  I帧  P帧 B帧
为什么要压缩
未经压缩的数字视频的数据量巨大，存储困难，一张DVD只能存储几秒钟的未压缩数字视频。

视频压缩中，每帧代表一幅静止的图像。而在实际压缩时，会采取各种算法减少数据的容量，其中I P B就是最常见的。

I帧特点：I帧不需要参考其他画面而生成; I帧是帧组GOP的基础帧(第一帧),在一组中只有一个I帧; I帧是P帧和B帧的参考帧(其质量直接影响到同组中以后各帧的质量);
P帧：P帧表示的是这一帧跟之前的一个关键帧（或P帧）的差别，解码时需要用之前缓存的画面叠加上本帧定义的差别，生成最终画面。
B帧：B帧是双向差别帧，通过前后画面的与本帧数据的叠加取得最终的画面。B帧压缩率高
 注：I、B、P各帧是根据压缩算法的需要，是人为定义的,它们都是实实在在的物理帧
 
###视频缓存处理
拍摄完成视频之后 ，缓存到本地，上传wos后获取视频url上传服务器，将url进行MD5加密之后，替换本地url。点击列表播放的时候，先从本地缓存去获取，如果有，直接播放，如果本地没有，就从网络下载。



