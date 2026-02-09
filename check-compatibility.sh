#!/bin/bash

# Gradle 9.2.0 Compatibility Checker for MockLocationApp
# This script verifies your system meets all requirements

echo "================================================"
echo "  Gradle 9.2.0 Compatibility Checker"
echo "================================================"
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check functions
check_java() {
    echo -n "Checking Java version... "
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
        if [ "$JAVA_VERSION" -ge 17 ]; then
            echo -e "${GREEN}✓ Java $JAVA_VERSION detected${NC}"
            return 0
        else
            echo -e "${RED}✗ Java $JAVA_VERSION detected (17+ required)${NC}"
            return 1
        fi
    else
        echo -e "${RED}✗ Java not found${NC}"
        return 1
    fi
}

check_gradle_wrapper() {
    echo -n "Checking Gradle wrapper... "
    if [ -f "./gradlew" ]; then
        echo -e "${GREEN}✓ Gradle wrapper found${NC}"
        return 0
    else
        echo -e "${RED}✗ Gradle wrapper not found${NC}"
        return 1
    fi
}

check_gradle_version() {
    echo -n "Checking Gradle version... "
    if [ -f "gradle/wrapper/gradle-wrapper.properties" ]; then
        GRADLE_VERSION=$(grep "distributionUrl" gradle/wrapper/gradle-wrapper.properties | grep -oP '(?<=gradle-)[0-9.]+(?=-)')
        if [ "$GRADLE_VERSION" = "9.2" ]; then
            echo -e "${GREEN}✓ Gradle 9.2 configured${NC}"
            return 0
        else
            echo -e "${YELLOW}⚠ Gradle $GRADLE_VERSION configured (9.2 expected)${NC}"
            return 1
        fi
    else
        echo -e "${RED}✗ gradle-wrapper.properties not found${NC}"
        return 1
    fi
}

check_android_studio() {
    echo -n "Checking Android Studio... "
    # Check common installation paths
    if [ -d "/Applications/Android Studio.app" ] || \
       [ -d "$HOME/Applications/Android Studio.app" ] || \
       [ -d "/opt/android-studio" ] || \
       [ -d "$HOME/.local/share/JetBrains/Toolbox/apps/AndroidStudio" ] || \
       command -v studio &> /dev/null; then
        echo -e "${GREEN}✓ Android Studio detected${NC}"
        return 0
    else
        echo -e "${YELLOW}⚠ Android Studio not detected (optional)${NC}"
        return 0
    fi
}

check_build_files() {
    echo -n "Checking build configuration files... "
    if [ -f "build.gradle" ] && [ -f "settings.gradle" ] && [ -f "app/build.gradle" ]; then
        echo -e "${GREEN}✓ All build files present${NC}"
        return 0
    else
        echo -e "${RED}✗ Missing build files${NC}"
        return 1
    fi
}

check_gradle_properties() {
    echo -n "Checking gradle.properties... "
    if [ -f "gradle.properties" ]; then
        if grep -q "org.gradle.parallel=true" gradle.properties; then
            echo -e "${GREEN}✓ Optimizations enabled${NC}"
            return 0
        else
            echo -e "${YELLOW}⚠ Some optimizations may be missing${NC}"
            return 0
        fi
    else
        echo -e "${RED}✗ gradle.properties not found${NC}"
        return 1
    fi
}

# Run all checks
ERRORS=0

check_java || ERRORS=$((ERRORS+1))
check_gradle_wrapper || ERRORS=$((ERRORS+1))
check_gradle_version || ERRORS=$((ERRORS+1))
check_android_studio
check_build_files || ERRORS=$((ERRORS+1))
check_gradle_properties || ERRORS=$((ERRORS+1))

echo ""
echo "================================================"

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}✓ All checks passed! You're ready to build.${NC}"
    echo ""
    echo "Next steps:"
    echo "  1. Open project in Android Studio"
    echo "  2. Let Gradle sync complete"
    echo "  3. Build the project: ./gradlew build"
    echo ""
    echo "For more info, see GRADLE_UPGRADE_GUIDE.md"
else
    echo -e "${RED}✗ $ERRORS check(s) failed. Please review the issues above.${NC}"
    echo ""
    echo "Common fixes:"
    echo "  - Install Java 17+: https://adoptium.net/"
    echo "  - Ensure all project files are present"
    echo "  - Run from project root directory"
    echo ""
    echo "See GRADLE_UPGRADE_GUIDE.md for detailed help"
fi

echo "================================================"
exit $ERRORS
