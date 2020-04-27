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
   
 