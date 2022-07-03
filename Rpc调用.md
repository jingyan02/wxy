package com.rpc.client;

import rpcservice.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcNetTransport {

```java
private String host;
private int port;

public RpcNetTransport(String host, int port) {
    this.host = host;
    this.port = port;
}

public Object send(RpcRequest request){
    Object result = null;
    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;
    try {
        //client请求远程服务器
        Socket socket = new Socket(host, port);
        //传输数据
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        
        //接收Server端返回的参数
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        result = objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }finally {
        if(objectInputStream!=null){
            try {
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(objectOutputStream!=null){
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    return result;
 }
}
```
