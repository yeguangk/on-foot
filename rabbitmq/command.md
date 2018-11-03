# RabbitMq Command

- 启动 Rabbitmq：sbin/rabbitmq-server 或者 后台运行：sbin/rabbitmq -server -detach
- 停止 Rabbitmq 应用程序和节点: rabbitmqctl stop \[-n rabbitmq@hostname] 
- 关闭 Rabbitmq 节点：rabbitmq stop_app 
- 查看状态：sbin/rabbitmqctl status
- basic.consume 自动获取消息
- basic.get 获取单条消息
- basic.reject 消息确认，消费者可以拒绝接收消息，如果设置 reject 的 requeue 设置为 true rabbitmq 将
把消息重新放回队列中重新投递，如果设置为 false 则丢弃该消息。
- queue.declare 声明队列，exclusive 设置 ture 只有一个消费者可以消费，auto-delete 最后一个消费者取消
订阅时，将队列删除 
- basic_publish($msg, '交换器', '路由键')   <- direct exchange
- vhost：rabbitmqctl add_vhost\[hostname] delete_vhost\[hostname] list_vhosts
- 添加用户：sbin/rabbitmqctl add_user userName password
- 删除用户：sbin/rabbitmqctl delete_user userName, 删除用户，该用户的所用访问控制权限也会被删除
- 所有用户：sbin/rabbitmqctl list_users
- 修改密码：sbin/rabbitmqctl change_password userName password

## 统计数据命令

- 所有队列：sbin/rabbitmqctl list_queues -p hostName \<queue info item>
- 所有交换器：sbin/rabbitmqctl list_exchanges -p hostName \<exchange info item>
- 队列和交换器绑定：sbin/rabbitmqctl list_bindings -p hostName
