# TPS

### ApiGateway
Se detallan los puntos de acceso al servicio
* **Categorías**
  * http://localhost:8080/menu/categoria,  Muestra un listado de categorías almacenadas en microservicio `mscategoria`
  * http://localhost:8080/menu/categoria/agregar, Formulario para agregar una nueva categoría. 
  
### MicroServicio Categoria
Se detalla como ejecutar los diferentes **procesos** del microservicio asociado
* Via IDE `IntelliJ`

    ![IntelliJ](https://drive.google.com/uc?export=download&id=1ccod3yztlrEkj1lJaBgW2maes22uQk0p)
  * Ejecutar `AllProcess` , ejecutará un proceso encargado de crear categorias (accesible vía api gateway) y otro para consultar la lista de categorías (accesible via apigateway) en una única instancia de `mscategoria`.
  * Ejecutar `ProcessCreate`, ejecutará únicamente el proceso encargado de la creación de categorías.
  * Ejecutar `ProcessList`, ejecutará únicamente el proceso encargado de devolver el listado de categorías en BD.

* Via `Terminal`
  * `mvn install -DskipTests`
  * Micro servicio completo -> `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar All`
    * En caso de ejecutar proceso **Creacion** y **Lista** en diferentes terminales
    * Ejecutar `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar Creacion --server.port=8094`
    * Ejecutar `java -jar target/mscategoria-0.0.1-SNAPSHOT.jar Lista --server.port=8093`
   
