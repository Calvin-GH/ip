@ECHO OFF

if not exist ..\out mkdir ..\out
if exist ACTUAL.TXT del ACTUAL.TXT

REM Compile Java files
javac -cp ..\src\main\java -d ..\out ..\src\main\java\*.java
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)

REM Run the program (your main class is Timer)
java -classpath ..\out Timer < input.txt > ACTUAL.TXT

REM Compare output
FC ACTUAL.TXT EXPECTED.TXT
