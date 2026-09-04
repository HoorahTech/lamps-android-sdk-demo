# Lamps Android SDK Demo

独立 Android Demo，给业务方按远程 Maven 方式接入 Lamps SDK。已同时接入：

- `sdk`：必选主 SDK
- `pangle`：穿山甲
- `ylh`：优量汇
- `noah`：汇川
- `sdk-tools`：仅 Debug 调试工具，Release 不会打进包

工程名：`lamps-android-sdk-demo`  
包名：`com.lamps.sdk.demo`  
当前远程版本：`1.0.0`

## 依赖

仓库至少包含 Google、Maven Central。优量汇 / 穿山甲 vendor 可能还需要公司 Nexus 或穿山甲仓库：

```kotlin
repositories {
    google()
    mavenCentral()
    maven("https://nexus.hupu.io/repository/hupu-android-public/")
    maven("https://artifact.bytedance.com/repository/pangle")
}
```

```kotlin
val lampsVersion = "1.0.0"

dependencies {
    implementation("io.github.hoorahtech:sdk:$lampsVersion")
    implementation("io.github.hoorahtech:pangle:$lampsVersion")
    implementation("io.github.hoorahtech:ylh:$lampsVersion")
    implementation("io.github.hoorahtech:noah:$lampsVersion")
    debugImplementation("io.github.hoorahtech:sdk-tools:$lampsVersion")
}
```

未使用的广告渠道不要引入。`sdk-tools` 仅用于内部调试，正式应用不要依赖。

版本在根目录 `gradle.properties` 的 `lampsVersion` 中修改。

当前 Demo 默认走 **远程 Maven**。如需改成本地 AAR，注释 `io.github.hoorahtech:*`，打开 `app/build.gradle.kts` 里 `app/libs/` 的 `files()` 依赖；本地 AAR 没有 POM 传递依赖，需要同时放入 `core` 和渠道 vendor AAR。

## 初始化

在隐私协议同意后调用，顺序为 `init` -> `startAsync`。`startAsync` 完成前不要使用 SDK 能力。

```kotlin
val config = LampsConfig.Builder()
    .appId("your_app_id")
    .setOaidProvider { mediaOaid }
    .setDebug(false)
    .setLampsInitPangleSdk(true)
    .setLampsInitYlhSdk(true)
    .setLampsInitNoahSdk(true)
    .setCustomData(mapOf("source" to "your_app"))
    .build()

LampsSdk.init(application, config)
LampsSdk.startAsync(object : InitCallback {
    override fun success() { }
    override fun fail(code: Int, message: String?) { }
})
```

Demo 默认 `appId=10001`，并打开穿山甲、优量汇、汇川初始化。业务接入时改成自己的 `appId`，并换成真实 OAID。

## Demo 能力

| 入口 | 说明 |
| --- | --- |
| 打开 SDK 工具 | Debug 打开 `LampsSdkTools`；Release 隐藏按钮 |
| 跳转游戏中心 | `LampsSdk.navigateToGameCenter` |
| 跳转游戏 | 输入 `gameId` 后调用 `LampsSdk.navigateToGame` |
| TabLayout 演示 | `LampsSdk.getGameCenterView()` 嵌到 Tab / 列表 |

## 运行

```bash
./gradlew :app:assembleDebug
```

安装 Debug APK 后即可验证三渠道初始化、游戏中心和 tools。

## 初始化错误码

| code | 含义 |
| --- | --- |
| 1001 | 未调用 `init` |
| 1002 | `appId` 为空 |
| 1004 | Context 不可用 |
| 1005 | 服务端初始化配置请求失败，且无可用缓存 |
| 1006 | 初始化正在进行 |
| 1007 | 穿山甲 SDK 初始化失败 |
| 1008 | 优量汇 SDK 初始化失败 |
| 1009 | 汇川 SDK 初始化失败 |
| 1010 | 第三方 SDK 初始化分发失败 |
