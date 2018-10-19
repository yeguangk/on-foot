# docker 遇到问题

## docker 启动失败

---

      Error starting daemon: SELinux is not supported with the overlay2 graph driver

   描述：此linux的内核中的 SELinux 不支持 overlay2 graph driver
   
   解决方法：
   
   - 启动一个新内核 
   - docker 禁用 selinux： --selinux-enabled=false vi /etc/sysconfig/docker <code> OPTIONS='--selinux-enabled=false --log-driver=journald --signature-verification=false' </code>
   
---

    

--- 