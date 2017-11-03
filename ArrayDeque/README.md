## ArrayDeque

1. ArrayDeque 如何确保数组长度为2的n次方
![ArrayDeque1](http://upload-images.jianshu.io/upload_images/1513759-0009d8e651d8fbf9.png)
利用或运算和右移运算，使得initialCapacity的值为2的N次方。设计数组的长度为2的n次方是为循环链表考虑的。如下图所示。
![ArrayDeque2](http://upload-images.jianshu.io/upload_images/1513759-31b252afd12d480e.png)

2. ArrayDeque 中addFirst() 方法

1).在头部插入数据，头指针向左移动1，即减一，elments.length为2的n次方。所以elements.length-1的二进制为1111...1111。
2).特殊情况：假设数组的长度为16，初始化时head为0，那么(head - 1) & (elements.length - 1)就是-1 & 15的值。所以当首次向队列头部插入数据时，head在数组的末尾。

3. 线程安全的无锁环形队列
   具体可参考：http://ifeve.com/disruptor/
 
4. CAS:每次从内存中读取数据然后将此数据和+1后的结果进行CAS操作，如果成功就返回结果，否则重试直到成功为止。compareAndSet利用JNI来完成Java的非阻塞算法。
![ArrayDeque3](http://upload-images.jianshu.io/upload_images/1513759-d80d7e9a314bc573.png)

ValueOffset:是变量值在内存中的偏移地址。Unsafe就是根据内存偏移地址获取数据的原值的。
compareAndSwapInt方法可以看成如下代码形式，能更好的理解。
if (this == expect) {
  this = update
  return true;
} else {
return false;
}
 
缓存一致性：http://www.infoq.com/cn/articles/cache-coherency-primer


