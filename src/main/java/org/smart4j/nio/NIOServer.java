package org.smart4j.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ithink on 17-8-5.
 */
public class NIOServer {

    //监听TCP连接的通道，类似标准IO中的ServerSocket
    private static ServerSocketChannel server;
    //负责监视socket的IO状态
    private static Selector selector;
    //
    private static Charset charset = Charset.forName("UTF-8");

    public static void initServer(String ip, int port) throws IOException{
        server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(ip, port));
        server.configureBlocking(false);

        selector = Selector.open();

        //将server注册到selector中,selector可以监听到server的“接收就绪状态”
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void start() throws IOException{
        while(selector.select() > 0){
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if(selectionKey.isAcceptable()){
                    SocketChannel socket = server.accept();
                    if(socket != null) {
                        socket.configureBlocking(false);
                        socket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

                        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
                    }
                }else if(selectionKey.isReadable()){
                    SocketChannel socket = (SocketChannel)selectionKey.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(50);
                    //StringBuffer sb = new StringBuffer();
                    while(socket.read(buffer) > 0) {
                        buffer.flip();
                        while(buffer.hasRemaining()){
                            System.out.print((char)buffer.get());
                        }
                        System.out.println("");
                        buffer.clear();
                    }
                    //System.out.println(sb.toString());

                    selectionKey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException{
        initServer("127.0.0.1", 8080);
        start();
    }

}
