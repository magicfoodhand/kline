#!/bin/bash

run_type="$1"

case $run_type in
    build)
        docker-compose build
        ;;
    start)
        watchman -- trigger ./api rebuild -- docker-compose up -d --no-deps --build api
        docker-compose up --build
        ;;
    stop)
        docker-compose down
        ;;
    *)
        echo $"Usage: $0 {start|build}"
        exit 1
        ;;
esac
