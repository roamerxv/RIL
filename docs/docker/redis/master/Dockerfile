FROM redis:4
MAINTAINER "roamer <roamerxv@gmail.com>"
#自定义的配置文件，以替换原有image中的配置文件
COPY redis-master.conf /usr/local/etc/redis/redis.conf
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime

CMD [ "redis-server", "/usr/local/etc/redis/redis.conf" ]
#RUN apt-get update && apt-get install vim -y
