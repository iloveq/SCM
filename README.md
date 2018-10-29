# SCM - 组件化通信工具
#####  一切皆组件，当每个组件可看成一个微服务，组件化项目才会真正解耦
# 组件化介绍：
通过一个类比的例子来介绍组件化
随着业务的复杂度增大，业务线纵横交错，往往牵一发而动全身。
我们希望业务代码也能像一部智能手机。
入口模块：应用桌面图标，更像是home模块，只提供入口；
业务模块：通过appstore，下载我们需要的app，每个app（模块）职责分明，闹钟，短信，电话......；
当某个模块需求变化，并不想影响到其他业务线的大规模变动,只用更新当前app（模块）；
甚至 -- 我的模块、组件可以发到 appstore 给其他手机使用（插件化），每个应用可以资源互动（组件通信），可以增量更新…
所以：
一切皆组件，
每个模块就是一个微服务，通过 rest api 来访问，用完即走。

# SCM介绍：
##### SCM就是用来定义api接口(通过注解在编译期生成服务注册表)，通过请求(SCM.req)，来拿到对应服务(action)的响应结果(ScCallback)的工具
SCM 不支持 Install Run
### 1: 通过注解注册 @Action(name = "") 声明服务 类实现ScAction 
### 2: 一个 Action 对应一个微服务
### 3: 通过对注解 Compiler 编译期生成 -> 服务注册表
### 4: Application 初始化 运行期扫描服务、缓存注册表
### 5: 调用 SCM.req(String action,Callback A) 调用服务, 真正实现解耦
action  ---> module actionTable  --->  scmTable
![SCM.png](https://upload-images.jianshu.io/upload_images/8886407-2b1024e63dfdbe4e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
## 使用：
1:根项目root-project的build.gradle
```
repositories {
        ...
        maven {
            url  "https://dl.bintray.com/woaigmz/SCM"
        }
    }
```
2:你的每个module的build.gradle
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
...
dependencies {
    compile 'com.woaigmz.scm:scm-api:0.0.5'
    compile 'com.woaigmz.scm:scm-annotation:0.0.5'
    annotationProcessor 'com.woaigmz.scm:scm-compiler:0.0.5'
    }
```
3：初始化
```
@Modules(names = {"Main", "Home"})
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SCM.get().scanningSCMTable(App.class);
    }
}
```
4：定义 Service（@Action）为 module-home/actions/HomeLoadConfigAction，为 home-module 提供给外界的api
```
//home-module/actions/HomeLoadConfigAction 
@Action(name = "LoadConfig", module = "Home")
public class HomeLoadConfigAction implements ScAction {

    @Override
    public void invoke(Context context, String param, ScCallback callback) {
        //模拟加载网络数据
        DataProvider.getConfig(callback);
    }

}
//home-module/actions/HomeEntryAction 
@Action(name = "HomeEntry", module = "Home")
public class HomeEntryAction implements ScAction {

    @Override
    public void invoke(Context context, String param, ScCallback callback) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
        callback.onCallback(true, "HomeEntryAction:我把HomeActivity打开了", "");
    }
}
```

//除页面跳转之外，大部分都是异步，业务情景不同，本框架不提供异步转同步，开发者自己实现
5：请求：由action-name通过 SCM.req()方法在module-app / [MainActivity](https://github.com/woaigmz/SCM/blob/master/app/src/main/java/com/woaiqw/simpledemo/MainActivity.java) 获取 module-home/actions 下的服务action
```
private WeakHandler h = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String s = (String) msg.obj;
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            tvLoadConfig.setText(s);
            return false;
        }
    });
//通过Action ："LoadConfig"请求; 响应：ScCallback 返回的是子线程进行的网络请求结果要用handler
private void loadConfigByHomeModule() {
        try {
            SCM.get().req(MainActivity.this, "LoadConfig", new ScCallback() {
                @Override
                public void onCallback(boolean b, String data, String tag) {
                    if (b) {
                        Message obtain = Message.obtain();
                        obtain.obj = data;
                        if (h != null) {
                            h.sendMessage(obtain);
                        } else {
                            Toast.makeText(MainActivity.this, "WeakHandler has been Gc", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        } catch (Exception e) {
            Log.e(Constants.SCM, e.getMessage());
        }
    }
//跳转到  Home-module/view/HomeActivity
try {
                SCM.get().req(MainActivity.this, "HomeEntry", new ScCallback() {
                    @Override
                    public void onCallback(boolean b, final String data, String tag) {
                        if (b)
                            Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(Constants.SCM, e.getMessage());
            }
```
# 想法：
想到 java 后台通过action来定义一个接口 ，servlet里的 req，res，有更好的欢迎issue，star fork 。。。
具体实现思路可用参考项目：[SCM地址：](https://github.com/woaigmz/SCM)
注解工具的作用：action 加到注册表，通过 annotationProcessor 编译时注解，编译期生成（SCMTable）表注册
注解工具链接：[ActionProcessor.java](https://github.com/woaigmz/SCM/blob/67f8236f029388b6791b822ffcc27c242b828150/scm-compiler/src/main/java/com/woaiqw/scm_compiler/processor/ActionProcessor.java)
SCM优点：

1：跨进程，通过传递的 json/string 字符实现类似 [CC](https://github.com/luckybilly/CC) 的通过socket协议传递字符流

2：通过action对应的name，反射调用Action的invoke方法，实现解耦合 类似组件化项目 [ModularizationArchitecture](https://github.com/SpinyTech/ModularizationArchitecture) (aidl + service跨进程)

3：注册表简洁易懂，想到了R文件的生成，public static final  "$actionName" = "@action对应action的实际包名"

4：只要原来的协议不变（@action(name="XXX")）代码重构不会对action有影响

5：编译期生成SCMTable提高了扫描整个包的性能


maven上传命令：
gradle install  /  gradlew bintrayUpload  上传项目到 maven

查看有焦点的activity的包名

linux:adb shell dumpsys activity | grep "mFocusedActivity" / windows:adb shell dumpsys activity | findstr "mFocusedActivity"

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


