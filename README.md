#plugin

1.路由插件启动:

    public void configRoute(Routes me) {
	
        AutoCollerBindPlugin abp = new AutoCollerBindPlugin("com.project.web.controller");
		
        abp.start(me);
		
    }

使用:

@Coller("/user") 

public class UserController extends Controller {

或

@Coller({"/user"})

public class UserController extends Controller {

2.表插件启动：

    public void configPlugin(Plugins me) {
	
        AutoTableBindPlugin tables = new AutoTableBindPlugin("com.project.entity.model");
		
        tables.start(arp);
    }

使用：

@Table("user")

public class User extends Model<User> {

注意:

不加注解Coller 默认按照路径路由 例如：UserController -- /user
    
不加注解Table 默认按照类名小写绑定 例如：User -- user 	


