#!/bin/bash

echo "-----------------------------------------------"
echo "3oh1.io"
echo "-----------------------------------------------"

echo "1. building application"
./grailsw war && cp build/libs/*.war docker-image/ROOT.war

echo "2. building docker image"
docker build -t 3oh1 docker-image/

echo "run docker container: 'docker run -it --rm -p 9000:8080 3oh1'"
