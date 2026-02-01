FROM mariadb:latest

COPY scripts /docker-entrypoint-initdb.d/

ENV MARIADB_ROOT_PASSWORD=mainpass

ENV MARIADB_USER=prospring6
ENV MARIADB_PASSWORD=prospring6
ENV MARIADB_DATABASE=musicdb

ENV PORT 3306
EXPOSE 3307