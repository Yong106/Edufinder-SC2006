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

echo "=== Setup completed successfully ==="
