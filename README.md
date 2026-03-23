# 🎭 Guise — Android 设备伪装模块

> **Put a sheep's skin on your Android app.**
> 为每个应用穿上不同的「设备外衣」，保护隐私，绕过设备指纹检测。

[![编译状态](https://github.com/xiaojing110/Guise/actions/workflows/build.yml/badge.svg)](https://github.com/xiaojing110/Guise/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)

---

## ✨ 功能特性

### 📱 设备信息伪装
- **厂商 / 型号 / 品牌**：一键切换为任意品牌机型
- **系统版本**：伪装 Android 版本（支持 Android 1 ~ 16）
- **Fingerprint / Board / Product**：完整设备指纹伪造
- **2025/2026 最新机型**：内置 Xiaomi 15、Galaxy S25、Pixel 9、iPhone 16、Mate 70 等

### 🛡️ 反检测隐身模式
- 隐藏 **Xposed / LSPosed / EdXposed / Riru** 框架痕迹
- 隐藏 **Root / su** 检测
- 隐藏 **Magisk** 文件与命令
- 隐藏 **Frida** 调试工具
- 模拟器特征伪造（Build 属性、SystemProperties）
- 清除调用栈中的 Xposed 痕迹

### 🖥️ 硬件信息伪装
- GPU 厂商 / 渲染器 / 版本
- 屏幕分辨率 / DPI / 字体缩放

### 🌐 网络信息伪装
- 网络类型 / 移动网络制式
- WiFi SSID / BSSID / MAC
- 蓝牙名称 / MAC

### 📍 位置信息
- GPS 经纬度 / 海拔
- 地图选点
- 禁用 WiFi / 基站定位

### 🔒 隐私保护
- SIM 卡信息伪装
- IMEI / 设备 ID / 手机号
- 空白通行证（照片/视频/音频/联系人）
- 截图控制

---

## 🆕 最新机型数据库（2025-2026）

| 品牌 | 新增机型 |
|:---|:---|
| **Xiaomi** | Xiaomi 15 / 15 Pro / 15 Ultra, Redmi Note 14 系列, Redmi K80 系列, POCO F6 Pro / X7 Pro |
| **Samsung** | Galaxy S25 / S25+ / S25 Ultra, A56 / A36 / A26 / A16, Z Fold7 / Flip7, Tab S10 |
| **Huawei** | Mate 70 系列, Pura 80 系列, nova 13 系列 |
| **Google** | Pixel 9 / 9 Pro / 9 Pro XL / 9 Pro Fold / 9a |
| **Apple** | iPhone 16 / 16 Plus / 16 Pro / 16 Pro Max |
| **OPPO** | Find X8 / X8 Pro / X8 Ultra |
| **OnePlus** | OnePlus 13 / 13R |
| **vivo** | X200 / X200 Pro / X200 Ultra, iQOO 13 |
| **realme** | GT 7 Pro / GT 7 |
| **Honor** | Magic7 / Magic7 Pro / Magic7 Ultimate |

> 数据库共 **5100+** 机型，覆盖国内外主流品牌。

---

## 📦 安装使用

### 前置条件
1. Android 设备已获取 **Root 权限**
2. 已安装 **Xposed 框架**（推荐 [LSPosed](https://github.com/LSPosed/LSPosed)）
3. Android 版本 ≥ 7.0（API 24）

### 安装步骤
1. 在 [Releases](../../releases) 页面下载最新 APK
2. 安装 APK
3. 在 LSPosed 中启用 **Guise** 模块
4. 选择需要伪装的目标应用
5. 配置伪装参数 → 重启目标应用

### 免 Root 模式
如使用免 Root 框架（如太极、VirtualXposed），可在设置中开启「免 Root 模式」，配置文件将存储在 `/sdcard/Android/media/<包名>/twig.json`。

---

## 🔨 自动编译（GitHub Actions）

本项目已配置 GitHub Actions 自动编译，**推送代码即自动构建 APK**。

### 触发条件
| 事件 | 行为 |
|:---|:---|
| Push 到 `master` / `main` | 编译 Debug APK → 上传为 Artifact |
| Pull Request | 编译 Debug APK → 用于 Code Review |
| 打 Tag（如 `v1.2.0`） | 编译 Release APK → 创建 GitHub Release |

### 签名配置（可选）

如需编译**正式签名版本**，请在仓库 **Settings → Secrets and variables → Actions** 中配置以下 Secrets：

| Secret 名称 | 说明 |
|:---|:---|
| `SIGNATURE_STORE_FILE_BASE64` | 签名文件的 Base64 编码（`base64 your-keystore.jks`） |
| `SIGNATURE_STORE_PASSWORD` | 签名文件密码 |
| `SIGNATURE_STORE_TYPE` | 签名类型（通常为 `jks`） |
| `SIGNATURE_KEY_ALIAS` | Key 别名 |
| `SIGNATURE_KEY_PASSWORD` | Key 密码 |

> 未配置 Secrets 时，自动使用 Android SDK 默认的 debug 签名编译。

#### 生成签名文件 Base64
```bash
# Linux / macOS
base64 -i your-keystore.jks | tr -d '\n'

# Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("your-keystore.jks"))
```

#### 快速生成测试签名
```bash
keytool -genkey -v -keystore guisesign.jks -alias guise \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -storepass android -keypass android \
  -dname "CN=Guise, OU=Dev, O=Guise, L=Beijing, ST=Beijing, C=CN"
```

---

## 🏗️ 项目结构

```
Guise/
├── app/                        # 主应用模块
│   └── src/main/
│       ├── assets/devices.db   # 设备数据库（5100+ 机型）
│       ├── java/com/houvven/twig/
│       │   ├── config/         # 配置数据类
│       │   ├── preset/         # 预设选项（设备/语言/时区等）
│       │   ├── ui/             # Jetpack Compose UI
│       │   │   ├── components/ # 通用组件
│       │   │   ├── route/      # 页面路由
│       │   │   └── theme/      # 主题配色
│       │   └── xposed/         # Xposed Hook 实现
│       │       ├── hook/       # 各类 Hook（设备/系统/网络/反检测...）
│       │       └── MainHook.kt # Hook 入口
│       └── res/                # 资源文件（中英文）
├── androidc/                   # Android 通用工具库
├── ktxposed/                   # Kotlin Xposed 工具库
├── abcl/                       # 编译时注解处理
└── .github/workflows/          # CI/CD 自动编译
```

---

## 🛠️ 开发编译

### 环境要求
- **JDK 17**
- **Android SDK** API 33
- **Gradle 8.0+**

### 本地编译
```bash
# 1. 配置签名（可选）
cat > local.properties << EOF
signature.store.file.path=/path/to/your-keystore.jks
signature.store.password=your-password
signature.store.type=jks
signature.key.alias=your-alias
signature.key.password=your-key-password
EOF

# 2. 编译
./gradlew assembleRelease

# 3. 输出文件
ls app/build/outputs/apk/release/*.apk
```

### 无签名编译
```bash
# 不配置 local.properties，自动使用 debug 签名
./gradlew assembleDebug
```

---

## 📋 更新日志

### v1.2.0（当前）
- ✅ 新增 2025/2026 最新机型（S25、Pixel 9、iPhone 16、Mate 70 等）
- ✅ 新增 **反检测隐身模式**（隐藏 Xposed/Root/Magisk/Frida 痕迹）
- ✅ 全新 Indigo/Violet 紫色主题配色
- ✅ 配置分区增加 Material Icon 图标
- ✅ Android 14/15/16 版本支持
- ✅ GitHub Actions 自动编译 CI/CD

---

## ⚠️ 免责声明

本项目仅供学习和研究使用。使用本模块进行的任何行为均与本项目无关，请勿用于任何违反法律法规的用途。

---

## 📄 开源许可

```
Copyright (C) 2023 houvven

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
```
