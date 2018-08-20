# SCM
服务注册表，组件化解耦，每个组件可看成一个微服务

### 通过注解注册 @Action(name = "") 
### 一个 Action 对应一个微服务
### 注解 Compiler 通过编译期 生成 注册表
### Application 初始化 运行期扫描、缓存注册表
### 调用 SCM.req(String action,Callback A)
