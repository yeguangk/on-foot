# 存储引擎
存储引擎包含存储数据，建立索引，数据更新查询、事务机制等技术。存储引擎是基于表存储，所以存储引擎也可以称为表类型。
MySQL 的主流存储引擎有 InnoDB、MyISAM、Memory;<br/> 
InnoDB 是支持事务的存储引擎，它缓存数据和索引信息，MyISAM 不支持事务，缓存的是索引。在读取数据上 MyISAM 比 InnoDB 慢，
可以设置 InnoDB 的 innodb_flush_log_at_try_commit = 2 提高读取性能<br/>
  - 查询等待锁的时间超时：innodb_lock_wait_timeout 参数里设置的时间默认是 50s 
  - 为了确定在 InnoDB 中一个请求是否阻塞，可以执行 SHOW ENGINE INNODB STATUS 命令，该命令是 InnoDB 监控器机制的
  一部分，在并发事务分析中尤为重要
  
## 多线程主从复制导致数据不一致，INSERT DELAYED


## MyISAM 和 InnoDB 区别：
   
   1、MyISAM类型的表强调的是性能，但是不支持事务、及外部键等高级功能。
   
   MySQL默认采用的是MyISAM。
   MyISAM不支持事务，所以MyISAM就不存在事务隔离级别了，而InnoDB支持，所有有4种事务隔离级别，InnoDB的AUTOCOMMIT默认是打开的，即每条SQL语句会默认被封装成一个事务，自动提交，这样会影响速度，所以最好是把多条SQL语句显示放在begin和commit之间，组成一个事务去提交。
   InnoDB支持数据行锁定，MyISAM不支持行锁定，只支持锁定整个表。即MyISAM同一个表上的读锁和写锁是互斥的，MyISAM并发读写时如果等待队列中既有读请求又有写请求，默认写请求的优先级高，即使读请求先到，所以MyISAM不适合于有大量查询和修改并存的情况，那样查询进程会长时间阻塞。因为MyISAM是锁表，所以某项读操作比较耗时会使其他写进程饿死。
   InnoDB支持外键，MyISAM不支持。
   InnoDB的主键范围更大，最大是MyISAM的2倍。
   InnoDB不支持全文索引，而MyISAM支持。全文索引是指对char、varchar和text中的每个词（停用词除外）建立倒排序索引。MyISAM的全文索引其实没啥用，因为它不支持中文分词，必须由使用者分词后加入空格再写到数据表里，而且少于4个汉字的词会和停用词一样被忽略掉。
   MyISAM支持GIS数据，InnoDB不支持。即MyISAM支持以下空间数据对象：Point,Line,Polygon,Surface等。
   没有where的count(\*)使用MyISAM要比InnoDB快得多。因为MyISAM内置了一个计数器，count(\*)时它直接从计数器中读，而InnoDB必须扫描全表。所以在InnoDB上执行count(*)时一般要伴随where，且where中要包含主键以外的索引列。为什么这里特别强调“主键以外”？因为InnoDB中primary index是和raw data存放在一起的，而secondary index则是单独存放，然后有个指针指向primary key。所以只是count(*)的话使用secondary index扫描更快，而primary key则主要在扫描索引同时要返回raw data时的作用较大。
   MyISAM和InnoDB锁的不同： https://blog.csdn.net/zhanghongzheng3213/article/details/51753189
   2、并发
   
   MyISAM读写互相阻塞：不仅会在写入的时候阻塞读取，MyISAM还会在读取的时候阻塞写入，但读本身并不会阻塞另外的读
   
   InnoDB 读写阻塞与事务隔离级别相关
   
   3、场景选择
   
### MyISAM
   
   不需要事务支持（不支持）
   并发相对较低（锁定机制问题）
   数据修改相对较少（阻塞问题），以读为主
   数据一致性要求不是非常高
   尽量索引（缓存机制）
   调整读写优先级，根据实际需求确保重要操作更优先
   启用延迟插入改善大批量写入性能
   尽量顺序操作让insert数据都写入到尾部，减少阻塞
   分解大的操作，降低单个操作的阻塞时间
   降低并发数，某些高并发场景通过应用来进行排队机制
   对于相对静态的数据，充分利用Query Cache可以极大的提高访问效率
   MyISAM的Count只有在全表扫描的时候特别高效，带有其他条件的count都需要进行实际的数据访问
   
### InnoDB 
   需要事务支持（具有较好的事务特性）
   行级锁定对高并发有很好的适应能力，但需要确保查询是通过索引完成
   数据更新较为频繁的场景
   数据一致性要求较高
   硬件设备内存较大，可以利用InnoDB较好的缓存能力来提高内存利用率，尽可能减少磁盘 IO
   主键尽可能小，避免给Secondary index带来过大的空间负担
   避免全表扫描，因为会使用表锁
   尽可能缓存所有的索引和数据，提高响应速度
   在大批量小插入的时候，尽量自己控制事务而不要使用autocommit自动提交
   合理设置innodb_flush_log_at_trx_commit参数值，不要过度追求安全性
   避免主键更新，因为这会带来大量的数据移动
   
   4、其它细节
   1）InnoDB 中不保存表的具体行数，注意的是，当count(*)语句包含 where条件时，两种表的操作是一样的
   2）对于AUTO_INCREMENT类型的字段，InnoDB中必须包含只有该字段的索引，但是在MyISAM表中，可以和其他字段一起建立联合索引， 如果你为一个表指定AUTO_INCREMENT列，在数据词典里的InnoDB表句柄包含一个名为自动增长计数器的计数器，它被用在为该列赋新值。自动增长计数器仅被存储在主内存中，而不是存在磁盘
   3）DELETE FROM table时，InnoDB不会重新建立表，而是一行一行的删除
   4）LOAD TABLE FROM MASTER操作对InnoDB是不起作用的，解决方法是首先把InnoDB表改成MyISAM表，导入数据后再改成InnoDB表，但是对于使用的额外的InnoDB特性(例如外键)的表不适用
   5）如果执行大量的SELECT，MyISAM是更好的选择，如果你的数据执行大量的INSERT或UPDATE，出于性能方面的考虑，应该使用InnoDB表
   5、为什么MyISAM会比Innodb 的查询速度快
   
   InnoDB 在做SELECT的时候，要维护的东西比MYISAM引擎多很多；
   1）InnoDB 要缓存数据和索引，MyISAM只缓存索引块，这中间还有换进换出的减少
   2）innodb寻址要映射到块，再到行，MyISAM记录的直接是文件的OFFSET，定位比INNODB要快
   3）InnoDB 还需要维护MVCC一致；虽然你的场景没有，但他还是需要去检查和维护
   
   MVCC ( Multi-Version Concurrency Control )多版本并发控制
   InnoDB ：通过为每一行记录添加两个额外的隐藏的值来实现MVCC，这两个值一个记录这行数据何时被创建，另外一个记录这行数据何时过期（或者被删除）。但是InnoDB并不存储这些事件发生时的实际时间，相反它只存储这些事件发生时的系统版本号。这是一个随着事务的创建而不断增长的数字。每个事务在事务开始时会记录它自己的系统版本号。每个查询必须去检查每行数据的版本号与事务的版本号是否相同。让我们来看看当隔离级别是REPEATABLE READ时这种策略是如何应用到特定的操作的
   SELECT InnoDB必须每行数据来保证它符合两个条件
   1、InnoDB必须找到一个行的版本，它至少要和事务的版本一样老(也即它的版本号不大于事务的版本号)。这保证了不管是事务开始之前，或者事务创建时，或者修改了这行数据的时候，这行数据是存在的。
   2、这行数据的删除版本必须是未定义的或者比事务版本要大。这可以保证在事务开始之前这行数据没有被删除。
