# 🎭 Guise — Android 设备伪装模块

> **Put a sheep's skin on your Android app.**
> 为每个应用穿上不同的「设备外衣」，保护隐私，绕过设备指纹检测。

[![编译状态](https://github.com/xiaojing110/Guise/actions/workflows/build.yml/badge.svg)](https://github.com/xiaojing110/Guise/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)

---

## ✨ 功能特性

### 📱 设备信息伪装
- **厂商 / 型号 / 品牌**：一键切换为任意品牌机型
- **系统版本**：伪装 Android 版本（支持 Android 1 ~ 17）
- **Fingerprint / Board / Product**：完整设备指纹伪造
- **2025/2026 最新机型**：内置 Xiaomi 18 Pro Max / 18 Fold、Galaxy S26、Pixel 11、iPhone 18、Mate 80 等

### 🎲 一键随机与模板
- **一键随机**：从 2026 年机型库随机生成一整套互相自洽的 Build / 硬件 / 网络 / 标识配置，并立即保存
- **配置模板**：把当前应用配置保存为模板，一键应用到其他应用
- **内置模板**：默认内置「小米18 Pro Max」「小米18 Fold」，开箱即用
- **持久化**：随机结果和用户模板都会写入本地存储，重启不丢失

### 🧾 日志
- 目标应用进程日志统一写入 `/sdcard/Android/media/<包名>/twig.log`
- 「日志」页汇总展示、刷新与清空，便于排查 Hook 是否生效

### 🛡️ 防指纹上传
- **HTTP 请求头清洗**：自动替换 IMEI / 设备 ID / 型号 / 品牌 / MAC / OAID / Fingerprint 等指纹字段
- **WebView 指纹加噪**：对 Canvas / WebGL / AudioContext / navigator / screen 做一致性加噪
- **跨应用隔离**：降低同一台设备被多个应用上报后关联的风险

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

## 🆕 最新机型数据库（2026）

| 品牌 | 新增机型 |
|:---|:---|
| **Xiaomi** | Xiaomi 18 Pro / 18 Pro Max / 18 Fold / 17 Ultra, Redmi K100 / K100 Pro / K100 Pro Max, Redmi Note 17 系列, Redmi Turbo 5, POCO F9 / X8 系列 |
| **Samsung** | Galaxy S26 / S26+ / S26 Ultra / S26 FE, Z Fold8 / Z Fold8 Ultra / Z Flip8, A57 / A37 / A27, Galaxy Tab S12 |
| **Apple** | iPhone 18 Pro / 18 Pro Max / iPhone Duo / 17e, iPad Air 11/13 (2026) |
| **Huawei** | Mate 90 / 90 Pro / 90 Pro Max / 90 RS Ultimate, Pura 90 系列, nova 16 系列, Mate XT 2 Ultimate |
| **Google** | Pixel 11 / 11 Pro / 11 Pro XL / 11 Pro Fold, Pixel 10a |
| **OPPO** | Find X9 Ultra, Find N6, Reno15 / Reno16 系列 |
| **vivo / iQOO** | X300 Ultra, X Fold6, iQOO 15 Ultra / 15T / Z11 Turbo |
| **Honor** | Magic8 RSR Porsche Design, Magic V6, Robot Phone, 600 系列 |
| **OnePlus** | OnePlus 16, 15T, Ace 7 / Ace 7 Pro, Nord 6 |
| **realme** | 16 Pro / 16 Pro+, Neo8 |
| **其他** | Motorola Razr Fold、Nothing Phone (4a)/(4b)、Nubia RedMagic 11 系列、Sony Xperia 1 VIII 等 |

> 数据库共 **5700+** 机型，已补充 2026 年 1 月至 11 月的 400+ 款新机型。

---

## 🎲 一键随机 / 模板 / 日志

- **随机**：配置编辑页右上角骰子或「模板」页右下角骰子，随机生成并保存一套 2026 机型配置
- **保存模板**：配置编辑页菜单 →「保存为模板」；模板页可删除用户模板
- **应用模板**：配置编辑页菜单 →「应用模板」，或在模板页点击「应用」并选择目标应用
- **内置模板**：小米18 Pro Max、小米18 Fold 默认存在，不可删除
- **日志**：目标应用重启后，在「日志」页刷新查看 `twig.log`，可一键清空

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
│       ├── assets/devices.db   # 设备数据库（5700+ 机型，含 2026 年机型）
│       ├── java/com/houvven/twig/
│       │   ├── config/         # 配置数据类 / 模板仓库 / 一键随机
│       │   ├── preset/         # 预设选项（设备/语言/时区等）
│       │   ├── ui/             # Jetpack Compose UI
│       │   │   ├── components/ # 通用组件
│       │   │   ├── route/      # 页面路由（配置 / 模板 / 日志 / 设置）
│       │   │   └── theme/      # 主题配色
│       │   └── xposed/         # Xposed Hook 实现
│       │       ├── hook/       # 各类 Hook（设备/系统/网络/反检测/防指纹上传...）
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

### v1.3.0（当前）
- ✅ 修复一键随机结果丢失：随机后立即回填并持久化
- ✅ 新增配置模板：保存 / 应用 / 删除，内置「小米18 Pro Max」「小米18 Fold」
- ✅ 新增「防指纹上传」：HTTP 指纹请求头清洗 + WebView/JS 指纹加噪
- ✅ 修复日志：目标应用日志落盘，新增日志查看 / 刷新 / 清空页面
- ✅ 新增 2026 年 1 月至今 400+ 款机型（Xiaomi 18 系列、Galaxy S26、iPhone 18、Pixel 11 等）
- ✅ 设备型号选择：无 codename 的机型自动回退到型号代码，避免 DEVICE 为空
- ✅ 版本号 1.3.0（15）

### v1.2.0
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
