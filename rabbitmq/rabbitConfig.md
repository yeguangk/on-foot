# RabbitMQ 配置文件

## Mnesia 数据库配置
> - dump_log_write_threshold 默认值 10 将仅限追加的日志内容刷出/转储至真实数据库文件的频度

## RabbitMq 配置
> - tcp_listeners  默认值 \[{"0.0.0.0", "5672"}]<br/>
                   定义 RabbitMq 监听的非 SSL 加密通信 IP 和 Port
> - ssl_listeners  默认 空 格式：{"ip", "port"}<br/>
                   定义 RabbitMq 监听的 SSL 加密通信的 IP 和 Port
> - ssl_option     默认值 空 格式：{"键", 值}<br/>
                   SSL 有效选项：cacertfile(CA 证书文件)、certifile(服务器证书文件)、keyfile(服务器密钥文件)、
                   fail_if_no_peer_cert(是否需要客户端安装有效证书 True/False)
> - vm_memory_high_watermark 默认值 0.4 <br/>
                   控制 RabbitMq 允许消耗的内存
> - msg_store_file_size_limit 默认值 16777216  <br/>
                   RabbitMQ 垃圾收集存储内容之前，消息存储数据库的最大大小
> - queue_index_max_journal_entries 默认值 262144 <br/>
                   在转储到消息存储数据库并提交之前，消息存储日志里的最大条目数