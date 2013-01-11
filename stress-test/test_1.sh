#!/bin/bash
#
# Test 1 : Simple test
#
source $(dirname $0)/global.sh

echo ":: Test 1 initiated ::"

echo ":: Insert into current node ::"
$CASSANDRA_STRESS -n 1000 # 1.000 inserts to current node
echo ":: Insert complete ::"

echo ":: Reading from current node ::"
$CASSANDRA_STRESS -o read -n 1000 # 1.000 reads from current node
echo ":: Reading complete ::"

echo ":: Test 1 completed ::"
