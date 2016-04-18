package org.jna.lt_interface;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Created by liwanzhong on 2016/2/29.
 */
public interface LT_InterfaceJNAStdcallDll extends Library {

    LT_InterfaceJNAStdcallDll instanceDll  = (LT_InterfaceJNAStdcallDll) Native.loadLibrary("lt_interface",LT_InterfaceJNAStdcallDll.class);


    interface JavaCallbackAdd extends Callback{
        int callbackfSendMessage(String AType, String AContent);
    }

    int test_callback_stdcall(int AValue);

    int com_checkstatus();

    //注册回调函数
    int reg_callback_stdcall(JavaCallbackAdd callback)throws Exception;


    //串口初始化
    int com_init()throws Exception;

    //打开串口
    int com_open()throws Exception;

    //关闭串口
    int com_close()throws Exception;

    /**
     * 查询接触情况
     * 成功返回0，失败返回1，未初始化串口返回2。
     *
     * 回调类型	回调内容	说明
     A	0x30	二边都没有接触
     A	0x31	左边接触
     A	0x32	右边接触
     A	0x33	二边都接触
     */


    //移动杆进
    int com_movein ()throws Exception;

    //移动杆出
    int com_moveout ()throws Exception;
}