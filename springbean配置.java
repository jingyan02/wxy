//1.在XML中装配bean

<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context">
    <import resource="spring/spring-dao.xml"/>

    <bean id="postservice" class="com.bbs.service.impl.PostserviceImpl">
          <constructor-arg ref="postdao"/>
            <constructor-arg ref="userdao"/>
    </bean>
</beans>
//通过java代码装配bean
@Configuration //表示这个类是用来配置Bean的
class Config{
    @Value("张三") String name;  
    //配置一个Bean，相当于xml中的一个<bean>
    @Bean(name = "student") 
    public Student student(){
        //...具体业务逻辑
        return student;
    } 
}
//自动化配置
@Configuration
@ComponentScan
public class CDPlayerConfig {
 
}