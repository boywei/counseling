# counseling

#### 介绍
心理咨询平台后端

#### 软件架构
Spring Boot + Mybatis-plus

#### 目录说明
- admin: 管理员；
- binding: 绑定关系；
- caller: 访客；
- chat: 咨询会话及会话记录
- common: 通用部分；
- counselor: 咨询师；
- help: 求助会话及求助记录
- schedule: 排班表；
- supervisor: 督导；
- waiting: 申请咨询序列；

#### 分工
- 小程序接口(黄力): caller, counselor(查看咨询师列表和详情)，waiting, chat
- web接口(危敏军): admin, binding, help, schedule, supervisor, counselor

