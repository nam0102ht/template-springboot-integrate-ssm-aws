#!/bin/bash
mqHostname=$(aws ssm get-parameter --name "/com/ntnn/mq/hostname" --region ap-southeast-1 --with-decryption --query Parameter.Value | cut -d'"' -f 2)
if [ -z "$mqHostname" ]
then
      aws ssm put-parameter \
      --name "/com/ntnn/mq/hostname" \
      --value rabbitmq \
      --type String
else
      echo "mq hostname: $mqHostname"
fi
mqPassword=$(aws ssm get-parameter --name "/com/ntnn/mq/password" --region ap-southeast-1 --with-decryption --query Parameter.Value | cut -d'"' -f 2)

if [ -z "$mqPassword" ]
then
      aws ssm put-parameter \
      --name "/com/ntnn/mq/password" \
      --value rabbitmq \
      --type String
else
      echo "mq password: $mqPassword"
fi

mqUsername=$(aws ssm get-parameter --name "/com/ntnn/mq/username" --region ap-southeast-1 --with-decryption --query Parameter.Value | cut -d'"' -f 2)

if [ -z "$mqUsername" ]
then
      aws ssm put-parameter \
      --name "/com/ntnn/mq/username" \
      --value localhost \
      --type String
else
      echo "mq hostname: $mqUsername"
fi

dbUsername=$(aws ssm get-parameter --name "/com/ntnn/database/username" --region ap-southeast-1 --with-decryption --query Parameter.Value | cut -d'"' -f 2)

if [ -z "$dbUsername" ]
then
      aws ssm put-parameter \
      --name "/com/ntnn/database/username" \
      --value localhost \
      --type String
else
      echo "Db username: $dbUsername"
fi

dbPassword=$(aws ssm get-parameter --name "/com/ntnn/database/password" --region ap-southeast-1 --with-decryption --query Parameter.Value | cut -d'"' -f 2)

if [ -z "$dbPassword" ]
then
      aws ssm put-parameter \
      --name "/com/ntnn/database/password" \
      --value localhost \
      --type String
else
      echo "Db password: $dbPassword"
fi

dbHostname=$(aws ssm get-parameter --name "/com/ntnn/database/hostname" --region ap-southeast-1 --with-decryption --query Parameter.Value | cut -d'"' -f 2)

if [ -z "$dbHostname" ]
then
      aws ssm put-parameter \
      --name "/com/ntnn/database/hostname" \
      --value "jdbc:postgresql://localhost:5432/postgres" \
      --type String
else
      echo "Db hostname: $dbHostname"
fi
