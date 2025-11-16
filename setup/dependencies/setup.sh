#!/usr/bin/env bash
set -e

echo "=== Backend Setup ==="
cd backend/edufinder

if [ -f "./mvnw" ]; then
    chmod +x mvnw
    ./mvnw clean install
else
    mvn clean install
fi

cd ../..

echo "=== Frontend Setup ==="
cd frontend

if command -v npm >/dev/null 2>&1; then
    npm install
else
    echo "npm not installed. Install Node.js first."
    exit 1
fi

cd ../..

echo "=== Setup completed successfully ==="
