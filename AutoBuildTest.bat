call mvn clean
call mvn build
call mvn test
echo %errorlevel%
pause