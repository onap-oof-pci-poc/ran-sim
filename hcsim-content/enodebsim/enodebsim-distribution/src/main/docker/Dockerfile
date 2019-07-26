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

RUN mkdir -p /opt/app/honeycomb

WORKDIR /opt/app/honeycomb

COPY . .

ENTRYPOINT [ "bash", "./honeycomb" ]

