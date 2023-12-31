#!/bin/bash

#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh 执行脚本.sh [port|mount|monitor|base|start|stop|stopall|rm|rmiNoneTag]"
    exit 1
}

#开启所需端口(生产环境不推荐开启)
port(){
    # mysql 端口
    firewall-cmd --add-port=3306/tcp --permanent
    # redis 端口
    firewall-cmd --add-port=6379/tcp --permanent
    # minio api 端口
    firewall-cmd --add-port=9000/tcp --permanent
    # minio 控制台端口
    firewall-cmd --add-port=9001/tcp --permanent
    # 监控中心端口
    firewall-cmd --add-port=9100/tcp --permanent
    # 任务调度中心端口
    firewall-cmd --add-port=9900/tcp --permanent
    # nacos端口
    firewall-cmd --add-port=8848/tcp --permanent
    firewall-cmd --add-port=9848/tcp --permanent
    firewall-cmd --add-port=9849/tcp --permanent
    # sentinel端口
    firewall-cmd --add-port=8718/tcp --permanent
    # seata端口
    firewall-cmd --add-port=8091/tcp --permanent
    # 重启防火墙
    service firewalld restart
}

##放置挂载文件
mount(){
    #挂载 nginx 配置文件
    if test ! -f "/docker/nginx/conf/nginx.conf" ;then
        mkdir -p /docker/nginx/conf
        cp nginx/nginx.conf /docker/nginx/conf/nginx.conf
    fi
    #挂载 redis 配置文件
    if test ! -f "/docker/redis/conf/redis.conf" ;then
        mkdir -p /docker/redis/conf
        cp redis/redis.conf /docker/redis/conf/redis.conf
    fi
    #挂载 nacos 配置文件
    if test ! -f "/docker/nacos/conf/custom.properties" ;then
        mkdir -p /docker/nacos/conf
        cp nacos/custom.properties /docker/nacos/conf/custom.properties
    fi
    #挂载 seata 配置文件
    if test ! -f "/docker/seata/conf/registry.conf" ;then
        mkdir -p /docker/seata/conf
        cp seata/registry.conf /docker/seata/conf/registry.conf
    fi
}

#启动基础模块
base(){
    docker-compose up -d mysql nacos seata-server nginx-web redis minio
}

#启动监控模块
monitor(){
    docker-compose up -d euler-monitor sentinel euler-xxl-job-admin
}

#启动程序模块
start(){
    docker-compose up -d euler-gateway euler-auth euler-system euler-resource
}

#停止程序模块
stop(){
    docker-compose stop euler-gateway euler-auth euler-system euler-resource
}

#关闭所有模块
stopall(){
    docker-compose stop
}

#删除所有模块
rm(){
    docker-compose rm
}

#删除Tag为空的镜像
rmiNoneTag(){
    docker images|grep none|awk '{print $3}'|xargs docker rmi -f
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"port")
    port
;;
"mount")
    mount
;;
"base")
    base
;;
"monitor")
    monitor
;;
"start")
    start
;;
"stop")
    stop
;;
"stopall")
    stopall
;;
"rm")
    rm
;;
"rmiNoneTag")
    rmiNoneTag
;;
*)
    usage
;;
esac
