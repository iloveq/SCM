# SCM
服务注册表，组件化解耦，每个组件可看成一个微服务

### 通过注解注册 @Action(name = "") 
### 一个 Action 对应一个微服务
### 注解 Compiler 通过编译期 生成 注册表
### Application 初始化 运行期扫描、缓存注册表
### 调用 SCM.req(String action,Callback A)

##### gradle install  /  gradlew bintrayUpload  上传项目到 maven
##### linux:adb shell dumpsys activity | grep "mFocusedActivity" / windows:adb shell dumpsys activity | findstr "mFocusedActivity"

感谢 ：）
该项目 SCM 不支持 Install Run
随着业务的复杂度增大，业务线纵横交错。
我们更希望控制业务代码像操作一部手机。
appstore，下载我们需要的东西；
应用桌面图标，将需求分离实现；
当某个模块需求变化，并不想影响到其他业务线的大规模变动；
（加强版）甚至 -- 我的模块、组件可以发到 appstore 给其他手机使用（插件化），每个应用可以资源互动（组件通信），可以增量更新…

一切皆组件，
或许，每个模块就是一个微服务，通过 rest api 来访问，用完即走。

[SCM地址：](https://github.com/woaigmz/SCM)
action  ---> module actionTable  --->  scmTable
![SCM.png](https://upload-images.jianshu.io/upload_images/8886407-2b1024e63dfdbe4e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

将所有 action 注册到注册表，通过注册表，我们就可以 通过 action 访问对应的服务了，
##### 1：通过 req（action） 我们拿到 response （ScCallback）
```
try {
            SCM.get().req(MainActivity.this, "LoadConfig", new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b)
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(Constants.SCM, e.getMessage());
        }
```
#####  2：action 加到注册表，通过 annotationProcessor 编译时注解，编译期生成（SCMTable）表注册
[ActionProcessor.java](https://github.com/woaigmz/SCM/blob/67f8236f029388b6791b822ffcc27c242b828150/scm-compiler/src/main/java/com/woaiqw/scm_compiler/processor/ActionProcessor.java)
1:想到了R文件的生成，public static final  "$actionName" = "@action对应action的实际包名"
2:每次编译的value都不同，所以重构不会对action有影响，只要原来的协议不变（@action(name="XXX")）
配置注解解析器：
```
defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [KEY_MODULE_NAME:"Main"]
                includeCompileClasspath true
            }
        }
    }
dependencies {
   ...
    //SCM 没做mave jcenter，所以要如此引用
    api project(':scm-api')
    annotationProcessor files('libs/scm-compiler.jar')
    compile 'com.squareup:javapoet:1.8.0'
}
```
##### 3：SCM 初始化同步注册表，这样就像域名被同步到所有路由
```
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //对应每个module的 arguments = [KEY_MODULE_NAME:"Main"]
        SCM.get().scanningSCMTable(new String[]{"Main", "Home"});
    }
}
```
//通过module名获取每个module下的SCMTable，获取action，再action 汇总到 actionTable。
```
public void scanningSCMTable(String[] moduleNames) {
        for (String moduleName : moduleNames) {
            try {
                Class clazz = Class.forName(PACKAGE_OF_GENERATE_FILE + "." + moduleName + "SCMTable");
                Field[] fields = clazz.getFields();
                for (Field field : fields) {
                    System.out.println("111" + "action:" + field.getName() + "value:" + field.get(field.getName()));
                    String name = field.getName();
                    String path = (String) field.get(name);
                    ScAction sca = (ScAction) Class.forName(path).newInstance();
                    actionMap.put(name, sca);
                }
            } catch (Exception e) {
                Log.e(Constants.SCM, e.getMessage());
            }
        }
        isReady = true;
    }
```
##### 4：最重要的定义一个Action
```
@Action(name = "LoadConfig")
public class HomeLoadConfigAction implements ScAction {
    @Override
    public void invoke(Context context, String param, ScCallback callback) {
        loadConfig();
        callback.onCallback(true, "加载了配置", "HHH");
    }

    private void loadConfig() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
不提供异步转通过方法，因为除了页面跳转，一般的action都是耗时操作，处理方式留给开发者具体问题具体对待，
```
几种异步转同步：
wait / notify
条件锁
Future
CountDownLatch/CyclicBarrier
Handler
信号量
```


