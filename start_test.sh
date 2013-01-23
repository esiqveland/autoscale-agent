#!/bin/bash

echo "Starting logging..."
bin/logging &

echo "Starting autoscaler..."
bin/autoscale &

echo "Startup complete"
