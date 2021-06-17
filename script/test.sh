#!/bin/bash
for i in {1..600}
do
  echo "-------------------------- test $i ---------------------\n"
  curl localhost:8888/test/pool/${i}
  sleep 1s
done
