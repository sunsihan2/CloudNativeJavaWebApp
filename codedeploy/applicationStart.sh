#!/bin/bash
echo "Start Application"
`cat /etc/profile.d/envvariables.sh`
sleep 5
#sudo systemctl start health-check-api.service
cd /home/ec2-user/
nohup java -jar /home/ec2-user/webservice/health-check-api-0.0.1-SNAPSHOT.jar > /home/ec2-user/log.txt 2> /home/ec2-user/log.txt < /home/ec2-user/log.txt &