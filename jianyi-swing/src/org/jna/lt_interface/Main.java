package org.jna.lt_interface;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by liwanzhong on 2016/4/19.
 */
public class Main {

    public static void main(String[] args)throws Exception{
        while (true){
            Thread.sleep(500);
            String configpath = "D:/idea-workspack/works/ltcom/comdata.ini";
            FileInputStream fis = null; // 读
            Properties pp= new Properties();
            fis = new FileInputStream(configpath);
            pp.load(fis);
            String GPSvalue = pp.get("A").toString();
            System.out.println(GPSvalue);
            fis.close();
        }
    }

}
