# Cost-Optimized AWS Hosting Guide (On-Demand)

This guide shows you how to host the System Under Test (SUT) on AWS with **near-zero idle cost**. The service will only be up when you need it for a demo.

## Strategy: On-Demand EC2 + Docker Compose

We will use a single EC2 instance to run both the database and the application in Docker. This approach is:
1.  **Cost Effective**: You only pay for the virtual machine while it's "Running". When "Stopped", you only pay (~$1.60/month) for storage.
2.  **Simple**: It uses the same `docker-compose.yml` you use locally.

---

## Step 1: Launch an EC2 Instance

1.  Go to the [EC2 Console](https://console.aws.amazon.com/ec2/).
2.  Click **Launch Instance**.
3.  **Name**: `performance-sut-demo`.
4.  **AMI**: `Amazon Linux 2023`.
5.  **Instance Type**: `t3.medium` (Recommended for performance testing) or `t3.small`.
6.  **Key pair**: Create one to SSH into the machine.
7.  **Network Settings**: 
    - Allow SSH traffic from: **My IP**.
    - Allow HTTPS/HTTP from: **Anywhere** (or your specific IP).
    - **Add Rule**: Custom TCP, Port `8081` (for the API).
    - **Add Rule**: Custom TCP, Port `9090` (for Prometheus UI).
8.  **Launch Instance**.


---

## Step 2: Install Docker on EC2

Once the instance is running, SSH into it and run:

```bash
# Update and install Docker
sudo dnf update -y
sudo dnf install -y docker
sudo service docker start
sudo usermod -a -G docker ec2-user

# Install Docker Compose
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Logout and log back in for group changes to take effect
exit
```

---

## Step 3: Deploy the SUT

1.  Copy your code to the EC2 instance (using `git clone` or `scp`).
2.  Run the stack:
    ```bash
    cd performance-backend
    docker-compose up -d --build
    ```

---

## Step 4: Cost Optimization (IMPORTANT)

To keep costs low, always **Stop** the instance when your demo is finished.

### via AWS Console:
- Select the instance -> **Instance state** -> **Stop instance**.

### via AWS CLI:
If you have the AWS CLI configured, you can use the provided script:
```bash
./start-demo.sh start  # To start the demo
./start-demo.sh stop   # To save money
```

---

## Verification

Once started, the application will be available at:
`http://[EC2_PUBLIC_IP]:8081/api/users/1`

> [!NOTE]
> The public IP changes when you stop/start the instance unless you assign an **Elastic IP** (which costs money if the instance is stopped). It is recommended to just check the new IP in the console each time you start it.
