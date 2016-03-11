package com.jianyi.companate;

import javax.swing.*;
import java.awt.*;

/**
 * Created by liwanzhong on 2016/3/10.
 */
public class BackgroundPanel extends JPanel {

    private static final long serialVersionUID = -6352788025440244338L;

    private String image = null;

    public BackgroundPanel(String image) {
        this.image = image;
    }

    // 固定背景图片，允许这个JPanel可以在图片上添加其他组件
    /*protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }*/
    public void paintComponent(Graphics g){
        int x=0,y=0;
        java.net.URL imgURL=getClass().getResource(this.image);

        //test.jpg是测试图片，与Demo.java放在同一目录下
        ImageIcon icon=new ImageIcon(imgURL);
        g.drawImage(icon.getImage(),x,y,getSize().width,getSize().height,this);
        while(true){
            g.drawImage(icon.getImage(),x,y,this);
            if(x>getSize().width && y>getSize().height)break;
            //这段代码是为了保证在窗口大于图片时，图片仍能覆盖整个窗口
            if(x>getSize().width){
                x=0;
                y+=icon.getIconHeight();
            }else
                x+=icon.getIconWidth();
        }
    }

}
