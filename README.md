###Object-Pool

对象池是一种典型的应用，其目的在于节约重复创建对象的开销。同时，对象池中的每个Object，需保持`无状态`的特性。

apache提供了一套标准的对象池接口，`PooledObjectFactory`以及`ObjectPool`。


同时Object-Pool其实是一种组合模式，包含`单例`、`工厂`等。

#### Archive

1. 完成一个示例Demo，利用了Semaphore以及ReentrantLock（done）
2. 优化结构，包括运行时Pool状态等（todo）
4. 深刻理解工厂模型及其衍生的模型（todo）
3. 替换apache标准接口（todo）
