# 通用配置
daemonize yes # 后台启动
protected-mode no # 关闭保护模式
bind 0.0.0.0  # 允许任意远程连接
appendonly yes # 开启AOF持久化

# redis_6379.conf    master
# 包含命令，有点复用的意思
port    6379
logfile /var/log/redis_6379.log # 指定日志文件目录
dir /var/lib/redis/6379 # 指定工作目录，不存在则需要创建 `mkdir /var/lib/redis/6379`

# redis_6380.conf    slave1
port    6380
logfile /var/log/redis_6380.log # 指定日志文件目录
dir /var/lib/redis/6380 # 指定工作目录
replicaof 127.0.0.1 6379 # 指定要同步的master节点IP和端口

# redis_6381.conf    slave1
port    6381
logfile /var/log/redis_6381.log # 指定日志文件目录
dir /var/lib/redis/6381 # 指定工作目录
replicaof 127.0.0.1 6379 # 指定要同步的master节点IP和端口

主节点可选配置
requirepass 123456 # 设置密码

从节点可选配置
masterauth 123456 # 从节点访问主节点的密码(必须和requirepass 一致)【没有可不配置】
replica-read-only yes # 从节点只读模式



启动节点

# 创建软连接 `/usr/local/redis/redis-server` 目录为redis安装目录
ln -s /usr/local/redis/bin/redis-server /usr/bin/redis-server
ln -s /usr/local/redis/bin/redis-cli /usr/bin/redis-cli

# 顺序启动节点
$ redis-server redis_6379.conf
$ redis-server redis_6380.conf
$ redis-server redis_6381.conf

# 进入redis 客户端，开多个窗口查看方便些
$ redis-cli -p 6379
$ info replication

