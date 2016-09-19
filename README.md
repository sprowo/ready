# ready
##2.0版本
>1. 增加spring支持，全部是单例，不再使用静态方法；<br>
>2. 模板引擎可以自由选择，实现接口IConfiguration和ITemplate即可；<br>
>3. yml的命名空间使用多个，排在最前面的命名空间优先选择。<br>

##在1.1版本的基础上，修改了ymlChain的运行方式。
>1. 增加了end 备选处理方式，在正常结束chain和发生exception之后可以进入end执行终了方法;<br>
>2. 返回的时候，都是直接向httpServletResponse中写入字符串;<br>
>3. 增加了freemarker模板引擎，将模板生成的html或者其他文档写进response的流中。
