docker build -t redis/master .
docker run --name redis-master -v /home/docker/redis/master/data:/data -d -p 16379:6379  --restart=always  redis/master
