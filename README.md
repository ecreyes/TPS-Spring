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
* **Usuario**
  * http://localhost:8080/usuario/agregar, agregar usuario via `POST`
    + Esquema -> ```{"email": "email@email.com","username": "username","password": "pass","estado": "creado"}```
  * http://localhost:8080/usuario/editar, agregar usuario via `PUT`
    + Esquema -> ```{"id": 1,"email": "email@email.com","username": "username","password": "pass","estado": "editado"}```
  * http://localhost:8080/usuario/eliminar, agregar usuario via `DELETE`
    + Esquema -> ```{"id": 1}```
  * http://localhost:8080/usuario/login, loguear usuario via `POST`
    + Esquema -> ```{"email": "email@email.com","password": "pass1"}```
  
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
   
