#!/bin/sh
set -e
#this script is used for sending delete mount request to sdnc

echo "deleting mount data for 11"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/11 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 22"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/22 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for cucpserver1"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/cucpserver1 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for cucpserver2"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/cucpserver2 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 1111"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/1111 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 2222"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/2222 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 110"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/110 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 220"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/220 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 330"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/330 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 440"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/440 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 550"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/550 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"

echo "deleting mount data for 660"
curl -i -X DELETE https://10.31.4.22:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/660 -k -H 'Accept: application/xml' -H 'Content-Type: application/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U"


echo "script executed successfully"
