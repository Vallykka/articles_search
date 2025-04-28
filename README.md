# articles_search
This app searches articles from Scopus API, you can watch result in table.
You can request either single term or collocation.

For now local setup is supported

1.We need to generate node_modules 
run 'npm install' in articles_frontend

2.Run Docker Compose command from articles folder:
docker-compose up -d

Front page is available at
http://localhost:8080/

Since, Scopus test apikey is invalid, I have added custom one.
You can change it to yours in articles/Dockerfile (SCOPUS_API_KEY var) 
