#jfinal-plugin

旧项目地址：https://git.oschina.net/zengweilong/plugin.git

#Maven 坐标
```

	<dependency>
	  <groupId>net.oschina.zwlzwl376</groupId>
	  <artifactId>jfinal-plugin</artifactId>
	  <version>0.0.1</version>
	</dependency>

```


#AutoCollerBindPlugin
路由插件启动:
```

    public void configRoute(Routes me) {
	
        AutoCollerBindPlugin abp = new AutoCollerBindPlugin("com.project.web.controller");
		
        abp.start(me);
		
    }
```
使用:
```

	@Coller("/user") 
	
	public class UserController extends Controller {
	
	或
	
	@Coller({"/user"})
	
	public class UserController extends Controller {
	
	或
	
	@Coller(value="/user",views="page") 
	
	public class UserController extends Controller {
	
	或
	
	@Coller(value={"/user"},views={"page"})
	
	public class UserController extends Controller {
```
#AutoTableBindPlugin
2.表插件启动：
```

    public void configPlugin(Plugins me) {
	
        AutoTableBindPlugin tables = new AutoTableBindPlugin("com.project.entity.model");
		
        tables.start(arp);
    }

使用：

	@Table("user")
	
	public class User extends Model<User> {
	
```

#注意:

#不加注解Coller 默认按照路径路由 例如：UserController -- /user
    
#不加注解Table 默认按照类名小写绑定 例如：User -- user , TableNameTest -- table_name_test

#VelocityLayoutRender


3.VelocityLayoutRender插件启动
```

    @Override
    public void configConstant(Constants me) {
        me.setMainRenderFactory(new VelocityLayoutRenderFactory());
        me.setViewType(ViewType.VELOCITY);
        me.setEncoding(Const.DEFAULT_ENCODING);
        
```       

该插件需要配置velocity.properties 文件，支持宏/模板/toolbox.xml工具等。


#QuartzPlugin (源码来自社区qq群 由qq-->322076903上传，本人在此收集，并做调整)


4.定时器插件

```

	private static QuartzPlugin quartz = new QuartzPlugin();
	
	 ...
	
	 @Override
    public void afterJFinalStart() {
        quartz.start();
    
    ...
        
    @Override
    public void beforeJFinalStop() {
        quartz.stop();
        
```

该插件需要配置job.properties 文件





![输入图片说明](http://a1.qpic.cn/psb?/V13RURSC1CPqha/3tLOG6T3FtrZqSlKATxGnCtRYgWgYASG7xQkS5AxNGU!/c/dAQBAAAAAAAA&ek=1&kp=1&pt=0&bo=AAIAAgAAAAABACc!&sce=0-12-12&rf=0-18 "在这里输入图片标题")





