# 存储引擎
存储引擎包含存储数据，建立索引，数据更新查询、事务机制等技术。存储引擎是基于表存储，所以存储引擎也可以称为表类型。
MySQL 的主流存储引擎有 InnoDB、MyISAM、Memory;<br/> 
InnoDB 是支持事务的存储引擎，它缓存数据和索引信息，MyISAM 不支持事务，缓存的是索引。在读取数据上 MyISAM 比 InnoDB 慢，
可以设置 InnoDB 的 innodb_flush_log_at_try_commit = 2 提高读取性能<br/>
