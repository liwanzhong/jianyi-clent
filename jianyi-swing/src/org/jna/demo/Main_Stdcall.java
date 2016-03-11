package org.jna.demo;

/**
 * Created by liwanzhong on 2016/2/29.
 */
public class Main_Stdcall {

    public static void main(String[] args)throws Exception{
        System.setProperty("jna.encoding","GBK");
        JNAStdcallDll.JavaCallbackAdd callback=new JNAStdcallDll.JavaCallbackAdd()
        {


            @Override
            public int callbackfSendMessage(String AType, String AContent) {
                System.out.println(AType);
                System.out.println(AContent);
                return 0;
            }
        };

        JNAStdcallDll.instanceDll.reg_callback_stdcall(callback);
        JNAStdcallDll.instanceDll.test_callback_stdcall(0);
    }
}
