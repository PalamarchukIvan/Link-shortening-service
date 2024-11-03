#!/bin/bash

# Usage function to show how to use the script
usage() {
  echo "Usage: $0 [tag]"
  echo "Example: $0 v1.0"
  echo "If no tag is provided, 'latest' will be used as the default."
}

# Debug: Display received arguments
echo "Arguments received: $@"

# Check for help flag
if [[ $1 == "-h" || $1 == "--help" ]]; then
  usage
  exit 0
fi

# Set the tag to the provided argument or default to 'latest'
TAG=${1:-latest}

# Debug: Display the tag that will be used
echo "Using tag: $TAG"

# Step 1: Build the JAR without running tests
echo "Building the JAR without running tests..."
mvn clean package -DskipTests

# Step 2: Rename the JAR if necessary
echo "Renaming the JAR to Link-shortening-service.jar..."
mv target/*.jar target/Link-shortening-service.jar

# Step 3: Build the Docker image with the specified tag
echo "Building Docker image with tag: $TAG"
docker build -t link-shortening-service:"$TAG" -f "Dockerfile" .

# Print success message
echo "Docker image 'link-shortening-service:$TAG' built successfully."
