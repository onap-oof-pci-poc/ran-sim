#!/bin/bash

dirs=`ls -l | egrep '^d' | awk '{print $9}'`

# and now loop through the directories:
for dir in $dirs
do
if [[ ${dir:0:2} = "hc" ]] && [[ ${dir} != "hc_50000" ]] 
then
echo  ${dir}
cd ${dir}
rm -rf var/log/honeycomb/honeycomb.log
./honeycomb &
sleep 30
cd ..
fi
done
