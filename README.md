# Property Manager - 物业管理系统

## 📱 项目概述

Android 物业管理系统，支持管理 10-20 套物业的租约、租客、租金和支出记录。

### ✨ 功能特性

| 功能 | 描述 |
|------|------|
| 🏠 **物业信息管理** | 地址、类型、面积、照片、产权信息 |
| 👤 **租客管理** | 姓名、联系方式、证件号、入住/搬出记录 |
| 📅 **租约管理** | 开始/结束日期、租金金额、押金、条款 |
| 💰 **租金记录** | 收款记录、催缴提醒、欠租统计 |
| 📋 **支出记录** | 维修费、管理费、杂项、发票拍照 |
| 📊 **报表统计** | 收入/支出汇总、现金流、空置率 |
| 🔔 **到期提醒** | 租约到期、租金到期自动提醒 |
| 📸 **拍照上传** | 发票/收据拍照上传 |
| ☁️ **云端备份** | Firebase 云端备份和恢复 |
| 🌐 **多语言** | 中文 / 英文 / 西班牙文 |

---

## 🏗️ 技术架构

| 项目 | 方案 |
|------|------|
| 开发语言 | Kotlin |
| UI 框架 | Jetpack Compose |
| 数据存储 | Room (SQLite) |
| 相机 | CameraX |
| 推送 | WorkManager |
| 云端 | Firebase (Firestore + Storage) |
| 导出 | Apache POI (Excel) + iText (PDF) |
| 异步 | Kotlin Coroutines + Flow |

---

## 📁 项目结构

```
property-manager/
├── .github/workflows/
│   └── build-apk.yml          # GitHub Actions 自动构建
├── app/
│   ├── src/main/
│   │   ├── java/com/propertymanager/
│   │   │   ├── data/          # 数据层 (Room DAO, Entity)
│   │   │   ├── ui/            # 界面层
│   │   │   │   ├── screens/   # 页面组件
│   │   │   │   ├── components/# 通用组件
│   │   │   │   ├── navigation/# 导航
│   │   │   │   └── theme/     # 主题
│   │   │   ├── util/          # 工具类
│   │   │   ├── work/          # WorkManager 定时任务
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   ├── values/        # 英文资源
│   │   │   ├── values-zh/     # 中文资源
│   │   │   ├── values-es/     # 西班牙文资源
│   │   │   └── ...
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json   # Firebase 配置
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 🚀 如何构建 APK

### 方法一：GitHub Actions（推荐）

1. 将项目上传到 GitHub
2. 配置 Firebase（替换 `google-services.json`）
3. GitHub Actions 会自动构建并生成 APK
4. 在 GitHub Releases 下载 APK

### 方法二：本地构建

```bash
cd property-manager

# 需要 Android SDK
export ANDROID_HOME=/path/to/android/sdk

# 构建 Debug APK
./gradlew assembleDebug

# 构建 Release APK（需要签名）
./gradlew assembleRelease
```

---

## 🔧 Firebase 配置

1. 在 [Firebase Console](https://console.firebase.google.com/) 创建项目
2. 添加 Android 应用，包名：`com.propertymanager`
3. 下载 `google-services.json` 放到 `app/` 目录
4. 启用 Firestore Database 和 Storage

---

## 📱 安装

1. 下载 APK 文件
2. 在 Android 设备上允许"安装未知应用"
3. 打开 APK 文件进行安装

---

## 📄 许可证

MIT License

---

## 📞 联系

- 开发者：Nelson Lau
- 公司：3way Technology, Ltd. (Panama)
- Email：nelson@3way-tech.com
