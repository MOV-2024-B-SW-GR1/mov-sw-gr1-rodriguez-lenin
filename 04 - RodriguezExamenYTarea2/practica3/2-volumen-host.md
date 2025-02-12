# VOLUMEN TIPO HOST
Un volumen host (o bind mount) es un tipo de volumen donde se monta un directorio o archivo específico del sistema de archivos del host en un contenedor.

```
docker run -d --name <nombre contenedor> -v <ruta carpeta host>:<ruta carpeta contenedor> <imagen> 
```

### Crear un volumen tipo host con la imagen nginx:alpine, para la ruta carpeta host: directorio en donde se encuentra la carpeta html en tu computador y para la ruta carpeta contenedor: /usr/share/nginx/html esta ruta se obtiene al revisar la se obtiene desde la documentación
![Volúmenes](imagenes/volumen-host.PNG)
COMANDO
```
docker run -d --name contenedor1 -p 8080:80  -v C:\html:/usr/share/nginx/html nginx:alpine
```
### ¿Qué sucede al ingresar al servidor de nginx?

Cuando visitas el servidor de nginx, verás lo que está en la carpeta html de tu computadora. Es como abrir una carpeta en tu navegador.
### ¿Qué pasa con el archivo index.html del contenedor?

El archivo index.html original del contenedor será reemplazado por el index.html de tu computadora. Si tienes un index.html en tu carpeta, eso es lo que verás en el navegador cuando ingreses al servidor de nginx.


### Ir a https://html5up.net/ y descargar un template gratuito, descomprirlo dentro de nginx/html
### ¿Qué sucede al ingresar al servidor de nginx?

Al ingresar al servidor de nginx, se vera el contenido que se encuentra dentro del directorio html en este caso la pagina web descargada en el sitio web proporcionado

```
docker rm -f contenedor1
```

### ¿Qué sucede al crear nuevamente el mismo contenedor con volumen de tipo host a los directorios definidos anteriormente?
Al crear nuevamente el mismo contenedor con el mismo volumen tipo host y los mismos directorios definidos, se montará el directorio html de tu computadora en el contenedor. Esto significa que cualquier cambio en los archivos dentro de ese directorio se reflejará automáticamente en el servidor de nginx.

### ¿Qué hace el comando pwd?

El comando pwd (print working directory) muestra la ruta completa del directorio actual en el que te encuentras en el sistema de archivos. Esto es útil para saber en qué ubicación del sistema estás trabajando en un momento dado

Si quieres incluir el comando pwd dentro de un comando de Docker, lo puedes hacer de diferentes maneras dependiendo del shell que estés utilizando.


### Volumen tipo host usando PWD y PowerShell
```
docker run -d --name <nombre contenedor> --publish published=<valorPuertoHost>,target=<valor> -v ${PWD}/<ruta relativa>:<ruta absoluta> <nombre imagen>:<tag> 
```

### Volumen tipo host usando PWD (Git Bash)

```
docker run -d --name <nombre contenedor> --publish published=<valorPuertoHost>,target=<valor> -v $(pwd -W)/html:/usr/share/nginx/html <nombre imagen>:<tag> 
```

### Volumen tipo host usando PWD (en Linux)

```
docker run -d --name <nombre contenedor> --publish published=<valorPuertoHost>,target=<valor> -v $(pwd)/html:/usr/share/nginx/html <nombre imagen>:<tag> 
```

