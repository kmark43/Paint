@echo off
path C:\Program Files\Java\jdk1.8.0_31\bin;%path%
javac -d bin @sourceFiles.txt && java -cp bin main/Main && goto eof
pause
:eof