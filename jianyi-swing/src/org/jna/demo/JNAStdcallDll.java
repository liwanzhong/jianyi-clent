package org.jna.demo;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Created by liwanzhong on 2016/2/29.
 */
public interface JNAStdcallDll extends Library {

    JNAStdcallDll instanceDll  = (JNAStdcallDll) Native.loadLibrary("lt_interface",JNAStdcallDll.class);


    interface JavaCallbackAdd extends Callback
    {
        int callbackfSendMessage(String AType,String AContent);
    }


    void reg_callback_stdcall(JavaCallbackAdd callback);
    int test_callback_stdcall(int AValue);
}