#命令
- 查看硬盘的操作：iostat -d -x -k 1
- 查看 磁盘信息：df -h
- 查看 文件夹的大小：du -h -x --max-depth=1
- 增加套接字缓冲区数量：
    net.core.rmem_default 和 net.core.rmem_max(默认值 128K)，对于大多数环境而言，16M足够了，可以在 /etc/sysctl.conf 中修改此值。
- centos /dev/mapper/cl-root 100% 
  查看 大文件：find / -type f -size +1024000k -exec du -h {} \;
- 查看 TCP 链接：netstat -tnp | grep 11211 | grep ESTABLISHED | grep - | wc -l
- 查看 TCP TIME_WAIT 状态：netstat -ae|grep "TIME_WAIT"
- 查看 socket 信息：netstat -lnp|grep 8657
- TCP 链接状态：netstat -nat|awk '{print $6}'|sort|uniq -c|sort -rn   
- 设置TCP 端口链接：/proc/sys/net/core/somaxconn
