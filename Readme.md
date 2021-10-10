# StoreFetchApp

### Command

* To Deploy the server use the following command
````shell
java reza.server.AppServer
````

* Client side command

```shell
java reza.client.StoreFetchApp [option] [source] [server-address : optional] [encoding-method : optional]
```

* Client side command for getting file and directory list of the specified path relative to the server.

```shell
java reza.client.StoreFetchApp ls [directory-name] [server-address : optional]
```

### Parameters


__option__: Available options: [store | fetch | ls]

__source__ : The file that needs to be sent or fetch. File must be provided with extension.

__server-address__: provide server address and port. (optional) [default: localhost:3000]

__encoding-method__: values: base64, character (optional) [default: base64]

server-address can be porvided as only the __IP__ or as __IP:PORT__


### Example
* To send a file named cat.jpg use the following command

```shell
java reza.client.StoreFetchApp store cat.jpg
```
or
```shell
java reza.client.StoreFetchApp store cat.jpg 127.0.0.1
```
or
```shell
java reza.client.StoreFetchApp store cat.jpg 127.0.0.1:3000
```


* To fetch a file from the server use the following command

```shell
java reza.client.StoreFetchApp fetch cat.jpg
```
or
```shell
java reza.client.StoreFetchApp fetch cat.jpg 127.0.0.1
```
or
```shell
java reza.client.StoreFetchApp fetch cat.jpg 127.0.0.1:3000
```

* To fetch the directory list from the server use the following command

```shell
java reza.client.StoreFetchApp ls ./directory-name
```
or
```shell
java reza.client.StoreFetchApp ls ./directory-name 127.0.0.1
```
or
```shell
java reza.client.StoreFetchApp ls ./directory-name 127.0.0.1:3000
```