#!/usr/bin/env bash


# Kill old containers

echo "Killing old containers..."
if [ $(docker ps | grep backend-test | wc -l) -gt 0 ]; then
  docker kill backend-test
fi
if [ $(docker ps | grep portal-db-test | wc -l) -gt 0 ]; then
  docker kill portal-db-test
fi
if [ $(docker ps | grep features-db-test | wc -l) -gt 0 ]; then
  docker kill features-db-test
fi


# Remove old containers

echo "Removing old containers..."
if [ $(docker ps -a | grep backend-test | wc -l) -gt 0 ]; then
  docker rm -f backend-test
fi
if [ $(docker ps -a | grep portal-db-test | wc -l) -gt 0 ]; then
  docker rm -f portal-db-test
fi
if [ $(docker ps -a | grep features-db-test | wc -l) -gt 0 ]; then
  docker rm -f features-db-test
fi


# Run databases containers

echo "Running databases containers..."
docker run --name features-db-test -p 65432:5432 -v $(pwd)/tests/features-db/sql:/docker-entrypoint-initdb.d/ -e POSTGRES_USER=postgres -d postgres:9.5.3
docker run --name meta-db-test -p 65433:5432 -v $(pwd)/tests/meta-db/sql:/docker-entrypoint-initdb.d/ -e POSTGRES_USER=postgres -d postgres:9.5.3
docker run --name portal-db-test -p 65434:5432 -e POSTGRES_USER=postgres -d postgres:9.5.3


# Get gateway IP

echo "Searching gateway IP..."
GATEWAY_IP=$(docker inspect features-db-test | grep \"Gateway\":\ \" | sed 's/.*Gateway\":\ \"\([^-]*\)\",/\1/' | head -n 1)
echo "Gateway IP: $GATEWAY_IP"


# Wait for databases to be ready

echo "Waiting for features-db to start..."
if [ "$CIRCLECI" = true ] ; then
  sleep 10
else
  until [ $(docker exec features-db-test psql -U postgres -c "\q" 2>&1 | wc -l) -eq 0 ]; do
      printf '.'
      sleep 1
  done
fi
echo ""

echo "Waiting for meta-db to start..."
if [ "$CIRCLECI" = true ] ; then
  sleep 10
else
until [ $(docker exec meta-db-test psql -U postgres -c "\q" 2>&1 | wc -l) -eq 0 ]; do
    printf '.'
    sleep 1
done
fi
echo ""

echo "Waiting for portal-db to start..."
if [ "$CIRCLECI" = true ] ; then
  sleep 10
else
until [ $(docker exec portal-db-test psql -U postgres -c "\q" 2>&1 | wc -l) -eq 0 ]; do
    printf '.'
    sleep 1
done
fi
echo ""


# Run backend container

echo "Running backend container..."
docker run --name backend-test -p 65440:8080 \
-e "PORTAL_DB_URL=jdbc:postgresql://$GATEWAY_IP:65434/postgres" \
-e "PORTAL_DB_SERVER=$GATEWAY_IP:65434/postgres" \
-e "PORTAL_DB_USER=postgres" \
-e "META_DB_URL=jdbc:postgresql://$GATEWAY_IP:65433/postgres" \
-e "META_DB_SERVER=$GATEWAY_IP:65433/postgres" \
-e "META_DB_USER=postgres" \
-e "FEATURES_DB_URL=jdbc:postgresql://$GATEWAY_IP:65432/postgres" \
-e "FEATURES_DB_SERVER=$GATEWAY_IP:65432/postgres" \
-e "FEATURES_DB_USER=postgres" \
-e "CONTEXT_PATH=/services" \
-e "AUTHENTICATION=0" \
-d hbpmip/portal-backend:latest


# Wait for backend to be ready

echo "Waiting for backend to start..."
until [ $(docker logs backend-test | grep "Started MIPApplication" | wc -l) -eq 1 ]; do
    printf '.'
    sleep 1
done


echo "DONE"
