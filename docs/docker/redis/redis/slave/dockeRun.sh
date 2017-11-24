docker build -t redis/slave .
docker run --name redis-slave-01 -v /home/docker/redis/slave/data-01:/data -d -p 26379:6379  --restart=always  redis/slave
