#!/bin/bash

# Variables
SERVICE_NAME="link-shortening-service"
DOCKER_COMPOSE_FILE="../docker-compose.yml"

# Check if a tag was provided, otherwise use "latest"
TAG=${1:-latest}

# Step 1: Build the Docker image using the existing script
echo "Building the Docker image for ${SERVICE_NAME} with tag ${TAG}..."
./build-image.sh "$TAG"

if [ $? -ne 0 ]; then
  echo "Failed to build the Docker image. Exiting."
  exit 1
fi

echo "Docker image for ${SERVICE_NAME}:${TAG} built successfully."

# Step 2: Start the Docker Compose environment
echo "Starting Docker Compose services..."
docker-compose -f "${DOCKER_COMPOSE_FILE}" up -d --build

if [ $? -ne 0 ]; then
  echo "Failed to start Docker Compose. Exiting."
  exit 1
fi

echo "Docker Compose services started successfully."

# Step 3: Display the status of the Docker Compose services
echo "Docker Compose services status:"
docker-compose -f "${DOCKER_COMPOSE_FILE}" ps
