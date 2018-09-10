# Code Review小试

## ICode

ICode使用方法：

### 一、eclips插件方法发起Review

1. [登陆codereview平台](http://newcoder.58corp.com)，登陆sso生成review账号 。评委们也要登陆，否则没有账号收不到review邮件。

2. 点击右侧账号图标，点击设置，选择“组”，点join。
3. 修改本地eclips插件配置
      server ：       http://newcoder.58corp.com 
      User name :  oa账号（比如 luting01）
      password：  aaa123 （所有人都一样，都用这个密码）
4. 发起review，剩下的就和原来一样啦

### 二、用rbtools命令行方式发起review  

1. [登陆codereview平台](ttp://newcoder.58corp.com)，登陆sso生成review账号 。评委们也要登陆，否则没有账号收不到review邮件。

2. 安装rbtools  请参考：https://www.reviewboard.org/downloads/rbtools/#linux 

3. rbt post  -p -d --target-groups testgroup --summary test --description test --target-people admin

具体使用：  http://ishare.58corp.com/index.php/topic/show/783
命令行问题请联系renhongwei

### 三、平台界面发起review

1. [登陆codereview平台](http://newcoder.58corp.com )，登陆sso生成review账号 。
2. 点击 新建审查
3. 选择或上传patch，发起审查

*问题：[ICode中的CodeReview](http://icode.58corp.com)的使用目前只支持SVN，并且每次提交Review操作较为复杂，这有点限制我们的使用。*

## Review

Review的过程中发现了代码规范有以下几点：

* 方法名字尽量要让调用者不开注释就明白意思：尽量不要出现像：`set`这样含义不明的方法名。
* 业务代码中不要写有框架的东西。
* 过期时间等在某个项目中不变的内容，应该放到配置文件中去，这样可以避免赋值错误，也可以避免改动时候修改很多地方。
* 方法接口要更加健壮，以避免调用错误，或者转换错误。

## 问题解决

超时问题定位到方法之后，可以在方法的入口和出口打下日志记录执行时间。
可以用相关工具进行分析。通过添加超时统计包的形式分析。

## 改进点

每中语言定义一套代码规范。
每次Review前要将需要Review的代码给Reviewer看，Review做好标记，然后在一起讨论。
Review的内容应该是项目中可能出错的点，或者代码的作者认为可能出错的点。




