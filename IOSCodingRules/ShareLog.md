# 移动端技术交流总结

### 一、IOS编程规范

本次交流内容为IOS编程规范，[交流相关资料](http://7xsbfz.com1.z0.glb.clouddn.com/iOSStandardCodding.pdf)，交流过程中遇到的问题，总结如下：

#### 使用`Switch case`时需注意的几个小点：

  1. 如果`case`后面的内容最好加上`{}`,如果不加的话，那么表达式中的临时变量就会在同一个作用域中，这样在某些情况下，如果跳过了那个定义变量的case，就会出现意向不到的错误。如下所示,最终b输出的是`b=32770`

		      int main()
				{
				 int a ;
				 switch(1){
				 case 0:
				    a =1;
				    int b =2;
				   printf("case 0\n");
				  break;
				 case 1:
				   a = 2;
				   b +=3;
				   printf("b=%d\n",b);
				  break;
				   };
			    }   
		  
  2. 在数据库升级，或者其它某些需要顺序执行，并且不能跳着执行，那么可以把`break`去掉。例如：
		
		
		        switch (dbVersion) { // 获取当前数据库的版本号
		        case -1:
		        {
		            //创建系统消息表
		            [self createSystemMesDB];
		            //NSLog(@"初始化数据库");
		            
		            //创建400电话详情表 & 通话列表
		            [self createPhoneDetailTable];
		            [self createDBinfo];
		            [self setDBVerion:@"1"];
		         
		        }
		        case 1:
		        {
		           //第二版升级数据库需要变更时
		            [self createSpecialClientImMessageTable];
		            [self setDBVerion:@"2"];
		        }
		        case 2:
		        {
		            //下一版需要修改数据库时
		            [self createQuickReplyMessageTable];
		            //创建用户备注的表
		            [self creatRemarkMessageTable];
		            [self setDBVerion:@"3"];
		            
		            
		        }
		        case 3:
		        {
		            //第三版本需要改动的字段
		        }

 这种方式，可以解决连续版本升级也可以解决跨版本升级带来的数据库升级问题。


##### 有副作用的方法

如果一个方法改变了方法外的某个变量，难么就称该方法有`副作用`（side effect），有副作用的方法最好不要有返回值，如果有了返回值，那么这个方法的作用就不是唯一的，该方法的名字就说明不了该方法的作用。如果外面也用到了这个被改变的变量。很容易造成bug，并且很难被发现。[相关资料请见WIKI百科](https://en.wikipedia.org/wiki/Side_effect_(computer_science)
)

##### 编译器语言`\`

`\`是一种编译器语言，编译器遇到`\`就认为该行结束了，如果某行代码很长，其实可以加`\`，然后将剩下的内容在下一行顶格写即可。


##### IOS中的状态枚举和选项枚举

IOS中有两种枚举形式如下
![IOS中的ENUM](http://7xsbfz.com1.z0.glb.clouddn.com/IOSEnum.png)
第一种枚举是状态枚举，某个该枚举值不会同时有多重数据。第二种枚举可以让某个数据同时具有多种情况（比如，某个同学是班长，数学课代表，有是迅雷VIP会员），这个时候就使用选项枚举。具体的选项个数和电脑的位数有关，如：64位机，最多有64个选项。具体的赋值和使用如下所示：
![如何使用ENUM](http://7xsbfz.com1.z0.glb.clouddn.com/USEEnum.png)


##### 单例的规范写法

在使用单例中为了防止单例被非`sharedInstance`之外方法所创建，可以使用如下的创建方式：

     //.h
	   + (instancetype)sharedInstance;
		- (instancetype)init NS_UNAVAILABLE;
		- (instancetype)new  NS_UNAVAILABLE;
	  //.m
	 + (instancetype)sharedInstance {

	    static HYRobOrderWindowManager *robManager = nil;
	    static dispatch_once_t onceToken;
	    dispatch_once(&onceToken, ^{
	        
	        robManager = [super allocWithZone:NULL];
	               
	    });
	    return robManager;
    }

	+ (instancetype)allocWithZone:(struct _NSZone *)zone {
	
	    return [HYRobOrderWindowManager sharedInstance];
	    
	}


#### 集中常用的大小写问题

1. 宏定义全部用大写：例如苹果的`NS_UNAVAILABLE`
2. static用k开头，如：`kImageThumbnailHeight`
3. entern，使用项目的前缀:`HYCacheControllerDidClearCacheNotification.`


#### IOS中的__kindof

如果方法中参数加上`__kindof`，则表明该方法可以返回这个类及其子类，且不会报警告。如下所示:


    - (__kindof NSArray)getArray{

      return [NSMutableArray ]
    }

加__kindof修饰后，该方法的返回值原本是NSArray，但是方法里边却返回了一个NSArray的子类
NSMutableArray，也就是说，加__kindof修饰后，本类及其子类都是返回，调用使用时也可以使用本类或者本类的子类去接收方法的返回值，调用方法如下，使用NSArray和NSMutableArray来接收返回值都是正确的：

	NSArray *array = [instance getArray];
	NSMUtableArray *mutableArray = [instance getArray];



