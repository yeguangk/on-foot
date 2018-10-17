# zookeeper docker 搭建

# 镜像拉取

> 查询 docker zookeeper 镜像: `docker search zookeeper` <br/>
> 拉取 wurstmeister/zookeeper 镜像： `docker pull wurstmeister/zookeeper` <br/>
> [Dockfile 地址](https://github.com/wurstmeister/zookeeper-docker) <br/>
> 

## 单机构建
> 
> docker 启动命令：`docker run -d --name zookeeper -p 2181:2181 -t wurstmeister/zookeeper` <br/>
> 挂载配置文件 启动命令： `docker run -tid --name=zookeeper --restart=always -p 2181:2181 -v /opt/kafka_cluster/zookeeper/conf:/opt/zookeeper-3.4.9/conf -v /opt/kafka_cluster/zookeeper/data:/opt/data/zookeepeer-3.4.9/data -v /opt/kafka_cluster/zookeeper/datalog:/opt/data/zookeeper/log zookeeper` <br/>

## 集群构建
> 使用 docker-compose.yml 进行配置
> 伪集群配置文件 [docker-compose.yml](./docker/docker-compose.yml) <br/>
> 
>     配置文件说明: 
>                  docker 镜像：wurstmeister/zookeeper
>                  docker 容器: zoo1/zoo2/zoo3
>                  端口映射: 2181:2181/2182:2181/2183:2181
>                  zookeeper my_id: ZOO_MY_ID  值范围：1-255
>                  zookeeper 集群配置 ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888
>
 
## 详细说明
> 
> wurstmeister/zookeeper 容器的 zookeeper 目录：/opt/zookeeper-3.4.9/ 
  配置文件的自定义及数据目录挂载都可以在挂载到当前目录中 <br/>
> 
> zookeeper [官网地址](https://zookeeper.apache.org/) <br/>
> 


