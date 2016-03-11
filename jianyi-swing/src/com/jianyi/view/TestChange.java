/*
 * Created by JFormDesigner on Thu Mar 10 23:08:24 CST 2016
 */

package com.jianyi.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.intellij.uiDesigner.core.*;

/**
 * @author llll
 */
public class TestChange extends JFrame {
    public TestChange() {
        initComponents();
    }

    private void button2ActionPerformed(ActionEvent e) {
        System.out.println("sssssssssssss");
    }

    private void button3ActionPerformed(ActionEvent e) {
        System.out.println("55555555555");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("com.jianyi.view.resources.message");
        frame1 = new JFrame();
        panel1 = new JPanel();
        button2 = new JButton();
        button3 = new JButton();
        separator1 = new JSeparator();
        panel2 = new JPanel();
        JLabel 第一张卡片 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();

        //======== frame1 ========
        {
            frame1.setTitle(bundle.getString("TestChange.frame1.title"));
            Container frame1ContentPane = frame1.getContentPane();
            frame1ContentPane.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1, true, true));

            //======== panel1 ========
            {
                panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));

                //---- button2 ----
                button2.setText(bundle.getString("TestChange.button2.text"));
                button2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        button2ActionPerformed(e);
                    }
                });
                panel1.add(button2, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

                //---- button3 ----
                button3.setText(bundle.getString("TestChange.button3.text"));
                button3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        button3ActionPerformed(e);
                    }
                });
                panel1.add(button3, new GridConstraints(0, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
            }
            frame1ContentPane.add(panel1, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
            frame1ContentPane.add(separator1, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //======== panel2 ========
            {
                panel2.setLayout(new CardLayout());

                //---- 第一张卡片 ----
                第一张卡片.setText(bundle.getString("TestChange.label1.text"));
                panel2.add(第一张卡片, "card1");

                //---- label2 ----
                label2.setText(bundle.getString("TestChange.label2.text"));
                panel2.add(label2, "card2");

                //---- label3 ----
                label3.setText(bundle.getString("TestChange.label3.text"));
                panel2.add(label3, "card3");

                //---- label4 ----
                label4.setText(bundle.getString("TestChange.label4.text"));
                panel2.add(label4, "card4");

                //---- label5 ----
                label5.setText(bundle.getString("TestChange.label5.text"));
                panel2.add(label5, "card6");
            }
            frame1ContentPane.add(panel2, new GridConstraints(2, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
            frame1.pack();
            frame1.setLocationRelativeTo(frame1.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame frame1;
    private JPanel panel1;
    private JButton button2;
    private JButton button3;
    private JSeparator separator1;
    private JPanel panel2;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void  main(String [] args){
        TestChange testChange = new TestChange();
        testChange.setVisible(true);
    }
}
