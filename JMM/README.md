
## JMM分享小结

本期分享主要讲解了JMM的原理并举例。讲解过程中遇到了部分问题，现将其总结。

### 怎样理解Thread的join方法？

```java
 static int x = 0, y = 0;
 static int a = 0, b = 0;
    public static void main(String[] args) throws InterruptedException{
        Thread one = new Thread(new Runnable() {
            @Override
            public void run() {
                a = 1;  // A1
                x = b;  // A2
                try {
                    Thread.sleep(10000);
                    System.out.println("aaaaaaaaaaaaaa");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread two = new Thread(new Runnable() {
            @Override
            public void run() {
                b = 2;  //B1
                y = a;  //B2

                try {
                    Thread.sleep(1000);
                    System.out.println("bbbbbbbbbbbbb");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        one.start();
        two.start();
        one.join();
        two.join();
        System.out.println("x =" + x + ";" + "y =" + y);

    }
```

thread.join方法是阻塞直到thread运行完死亡，具体的讲解请参考：[StackOverFlow相关解释](https://stackoverflow.com/questions/15956231/what-does-this-thread-join-code-mean)，这里的是否调用join对最后的结果没有任何影响，因为int是值类型的，不是引用类型，所以在y = a;之后即便是a = 1;这时y的值也不会变为1。

### Cache中的数据何时刷入Main Memory？

一致性的粒度是块状的，这个粒度在1到64个字节之间。理论上来说一致性可以以每次的加载和存储的粒度。然而实际上会以整个缓存块作为粒度。也就是说硬件确保的缓存快为粒度进行一致性的。

### ABA问题是什么？

关于ABA问题，以及ABA问题的解决方案可以参考：https://www.cnblogs.com/549294286/p/3766717.html。