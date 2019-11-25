# 用途

开发基础库适用于日常开发中DEMO、小型项目、高级开发库的基础支撑，主要集成了日志记录、异常监控、权限请求、常用工具类等绝大多数项目都会用到的基础功能集成。统一了日志、异常记录方式，以及不同Android版本的兼容处理等。除了EventBus，此库还融入了Mvp的精简设计，方便工程解耦。

# 功能组成

## 日志美化、本地日志记录

日志美化和本地日志记录，是通过[AndroidUtilCode](#常用工具类)工具集合的[LogUtils](https://github.com/Blankj/AndroidUtilCode/blob/master/feature/utilcode/pkg/src/main/java/com/blankj/utilcode/pkg/feature/log/LogActivity.kt)类实现。

## 远程异常监控

远程异常监控使用的是腾讯的[Bugly](https://bugly.qq.com/v2)

## 常用工具类

绝对是[柯基](https://blankj.com/)的[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

## EventBus

[同名](https://github.com/greenrobot/EventBus)

## 权限请求

[PermissionsUtils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/PermissionsUtils.kt)

基础库默认使用了5个权限：

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

如要使用，请在BaseActivity的init方法中设置：isCheckPermissions = true。

如果需要增改请求，请在BaseAppLication的子类中，在init方法修改。

## Mvp架构

在BaseActivity和BaseFragment中，通过泛型配置Presenter，且在BaseFragment中增加View的泛型配置。具体可见代码。

[BaseActivity](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/base/BaseActivity.kt)

[BaseFragment](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/base/BaseFragment.kt)

## 其他

工具类|功能描述
-|-
[ActivityStackUtils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/ActivityStackUtils.kt)|Activity堆栈手动管理工具类
[AesUtils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/AesUtils.kt)|AES加密解密工具类
[Base64Utils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/Base64Utils.kt)|Base64可逆加密算法工具类
[FastJsonUtil](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/FastJsonUtil.kt)|FastJSON工具类
[ImageUtils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/ImageUtils.kt)|图片相关工具类（通过代码改变图片颜色，图片压缩、图片水印）
[IntentUtils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/IntentUtils.kt)|意图相关工具类（Activity跳转）
[PermissionsUtils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/PermissionsUtils.kt)|权限扫描工具类
[ProcessUtils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/ProcessUtils.kt)|进程相关工具类
[StyleUtils](https://github.com/Handy045/HandyBasic/blob/master/library/src/main/java/com/handy/basic/utils/StyleUtils.kt)|样式效果工具类（通过代码设置Selector效果）

# 接入方式

1、 在工程的build.gradle增加maven地址

```xml
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

2、 在library的build.gradle增加依赖

```xml
dependencies {
        implementation 'com.github.Handy045:HandyBasic:最新版本'
}
```

3、 在接入工程的AndroidManifest中设置application为BaseAppLication或其子类。但如果是接入第三方工程，无需设置，但需要在第三方工程的application onCreate方法中增加：new BaseApplication(this).onCreate()。

最新版本：

[![](https://jitpack.io/v/Handy045/HandyBasic.svg)](https://jitpack.io/#Handy045/HandyBasic)

# 相关链接

[HandyBasic](https://github.com/Handy045/HandyBasic)

[EventBus](https://github.com/greenrobot/EventBus)

[FastJson](https://github.com/alibaba/fastjson)

[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

[Bugly](https://bugly.qq.com/)
