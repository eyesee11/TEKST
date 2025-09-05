#!/bin/bash
# TEKST - Launch Script for Linux/Mac

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "📥 Please install Java 21 or higher from: https://adoptium.net/"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "❌ Java 21 or higher is required. Current version: $JAVA_VERSION"
    echo "📥 Please upgrade Java from: https://adoptium.net/"
    exit 1
fi

echo "🚀 Starting TEKST - Advanced Text Editor"
echo "☕ Java Version: $(java -version 2>&1 | head -n 1)"

# Launch TEKST
java -jar text-editor-1.0.0.jar

echo "👋 TEKST has been closed. Thank you for using TEKST!"
