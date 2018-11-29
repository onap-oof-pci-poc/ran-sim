VAR=0
echo "=======Starting======" > sysmon.txt

while [ "$VAR" -eq 0 ]; do

echo "=======date======" >> sysmon.txt
date >> sysmon.txt
echo "=======top======" >> sysmon.txt
top -b -n 1 >> sysmon.txt
echo "=======free======" >> sysmon.txt
free -m >> sysmon.txt
echo "=======df======" >> sysmon.txt
df -k >> sysmon.txt

sleep 2

done
