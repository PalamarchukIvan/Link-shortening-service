#!/bin/bash

# Usage function to show how to use the script
usage() {
  echo "Usage: $0 [tag]"
  echo "Example: $0 v1.0"
  echo "If no tag is provided, 'latest' will be used as the default."
}

# Check for help flag
if [[ $1 == "-h" || $1 == "--help" ]]; then
  usage
  exit 0
fi

# Set the tag to the provided argument or default to 'latest'
TAG=${1:-latest}

# Navigate to the project root directory
cd "$(dirname "$0")/.." || exit 1

# Step 1: Build the JAR without running tests
echo "Building the JAR without running tests..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
  echo "Maven build failed. Exiting."
  exit 1
fi

# Step 2: Rename the JAR if necessary
echo "Renaming the JAR to Link-shortening-service.jar..."
mv target/*.jar target/Link-shortening-service.jar

# Step 3: Build the Docker image with the specified tag
echo "Building Docker image with tag: $TAG"
docker build -t link-shortening-service:"$TAG" -f "Dockerfile" .

if [ $? -ne 0 ]; then
  echo "Failed to build Docker image. Exiting."
  exit 1
fi

# Print success message
echo "Docker image 'link-shortening-service:$TAG' built successfully."
