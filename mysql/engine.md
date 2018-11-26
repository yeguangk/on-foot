# 存储引擎
存储引擎包含存储数据，建立索引，数据更新查询、事务机制等技术。存储引擎是基于表存储，所以存储引擎也可以称为表类型。
MySQL 的主流存储引擎有 InnoDB、MyISAM、Memory;<br/> 
InnoDB 是支持事务的存储引擎，它缓存数据和索引信息，MyISAM 不支持事务，缓存的是索引。在读取数据上 MyISAM 比 InnoDB 慢，
可以设置 InnoDB 的 innodb_flush_log_at_try_commit = 2 提高读取性能<br/>
  - 查询等待锁的时间超时：innodb_lock_wait_timeout 参数里设置的时间默认是 50s 
  - 为了确定在 InnoDB 中一个请求是否阻塞，可以执行 SHOW ENGINE INNODB STATUS 命令，该命令是 InnoDB 监控器机制的
  一部分，在并发事务分析中尤为重要
  
