$ErrorActionPreference = "Stop"

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "   Auto-Setup & Run Script" -ForegroundColor Cyan
Write-Host "=========================================="
Write-Host ""

# 1. Check for Java
if (-not (Get-Command "java" -ErrorAction SilentlyContinue)) {
    Write-Error "Java is not installed or not in PATH. Please install JDK 21+."
    exit 1
}

# 2. Check for Maven
$mvnCmd = "mvn"
if (-not (Get-Command "mvn" -ErrorAction SilentlyContinue)) {
    Write-Host "[WARN] Maven not found in PATH." -ForegroundColor Yellow
    
    $localMvnDir = "$PSScriptRoot\.local-maven"
    $mvnBin = "$localMvnDir\apache-maven-3.9.6\bin\mvn.cmd"
    
    if (-not (Test-Path $mvnBin)) {
        Write-Host "[INFO] Downloading Portable Maven..." -ForegroundColor Green
        New-Item -ItemType Directory -Force -Path $localMvnDir | Out-Null
        
        $url = "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
        $output = "$localMvnDir\maven.zip"
        
        Invoke-WebRequest -Uri $url -OutFile $output
        
        Write-Host "[INFO] Extracting Maven..." -ForegroundColor Green
        Expand-Archive -Path $output -DestinationPath $localMvnDir -Force
        Remove-Item $output
    }
    
    $mvnCmd = $mvnBin
    Write-Host "[INFO] Using local Maven: $mvnCmd" -ForegroundColor Gray
}

# 3. Compile and Run
Write-Host "[INFO] Cleaning and Building..." -ForegroundColor Green
& $mvnCmd clean javafx:run

if ($LASTEXITCODE -ne 0) {
    Write-Error "Application failed to start. Check build errors above."
    Pause
}
