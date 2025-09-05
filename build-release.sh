#!/bin/bash
# TEKST Release Build Script

echo "🔨 Building TEKST Release Package..."
echo "=================================="

# Clean previous builds
echo "🧹 Cleaning previous builds..."
mvn clean

# Run tests
echo "🧪 Running tests..."
mvn test

# Build with assembly
echo "📦 Building release package..."
mvn package assembly:single

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "📁 Release packages created:"
    ls -la target/*release*
    echo ""
    echo "🚀 Release packages are ready in the target/ directory:"
    echo "   - text-editor-1.0.0-release.zip"
    echo "   - text-editor-1.0.0-release.tar.gz"
    echo ""
    echo "📋 Each package contains:"
    echo "   - text-editor-1.0.0.jar (main application)"
    echo "   - tekst.bat (Windows launcher)"
    echo "   - tekst.sh (Linux/Mac launcher)"
    echo "   - README.md (user guide)"
    echo "   - LICENSE (license file)"
    echo "   - samples/ (sample documents)"
    echo ""
    echo "🎯 To distribute:"
    echo "   1. Upload packages to GitHub Releases"
    echo "   2. Share download links with users"
    echo "   3. Users extract and run tekst.bat or tekst.sh"
else
    echo "❌ Build failed!"
    exit 1
fi
