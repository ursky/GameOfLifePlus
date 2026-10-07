@echo off
rem Double-click launcher for GameOfLife+ (Windows).
rem
rem On first run it also creates a "GameOfLife+" shortcut next to this file that carries the game
rem icon (a .bat file can't have its own icon) - use that one from then on, or copy it anywhere.
rem It finds Java, builds runGame.jar if it's missing, and starts the game.

setlocal
cd /d "%~dp0"
set "LOG=%~dp0launch.log"
echo. > "%LOG%"

rem --- One-time: shortcut with the game icon ---
if not exist "%~dp0GameOfLife+.lnk" (
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
      "$s = (New-Object -ComObject WScript.Shell).CreateShortcut('%~dp0GameOfLife+.lnk');" ^
      "$s.TargetPath = '%~f0'; $s.WorkingDirectory = '%~dp0'; $s.WindowStyle = 7;" ^
      "$s.IconLocation = '%~dp0icons\GameOfLifePlus.ico'; $s.Description = 'GameOfLife+'; $s.Save()" >nul 2>&1
)

rem --- Java ---
where java >nul 2>&1
if errorlevel 1 (
    call :alert "Java isn't installed. Install a JDK (version 11 or newer) and try again."
    exit /b 1
)

rem --- Build the game the first time (runGame.jar isn't kept in git) ---
if not exist runGame.jar (
    if not exist bin mkdir bin
    javac --release 11 -d bin -sourcepath src src\LaunchGame.java >> "%LOG%" 2>&1
    if errorlevel 1 ( call :alert "Couldn't build the game. Details are in launch.log." & exit /b 1 )
    pushd bin
    jar cfm ..\runGame.jar ..\MANIFEST.MF . >> "%LOG%" 2>&1
    popd
    if not exist runGame.jar ( call :alert "Couldn't build the game. Details are in launch.log." & exit /b 1 )
)

rem javaw = no console window; the batch window closes straight away.
start "" javaw -Xmx8192m -jar runGame.jar
exit /b 0

:alert
powershell -NoProfile -Command "Add-Type -AssemblyName PresentationFramework; [System.Windows.MessageBox]::Show('%~1', 'GameOfLife+') | Out-Null"
exit /b 0
