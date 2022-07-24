import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class AppProducer {
    private static final String url = "tcp://localhost:61616";
    private static final String queueName = "queue-test";

    public static void main(String[] args) throws JMSException {
        //1.创建ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        //2.创建连接
        Connection connection = connectionFactory.createConnection();

        //3.启动连接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //5.创建一个目标
        Destination destination = session.createQueue(queueName);

        //6.创建一个生产者
        MessageProducer producer = session.createProducer(destination);

        for (int i = 0; i < 100; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("test" + i);
            producer.send(textMessage);
            System.out.println("发送消息：" + textMessage.getText());
        }

        //8.关闭连接
        connection.close();
    }
}
　　（3）实现消费者（队列模式）

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class AppConsumer {
    private static final String url="tcp://localhost:61616";
    private  static final String queueName="queue-test";
    public static void main(String[] args) throws JMSException {
        //1.创建ConnectionFactory
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory(url);

        //2.创建连接
        Connection connection = connectionFactory.createConnection();

        //3.启动连接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //5.创建一个目标
        Destination destination = session.createQueue(queueName);

        //6.创建一个消费者
        MessageConsumer consumer=session.createConsumer(destination);

        //7.创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
              TextMessage textMessage= (TextMessage) message;
                try {
                    System.out.println("接收消息:"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
　　（4）ActiveMQ客户端查看队列模式

　　

　　（5）实现生产者（主题模式）


import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;



public class TopicProducer {
    private static final String url = "tcp://localhost:61616";
    private static final String topicName = "topic-test";

    public static void main(String[] args) throws JMSException {
        //1.创建ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        //2.创建连接
        Connection connection = connectionFactory.createConnection();

        //3.启动连接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //5.创建一个目标
        Destination destination = session.createTopic(topicName);

        //6.创建一个生产者
        MessageProducer producer = session.createProducer(destination);

        for (int i = 0; i < 100; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("test" + i);
            producer.send(textMessage);
            System.out.println("发送消息：" + textMessage.getText());
        }

        //8.关闭连接
        connection.close();
    }
}
　　（6）实现消费者模式（主题模式）


import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

/**
 *  消费者（主题模式）
 **/
public class TopicConsumer {
    private static final String url="tcp://localhost:61616";
    private  static final String topicName="topic-test";
    public static void main(String[] args) throws JMSException {
        //1.创建ConnectionFactory
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory(url);

        //2.创建连接
        Connection connection = connectionFactory.createConnection();

        //3.启动连接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //5.创建一个目标
        Destination destination = session.createTopic(topicName);

        //6.创建一个消费者
        MessageConsumer consumer=session.createConsumer(destination);

        //7.创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
              TextMessage textMessage= (TextMessage) message;
                try {
                    System.out.println("接收消息:"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        
    }
}