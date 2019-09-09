# TPS
## Estado y repositorios
|  [Apigateway](https://github.com/figonzal1/TPS-Apigateway) |  [MsCategorias](https://github.com/figonzal1/TPS-Mscategorias) |  [MsUsuarios](https://github.com/figonzal1/TPS-Msusuarios) | [MsFavoritos](https://github.com/figonzal1/TPS-Msfavoritos) |  [MsNoticias](https://github.com/figonzal1/TPS-Msnoticias)| [MsNoticias-API](https://github.com/ecreyes/TPS-msnoticias-api)
|------------|--------------|------------|-------------|------------|------------|
|[![Build Status](https://travis-ci.com/figonzal1/TPS-Apigateway.svg?branch=master)](https://travis-ci.com/figonzal1/TPS-Apigateway)|[![Build Status](https://travis-ci.com/figonzal1/TPS-Mscategorias.svg?branch=master)](https://travis-ci.com/figonzal1/TPS-Mscategorias)|[![Build Status](https://travis-ci.com/figonzal1/TPS-Msusuarios.svg?branch=master)](https://travis-ci.com/figonzal1/TPS-Msusuarios)|[![Build Status](https://travis-ci.com/figonzal1/TPS-Msfavoritos.svg?branch=master)](https://travis-ci.com/figonzal1/TPS-Msfavoritos)|[![Build Status](https://travis-ci.com/figonzal1/TPS-Msnoticias.svg?branch=master)](https://travis-ci.com/figonzal1/TPS-Msnoticias)|[![Build Status](https://travis-ci.org/ecreyes/TPS-msnoticias-api.svg?branch=master)](https://travis-ci.org/ecreyes/TPS-msnoticias-api)
## Docker
Imágenes docker de todos los servicios, disponibles en [docker hub (figonzal)](https://hub.docker.com/u/figonzal) y para `Ms-noticias-api` en [docker hub (ecreyes)](https://hub.docker.com/u/ecreyes)
### Modo auto con `Docker compose`
Para ejecutar el sistema completo:

```sh
$ ./init.sh
```

### Modo manual
Uso de docker para montar los microservicios.

 * Crear la Red: 
 ```sh 
 $ docker network create RED_TPS
 ```
 * Imagen de mysql: 
 ```sh
 $ docker run --name mysql --network RED_TPS -e MYSQL_DATABASE=tpsbd -e MYSQL_USER=tps -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_PASSWORD=gcOiwC4P3vO5ZVft -d mysql
 $ docker exec -it mysql mysql --user=tps --password=gcOiwC4P3vO5ZVft
 ```
 * ApiGateway: 
 ```sh
 $ docker run -d --name apigateway --network RED_TPS -p 80:8080 figonzal/apigateway:v1.0.1
 ```
 * MsCategorias: 
 ```sh
 $ docker run -d --name mscategorias --network RED_TPS figonzal/mscategorias:v1.0.0
 ```
 * MsUsuarios: 
 ```sh
 $ docker run -d --name msusuarios --network RED_TPS figonzal/msusuarios:v1.0.1
 ```
 * MsFavoritos: 
 ```sh
 $ docker run -d --name msfavoritos --network RED_TPS figonzal/msfavoritos:v1.0.1
 ```
 * MsNoticias: 
 ```sh
 $ docker run -d --name msnoticias --network RED_TPS figonzal/msnoticias:v1.0.1
 ```
 * Ms-Noticias-Api: 
 ```sh
 $ docker run -d --name msnoticiasapi -t ecreyes/ms-noticias-api:v1.0.3
 ```

## Acceso a ApiGateway
Se detallan los puntos de acceso a los servicios
* **Categorías**
  * http://34.207.217.162/categorias,  Muestra un listado de categorías almacenadas en microservicio `mscategorias` via `GET`
  * http://34.207.217.162/categoria/agregar, Agregar una categoria vía `POST`
    + Esquema JSON Request Body -> ```{"nombre": "Deportes","estado":"creado"}```
  * http://34.207.217.162/categoria/editar, edita una categoría vía `PUT`
    + Esquema JSON Request Body -> -> ```{"nombre": "NombreEditado","id": 7,"estado":"editado"}```
  * http://34.207.217.162/categoria/eliminar, eliminar una categoría vía `DELETE`
    + Esquema JSON Request Body -> -> ```{"id": 1}```
* **Noticias**
  * http://34.207.217.162/noticias, Muestra un listado de noticias almacenadas en microservicio `msnoticias` via `GET`
  * http://34.207.217.162/noticia/agregar, Agregar una noticia vía `POST`
    + Esquema JSON Request Body -> -> ```{"titular": "titular",	"descripcion":"descripcion","autor": "autor","url": "url","fuente": "fuente","id_categoria":5}```
  * http://34.207.217.162/noticia/editar, edita una noticia vía `PUT`
    + Esquema JSON Request Body -> -> ```{"id": 1,"titular": "titular","descripcion": "descripcion","autor": "autor","url": "url","fuente": "fuente","id_categoria":5}```
  * http://34.207.217.162/noticia/eliminar, eliminar una noticia vía `DELETE`
    + Esquema JSON Request Body -> -> ```{"id": 1}```
* **Favoritos**
  * http://34.207.217.162/favoritos, Listado de todos los favoritos registrados en microservicio `msfavoritos` via `GET`
  * http://34.207.217.162/favorito/usuario/{id}, Listado de las noticias que son guardadas como favoritos por un usuario según `id` vía `GET`
  * http://34.207.217.162/favorito/agregar, agregar favorito via `POST`
    + Esquema JSON Request Body -> -> ```{"id_usuario": 1,"id_noticia": 12}```
  * http://34.207.217.162/favorito/eliminar, eliminar favorito via `DELETE`
    + Esquema JSON Request Body -> -> ```{"id_usuario": 1,"id_noticia": 12}```
* **Usuarios**
  * http://34.207.217.162/usuarios, Listado de todos los usuarios registrados en microservicio `msusuarios` vía `GET`
  * http://34.207.217.162/usuario/agregar, agregar usuario via `POST`
    + Esquema JSON Request Body -> -> ```{"email": "email@email.com","username": "username","password": "pass1","estado": "creado"}```
  * http://34.207.217.162/usuario/editar, editar usuario via `PUT`
    + Esquema JSON Request Body -> -> ```{"id": 1,"email": "email@email.com","username": "username","password": "pass1","estado": "editado"}```
  * http://34.207.217.162/usuario/eliminar, eliminar usuario via `DELETE`
    + Esquema JSON Request Body -> -> ```{"id": 1}```
  * http://34.207.217.162/usuario/login, loguear usuario via `POST`
    + Esquema JSON Request Body -> -> ```{"email": "email@email.com","password": "pass1"}```

## Compilaciones `.jar`
### Apigateway
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * `java -jar target/apigateway-0.0.1-SNAPSHOT.jar`
  
### MsCategorias
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar`
    
### MsNoticias
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * `java -jar target/msnoticias-0.0.1-SNAPSHOT.jar`

### MsUsuarios
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * `java -jar target/msusuario-0.0.1-SNAPSHOT.jar`
 
### MsFavoritos
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * `java -jar target/msfavorito-0.0.1-SNAPSHOT.jar`
   
