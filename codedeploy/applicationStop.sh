#!/bin/bash
pid=$(sudo lsof -i tcp:8080 -t)
echo $pid

kill $pid

sudo rm -rf codedeploy/
sudo rm -f appspec.yml

echo "ending"