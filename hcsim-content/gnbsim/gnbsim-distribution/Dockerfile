FROM openjdk:8-jre-alpine
ADD lib /app/libs
ADD gnbsim-distribution-1.19.08-SNAPSHOT.jar /app/gnbsim-distribution.jar
CMD java -cp /app/libs/*:/app/gnbsim-distribution.jar com.wipro.www/gnbsim-distribution
ARG HTTP_PROXY="http://TA20102459:JAN19%402001@10.145.22.10:8080/"
ARG HTTPS_PROXY="http://TA20102459:JAN19%402001@10.145.22.10:8080/"

#ENV http_proxy $HTTP_PROXY
#ENV https_proxy $HTTPS_PROXY

ENV http_proxy http://TA20102459:JAN19%402001@10.145.22.10:8080/
ENV https_proxy http://TA20102459:JAN19%402001@10.145.22.10:8080/

FROM openjdk:8-jre-alpine
ADD lib /app/lib
ADD lib/io/fd/honeycomb/minimal-distribution/1.19.08-SNAPSHOT/minimal-distribution-1.19.08-SNAPSHOT.jar /app/minimal-distribution.jar
CMD java -cp /app/lib/*:/app/minimal-distribution.jar io.fd.honeycomb.infra.distro.Main

FROM java:openjdk-8

ARG HTTP_PROXY=${HTTP_PROXY}
ARG HTTPS_PROXY=${HTTPS_PROXY}

ARG enodebsimIp=${enodebsimIp}
ARG enodebsimPort=${enodebsimPort}
ARG ransimCtrlrIp=${ransimCtrlrIp}
ARG ransimCtrlrPort=${ransimCtrlrPort}
ARG vesEventListenerUrl=${vesEventListenerUrl}

ENV http_proxy $HTTP_PROXY
ENV https_proxy $HTTPS_PROXY

ENV enodebsimIp $enodebsimIp
ENV enodebsimPort $enodebsimPort
ENV ransimCtrlrIp $ransimCtrlrIp
ENV ransimCtrlrPort $ransimCtrlrPort
ENV vesEventListenerUrl $vesEventListenerUrl

ARG HTTP_PROXY=""
ARG HTTPS_PROXY=""

ENV http_proxy ""
ENV https_proxy ""

RUN mkdir -p /opt/app/honeycomb

WORKDIR /opt/app/honeycomb

COPY . .

ENTRYPOINT [ "bash", "./honeycomb" ]


