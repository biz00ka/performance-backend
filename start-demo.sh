#!/bin/bash

# Configuration
INSTANCE_ID="i-xxxxxxxxxxxxxxxxx" # Replace with your EC2 Instance ID
REGION="us-east-1"               # Replace with your AWS Region

# Check if AWS CLI is installed
if ! command -v aws &> /dev/null
then
    echo "AWS CLI could not be found. Please install it to use this script."
    exit
fi

case "$1" in
    start)
        echo "Starting SUT Demo Instance..."
        aws ec2 start-instances --instance-ids $INSTANCE_ID --region $REGION
        echo "Waiting for instance to be running..."
        aws ec2 wait instance-running --instance-ids $INSTANCE_ID --region $REGION
        PUBLIC_IP=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID --region $REGION --query 'Reservations[0].Instances[0].PublicIpAddress' --output text)
        echo "------------------------------------------------"
        echo "SUT is starting! Once the containers are up,"
        echo "it will be available at: http://$PUBLIC_IP:8081"
        echo "------------------------------------------------"
        ;;
    stop)
        echo "Stopping SUT Demo Instance..."
        aws ec2 stop-instances --instance-ids $INSTANCE_ID --region $REGION
        echo "Instance stopped. You are no longer being charged for compute."
        ;;
    status)
        aws ec2 describe-instances --instance-ids $INSTANCE_ID --region $REGION --query 'Reservations[0].Instances[0].State.Name' --output text
        ;;
    *)
        echo "Usage: $0 {start|stop|status}"
        exit 1
esac
