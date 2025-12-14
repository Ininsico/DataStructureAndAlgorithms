@echo off
cd src\main\java
javac -d ..\..\..\target\classes -cp ".;..\..\..\target\classes" com\dsa\core\*.java com\dsa\data\*.java com\dsa\algorithms\*.java com\dsa\ui\*.java 2> ..\..\..\compile_errors.txt
type ..\..\..\compile_errors.txt
