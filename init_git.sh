#!/bin/bash

# 物业管理系统 - Git 初始化脚本
# 用法: ./init_git.sh [github_username]

set -e

PROJECT_DIR="/home/nelson/.openclaw/workspace/property-manager"
GITHUB_USER="${1:-nelsonlau}"
REPO_NAME="property-manager"

cd "$PROJECT_DIR"

echo "=========================================="
echo "物业管理系统 - Git 初始化"
echo "=========================================="
echo "GitHub 用户: $GITHUB_USER"
echo "仓库名称: $REPO_NAME"
echo "项目目录: $PROJECT_DIR"
echo ""

# 创建 .gitignore
cat > .gitignore << 'EOF'
# Gradle
.gradle/
build/
!gradle/wrapper/gradle-wrapper.jar

# IDE
.idea/
*.iml
*.ipr
*.iws
.vscode/
*.swp
*.swo

# Android
*.apk
*.aab
*.ap_
*.dex
*.class

# Keystore
*.jks
*.keystore
!gradle.properties

# Firebase
google-services.json

# Local properties
local.properties

# OS
.DS_Store
Thumbs.db

# Logs
*.log
EOF

echo "✅ .gitignore 已创建"

# 初始化 Git
git init
echo "✅ Git 仓库已初始化"

# 配置 Git 用户（如果未配置）
git config user.name "Nelson Lau" 2>/dev/null || true
git config user.email "nelson@3way-tech.com" 2>/dev/null || true

# 添加所有文件
git add .
echo "✅ 所有文件已添加"

# 提交
git commit -m "Initial commit: Property Manager v1.0

Features:
- Property management (info, photos)
- Tenant management
- Lease management with expiry reminders
- Rent payment tracking
- Expense tracking with photo upload
- Multi-language support (CN/EN/ES)
- Cloud backup (Firebase)
- WorkManager scheduled tasks

Tech stack:
- Kotlin + Jetpack Compose
- Room Database
- CameraX
- Firebase (Firestore + Storage)"

echo "✅ 首次提交完成"

# 创建 GitHub 仓库（需要 GitHub CLI 或手动创建）
echo ""
echo "=========================================="
echo "下一步：创建 GitHub 仓库"
echo "=========================================="
echo ""
echo "选项 1: 使用 GitHub CLI (推荐)"
echo "  gh repo create $GITHUB_USER/$REPO_NAME --public --source=. --remote=origin --push"
echo ""
echo "选项 2: 手动创建"
echo "  1. 打开 https://github.com/new"
echo "  2. 仓库名: $REPO_NAME"
echo "  3. 选择 Public"
echo "  4. 不要勾选 'Add README'"
echo "  5. 点击 'Create repository'"
echo "  6. 然后运行下面的命令:"
echo ""
echo "  git remote add origin https://github.com/$GITHUB_USER/$REPO_NAME.git"
echo "  git branch -M main"
echo "  git push -u origin main"
echo ""
echo "=========================================="
echo "配置 Firebase"
echo "=========================================="
echo ""
echo "1. 打开 https://console.firebase.google.com/"
echo "2. 创建新项目"
echo "3. 添加 Android 应用"
echo "   - 包名: com.propertymanager"
echo "4. 下载 google-services.json"
echo "5. 替换 app/google-services.json"
echo "6. 启用 Firestore Database"
echo "7. 启用 Storage"
echo ""
echo "=========================================="
echo "自动构建 APK"
echo "=========================================="
echo ""
echo "推送后，GitHub Actions 会自动构建 APK"
echo "在 GitHub 仓库的 'Actions' 标签页查看构建状态"
echo "构建完成后，在 'Releases' 下载 APK"
echo ""
