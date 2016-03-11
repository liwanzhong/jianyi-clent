package org.jna.demo;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Created by liwanzhong on 2016/2/29.
 */
public interface JNACdeclDll extends Library {

    JNACdeclDll instanceDll  = (JNACdeclDll) Native.loadLibrary("lt_interface",JNACdeclDll.class);


    interface JavaCallback extends Callback
    {
        int cppCallback(String AType,String AContent);
    }


    void reg_callback_cdecl(JavaCallback callback);
    int  test_callback_cdecl(int AValue);



}
