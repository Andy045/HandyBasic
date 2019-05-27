<h1>敏捷开发基础库</h1>

![](HandyBasic.png)

# 最新版本

   [![](https://jitpack.io/v/Handy045/HandyBasic.svg)](https://jitpack.io/#Handy045/HandyBasic)

# Javadoc文档查看

[最新版本-Javadoc文档](https://javadoc.jitpack.io/com/github/Handy045/HandyBasic/latest/javadoc/)

# 项目引用

## Step 1.添加maven地址到Project的build.gradle配置文件中
```javascript
buildscript {
  repositories {
    ......
    maven { url 'https://jitpack.io' }
  }
  ......
}
    
allprojects {
  repositories {
    ......
    maven { url 'https://jitpack.io' }
  }
}
```
## Step 2.添加compile引用到Module的build.gradle配置文件中
```javascript
apply plugin: 'com.android.application'（or：apply plugin: 'com.android.library'）

android {
  ...
  defaultConfig {
    ...
    javaCompileOptions {
      annotationProcessorOptions {
        arguments = [ eventBusIndex : '当前模块的包名.MyEventBusIndex' ]
      }
    }
  }
      
  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }
}

dependencies {
  ...
  compile 'com.github.liujie045:HandyBasic:最新版本'
  annotationProcessor 'com.github.Raizlabs.DBFlow:dbflow-processor:4.2.4'
  annotationProcessor 'org.greenrobot:eventbus-annotation-processor:3.1.1'
}
```

## Step 3.HandyBase基础库初始化

### 将自定义Application继承BaseApplication
```java
public class MyBaseApplication extends BaseApplication {
  {
    // Bugly应用ID
    public String buglyAppId = "";
    // 是否使用Log打印工具
    public boolean isInitLogUtils = true;
    // 是否使用全局异常监控
    public boolean isUseCrashUtil = true;
  }
  ...
}
```

### 将Activity继承BaseActivity方便扩展

```java
public class MyBaseApplication extends BaseApplication {
  {
    // 是否Log打印Activity的生命周期
    public boolean isLogActivityLife = false;
    // 是否启用权限扫描功能
    public boolean isCheckPermissions = false;
    // 是否使用滑动返回功能
    public boolean isUseSwipeBackFinish = true;
  }
  ...
}
```

### 将Fragment继承BaseFragment方便扩展

```java
public class MyBaseApplication extends BaseApplication {
  {
    // 用于控制每个Fragment进入{@link BaseFragment#setUserVisibleHint(boolean)} 时，是否重新执行onRequest()方法
    public boolean isLazyLoadHDB = true;
    // 是否Log打印Fragment的生命周期
    public boolean isLogFragmentLife = false;
  }
  ...
}
```

## Step 4.已在BaseActivity中内置Android6.0权限扫描功能，框架已默认添加了五种权限

  已默认追加的权限：
```javascript
<uses-permission android:name="android.permission.INTERNET"/> <!-- 网络通信-->
<uses-permission android:name="android.permission.READ_PHONE_STATE"/> <!-- 获取设备信息 -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>  <!-- 获取WIFI状态 -->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/> <!-- 获取网络状态-->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/> <!-- 手机存储读写 -->
```

  在BaseApplication的子类中更多需扫描的权限（追加的权限必须在AndroidManifest中有配置使用）：

```java
public class MyBaseApplication extends BaseApplication {
  {
  ...
  PermissionsUtils.addPermissions(new ArrayList<String>() {{
    add(Manifest.permission.CAMERA);
  }});
}
```

  配置好权限后在BaseActivity中onCreate方法中会默认进行扫描操作。
​  如果扫描权限发现已全部允许，则调用onPermissionSuccessHDB()接口方法。
​  如果扫描权限发现有未启用的权限，则调用onPermissionRejectionHDB()接口方法。在此方法中可以弹出对话框提示用户手动开启权限，从设置界面返回到应用时需再次扫描权限
​  参考操作：

```java
......
@Override
public void onPermissionRejectionHDB() {
  super.onPermissionRejectionHDB();
  SweetDialogUT.showNormalDialog((BaseActivity) activity, "发现未启用权限", "为保障应用正常使用，请开启应用权限", "开启", "退出", new SweetAlertDialog.OnSweetClickListener()
  @Override
  public void onClick(SweetAlertDialog sweetAlertDialog) {
    ToastUtils.getInstance().showShortToast("请在手机设置权限管理中启用开启此应用系统权限");
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.parse("package:" + getPackageName()));
    startActivityForResult(intent, 45);
    sweetAlertDialog.dismiss();
  }
}, new SweetAlertDialog.OnSweetClickListener() {
  @Override
  public void onClick(SweetAlertDialog sweetAlertDialog) {
    sweetAlertDialog.dismiss();
    ActivityStackUtils.getInstance().AppExit(context);
  }
}).setCancelable(false);
......
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  // 若从设置界面返回，重新扫描权限（请将此方法放与onActivityPermissionRejection()同级）  
  if (requestCode == 45) {
    PermissionsUtils.checkPermissions(activity, true);
  }
}
```

  若要动态扫描权限，可以使用：public boolean checkPermissions(Activity activity, List<String> permissions, boolean isRequest)方法。
​  参数permissions为要扫描的权限，扫描后的处理同上。

# 第三方接入

  第三方接入的工程已存在自定义Application时，可在Application的onCreate()方法或者首Activity的onCreate()中调用初始化类

```java
...
@Override
protected void onCreate(Bundle savedInstanceState) {
  ...
  HandyBase.getInstance()
    .setBuglyAppId("") //设置Bugly异常监控AppKey
    .setInitLogUtils(true) //是否使用默认的日志打印功能（打印方法 LogUtils.日志级别）
    .setUseCrashUtil(true) //是否使用默认的异常捕获处理功能（Debug模式下会将异常日志记录在手机存储中）
    .setBuglyConfigApi(new HandyBase.BuglyConfigApi() { //设置Bugly重置方法。可以通过此方法自定义Bugly配置
      @Override
      public BuglyConfig resetBuglyConfig(BuglyConfig buglyConfig) {
        buglyConfig.setCrashAddInfo(...);
        return buglyConfig; //注意返回
      }
    }).init(getApplication()); //启动基础库功能
  ...
}
...
```

##  内容目录

* 初始化敏捷开发框架项目
* 新增base包，含有Activity、Fragment、Application的基类
* 新增Util补充工具类

* 新接入[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)
* 新接入[Bugly](https://bugly.qq.com/docs/user-guide/instruction-manual-android/?v=20180709165613)
* 新接入[EventBus](https://github.com/greenrobot/EventBus)
* 新接入[DBFlow](https://github.com/agrosner/DBFlow)
* 新接入[BGASwipeBackLayout](https://github.com/bingoogolapple/BGASwipeBackLayout-Android)

# 说明
  HandyBasic只是作为未来敏捷开发框架的基础库。用于基础功能、工具、UI等的承载。未来将会通过[HandyFrame](https://github.com/Handy045/HandyFrame)实现敏捷开发框架的设计
