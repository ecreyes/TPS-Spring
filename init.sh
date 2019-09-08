#!/bin/bash
docker-compose up wait_for_mysql
docker-compose up wait_for_mscategorias
docker-compose up wait_for_usuarios_noticias 
docker-compose up wait_for_favoritos_noticias_api 
docker-compose up wait_for_apigateway 
docker rm -f w4d w4d2 w4d3 w4d4 w4d5
