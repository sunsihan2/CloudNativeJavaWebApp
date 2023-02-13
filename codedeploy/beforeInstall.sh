#!/bin/bash
echo "before install"
sudo systemctl stop health-check-api.service
sudo rm -f /home/ec2-user/webservice/health-check-api-0.0.1-SNAPSHOT.jar
sudo rm -f /opt/aws/amazon-cloudwatch-agent/cloudwatch-config.json