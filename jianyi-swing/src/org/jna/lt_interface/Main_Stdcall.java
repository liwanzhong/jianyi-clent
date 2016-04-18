package org.jna.lt_interface;

import org.jna.demo.JNAStdcallDll;

/**
 * Created by liwanzhong on 2016/2/29.
 */
public class Main_Stdcall {

    public static void main(String[] args)throws Exception{
        System.setProperty("jna.encoding","GBK");
        LT_InterfaceJNAStdcallDll.JavaCallbackAdd callback=new LT_InterfaceJNAStdcallDll.JavaCallbackAdd()
        {
            @Override
            public int callbackfSendMessage(String AType, String AContent) {
                System.out.println(AType);
                System.out.println(AContent);
                return 0;
            }
        };

        System.out.println(LT_InterfaceJNAStdcallDll.instanceDll.reg_callback_stdcall(callback));;
        System.out.println(LT_InterfaceJNAStdcallDll.instanceDll.com_init());
        System.out.println(LT_InterfaceJNAStdcallDll.instanceDll.com_open());
        System.out.println(LT_InterfaceJNAStdcallDll.instanceDll.com_checkstatus());

        System.out.println(System.getProperty("java.library.path"));

//        LT_InterfaceJNAStdcallDll.instanceDll.com_checkstatus();

//        LT_InterfaceJNAStdcallDll.instanceDll.test_callback_stdcall(1);


        Thread.sleep(10001);

    }
}
