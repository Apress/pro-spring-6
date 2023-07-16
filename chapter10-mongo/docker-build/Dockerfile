FROM mongo:latest

ENV MONGO_INITDB_ROOT_USERNAME=root
ENV MONGO_INITDB_ROOT_PASSWORD=mainpass

COPY scripts /docker-entrypoint-initdb.d/

ENV PORT 27017
EXPOSE 27017