FROM openjdk:8-jre-alpine
MAINTAINER Lucas Rogerio Caetano Ferreira "luneo7@gmail.com"

ENV SOLR_VERSION 7.2.1
ENV SOLR solr-$SOLR_VERSION

ENV JAVA_OPTS=""
ENV PROG_OPTS=""

# Install prerequisites
RUN apk add --no-cache \
        lsof \
        gnupg \
        procps \
        tar \
        bash \
        ca-certificates \
        curl \
        supervisor && \
    update-ca-certificates && \
    mkdir /etc/supervisord.d

ENV USER solr
ENV UID 8983

RUN addgroup -S -g $UID $USER && \
adduser -S -u $UID -G $USER -g $USER $USER

VOLUME /tmp

# Install Solr
RUN cd /tmp && \
    echo "downloading solr $SOLR_VERSION" >&2 && \
    curl --retry 3 http://ftp.unicamp.br/pub/apache/lucene/solr/$SOLR_VERSION/$SOLR.tgz -o /tmp/$SOLR.tgz && \
    mkdir -p /opt && \
    gzip -dc /tmp/$SOLR.tgz | tar -C /opt -x && \
    ln -sf /opt/$SOLR /opt/solr && \
    rm /tmp/$SOLR.tgz* && \
    rm -Rf /opt/$SOLR/docs/ && \
    mkdir -p /opt/solr/server/solr/lib /opt/solr/server/solr/mycores && \
    cp -r /opt/solr/server/solr/configsets/sample_techproducts_configs/ /opt/solr/server/solr/mycores/techproducts && \
    sh -c 'touch /opt/solr/server/solr/mycores/techproducts/core.properties' && \
    sed -i -e 's/#SOLR_PORT=8983/SOLR_PORT=8983/' /opt/solr/bin/solr.in.sh && \
    sed -i -e '/-Dsolr.clustering.enabled=true/ a SOLR_OPTS="$SOLR_OPTS -Dsun.net.inetaddr.ttl=60 -Dsun.net.inetaddr.negative.ttl=60"' /opt/solr/bin/solr.in.sh && \
    mkdir -p /var/log/supervisord/ /var/log/app && \
    chown -R $USER:$USER /opt && \
    chown -R $USER:$USER /var/log/supervisord && \
    chown -R $USER:$USER /var/log/app

ENV PATH /opt/solr/bin:$PATH

COPY baseapp-0.0.1.jar /opt/app.jar
COPY supervisord.conf /etc
RUN sh -c 'touch /opt/app.jar'

EXPOSE 8080 8983

WORKDIR /opt/solr
USER $USER

ENTRYPOINT ["supervisord", "--nodaemon", "--configuration", "/etc/supervisord.conf"]