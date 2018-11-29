FROM java:openjdk-8

ARG HTTP_PROXY=${HTTP_PROXY}
ARG HTTPS_PROXY=${HTTPS_PROXY}

ENV http_proxy $HTTP_PROXY
ENV https_proxy $HTTPS_PROXY

RUN mkdir -p /opt/app/honeycomb

WORKDIR /opt/app/honeycomb

COPY . .

ENTRYPOINT [ "bash", "./honeycomb" ]

