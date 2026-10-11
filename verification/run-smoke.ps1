param([string]$ServerDirectory = (Join-Path $PSScriptRoot 'runtime'))
$ErrorActionPreference = 'Stop'
$taskProject = Split-Path $PSScriptRoot -Parent
$taskTools = Join-Path (Split-Path $taskProject -Parent) '.tools'
if (Test-Path (Join-Path $taskTools 'jdk-25.0.4.1+1')) {
    $taskJava = Join-Path $taskTools 'jdk-25.0.4.1+1\bin'
} elseif ($env:JAVA_HOME) {
    $taskJava = Join-Path $env:JAVA_HOME 'bin'
} else { throw 'Set JAVA_HOME to JDK 25.' }
if (!(Test-Path (Join-Path $ServerDirectory 'paper.jar'))) { throw 'Supply a dedicated disposable test server with Paper 26.3 as paper.jar and an accepted EULA.' }
$taskPlugin = Join-Path $taskProject 'build\libs\NetherRatio-3.0.3-paper26.3.1.jar'
if (!(Test-Path $taskPlugin)) { throw 'Build the plugin first.' }
$taskClasses = Join-Path $PSScriptRoot 'classes'
New-Item -ItemType Directory -Force $taskClasses, (Join-Path $PSScriptRoot 'results') | Out-Null
$taskDependencies = @()
foreach ($taskCache in @((Join-Path $taskTools 'gradle-cache\caches\modules-2\files-2.1'), (Join-Path $ServerDirectory 'libraries'))) {
    if (Test-Path $taskCache) { $taskDependencies += Get-ChildItem $taskCache -Recurse -Filter '*.jar' | ForEach-Object FullName }
}
$taskClasspath = (@($taskPlugin) + $taskDependencies) -join ';'
& (Join-Path $taskJava 'javac.exe') -encoding UTF-8 -cp $taskClasspath -d $taskClasses (Join-Path $PSScriptRoot 'PortSmoke.java')
if ($LASTEXITCODE -ne 0) { throw 'Harness compilation failed.' }
Copy-Item (Join-Path $PSScriptRoot 'plugin.yml') (Join-Path $taskClasses 'plugin.yml') -Force
New-Item -ItemType Directory -Force (Join-Path $ServerDirectory 'plugins') | Out-Null
& (Join-Path $taskJava 'jar.exe') cf (Join-Path $ServerDirectory 'plugins\NetherRatioPortSmoke.jar') -C $taskClasses .
if ($LASTEXITCODE -ne 0) { throw 'Harness packaging failed.' }
Copy-Item $taskPlugin (Join-Path $ServerDirectory 'plugins') -Force
Push-Location $ServerDirectory
try { & (Join-Path $taskJava 'java.exe') -Xms256M -Xmx1G -jar paper.jar --nogui *> (Join-Path $PSScriptRoot 'results\server-console.log') }
finally { Pop-Location }
$taskLog = Join-Path $ServerDirectory 'logs\latest.log'
if (!(Select-String $taskLog -Pattern 'NETHERRATIO_SMOKE_PASS checks=16' -Quiet)) { throw 'Paper integration checks failed; inspect the server log.' }
Write-Host '16 Paper integration checks passed.'
