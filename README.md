# gmall2020

2020.04.26 第一天
    
    gmall-user test ok 
    
2020.04.27 项目结构重构
    
       gmall-parent 定义依赖版本信息 每个module都需要继承
       gmall-api 定义项目中的bean和接口 每个具体业务module都需要继承
       gmall-common-util 定义项目中通用的依赖 比如springboot-web devtools test lombok等等
       gmall-web-util 依赖common-util 并在其基础上加上web业务有关的依赖 比如thymeleaf
       gmall-service-util 依赖common-util 并在其基础上加上service业务有关的依赖 比如mysql驱动 jdbc redis等等
       gmall-user-service 8070, gmall-user-web 8080 遵循分布式SOA架构 可以让服务器的资源分配更加合理、系统可扩展性变强、服务调度更加灵活
   
 2020.04.30
       
       1.根据雷神的电商视频 测试了aop切面进行数据校验
       2.并对多张表保存的事务问题进行了最终解决 
       @TransactionManagement @Transactional配置事务回滚策略 传播行为
       注意！！！
            调用本类方法，要想正常实现事务，必须用代理对象去调方法才可以。因为事务管理器的本质就是aop，
            而aop的核心就是生成代理对象和并拦截代理对象运行目标方法！
            @EnableAspectJAutoProxy(expose = true) AopContext.currentProxy()
       3.保存数据比如id 多线程环境下 为了线程同步 要用ThreadLocal进行数据保存