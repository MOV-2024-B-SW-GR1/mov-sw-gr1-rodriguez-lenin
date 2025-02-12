## VOLUMEN NOMBRADO
Un volumen nombrado (named volume) es un tipo de volumen gestionado por Docker que se almacena en una ubicación específica del sistema de archivos del host y se identifica mediante un nombre único. Los volúmenes nombrados no requieren que especifiques una ruta del sistema de archivos del host, y en su lugar, Docker se encarga de la gestión y el almacenamiento del volumen.


### Crear volumen
```
docker volume create <nombre volumen>
```

### Crear el volumen nombrado: vol-postgres
```
docker volume create vol-postgres
```

## MOUNTPOINT
Un mountpoint se refiere al lugar en el sistema de archivos donde un dispositivo de almacenamiento se une (o monta) al sistema de archivos. Es el punto donde los archivos y directorios almacenados en ese dispositivo de almacenamiento son accesibles para el sistema operativo y las aplicaciones.

Por ejemplo, en Windows las unidades de almacenamiento (como `C:`, `D:`, etc.) actúan como puntos de montaje principales para discos duros, unidades flash, unidades ópticas y otros dispositivos de almacenamiento.

Cuando creas un volumen nombrado, Docker asigna un punto de montaje específico en el sistema de archivos del host para ese volumen.

### ¿Cuál es el Mountpoint de vol-postgres?

![Imagen](imagenes/img24.png)

```
/var/lib/docker/volumes/vol-postgres/_data
```

### Estructura del Punto de Montaje:
- /var/lib/docker/volumes/: Es la ubicación base donde Docker almacena todos los volúmenes en el sistema de archivos del host.
- nombreVolumen/: Es el nombre del volumen nombrado que has creado. Docker crea un directorio con este nombre dentro de /var/lib/docker/volumes/ para almacenar los datos del volumen.
- _data: Es el subdirectorio dentro de vol-postgres/ donde se almacenan los datos reales del volumen. El nombre _data es una convención utilizada por Docker para indicar el directorio donde se encuentran los datos del volumen.

### ¿Cómo acceder a ese Mountpoint?
En el contexto de WSL (Windows Subsystem for Linux), wsl$ se refiere al nombre de un recurso compartido de red especial que representa la raíz del sistema de archivos de Windows desde WSL. Cuando accedes a \\wsl$ desde el Explorador de archivos de Windows, puedes ver y acceder a los archivos del sistema de archivos de la distribución de Linux en WSL.
\\wsl.localhost\docker-desktop-data\data\docker\volumes

### Crear un contenedor vinculado a un volumen nombrado
```
docker run -d --name <nombre contenedor> -v <nombre volumen>:<ruta contenedor> <nombre imagen>
```

### Crear la red net-drupal de tipo bridge

```
docker network create --driver bridge net-drupal
```

### Crear un servidor postgres vinculado a la red net-drupal, completar la ruta del contenedor
docker run -d --name server-postgres -e POSTGRES_DB=db_drupal -e POSTGRES_PASSWORD=12345 -e POSTGRES_USER=user_drupal -v vol-postgres:<ruta contenedor> --network net-drupal postgres
_No es necesario exponer el puerto, debido a que nos vamos a conectar desde la misma red de docker_

![Imagen](imagenes/img25.png)


### Crear un cliente postgres vinculado a la red drupal a partir de la imagen dpage/pgadmin4, completar el correo
docker run -d --name client-postgres --publish published=9500,target=80 -e PGADMIN_DEFAULT_PASSWORD=54321 -e PGADMIN_DEFAULT_EMAIL=<correo> --network net-drupal dpage/pgadmin4

![Imagen](imagenes/img26.png)

### Usar el cliente postgres para conectarse al servidor postgres, para la conexión usar el nombre del servidor en lugar de la dirección IP.

Abre pgAdmin4 en tu navegador:
  Ve a http://localhost:9500 y accede con el correo johan.illicachi@epn.edu.ec y la contraseña 54321.

Agrega un nuevo servidor en pgAdmin4:

En la pestaña General, ingresa un nombre para el servidor, por ejemplo, server-postgres.

En la pestaña Connection, completa los campos de la siguiente manera:
  Host name/address: server-postgres (el nombre del contenedor del servidor PostgreSQL).
  Port: 5432 (el puerto por defecto de PostgreSQL).
  Maintenance database: db_drupal (el nombre de la base de datos que especificaste al crear el contenedor).
  Username: user_drupal (el usuario que especificaste al crear el contenedor).
  Password: 12345 (la contraseña que especificaste al crear el contenedor).

Una vez cumplido estos pasos tendremos acceso al servidor postgres

![Imagen](imagenes/img27.png)

### Crear los volúmenes necesarios para drupal, esto se puede encontrar en la documentación

```
docker volume create vol-drupal-modules
docker volume create vol-drupal-profiles
docker volume create vol-drupal-themes
```

### Crear el contenedor server-drupal vinculado a la red, usar la imagen drupal, y vincularlo a los volúmenes nombrados
docker run -d --name server-drupal --publish published=9700,target=80 -v <nombre volumen>:<ruta contenedor> -v <nombre volumen>:<ruta contenedor> -v <nombre volumen>:<ruta contenedor> -v <nombre volumen>:<ruta contenedor> --network net-drupal drupal

![Imagen](imagenes/img28.png)

### Ingrese al server-drupal y siga el paso a paso para la instalación.

![Imagen](imagenes/img29.png)

_La instalación puede tomar varios minutos, mientras espera realice un diagrama de los contenedores que ha creado en este apartado._

![Imagen](imagenes/img30.png)

### Eliminar un volumen específico
```
docker volume rm <nombre volumen>
```
**Considerar**
Datos Persistentes: Asegúrate de que el volumen no contiene datos críticos antes de eliminarlo, ya que esta operación no se puede deshacer.
Contenedores Activos: No puedes eliminar un volumen que está actualmente en uso por un contenedor activo. Debes detener y/o eliminar el contenedor primero.
