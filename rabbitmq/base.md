# RabbitMq 高效部署分布式队列

## 消息通信

### 信道
   生产者和消费者通过 TCP 链接 Rabbitmq，当TCP 链接打开，应用程序就可以创建一条 TCP 链接内的
具有唯一标识 ID 虚拟链接 AMQP 信道，这样命令就可以在 AMQP 的信道中传播。<br/>
   我们知道 TCP 是传输数据报协议，具有传输数据的能力，为什么不直接通过 TCP 直接传输 AMQP 数据？<br>
   主要原因：TCP 的建立和销毁开销非常大，系统 TCP 链接数量有限。

### 虚拟主机 交换器 队列 路由器 绑定
   AMQP 消息必须包含三部分：交换器、队列、绑定。生产者把消息发送到交换器上，路由器通过绑定的键，把消息
发送到对应的队列上.  
![AMQP 组成](./image/mq-AMQP.jpeg)
   消费者消费消息可以通过 basic.consume 和 basic.get，basic.consum 自动拉取消息实现高吞吐量，
basic.get 获取单条信息。mq 投递消息到队列，如果没有消费者订阅消息，消息将在队列中保存，如果有多个消费
者订阅，Rabbitmq 将以轮询的方式发送消息。Rabbitmq 对于发送的消息必须接收到消费者的消息确认后，进行
删除，否则 Rabbitmq 将在一段时间后认为消息未投递，进行再次发送。 <br/>
   direct exhange: 关注 payload(消息内容)、交换器、路由器  <br/>
   topic exchange: 关注 路由键，'.' 把路由键分为几部分，'*' 匹配特定位置的任意文本，'#' 匹配所有规则 <br/>
   多租户：*vhost* <br/>
   多租户特点: 数据隔离、安全、可移植性强。在 Rabbitmq 集群中创建 vhost 会同步整个集群
  
### 持久化
   队列的 durable 设置为 true. 要持久化一个消费需要保证：投递模式设置为 2、发送到持久化队列、到达持久化队列。
如果在持久化消息时服务器宕机了，消息丢失，却无法进行确认，需要 AMQP 的事务支持，事务十分影响性能。所以Rabbitmq
采用信道 confirm 模式确认消息投递，当信道设置成 confirm 模式，发布的每一条消息都会获得唯一的 ID 

## 管理
   RabbitMq 的日志目录：/var/log/rabbitmq <br/>
   Rabbitmq 的配置文件目录: /etc/rabbitmq.confg, 节点启动可以通过 rabbitmq-server 脚本对 CONFIG_FILE 设置，
配置文件是一个包含嵌套哈希表的数组。<br>
   <code> \[ <br/>
             { mnesia, \[{dump_log_write_threshold, 100}] } <br/>
             { rabbit, \[{vm_memory_high_watermark, 0.4}]} <br/>  
  </code> <br/>
  - mnesia 是 Mnesia 数据库配置用了存储 exchange 和 queue 元数据, dump_log_write_threshold:
  把 Mnesia 条目从仅限追加的文件刷出到真实数据文件的频度。Mensia 通过将 RabbitMq 元数据首先写入一个仅限追加的日志
  文件，以确保完整性，然后在定期将日志内容转储到真实的 Mnsia 数据库文件。 
  - rabbit 是 rabbit 的配置
  
## 权限控制
   权限控制组成：被授予权限的用户、权限控制 vhost、需要授予的读/写/配置权限的组合、权限范围。
   eg: sbin/rabbitmqctl set_permissions -p hostName userName ".*"(配置)  ".*"(写)  ".*"(读)
       sbin/rabbitmqctl clear_permission -p hostName userName
   
## 统计数据
   [命令](command.md)
   
## 日志

   ### 日志文件   
   *rabbitmq 日志信息可以通过 AMQP exchange queue binging 获取* <br/>
   日志路径：配置 LOG_BASE 环境变量， 默认值在 rabbitmq-server 脚本 LOG_BASE=/var/log/rabbitmq
   在该文件夹下会创建两个文件：<br/>
      RABBITMQ_NODENAME-sasl.log: 系统应用程序支持库 <br/>
      RABBITMQ_NODENAME.log: <br/>
   RABBITMQ_NODENAME 指 \_rabbit@localhost_ 或者 rabbit <br/>
   
   ### 轮换日志
    sbin/rabbitmqctl rotate_logs suffix
   
   ### 通过 AMQP 实时访问日志
   RabbitMq 利用 amq.rabbitmq.log topic exchange 把日志发布到交换器上，并以严重级别作为路由键：error、warning、info
    
   
   
    
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   