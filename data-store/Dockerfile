FROM oraclelinux:7-slim
MAINTAINER Evren Onem <evren.onem@gmail.com>

ENV PACKAGE_URL https://repo.mysql.com/yum/mysql-5.7-community/docker/x86_64/mysql-community-server-minimal-5.7.18-1.el7.x86_64.rpm

# Install server
RUN rpmkeys --import http://repo.mysql.com/RPM-GPG-KEY-mysql \
  && yum install -y $PACKAGE_URL \
  && yum install -y libpwquality \
  && rm -rf /var/cache/yum/*

RUN mkdir /docker-entrypoint-initdb.d

# Copy the database schema under the /docker-entrypoint-initdb.d/ directory so it's automatically picked up.
COPY ./schema.sql /docker-entrypoint-initdb.d/

VOLUME /var/lib/mysql

COPY ./docker-entrypoint.sh /entrypoint.sh

RUN \
 chmod +x /docker-entrypoint-initdb.d/schema.sql && \
 chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

EXPOSE 3306 33060
CMD ["mysqld"]