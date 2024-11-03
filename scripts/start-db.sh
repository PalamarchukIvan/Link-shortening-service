#!/bin/bash

# Set environment variables for PostgreSQL container
DB_NAME="example"
DB_USER="postgres"
DB_PASSWORD="postgres"
CONTAINER_NAME="link-shortening-db-"

# Pull PostgreSQL image if not already available
echo "Pulling PostgreSQL Docker image..."
docker pull postgres:latest

# Start the PostgreSQL container
echo "Starting PostgreSQL container..."
docker run --name "$CONTAINER_NAME" -e POSTGRES_USER="$DB_USER" -e POSTGRES_PASSWORD="$DB_PASSWORD" -e POSTGRES_DB="$DB_NAME" -p 5432:5432 -d postgres

# Wait for PostgreSQL to initialize
echo "Waiting for PostgreSQL to start..."
sleep 5  # Give it a few seconds to start up

# Check if the container started successfully
if [[ "$(docker ps -q -f name=$CONTAINER_NAME)" ]]; then
  echo "PostgreSQL container '$CONTAINER_NAME' started successfully."
  echo "Database: $DB_NAME"
  echo "Username: $DB_USER"
  echo "Password: $DB_PASSWORD"
  echo "Access it at localhost:5432"
else
  echo "Failed to start PostgreSQL container."
  exit 1
fi

# Create example schema if not already existing
echo "Creating schema 'example' if not already exists..."
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -c "CREATE SCHEMA IF NOT EXISTS example;"

# Confirm schema creation
echo "Schema 'example' created in database '$DB_NAME'."
