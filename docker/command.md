# Docker 命令

- centos 启动 docker: <code> systemctl start docker </code>
- 查看 docker 状态: <code> systemctl status docker </code>
- 动态映射 docker 容器端口到当前服务器 <code> iptables -t nat -A  DOCKER -p tcp --dport 本机端口 -j DNAT --to-destination 容器IP:容器端口 </code>
- 查看docker现在硬盘状态: docker system df