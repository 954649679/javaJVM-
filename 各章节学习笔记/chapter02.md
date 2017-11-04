### 2.1 概述

### 2.2 运行时数据区域

### 2.3 HotSpot 虚拟机对象探秘

#### 2.3.1 对象的创建

#### 2.3.2 对象的内存布局

在HotSpot虚拟机中，对象在内存中存储的存储布局可以分为3块区域：对象头（Header）、实例
数据（Instance Data)和对齐补充（Padding）

* HotSpot虚拟机的对象头包含两部分：第一部分用于存储对象自身的运行数据（哈希码、GC分代年
龄、锁状态标志、线程持有的锁、偏向线程ID、偏向时间戳等），官方称他为'Mark Word'，另外
一部分是类型指针，既对象指向它的类元数据的指针，虚拟机通过这个指针确定这个对象是哪个类
的实例。

* 接下来的实例数据部分是对象真正存储的有效信息，也是程序代码中所定义的各种类型的字段内容。
无论是从父类继承下来的，还是子类中定义的，都需要记录下来。

* 第三部分对其补充并不是必然存在的，也没有什么特别的含义，它仅仅起着占位符的作用。

#### 2.3.3 对象的访问定位

   建立对象是为了使用对象，我们的Java程序通过栈上的reference数据来错做堆上的具体对象，
目前主流的访问方式有使用句柄很直接指针两种

* 使用句柄的方式：

![使用句柄操作对象](image/%E9%80%9A%E8%BF%87%E5%8F%A5%E6%9F%84%E6%93%8D%E4%BD%9C%E5%AF%B9%E8%B1%A1.jpeg)

* 直接使用指针访问

![使用指针操作对象](image/%E9%80%9A%E8%BF%87%E6%8C%87%E9%92%88%E7%9B%B4%E6%8E%A5%E8%AE%BF%E9%97%AE%E5%AF%B9%E8%B1%A1.jpeg)

这两宗访问方式各有优势，使用句柄来访问最大好处就是在reference中存储的是稳定的句柄，在
对象被移动（垃圾收集是移动对象是非常普遍的行为）时只需要改变句柄中的实例数据的指针，而reference
本身不需要修改。
使用直接指针访问的最大好处就是速度快，他节省了一次指针定位的时间开销。HotSpot就是使用这种
方式进行对象访问的。

### 2.4 实战：OutOfMemoryError 异常

通过学习OutOfMemoryError 异常能够更加的清楚理解各个运行区存储的内容以及在实际工作中当
遇到内存溢出时能够根据异常的信息快速的判断是哪个区域的内存溢出

#### 2.4.1 Java堆溢出

Java堆内存的OOM异常时实际应用中常见的内存溢出情况。

分析异常时需要首先确定是内存泄漏还是内存溢出。如果是内存泄漏需要定位内存泄漏发生的代码
内置，进而解决此问题。如果是内存溢出，则需要判断内存对象是否必须活着，如果必须存活，则
通过设置对参数（-Xmx 和Xms）调大堆内存。如果内存对象是非必须存活的，则尽量缩短对象的
生命周期，减小运行期间内存消耗。

[试试看HeapOOM.java][JVMTest/src/test/HeapOOM.java]

#### 2.4.2 虚拟机栈和本地方法溢出

HotSpot虚拟机并不区分虚拟机栈和本地方法栈，所以对于HotSpot来说，虽然设置-Xoss参数（
是指本地方法栈大小）存在，但是实际上是无效的，栈容量只由-Xss参数设定。

如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常

如果虚拟机在扩展栈是无法申请到足够的内存空间，则将抛出OutOfMemoryError异常

[试试看JavaVMStackSOF.java][JVMTest/src/test/JavaVMStackSOF.java]

#### 2.4.3方法区和运行时常量池溢出

常量池异常错误信息：OutOfMemoryError:PermGen space

我们可以通过设置虚拟机参数 -XX:PermSize 和-XX:MaxPermSize 设置方法区大小同时也就
限制了常量池的容量（运行时常量池是方法区的一部分）

我在运行时控制台输出
`
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize10m; support was removed in 8.0
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize10m; support was removed in 8.0
`

在书中显示java1.6还是支持的。

[试试看RunTimeConstantPoolOOM.java][JVMTest/src/test/RunTimeConstantPoolOOM.java]