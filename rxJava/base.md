# RxJava 基础
响应式编程：一种面向数据流和变化传播的编程范式。
 
 响应式编程的特点：
   1. 异步编程，提供了合适的异步编程模型，能够挖掘多核 CPU 的能力、提高效率、降低延迟和阻塞
   2. 数据流，基于数据流模型，响应式编程提供一套统一的 Stream 风格的数据处理接口。与 Java 8 中的 Stream
   相比，响应式编程除了支持静态数据流，还支持动态数据流，并且允许复用和同时接入多个消费者。
   3. 变化传播，以一个数据流为输入，经过一连串操作转化为另一个数据流，然后分发给各个订阅者的过程。
   
RxJava 是 Reative Extension 的 Java 实现，用于通过使用 Observable/Flowable 序列来构建异步和基于事件的程序库，
RxJava 扩展观察者模式以支持数据/事件序列，并添加允许你以声明方式组合序列的操作符，同时提取对低优先级的线程、同步，
线程安全性和并发数据结构等问题的隐藏

## Observable 
   1. OnComplete：Observable 的 OnComplete 只处理 onComplete/onError 事件，不发送数据
   2. Maybe：能够发射 0、1 个数据，要么成功/要么失败
   3. do 操作符，可以给 Observable 的生命周期的各个阶段加上一系列回调监听<br/>
      doOnSubscribe -> doOnLifecycel -> doOnNext -> doOnEach(发送数据) -> subscriber(收到消息)
      -> doAfterNext -> doOnComplete -> doOnEach(onNext、onComplete、onError) -> doFinally(正常/异常终止) -> doAfterTerminal
   4. doOnlifecycle：订阅后可以取消订阅
   5. Hot/Cold Observable <br/>
      Hot Observable：无论有没有观察者进行订阅，事件始终都会发生。某些事件不确定何时发生或者不确定 Observable 发射的元素数量<br/>
      Cold Observable: 只有观察者订阅了，才开始执行发送数据流的代码 —— 事件独立，Observable 使用 just/create/range/fromXXX
       都是创建 Cold Observable
   6. Subscribe 和 Processor <br/>
      Subscribe 和 Processor 功能类似，继承自 Flowable，Processor 支持背压控制(Back Presure)。<br/>
      Processor: 是管制流数据在跨越异步边界进行流数据交换，可以认为元素传递到另一个线程或线程池，同时确保在接收端不是被迫缓冲任意数量的数据，
      背压是通过设置协调线程之间的队列大小进行限制。<br/>
      Reactive Streams 主要目标：
       - 通过异步边界来解耦系统组件，解耦的先决条件，分离事件/数据的发送方和接收方的资源使用。
       - 为背压处理定义一种模型，流处理理想规范是将数据从发布者推送到订阅者，这样发布者就可以快速发布数据，同时通过压力处理来确保速度更快的发布者
       不会对速度较慢的订阅者造成影响，背压处理通过使用流控制来确保操作的稳定性并能实现降级，从而提供弹性讷讷了。
      
   7. Subject 既是 Observable 又是 Observer
      Subject 作为观察者，可以订阅目标 Cold Observable，使对方开始发送事件，同时它又作为 Observable 转发或者发送新的事件，让 Cold Observable
      借助 Subject 转换为 Hot Observable。Subject 不是线程安全的，如果想要其线程安全，则需要调用 toSerialized() 方法 <br/>
      Subject 分成 4 类， AsyncSubject、BehaviorSubject、ReplaySubject、PublishSubject；
        - AsyncSubject：Observer 会接收 AsyncSubject 的 onComplete 之前的最后一个数据
        - BehaviorSubject:被订阅之前没有发送任何数据，则会发生一个默认数据。因为 BehaviorSubject 每次只会发射调用 subscribe 方法之前的最后一个事件，
          和调用 subscribe 方法之后的事件。
        - ReplaySubject：会发射所有来自原始 Observable 的数据给观察者，无论它们是何时订阅的。create(), createWithSize(缓存数),
        createWithTime()
        - PublishSubject: Observer 只接收 PublishSubject 被订阅之后发送的数据。
        - 可能错过的事件：Subject 可以不停的调用 onNext 来发送时间，直到遇到 onComplete 才会结束。subscribeOn() 将 Subject 切换到 I/O 线程，
      
   8. Hot Observable 转换为 Clod Observable <br/>
      (1). ConnetableObservable 的 refCount。RefCount 把从一个可连接的 Observable 链接和断开的过程自动化了，它操作一个可连接的 Observable，
      返回一个普通 Observable。当第一个订阅者/观察者订这个 Observable 时，RefCount 链接到下层的可连接的 Observable，RefCount 跟踪有多少个观察者
      订阅它，知道最后一个观察者完成，才断开与下层可连接 Observable 的链接。share() 对 RefCount 进行封装
   9. Flowable <br/>
      Flowable 可以看成 Observable 新的实现，它支持背压同时实现 Reactive Streams 的 Publisher 接口。Flowable 所有的操作控制强制支持背压<br/>
      Obeservable 和 Flowable 的使用场景：
      Obeservable: <br/>
        - 处理数据不超过 1000 条数据，并且几乎不会出现内存溢出
        - GUI 鼠标事件，基本不会背压
        - 处理同步流
      Flowable：<br/>
        - 处理以某种方式产生超过 10KB 的元素
        - 文件读取与分析
        - 读取数据库记录，也是一个阻塞的和基于拉取模式
        - 网络 I/O 流
        - 创建一个响应式非阻塞接口
   10. Single、Completable、Maybe <br/>
        - Single: SingleEmitter 只有 onSuccess 和 onError 事件。onSuccess 用于发射数据，而且只能发射一个数据，后面即使再发射数据也不会做任何处理。
       Single 的 SingleObserver 中只有 onSuccess 和 onError, 并没有 onComplete，这也是 Single 与 4 种观察者之间的最大区别。Single 可以通过
       toXXX 方法转换为 Observable、Flowable、Completable 及 Maybe 
        - Completable: CompletableEmitter 创建后不会发送任何数据，只有 onComplete 和 onError 事件。
        - Maybe: 可以看成是 Single 和 Completable的结合，MaybeEmitter 和 SingleEmitter 没有 onNext 方法，同样需要 onSuccess 方法发送数据
   11. Scheduler 线程控制器
       - single 使用定长为 1 的线程池，重复利用这个线程
       - newThread 每次都启用新线程，并在新线程中执行操作
       - computation 使用的固定的线程池，大小为 CPU 核数，适用于 CPU 密集型计算, CPU 密集型
       - tranmpoline 直接再当前线程运行，如果当前线程有其他任务正在执行，则会暂停其他任务
       - Schedulers.from 将 Executor 转换成一个调度实例，即可自定义一个Executor 来作为调度器 <br/>
       Scheduler 是 RxJava 的线程任务调度，Worker 是线程任务的具体执行者，Scheduler 在 scheduleDirect()、schedulePeriodicallyDirect() 方法中
       创建了 Worker，然后会调用 worker 的 schedule()、schedulePeriodically()来执行任务。<br/>
       Schedules ------> Scheduler ------> Worker ------> schedulePeriodically <br/>
       Scheduler:SingleScheduler/ComputationScheduler/IoScheduler/TrampolineScheduler ——> RxThreadFactory <br/>
       Worker：<br/>
        - ScheduleWorker ——> ScheduleExecutorService
        - EventLoopWorker ——> PoolWorker 
        - NewThreadWorker ——> PoolWorker/ScheduleExecutorService
        - TrampolineWorker ——> PriorityBlockingQueue
   12. 默认情况下，Observable 和 Observer 是在同一个线程中处理，可以使用 subscribeOn 和 observeOn 做线程切换。
        - subscribeOn 通过接收一个 Scheduler 参数来指定数据的处理运行在特定的线程调度处理器上，若多次设置 subscribeOn 只会生效一次
        - observeOn 接收一个 Scheduler 参数，用了指定下游操作运行在特定的线程调度器 Scheduler 上，多次执行 observeOn，则每次都起作用，线程会一直切换
  
       
      
     
   
   