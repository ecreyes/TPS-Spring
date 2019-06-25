# TPS

### ApiGateway
Se detallan los puntos de acceso al servicio
* **Categorías**
  * http://localhost:8080/menu/categoria,  Muestra un listado de categorías almacenadas en microservicio `mscategoria`
  * http://localhost:8080/menu/categoria/agregar, Formulario para agregar una nueva categoría. (Form Eliminable)
* **Noticia**
  * http://localhost:8080/addnoticia, agregar noticia via `POST`
* **Usuario**
  * http://localhost:8080/usuarios/agregar, agregar usuario via `POST`
  * http://localhost:8080/usuarios/login, loguear usuario via `POST`
  
### MicroServicio Categoria
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
   
