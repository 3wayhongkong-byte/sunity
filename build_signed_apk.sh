#!/bin/bash

# 物业管理系统 - 完整构建和签名脚本
# 用法: ./build_signed_apk.sh

set -e

PROJECT_DIR="/home/nelson/.openclaw/workspace/property-manager"
KEYSTORE_PATH="${KEYSTORE_PATH:-$HOME/.keystore/property-manager.keystore}"
KEY_ALIAS="${KEY_ALIAS:-propertymanager}"

cd "$PROJECT_DIR"

echo "=========================================="
echo "物业管理系统 - 签名 APK 构建"
echo "=========================================="

# 检查 keystore
if [ ! -f "$KEYSTORE_PATH" ]; then
    echo "⚠️  Keystore 不存在，创建新的..."
    mkdir -p "$HOME/.keystore"
    keytool -genkey -v -keystore "$KEYSTORE_PATH" \
        -alias "$KEY_ALIAS" \
        -keyalg RSA -keysize 2048 \
        -validity 10000 \
        -storepass "property123" \
        -keypass "property123" \
        -dname "CN=PropertyManager, OU=Development, O=3way Technology, L=Panama, ST=Panama, C=PA"
    echo "✅ Keystore 已创建: $KEYSTORE_PATH"
    echo "⚠️  请妥善保管 keystore 和密码!"
fi

# 创建 gradle.properties 中的签名配置
cat >> gradle.properties << EOF

# 签名配置 (已存在则跳过)
# signing.storeFile=$KEYSTORE_PATH
# signing.storePassword=property123
# signing.keyAlias=$KEY_ALIAS
# signing.keyPassword=property123
EOF

# 构建签名 APK
echo ""
echo "🔨 构建签名 APK..."
./gradlew assembleRelease

# 找到签名后的 APK
APK_PATH=$(find app/build/outputs/apk/release -name "*.apk" -type f | head -1)

if [ -f "$APK_PATH" ]; then
    echo ""
    echo "✅ 构建成功!"
    echo "APK 路径: $APK_PATH"
    echo "文件大小: $(du -h "$APK_PATH" | cut -f1)"
    echo ""
    echo "验证签名:"
    jarsigner -verify -verbose -certs "$APK_PATH"
    echo ""
    echo "安装到设备:"
    echo "  adb install $APK_PATH"
else
    echo "❌ 构建失败"
    exit 1
fi
