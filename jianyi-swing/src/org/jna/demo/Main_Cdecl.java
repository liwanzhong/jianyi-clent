package org.jna.demo;

/**
 * Created by liwanzhong on 2016/2/29.
 */
public class Main_Cdecl {

    public static void main(String[] args)throws Exception{
        System.setProperty("jna.encoding","GBK");
        JNACdeclDll.JavaCallback callback=new JNACdeclDll.JavaCallback()
        {


            @Override
            public int cppCallback(String AType, String AContent) {
                System.out.println(AType);
                System.out.println(AContent);
                return 0;
            }
        };

        JNACdeclDll.instanceDll.reg_callback_cdecl(callback);
        JNACdeclDll.instanceDll.test_callback_cdecl(0);
    }
}
