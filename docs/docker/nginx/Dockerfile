FROM nginx
MAINTAINER "roamer <roamerxv@gmail.com>"
#自定义的配置文件，以替换原有image中的配置文件
COPY nginx.conf /etc/nginx
COPY ssl/aliyun/214210368000139.* /etc/nginx/

#update system timezone
#RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
#update application timezone
RUN echo "Asia/Shanghai" >> /etc/timezone

#RUN apt-get update && apt-get install vim -y
