## CodeReview

本期我们尝试根据阿里的代码规范进行CodeReview。<br>
主要由以下几个特点：

### 匿名

Review的文件是匿名的，所以不用担心被Review的人会不好意思。

### 提前发出Code

代码提前发出，大家都先看，然后有个心理准备。主Reviewer提前看代码，做到心中有数。

### 改后直接提交Git

Review过程中，直接修改代码，然后提交，然后就会对该次Review有一个详细的记录。

### TODO

前期大家都抽时间来修改，每个人都来修改，然后给仓库的持有者提PR，交流过程中可以分别拿出来看。CodeReview的氛围可以更加轻松些。

### 本次技术交流发现的问题

#### 代码不规范 

代码规范的问题，比如操作符如:`+,-,*/`的左右添加空格，关键字前后加空格等。

#### 条件语句嵌套太深

还有就是`if` `else`嵌套造成的逻辑不清晰，这时使用`卫语`将不满足条件的情况统一返回，然后再处理正常的业务逻辑。

>在if else过多并且日后可能再添加条件的话，若条件对等可以考虑状态模式或者策略模式，如果条件是范围的，那么可以考虑使用责任链模式。

#### 对异常的处理

异常如果在Controller中统一处理，可能会丢失异常发生的详细信息，并且这个信息不能及时反馈给客户端，这就容易造成不必要的麻烦，所以，最好在具体的业务中处理异常，并且把异常告诉客户端，这样便于排查问题，也可以减少不必要的沟通。

#### 技术细节

某些技术细节需要注意，比如Array的遍历：

```java
   List<String> someList = new List<String>();
        someList.add("aaaaa");
        someList.add("bbbbb");
        
        // ......
        for (int i = 0; i< someList.size(); i ++) {

           // do some thing 
        }
```   

这时`someList.size()`在每次循环都会调用，造成不必要的性能损耗，这时写成下面这样会好些。

```
  List<String> someList = new List<String>();
        someList.add("aaaaa");
        someList.add("bbbbb");
        
        // ......
        for (int i = 0,j = someList.size(); i< j; i ++) {

           // do some thing 
        }
```

另外，如果要拼接字符串，最好不要使用"+"的形式，而使用`StringBuilder`的形式，具体原因：
[查看这个连接。](http://www.cnblogs.com/A_ming/archive/2010/04/13/1711395.html)




