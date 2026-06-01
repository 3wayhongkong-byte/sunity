#!/bin/bash

# 物业管理系统 - APK 构建脚本
# 用法: ./build_apk.sh [debug|release]

set -e

PROJECT_DIR="/home/nelson/.openclaw/workspace/property-manager"
BUILD_TYPE="${1:-debug}"

cd "$PROJECT_DIR"

echo "=========================================="
echo "物业管理系统 - APK 构建"
echo "=========================================="
echo "构建类型: $BUILD_TYPE"
echo "项目目录: $PROJECT_DIR"
echo ""

# 检查 Android SDK
if [ -z "$ANDROID_HOME" ]; then
    echo "❌ ANDROID_HOME 未设置"
    echo "请设置 Android SDK 路径，例如:"
    echo "  export ANDROID_HOME=/path/to/android/sdk"
    exit 1
fi

# 检查 Gradle Wrapper
if [ ! -f "gradlew" ]; then
    echo "⚠️  未找到 gradlew，尝试生成..."
    gradle wrapper
fi

# 清理构建
echo "🧹 清理构建..."
./gradlew clean

# 构建 APK
echo "🔨 构建 $BUILD_TYPE APK..."
if [ "$BUILD_TYPE" = "release" ]; then
    ./gradlew assembleRelease
    APK_PATH="app/build/outputs/apk/release/app-release-unsigned.apk"
else
    ./gradlew assembleDebug
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
fi

# 检查构建结果
if [ -f "$APK_PATH" ]; then
    echo ""
    echo "✅ 构建成功!"
    echo "APK 路径: $APK_PATH"
    echo "文件大小: $(du -h "$APK_PATH" | cut -f1)"
    echo ""
    echo "安装到设备:"
    echo "  adb install $APK_PATH"
else
    echo "❌ 构建失败"
    exit 1
fi
