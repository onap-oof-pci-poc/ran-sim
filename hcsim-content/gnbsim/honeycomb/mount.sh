#!/bin/sh
set -e
#this script is used for sending mount request to sdnc

echo "sending mount request for 11"

curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/11 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>11</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2850</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'

echo "sending mount request for 22"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/22 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>22</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2851</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'


echo "sending mount request for cucpserver1"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/cucpserver1 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>cucpserver1</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2852</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'



echo "sending mount request for cucpserver2"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/cucpserver2 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>cucpserver2</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2853</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'


echo "sending mount request for 1111"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/1111 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>1111</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2854</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'


echo "sending mount request for 2222"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/2222 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>2222</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2855</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'


echo "sending mount request for 110"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/110 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>110</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2856</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'


echo "sending mount request for 220"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/220 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>220</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2857</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'



echo "sending mount request for 330"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/330 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>330</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2858</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'


echo "sending mount request for 440"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/440 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>440</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2859</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'



echo "sending mount request for 550"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/550 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>550</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2860</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'



echo "sending mount request for 660"
curl -i -X PUT https://10.31.4.21:30267/restconf/config/network-topology:network-topology/topology/topology-netconf/node/660 -k\
  -H 'Accept: application/xml' -H 'Content-Type: text/xml' \
  --user "admin":"Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  -d '<node xmlns="urn:TBD:params:xml:ns:yang:network-topology">
  <node-id>660</node-id>
  <host xmlns="urn:opendaylight:netconf-node-topology">10.31.4.14</host>
  <port xmlns="urn:opendaylight:netconf-node-topology">2861</port>
  <username xmlns="urn:opendaylight:netconf-node-topology">admin</username>
  <password xmlns="urn:opendaylight:netconf-node-topology">admin</password>
  <tcp-only xmlns="urn:opendaylight:netconf-node-topology">false</tcp-only>
  <!-- non-mandatory fields with default values, you can safely remove these if you do not wish to override any of these values-->
  <reconnect-on-changed-schema xmlns="urn:opendaylight:netconf-node-topology">false</reconnect-on-changed-schema>
  <connection-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">20000</connection-timeout-millis>
  <max-connection-attempts xmlns="urn:opendaylight:netconf-node-topology">0</max-connection-attempts>
  <between-attempts-timeout-millis xmlns="urn:opendaylight:netconf-node-topology">2000</between-attempts-timeout-millis>
  <sleep-factor xmlns="urn:opendaylight:netconf-node-topology">1.5</sleep-factor>
  <!-- keepalive-delay set to 0 turns off keepalives-->
  <keepalive-delay xmlns="urn:opendaylight:netconf-node-topology">120</keepalive-delay>
</node>'
