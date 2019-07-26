#!/bin/python
import sys
import shutil
import os
import signal
import stat
import time
import subprocess
import fileinput
import re

number_of_arguments = len(sys.argv)
if number_of_arguments == 3:
    print('Staring...')
else:
    print('Missing arguement NoOfInstances')
    print('Usage:', sys.argv[0] ,' NoOfInstances [start/stop]')
    sys.exit(1)

NoOfInstances = int(sys.argv[1])
actionStr = sys.argv[2]

if NoOfInstances > 2000 or  NoOfInstances < 5:
    print('Invalid value for NoOfInstances ', NoOfInstances)
    print('Usage: ',sys.argv[0] ,' NoOfInstances')
    sys.exit(1)

if actionStr == "start" or actionStr == "stop" :
    print('...')
else:
    print('Invalid value for 2nd arguement [start/stop]')
    print('Usage:', sys.argv[0] ,' NoOfInstances [start/stop]')
    sys.exit(1)
    
INSTANCE_INDEX=50001
TCP_INSTANCE_INDEX=60001
COUNTER=0
while (COUNTER < NoOfInstances): 
    COUNTER = COUNTER + 1
    INSTANCE = str(INSTANCE_INDEX)
    TCP_INSTANCE = str(TCP_INSTANCE_INDEX)
    if actionStr == "start" :
        print('Preparing instance ', INSTANCE)
        try:
            shutil.copytree('hc_50000', 'hc_'+INSTANCE ,symlinks=False, ignore=None )
        except Exception:
            sys.exc_clear()
        os.chdir('hc_'+INSTANCE)
        print("Current working dir : %s" % os.getcwd())
        for line in fileinput.FileInput("config/netconf.json", inplace=1):
            line = line.replace("50000",INSTANCE)
            line = line.replace("60000", TCP_INSTANCE)
            print(line)

        for line in fileinput.FileInput("honeycomb", inplace=1):
            line = line.replace("50000",INSTANCE)
            print(line)
        print('Starting instance ', INSTANCE)
        subprocess.Popen(["nohup", "./honeycomb", INSTANCE]);
        os.chdir("../")
    if actionStr == "stop" :
        print('Stopping and removing instance ', INSTANCE)
        pidFindCmd="ps -eaf | grep java | grep enodebsim-distribution-1.18.10.jar | grep -v grep | grep " + INSTANCE + " | awk -F' ' '{ print $2 }'";
        print('Finding process ', pidFindCmd)
        #try:
        pidToKill=0
        pidToKill=subprocess.check_output([pidFindCmd], shell=True)
        print('pidFindCmd ', pidFindCmd, ' pidToKill ', pidToKill, type(pidToKill))
        if isinstance(pidToKill, str):
            print('Killing instance ', INSTANCE, ' pid is ', str(pidToKill))
            os.kill(int(pidToKill), signal.SIGKILL)
        if isinstance(pidToKill, list):
            print('Killing multiple processes')
            for pid in pidToKill:
                print('Killing instance with pid is ', str(pid))
                os.kill(int(pid), signal.SIGKILL)
        #except Exception:
        #    print 'Error in finding pid'
        #    sys.exc_clear()
        try:
            shutil.rmtree('./hc_'+INSTANCE)
        except Exception:
            print('Error in removing folder')
            sys.exc_clear()
    INSTANCE_INDEX = INSTANCE_INDEX + 1
    TCP_INSTANCE_INDEX = TCP_INSTANCE_INDEX + 1

if actionStr == "stop" :
    try:
        pidToKill=subprocess.check_output([pidFindCmd])
        if isinstance(pidToKill, int) and pidToKill > 100 : 
            print('Killing an instance with pid is ', str(pidToKill))
            os.kill(int(pidToKill), signal.SIGKILL)
        if isinstance(pidToKill, list):
            for pid in pidToKill:
                print('Killing instance with pid is ', str(pid))
                os.kill(int(pid), signal.SIGKILL)
    except Exception:
        sys.exc_clear()
