#!/bin/bash
max_iter_num_inter=1000000
max_steps=50000
java -Xmx8192m -cp target/burlap-0.0.1-SNAPSHOT-jar-with-dependencies.jar burlap.assignment4.EasyGridWorldLauncher testworlds/easy.csv $max_iter_num_inter $max_steps| tee log/easy.txt
