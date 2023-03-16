
# Cliente Servidor

Este es un ejemplo de una implementación de un servidor y cliente TCP. El servidor es capaz de recibir múltiples conexiones de clientes, y el cliente puede conectarse al servidor y enviar mensajes a través del socket.

## EchoTCPServer
La clase EchoTCPServer es la implementación del servidor TCP. Algunas de las características que tiene son las siguientes:

Tiene un mapa cuentas que almacena los nombres de usuario y objetos Cuenta.

El método readCuentasFromFile lee el archivo "dataBase.txt" y crea una instancia de Cuenta para cada usuario en el archivo, agregando el usuario y su instancia de cuenta al mapa.


El método init inicia el servidor y acepta conexiones entrantes de clientes. Cuando se recibe una conexión de un cliente, se crea un hilo para manejar las interacciones del cliente con el servidor. La lógica de la interacción se encuentra en el método menu.

## EchoTCPClient
La clase EchoTCPClient es la implementación del cliente TCP. Algunas de las características que tiene son las siguientes:

Tiene un método salirCuenta que envía un mensaje al servidor para salir de la cuenta actual.

El método createStreams crea los flujos de entrada y salida necesarios para enviar y recibir datos a través del socket.

El método init crea un socket y lo conecta al servidor. Una vez conectado, muestra un menú de opciones para que el usuario interactúe con el servidor.

## ¿Cómo utilizarlo?
Para utilizar este ejemplo, sigue estos pasos:

* Clona este repositorio en tu máquina local.
* Abre una terminal y navega hasta la carpeta entrega_final.
* Abre dos terminales, una para el servidor y otra para el cliente.
* En la terminal del servidor, ejecuta el comando java EchoTCPServer.
* En la terminal del cliente, ejecuta el comando java EchoTCPClient.
* Sigue las instrucciones en la terminal del cliente para interactuar con el servidor.

