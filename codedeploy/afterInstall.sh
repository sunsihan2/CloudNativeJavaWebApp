#!/bin/bash
echo "after install"
pwd
#sudo cp /home/ec2-user/webservice/cloudwatch-config.json /opt/aws/amazon-cloudwatch-agent/
#echo "exit code: $?"

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/aws/amazon-cloudwatch-agent/cloudwatch-config.json -s
echo "exit code: $?"