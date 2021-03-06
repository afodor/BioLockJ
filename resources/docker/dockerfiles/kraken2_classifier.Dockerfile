# suggested build command:
# name=kraken2_classifier
# cd ${BLJ}
# docker build -t biolockjdevteam/${name} . -f resources/docker/dockerfiles/${name}.Dockerfile 

ARG DOCKER_HUB_USER=biolockjdevteam
ARG FROM_VERSION=v1.2.9
FROM ${DOCKER_HUB_USER}/kraken2_classifier_dbfree:${FROM_VERSION}
 
#1.) Download 8GB miniKraken2 DB
ENV BLJ_DEFAULT_DB=/mnt/db
RUN mkdir -p ${BLJ_DEFAULT_DB} && \
	cd "${BLJ_DEFAULT_DB}" && \
	wget "ftp://ftp.ccb.jhu.edu/pub/data/kraken2_dbs/minikraken2_v1_8GB_201904_UPDATE.tgz" && \
	md5sum minikraken2_v1_8GB_201904_UPDATE.tgz && \
	bsdtar -xzf minikraken2_v1_8GB_201904_UPDATE.tgz && \
	chmod -R 777 "${BLJ_DEFAULT_DB}" && \
	mv "${BLJ_DEFAULT_DB}"/minikraken2*/* . && \
	rm -rf "${BLJ_DEFAULT_DB}"/minikraken2*

#2.) Cleanup
RUN	 rm -rf /usr/share/* 
