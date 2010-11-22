@echo off
title compile
cd ./src
echo Compiling IX
"C:\Program Files (x86)\Java\jdk1.6.0_21\bin\javac.exe" -cp . -d ./bin/ ./src/innovationx/classic/Server.java
pause