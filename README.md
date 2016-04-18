#jfinal-plugin

旧项目地址：https://git.oschina.net/zengweilong/plugin.git

#Maven 坐标
```

	<dependency>
	  <groupId>net.oschina.zwlzwl376</groupId>
	  <artifactId>jfinal-plugin</artifactId>
	  <version>0.0.2</version>
	</dependency>

```


#RoutesScanner
路由插件启动:
```

    public void configRoute(Routes me) {
	
        RoutesScanner abp = new RoutesScanner("com.project.web.controller");
		
		  abp.addScanner("com.project.web.controller2");
		
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
	
	@Coller(value="/user",path="page") 
	
	public class UserController extends Controller {
	
	或
	
	@Coller(value={"/user"},path={"page"})
	
	public class UserController extends Controller {
```
#TablesScanner
2.表插件启动：
```

    public void configPlugin(Plugins me) {
    	
        TablesScanner tables = new TablesScanner("com.project.entity.model");
	
		  tables.addScanner("com.project.entity.model2");
   
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


#QuartzPlugin


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



#dev 分支 IOC插件

1.增加Ioc for jfinal 该插件将在v0.0.2版本中更新，通过拦截器，注解方式实现。由于该版本尚在开发阶段暂不支持事务，优化了扫描机制

2.使用方法：

```

    public void configPlugin(Plugins me) {
        IocPlugin iocPlugin = new IocPlugin();
        iocPlugin.addPackage("com.project.web.service");
        me.add(iocPlugin);


    public void configInterceptor(Interceptors me) {
	     me.add(new IocInterceptor());


    @Autowired
    private UserService userService;


	 @Ioc(singleton = true)
	 public class UserService {
	 

```
