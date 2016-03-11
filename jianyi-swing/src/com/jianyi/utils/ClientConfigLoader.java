package com.jianyi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by liwanzhong on 2016/3/8.
 */
public class ClientConfigLoader {

    public  String FILE_NAME = "client_config.properties";


    private static ClientConfigLoader ourInstance = new ClientConfigLoader();

    private static Properties properties;



    public static Properties getProperties() {
        if(properties == null ){
            new ClientConfigLoader();
        }
        return properties;
    }

    private ClientConfigLoader() {
        if(properties==null){
            InputStream in=null;
            try {
                in = ClientConfigLoader.class.getClassLoader().getResourceAsStream(FILE_NAME);
                if(in != null) {
                    properties = new Properties();
                    try {
                        properties.load(in);
                    } catch (IOException var12) {
                        throw var12;
                    }
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            } finally {
                if(in != null) {
                    try {
                        in.close();
                    } catch (IOException var11) {
                        var11.printStackTrace();
                    }
                }

            }
        }
    }
}
