param([switch]$Offline)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $taskToolRoot = Join-Path (Split-Path $PSScriptRoot -Parent) '.tools'
    if (Test-Path -LiteralPath (Join-Path $taskToolRoot 'jdk-25.0.4.1+1\bin\java.exe')) {
        $env:JAVA_HOME = Join-Path $taskToolRoot 'jdk-25.0.4.1+1'
    }
    $taskOptions = @('--no-daemon')
    # Match the Windows argument-file encoding for paths containing non-ASCII characters.
    if ($env:OS -eq 'Windows_NT') {
        $taskAnsi = [System.Globalization.CultureInfo]::CurrentCulture.TextInfo.ANSICodePage
        $taskOptions += "-Dorg.gradle.jvmargs=-Dfile.encoding=windows-$taskAnsi"
    }
    if ($Offline) { $taskOptions += '--offline' }
    if (Test-Path -LiteralPath (Join-Path $taskToolRoot 'gradle-9.1.0\bin\gradle.bat')) {
        $env:GRADLE_USER_HOME = Join-Path $taskToolRoot 'gradle-cache'
        & (Join-Path $taskToolRoot 'gradle-9.1.0\bin\gradle.bat') @taskOptions clean releaseBundle
    } else {
        & '.\gradlew.bat' @taskOptions clean releaseBundle
    }
    if ($LASTEXITCODE -ne 0) { throw 'Build or tests failed.' }
    Write-Host 'Release files are ready in dist/'
} finally { Pop-Location }
