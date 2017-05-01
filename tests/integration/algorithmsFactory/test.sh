#!/usr/bin/env bash

# Create some test models
echo ""
echo "Creating some models..."
curl http://backend:8080/services/models -X POST -H "Content-Type: application/json" -d @model1.json
curl http://backend:8080/services/models -X POST -H "Content-Type: application/json" -d @model2.json

# Run some experiments
echo ""
echo "Run some experiments..."
response=$(curl -s http://backend:8080/services/experiments -X POST -H "Content-Type: application/json" -d @expQuery1.json)
echo "exp1: $response"
response=$(curl -s http://backend:8080/services/experiments -X POST -H "Content-Type: application/json" -d @expQuery2.json)
echo "exp2: $response"
response=$(curl -s http://backend:8080/services/experiments -X POST -H "Content-Type: application/json" -d @expQuery3.json)
echo "exp3: $response"
