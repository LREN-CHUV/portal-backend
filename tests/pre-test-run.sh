#!/usr/bin/env bash


# Kill old containers

if [ $(docker ps | grep backend-test | wc -l) -gt 0 ]; then
  docker kill backend-test
fi
if [ $(docker ps | grep portal-db-test | wc -l) -gt 0 ]; then
  docker kill portal-db-test
fi
if [ $(docker ps | grep science-db-test | wc -l) -gt 0 ]; then
  docker kill science-db-test
fi


# Remove old containers

if [ $(docker ps -a | grep backend-test | wc -l) -gt 0 ]; then
  docker rm -f backend-test
fi
if [ $(docker ps -a | grep portal-db-test | wc -l) -gt 0 ]; then
  docker rm -f portal-db-test
fi
if [ $(docker ps -a | grep science-db-test | wc -l) -gt 0 ]; then
  docker rm -f science-db-test
fi


# Run databases containers

docker run --name science-db-test -p 65432:5432 -v /science-db/sql:/docker-entrypoint-initdb.d/ -e POSTGRES_USER=science -d postgres:9.5.3
docker run --name portal-db-test -p 65433:5432 -v /meta-db/sql:/docker-entrypoint-initdb.d/ -e POSTGRES_USER=portal -d postgres:9.5.3


# Get gateway IP

GATEWAY_IP=$(docker inspect science-db-test | grep \"Gateway\":\ \" | sed 's/.*Gateway\":\ \"\([^-]*\)\",/\1/' | head -n 1)
echo "Gateway IP: $GATEWAY_IP"


# Wait for databases to be ready

echo "Waiting for science-db to start..."
until [ $(docker exec science-db-test psql -U postgres -c "\q" 2>&1 | wc -l) -eq 0 ]; do
    printf '.'
    sleep 1
done
echo ""

echo "Waiting for portal-db to start..."
until [ $(docker exec portal-db-test psql -U postgres -c "\q" 2>&1 | wc -l) -eq 0 ]; do
    printf '.'
    sleep 1
done
echo ""


# Run backend container

docker run --name backend-test -p 65434:8080 \
-e "PORTAL_DB_URL=jdbc:postgresql://$GATEWAY_IP:65433/portal" \
-e "PORTAL_DB_SERVER=$GATEWAY_IP:65433/portal" \
-e "PORTAL_DB_USER=portal" \
-e "META_DB_URL=jdbc:postgresql://$GATEWAY_IP:65433/portal" \
-e "META_DB_SERVER=$GATEWAY_IP:65433/portal" \
-e "META_DB_USER=portal" \
-e "SCIENCE_DB_URL=jdbc:postgresql://$GATEWAY_IP:65432/science" \
-e "SCIENCE_DB_SERVER=$GATEWAY_IP:65432/science" \
-e "SCIENCE_DB_USER=science" \
-e "CONTEXT_PATH=/services" \
-e "AUTHENTICATION=0" \
-d hbpmip/portal-backend:latest


# Wait for backend to be ready

echo "Waiting for backend to start..."
until [ $(docker logs backend-test | grep "Started MIPApplication" | wc -l) -eq 1 ]; do
    printf '.'
    sleep 1
done
