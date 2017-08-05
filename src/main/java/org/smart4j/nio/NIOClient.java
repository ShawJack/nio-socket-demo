package org.smart4j.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by ithink on 17-8-5.
 */
public class NIOClient implements Runnable{

    private SocketChannel socket;
    private static Charset charset = Charset.forName("UTF-8");

    public void run(){
        try {

            socket = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
            socket.configureBlocking(false);

            while(!socket.finishConnect())System.out.println("connecting.....");

            Scanner sc = new Scanner(System.in);
            while(true) {
                socket.write(charset.encode(Thread.currentThread().getName() + sc.nextLine()));
                TimeUnit.SECONDS.sleep(5);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
