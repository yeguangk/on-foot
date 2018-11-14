- 在测试环境中启动 consul 把日志输出到文件同时走 debug 模式，导致 /dev/mapper/centos-root 100%。
解决该问题思路：
   1. 查看 磁盘信息：df -h <br/>
     /dev/mapper/centos-root 关联的目录 <code>cd / , du -h -x --max-depth=1 </code> 删除占用磁盘大的数据
   2. 增大 容量：[链接](https://blog.csdn.net/e_wsq/article/details/79531493)
    