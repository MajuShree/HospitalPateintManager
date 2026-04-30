#!/bin/bash
# compile_and_run.sh - Compile and run the Hospital Patient Record Manager

echo "============================================"
echo "  Hospital Patient Record Manager"
echo "  Java Swing Application"
echo "============================================"
echo ""

# Create output directory
mkdir -p out

# Compile all Java source files
echo "[1/2] Compiling Java sources..."
javac -d out src/*.java

if [ $? -eq 0 ]; then
    echo "      Compilation SUCCESS"
    echo ""
    echo "[2/2] Launching application..."
    java -cp out hospital.HospitalApp
else
    echo "      Compilation FAILED. Check errors above."
    exit 1
fi
