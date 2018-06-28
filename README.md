# Stegobmp

## Compilación

```
$ mvn clean package
$ mv ./target/stegonography-1.0-SNAPSHOT-jar-with-dependencies.jar ./stegobmp.jar
```

## Ejecución

```
$ java -jar stegobmp [parametros]
```

## Comandos

* `-h / --help`:
    Ayuda.

### Extracción

* `-extract`
Indica que se va a extraer información.
* `-p bitmapfile`
Archivo bmp portador
* `-out file`
Archivo de salida obtenido
* `-steg <LSB1 | LSB4 | LSBE>`
algoritmo de esteganografiado: LSB de 1bit, LSB de 4 bits, LSB Enhanced

### Ocultamiento

* `-embed`:
Indica que se va a ocultar información.
* `-in [file]`:
Archivo que se va a ocultar.
* `-p [bitmapfile]`:
Archivo bmp que será el portador.
* `-out [bitmapfile]`:
Archivo bmp de salida, es decir, el archivo bitmapfile con la información de file
incrustada.
* `-steg <LSB1 | LSB4 | LSBE>`
algoritmo de esteganografiado: LSB de 1bit, LSB de 4 bits, LSB Enhanced 

### Parametros opcionales

* `-a <aes128 | aes192 | aes256 | des>`:
Algoritmo de encriptación
* `-m <ecb | cfb | ofb | cbc>`:
Método de encriptación
* `-pass password`:
Password de encripcion


