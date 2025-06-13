1.文件结构:
    （1）config: Web设置，用于放置过滤器Intercepter
    (2) controller: 业务层，包含业务逻辑代码和路由
    (2) exception:  自定义意外处理
    (4) interceptoe: 过滤器，用于验证会话身份
    (5) mapper: 数据库与后端间的接口，使用注解创建数据库操作接口
    (6) pojo:   对象（数据库对象，标准返回对象）
    （7）service：服务层，提供controller层需要的底层接口，Impl下为接口的具体实现
    （8）utils：工具类，提供JWT，MD5，等工具
