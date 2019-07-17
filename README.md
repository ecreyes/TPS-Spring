# TPS

### ApiGateway
Se detallan los puntos de acceso al servicio
* **Categorías**
  * http://localhost:8080/categoria,  Muestra un listado de categorías almacenadas en microservicio `mscategoria`
  * http://localhost:8080/categoria/agregar, Agregar una categoria vía `POST`
    + Esquema -> ```{"nombre": "Deportes"}```
  * http://localhost:8080/categoria/editar, edita una categoría vía `PUT`
    + Esquema -> ```{"nombre": "NombreEditado","id": 7}```
  * http://localhost:8080/categoria/eliminar, eliminar una categoría vía `DELETE`
    + Esquema -> ```{"id": 1}```
* **Noticia**
  * http://localhost:8080/addnoticia, agregar noticia via `POST`
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
Se detalla como ejecutar los diferentes **procesos** del microservicio asociado
* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar All`
    * En caso de ejecutar proceso **Creacion** y **Lista** en diferentes terminales
    * Ejecutar `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar Creacion --server.port=8094`
    * Ejecutar `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar Lista --server.port=8093`
    
### MicroServicio Noticia
Se detalla como ejecutar los diferentes **procesos** del microservicio asociado
* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/msnoticias-0.0.1-SNAPSHOT.jar All`
    * En caso de ejecutar proceso **Creacion**  en diferentes terminales
    * Ejecutar `java -jar target/msnoticias-0.0.1-SNAPSHOT.jar Creacion --server.port=8094`

### MicroServicio Usuario
Se detalla como ejecutar los diferentes **procesos** del microservicio asociado
* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/msusuario-0.0.1-SNAPSHOT.jar All`
    * En caso de ejecutar proceso **Creacion** y **Lista** en diferentes terminales
    * Ejecutar `java -jar target/msusuario-0.0.1-SNAPSHOT.jar Creacion --server.port=8094`
    * Ejecutar `java -jar target/msusuario-0.0.1-SNAPSHOT.jar Login --server.port=8093`
   
