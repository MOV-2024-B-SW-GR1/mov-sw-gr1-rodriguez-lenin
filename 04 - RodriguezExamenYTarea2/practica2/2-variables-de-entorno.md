# Variables de Entorno
### ¿Qué son las variables de entorno?
Las variables de entorno son valores dinámicos utilizados por el sistema operativo y programas para configurar su comportamiento y ambiente de ejecución. Contienen información como rutas de directorios, configuraciones de idioma, etc. Pueden ser globales o locales y se configuran en el sistema operativo o por programas específicos. Son útiles para personalizar programas y scripts y proporcionar información sobre el entorno de ejecución.

### Para crear un contenedor con variables de entorno

```
docker run -d --name <nombre contenedor> -e <nombre variable1>=<valor1> -e <nombre variable2>=<valor2>
```

### Crear un contenedor a partir de la imagen de nginx:alpine con las siguientes variables de entorno: username y role. Para la variable de entorno rol asignar el valor admin.

```
docker pull nginx:alpine
docker run -d --name mi-nginx -e username=Johan -e role=admin nginx:alpine
```
![Imagen](imagenes/img7.png)

### Crear un contenedor con mysql:8 , mapear todos los puertos
```
docker pull mysql:8
docker run -P -d --name mi-sql mysql:8
```

### ¿El contenedor se está ejecutando?
![Imagen](imagenes/img8.png)
El contenedor no se esta ejecutando, tiene como estado Exited

### Identificar el problema
![Imagen](imagenes/img9.png)
El problema es que no se crearon las variables de entorno necesarias, por lo tanto dio un error y la ejecucion se termino de manera inmediata

### Eliminar el contenedor creado con mysql:8 

```
docker rm mi-sql
```

### Para crear un contenedor con variables de entorno especificadas
- Portabilidad: Las aplicaciones se vuelven más portátiles y pueden ser desplegadas en diferentes entornos (desarrollo, pruebas, producción) simplemente cambiando el archivo de variables de entorno.
- Centralización: Todas las configuraciones importantes se centralizan en un solo lugar, lo que facilita la gestión y auditoría de las configuraciones.
- Consistencia: Asegura que todos los miembros del equipo de desarrollo o los entornos de despliegue utilicen las mismas configuraciones.
- Evitar Exposición en el Código: Mantener variables sensibles como contraseñas, claves API, y tokens fuera del código fuente reduce el riesgo de exposición accidental a través del control de versiones.
- Control de Acceso: Los archivos de variables de entorno pueden ser gestionados con permisos específicos, limitando quién puede ver o modificar la configuración sensible.

Previo a esto es necesario crear el archivo y colocar las variables en un archivo, **.env** se ha convertido en una convención estándar, pero también es posible usar cualquier extensión como **.txt**.
```
docker run -d --name <nombre contenedor> --env-file=<nombreArchivo>.<extensión> <nombre imagen>
```
**Considerar**
Es necesario especificar la ruta absoluta del archivo si este se encuentra en una ubicación diferente a la que estás ejecutando el comando docker run.

### Crear un contenedor con mysql:8 , mapear todos los puertos y configurar las variables de entorno mediante un archivo

![Imagen](imagenes/img4.png)

```
docker run -P -d --name mi-sql --env-file=mysql_env.env mysql:8
```

![Imagen](imagenes/img5.png)

### ¿Qué bases de datos existen en el contenedor creado?
![Imagen](imagenes/img6.png)
