# TPS

### ApiGateway
Se detallan los puntos de acceso al servicio
* **Categorías**
  * http://localhost:8080/categorias,  Muestra un listado de categorías almacenadas en microservicio `mscategoria`
  * http://localhost:8080/categoria/agregar, Agregar una categoria vía `POST`
    + Esquema -> ```{"nombre": "Deportes"}```
  * http://localhost:8080/categoria/editar, edita una categoría vía `PUT`
    + Esquema -> ```{"nombre": "NombreEditado","id": 7}```
  * http://localhost:8080/categoria/eliminar, eliminar una categoría vía `DELETE`
    + Esquema -> ```{"id": 1}```
* **Noticia**
  * http://localhost:8080/noticias, Muestra un listado de noticias almacenadas en microservicio `msnoticia`
  * http://localhost:8080/noticia/agregar, Agregar una noticia vía `POST`
    + Esquema -> ```{"titular": "titular",	"descripcion":"descripcion","autor": "autor","url": "url","fuente": "fuente"}```
  * http://localhost:8080/noticia/editar, edita una noticia vía `PUT`
    + Esquema -> ```{"id": 1,"titular": "titular","descripcion": "descripcion","autor": "autor","url": "url","fuente": "fuente"}```
  * http://localhost:8080/noticia/eliminar, eliminar una noticia vía `DELETE`
    + Esquema -> ```{"id": 1}```
* **Favorito**
  * http://localhost:8080/favorito/usuario/{id}, listado de las noticias que son guardadas como favoritos por un usuario según `id` vía `GET`
  * http://localhost:8080/favorito/agregar, agregar favorito via `POST`
    + Esquema -> ```{"id_usuario": 1,"id_noticia": 12}```
  * http://localhost:8080/favorito/eliminar, eliminar favorito via `DELETE`
    + Esquema -> ```{"id_usuario": 1,"id_noticia": 12}```
* **Usuario**
  * http://localhost:8080/usuario/agregar, agregar usuario via `POST`
    + Esquema -> ```{"email": "email@email.com","username": "username","password": "pass","estado": "creado"}```
  * http://localhost:8080/usuario/editar, editar usuario via `PUT`
    + Esquema -> ```{"id": 1,"email": "email@email.com","username": "username","password": "pass","estado": "editado"}```
  * http://localhost:8080/usuario/eliminar, eliminar usuario via `DELETE`
    + Esquema -> ```{"id": 1}```
  * http://localhost:8080/usuario/login, loguear usuario via `POST`
    + Esquema -> ```{"email": "email@email.com","password": "pass1"}```

## Docker
Uso de docker para montar los microservicios.

 * Crear la Red: ```docker network create RED_TPS```
 * Imagen de mysql (no tener usado puerto 3306 en el pc): 
 ```docker run --name mysql -e MYSQL_ROOT_HOST=% -e MYSQL_USER=root -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_DATABASE=mydb --network RED_TPS -p 3306:3306 -d  mysql```
 * ApiGateway: ```docker run -d --name apigateway --network RED_TPS -p 8080:8080 figonzal/apigateway```
 * MsUsuarios: ```docker run -d --name msusuarios --network RED_TPS figonzal/msusuarios```
 * MsFavoritos: ```docker run -d --name msfavoritos --network RED_TPS figonzal/msfavoritos```
 * MsNoticias: ```docker run -d --name msnoticias --network RED_TPS figonzal/msnoticias```
 * MsCategorias: ```docker run -d --name mscategorias --network RED_TPS figonzal/mscategorias```
 
 Comandos adicionales:
 * ver redes: `docker network ls`
 * ver contenedores: `docker container ps --all`
 * ver logs: `docker logs <nombre container>`
 * detener contenedor: `docker stop <nombre_container>`

### MicroServicio Categoría
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar`
    
### MicroServicio Noticia
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/msnoticias-0.0.1-SNAPSHOT.jar`

### MicroServicio Usuario
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/msusuario-0.0.1-SNAPSHOT.jar`
 
### MicroServicio Favoritos
Se detalla como ejecutar el microservicio asociado
* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/msfavorito-0.0.1-SNAPSHOT.jar`
   
