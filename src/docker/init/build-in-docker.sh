#!/bin/sh -e

echo ">>> Building inside Docker"
cd /portal

mvn package

echo ">>> Press Ctrl+C to stop, build is complete"
