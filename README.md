# Start api

./mvnw clean package  

docker compose -f kafka/docker-compose.yml up --build -d  

После того как запустится кафка  

docker compose -f api/docker-compose.yml up --build -d  



