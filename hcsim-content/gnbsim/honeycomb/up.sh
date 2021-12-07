#!/bin/bash
cd hc
docker-compose up -d
sleep 10

cd ..
cd cucp1
docker-compose up -d
sleep 10

cd ..
cd cuup1
docker-compose up -d
sleep 10

cd ..
cd du1
docker-compose up -d
sleep 10

cd ..
cd du2
docker-compose up -d
sleep 10

cd ..
cd du3
docker-compose up -d
sleep 10

cd ..
cd hc-1
docker-compose up -d
sleep 10

cd ..
cd cucp2
docker-compose up -d
sleep 10

cd ..
cd cuup2
docker-compose up -d
sleep 10

cd ..
cd du4
docker-compose up -d
sleep 10

cd ..
cd du5
docker-compose up -d
sleep 10

cd ..
cd du6
docker-compose up -d
sleep 10
