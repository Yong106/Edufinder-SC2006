Write-Host "=== Backend Setup ==="
Set-Location backend/edufinder

if (Test-Path "./mvnw.cmd") {
    ./mvnw.cmd clean install
} else {
    mvn clean install
}

Set-Location ../..

Write-Host "=== Frontend Setup ==="
Set-Location frontend

if (Get-Command npm -ErrorAction SilentlyContinue) {
    npm install
} else {
    Write-Host "npm not installed. Install Node.js first."
    exit 1
}

Set-Location ..

Write-Host "=== Setup completed successfully ==="
