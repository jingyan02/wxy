### 不同的GC

minor gc和full gc：

**新生代GC（Minor GC）**：指发生在新生代的垃圾收集动作，因为Java对象大多都具备朝生夕灭的特性，所以Minor GC非常频繁，一般回收速度也比较快。

**老年代GC（Major GC / Full GC）**：指发生在老年代的GC，出现了Major GC，经常会伴随至少一次的Minor GC。

1.**串行**GC

年轻代与老年代的大小之比为1:2

2.**并行**GC

-XX:+UseParallelGC 启用 并行GC，

 -XX:MaxGCPauseMillis=<N> 指定该最大值，

命令行选项 -XX:GCTimeRatio=<N> 来指定吞吐量。

3.**CMS** GC

阶段 1：Initial Mark（初始标记） 

阶段 2：Concurrent Mark（并发标记） 

阶段 3：Concurrent Preclean（并发预清理）

 阶段 4： Final Remark（最终标记） 

阶段 5： Concurrent Sweep（并发清除） 

阶段 6： Concurrent Reset（并发重置）

4.**G1** GC

G1会转入一个Full GC 进行回收：

（1）G1启动标记周期，但在Mix GC之前，老年代就被填满；

（2）G1收集器完成了标记阶段，开始启动混合式垃圾回收，清理老年代的分区，不过，老年代空间在垃圾回收释放出足够内存之前就会被耗尽。

### 堆内存

如果[内存](https://so.csdn.net/so/search?q=内存&spm=1001.2101.3001.7020)碎片化严重，也就是两个对象占用不连续的内存，已有的连续内存不够新对象存放，就会触发GC。

![img](https://imgconvert.csdnimg.cn/aHR0cDovL2kyLjUxY3RvLmNvbS9pbWFnZXMvYmxvZy8yMDE4MDgvMjEvYjExNjE3MDc3MWVjYjMxMTdhZTdmZWFkMDNmY2FhMGQucG5n?x-oss-process=image/format,png)