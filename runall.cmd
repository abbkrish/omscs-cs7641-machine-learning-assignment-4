@ECHO OFF
REM @call build.cmd

REM java -Xmx8192m -cp target\burlap-0.0.1-SNAPSHOT-jar-with-dependencies.jar burlap.assignment4.HardGridWorldLauncher
REM java -Xmx4096m -cp target\burlap-0.0.1-SNAPSHOT-jar-with-dependencies.jar burlap.assignment4.EasyGridWorldLauncher testworlds/easy.csv
java -Xmx4096m -cp target\burlap-0.0.1-SNAPSHOT-jar-with-dependencies.jar burlap.assignment4.EasyGridWorldLauncher testworlds/empty.csv
REM java -Xmx8192m -cp target\burlap-0.0.1-SNAPSHOT-jar-with-dependencies.jar burlap.assignment4.EasyGridWorldLauncher testworlds/world02.csv
REM java -Xmx8192m -cp target\burlap-0.0.1-SNAPSHOT-jar-with-dependencies.jar burlap.assignment4.EasyGridWorldLauncher testworlds/world03.csv
REM java -Xmx8192m -cp target\burlap-0.0.1-SNAPSHOT-jar-with-dependencies.jar burlap.assignment4.EasyGridWorldLauncher testworlds/crazy.csv
