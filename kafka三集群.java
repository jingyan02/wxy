#kafka三集群

cd kafka_2.12-3.0.0/config
vi server.properties

# 节点1
# 群中每个节点的唯一标识
broker.id=0
# 监听地址
listeners=PLAINTEXT://master:9092
# 数据的存储位置
log.dirs=/home/Kafka/data
# Zookeeper连接地址
zookeeper.connect=master:2181,slave1:2181,slave2:2181

# 节点2
broker.id=1
listeners=PLAINTEXT://slave1:9092
log.dirs=/home/Kafka/data
zookeeper.connect=master:2181,slave1:2181,slave2:2181

# 节点3
broker.id=2
listeners=PLAINTEXT://slave2:9092
log.dirs=/home/Kafka/data
zookeeper.connect=master:2181,slave1:2181,slave2:2181


# 启动

# 三节点分别运行启动
cd /home/Kafka/kafka_2.12-3.0.0
bin/kafka-server-start.sh -daemon config/server.properties 


#测试

# 三节点分别运行启动
cd /home/Kafka/kafka_2.12-3.0.0
bin/kafka-server-start.sh -daemon config/server.properties 
