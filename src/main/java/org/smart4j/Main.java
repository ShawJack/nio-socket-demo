package org.smart4j;

import org.smart4j.nio.NIOClient;
import org.smart4j.nio.NIOServer;

import java.io.IOException;

/**
 * Created by ithink on 17-8-5.
 */
public class Main {

    public static void main(String[] args) throws IOException{

        for(int i=0; i<1; i++){
            new Thread(new NIOClient(), "thread#"+(int)(Math.random()*10)).start();
        }
    }

}
