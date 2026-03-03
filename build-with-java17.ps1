# Run Gradle with Java 17 so the project compiles (Forge 1.20.1 requires JDK 17).
# Usage: .\build-with-java17.ps1 [gradle args...]
# Example: .\build-with-java17.ps1 compileJava
# Example: .\build-with-java17.ps1 runClient

$jdk17 = "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"
if (-not (Test-Path $jdk17)) {
    $jdk17 = (Get-ChildItem "C:\Program Files\Eclipse Adoptium" -ErrorAction SilentlyContinue | Where-Object { $_.Name -match "jdk-17" } | Select-Object -First 1).FullName
}
if (-not $jdk17 -or -not (Test-Path $jdk17)) {
    Write-Error "JDK 17 not found. Install Eclipse Temurin 17 or set the path in this script."
    exit 1
}

$env:JAVA_HOME = $jdk17
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

if ($args.Count -gt 0) {
    & .\gradlew --no-daemon @args
} else {
    & .\gradlew compileJava --no-daemon
}
