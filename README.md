# TPS

### ApiGateway
Se detallan los puntos de acceso al servicio
* **Categorías**
  * http://localhost:8080/menu/categoria,  Muestra un listado de categorías almacenadas en microservicio `mscategoria`
  * http://localhost:8080/menu/categoria/agregar, Formulario para agregar una nueva categoría. (Form Eliminable)
  * http://localhost:8080/addnoticia, agregar noticia via `POST`
  * http://localhost:8080/usuarios/agregar, agregar usuario via `POST`
  * http://localhost:8080/usuarios/login, loguear usuario via `POST`
  
### MicroServicio Categoria
Se detalla como ejecutar los diferentes **procesos** del microservicio asociado
* Via IDE `IntelliJ`

    ![IntelliJ](https://drive.google.com/uc?export=download&id=1ccod3yztlrEkj1lJaBgW2maes22uQk0p)
  * Ejecutar `AllProcess` , ejecutará un proceso encargado de crear categorias (accesible vía api gateway) y otro para consultar la lista de categorías (accesible via apigateway) en una única instancia de `mscategoria`.
  * Ejecutar `ProcessCreate`, ejecutará únicamente el proceso encargado de la creación de categorías.
  * Ejecutar `ProcessList`, ejecutará únicamente el proceso encargado de devolver el listado de categorías en BD.

* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar All`
    * En caso de ejecutar proceso **Creacion** y **Lista** en diferentes terminales
    * Ejecutar `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar Creacion --server.port=8094`
    * Ejecutar `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar Lista --server.port=8093`
    
### MicroServicio Noticia
Se detalla como ejecutar los diferentes **procesos** del microservicio asociado
* Via IDE `IntelliJ`
  * Ejecutar `AllProcess` , ejecutará todos los procesos en una única instancia de `msnoticias`.
  * Ejecutar `ProcessCreate`, ejecutará únicamente el proceso encargado de la creación de noticias.

* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/msnoticias-0.0.1-SNAPSHOT.jar All`
    * En caso de ejecutar proceso **Creacion**  en diferentes terminales
    * Ejecutar `java -jar target/msnoticias-0.0.1-SNAPSHOT.jar Creacion --server.port=8094`

### MicroServicio Usuario
Se detalla como ejecutar los diferentes **procesos** del microservicio asociado
* Via IDE `IntelliJ`
  * Ejecutar `AllProcess` , ejecutará un proceso encargado de crear usuario (accesible vía api gateway) y otro para realizar logins (accesible via apigateway) en una única instancia de `msusuario`.
  * Ejecutar `ProcessCreate`, ejecutará únicamente el proceso encargado de la creación de usuarios.
  * Ejecutar `ProcessLogin`, ejecutará únicamente el proceso encargado de verificar login de usuarios.

* Via `Terminal`
  * `mvn install`
  * Micro servicio completo -> `java -jar target/msusuario-0.0.1-SNAPSHOT.jar All`
    * En caso de ejecutar proceso **Creacion** y **Lista** en diferentes terminales
    * Ejecutar `java -jar target/msusuario-0.0.1-SNAPSHOT.jar Creacion --server.port=8094`
    * Ejecutar `java -jar target/msusuario-0.0.1-SNAPSHOT.jar Login --server.port=8093`
   
