/*
 * Created by JFormDesigner on Thu Mar 10 01:02:40 CST 2016
 */

package com.jianyi.view;

import com.jianyi.companate.BackgroundPanel;

import javax.swing.*;

/**
 *
 *http://www.cnblogs.com/SCAU_que/articles/1752677.html
 * @author llll
 */
public class TeastFrm extends JFrame {

    public TeastFrm() {
        initComponents();
    }

    private void initComponents() {


        //======== frame1 ========
        {

            JPanel panel  = new BackgroundPanel("/images/fengmian.jpg");
            this.getContentPane().add(panel);

            this.setExtendedState(JFrame.MAXIMIZED_BOTH);   //最大化
            this.setAlwaysOnTop(true);         //总在最前面
            this.setResizable(false);         //不能改变大小
            this.setUndecorated(true);         //不要边框
            this.setLocationRelativeTo(null);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    public static void main(String args[])throws Exception{
        TeastFrm teastFrm =  new TeastFrm();
        teastFrm.setVisible(true);
    }




}
