#!/bin/bash
echo 'Updating Packages'
sudo yum update -y
sleep 10
echo 'cleaning cache'
sudo yum clean all
sudo yum makecache
echo 'installing Open jdk 8'
cd ~
sudo yum install wget -y
echo "sleeping....."
sleep 10
sudo yum install java-1.8.0-openjdk -y
echo "sleeping....."
sleep 10
java -version
echo 'installed java successfully.'

#sudo yum install -y https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm


sudo wget https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
sudo rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2022
sudo rpm -Uvh mysql80-community-release-el7-3.noarch.rpm
echo "sleeping....."
sleep 10
sudo yum install -y mysql-community-client
# echo "sleeping....."
# sleep 10
# sudo systemctl start mysqld.service
# sudo systemctl status mysqld.service
# echo "sleeping....."
# sleep 10
# pwd=`sudo grep 'temporary password' /var/log/mysqld.log | rev | cut -d':' -f 1 | rev | xargs`
# mysql -uroot -p$pwd --connect-expired-password -e "Alter user 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Masters@2022'"
# mysql -uroot -pMasters@2022  -e "CREATE DATABASE IF NOT EXISTS user_db"

pwd
sudo chmod 755 health-check-api.service
sudo chmod 755 health-check-api-0.0.1-SNAPSHOT.jar
sudo mkdir webservice
sudo mv health-check-api-0.0.1-SNAPSHOT.jar webservice/

sudo mv health-check-api.service /etc/systemd/system/health-check-api.service
