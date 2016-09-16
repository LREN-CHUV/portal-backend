#!/usr/bin/env bash


# Define reference data

GROUPS_REF="{\"code\":\"root\",\"groups\":[{\"code\":\"tg1\",\"label\":\"Test Group 1\",\"groups\":[{\"code\":\"tg3\",\"label\":\"Test Group 3\",\"groups\":[]}]},{\"code\":\"tg2\",\"label\":\"Test Group 2\",\"groups\":[{\"code\":\"tg4\",\"label\":\"Test Group 4\",\"groups\":[]}]}]}"

VARIABLES_REF="[{\"code\":\"tv1\",\"label\":\"Test Variable 1\",\"type\":\"text\",\"group\":{\"code\":\"tg3\",\"label\":\"Test Group 3\"},\"isVariable\":true},{\"code\":\"tv2\",\"label\":\"Test Variable 2\",\"type\":\"integer\",\"group\":{\"code\":\"tg4\",\"label\":\"Test Group 4\"},\"isVariable\":true},{\"code\":\"tv3\",\"label\":\"Test Variable 3\",\"type\":\"real\",\"group\":{\"code\":\"tg4\",\"label\":\"Test Group 4\"},\"isVariable\":true}]"

STATS_REF="{\"users\":0,\"articles\":0,\"variables\":3}"

VARIABLES_HIERARCHY_REF="{\"code\":\"root\",\"groups\":[{\"code\":\"tg1\",\"label\":\"Test Group 1\",\"groups\":[{\"code\":\"tg3\",\"label\":\"Test Group 3\",\"variables\":[{\"code\":\"tv1\",\"label\":\"Test Variable 1\",\"type\":\"text\"}]}]},{\"code\":\"tg2\",\"label\":\"Test Group 2\",\"groups\":[{\"code\":\"tg4\",\"label\":\"Test Group 4\",\"variables\":[{\"code\":\"tv2\",\"label\":\"Test Variable 2\",\"type\":\"integer\"},{\"code\":\"tv3\",\"label\":\"Test Variable 3\",\"type\":\"real\"}]}]}]}"


# Get gateway IP

GATEWAY_IP=$(docker inspect backend-test | grep \"Gateway\":\ \" | sed 's/.*Gateway\":\ \"\([^-]*\)\",/\1/' | head -n 1)


# Test - GET groups

if [ "$(curl -s ${GATEWAY_IP}:65434/services/groups)" != "$GROUPS_REF" ]; then
  echo "Tests failed - failed to load groups"
  exit 1
fi


# Test - GET variables

if [ "$(curl -s ${GATEWAY_IP}:65434/services/variables)" != "$VARIABLES_REF" ]; then
  echo "Tests failed - failed to load variables"
  exit 1
fi


# Test - GET stats

if [ "$(curl -s ${GATEWAY_IP}:65434/services/stats)" != "$STATS_REF" ]; then
  echo "Tests failed - failed to load stats"
  exit 1
fi


# Test - GET variables hierarchy

if [ "$(curl -s ${GATEWAY_IP}:65434/services/variables/hierarchy)" != "$VARIABLES_HIERARCHY_REF" ]; then
  echo "Tests failed - failed to load variables hierarchy"
  exit 1
fi


echo "Tests successfully passed"

exit 0
