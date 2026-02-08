Write-Host "=== Backend Setup ==="
Set-Location backend/edufinder

if (Test-Path "./mvnw.cmd") {
    ./mvnw.cmd clean install
} else {
    mvn clean install
}

Set-Location ..

Write-Host "=== Setup completed successfully ==="
