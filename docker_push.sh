#!/usr/bin/env bash
set -e

export WORKSPACE=$(pwd)
captain push --branch-tags=false --commit-tags=true portal-backend
curl -k -X POST --data-urlencode payload@$WORKSPACE/src/docker/run/slack.json https://hbps1.chuv.ch/slack/dev-activity
