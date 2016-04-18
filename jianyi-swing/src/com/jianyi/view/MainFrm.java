/*
 * Created by JFormDesigner on Tue Mar 08 00:02:53 CST 2016
 */

package com.jianyi.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.*;
import javax.swing.text.html.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.uiDesigner.core.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jianyi.utils.ClientConfigLoader;
import com.jianyi.utils.httpclient.FinalHttpClient;
import com.jianyi.utils.sign.MapWithStringConvert;
import com.jianyi.utils.sign.RSASignature;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.net.dns.ResolverConfiguration;

/**
 * @author llll
 */
public class MainFrm extends JFrame {

    /**获取屏幕的边界*/
    private Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
    /**获取底部任务栏高度*/
    private int taskBarHeight = screenInsets.bottom;

    public MainFrm() {
        initComponents();
    }

    //查询用户信息事件处理
    private void query_btnActionPerformed(ActionEvent e) {
        //todo 获取相关信息
        String custom_name = custom_name_txt.getText();
        String phone = custom_phone_txt.getText();
        if(StringUtils.isBlank(custom_name) && StringUtils.isBlank(phone)){
            JOptionPane.showMessageDialog(this,"请至少输入一项以供查询!");
            return;
        }
        //todo 调用查询系统接口查询用户信息
        CookieStore cookieStore = new BasicCookieStore();
        // Populate cookies if needed
        BasicClientCookie usernameCookie = new BasicClientCookie("username", "value");
        usernameCookie.setDomain(".mycompany.com");
        usernameCookie.setPath("/");
        cookieStore.addCookie(usernameCookie);
        BasicClientCookie passwordCookie = new BasicClientCookie("password", "value");
        passwordCookie.setDomain(".mycompany.com");
        passwordCookie.setPath("/");
        cookieStore.addCookie(passwordCookie);

        CloseableHttpClient httpClient = FinalHttpClient.getInstance(cookieStore).getHttpClient();



        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();


        Map<String,String> mapdata =new HashMap<String, String>();
        mapdata.put("customName",custom_name);
        mapdata.put("phone", phone);
        mapdata.put("meathcode", ClientConfigLoader.getProperties().getProperty("server.meathcode"));//读取配置文件，获取机器码
//        mapdata.put("userid",ClientConfigLoader.getProperties().getProperty("client.userid"));
        mapdata.put("userid","4");
        // 签名
        try {
            mapdata.put("signature", RSASignature.sign(MapWithStringConvert.coverMap2String(mapdata)));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<String> keyset = mapdata.keySet();
        Iterator<String> iterator = keyset.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = mapdata.get(key);
            NameValuePair nameValuePair = new BasicNameValuePair(key, value);
            params.add(nameValuePair);
        }



        HttpUriRequest reqMethod  = RequestBuilder.post().setUri("http://localhost:8080/citfc/client_call/querycustom.shtml")
                .addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                .setConfig(requestConfig).build();
        JSONObject retobj = null ;
        try {
            CloseableHttpResponse response =  httpClient.execute(reqMethod);
            if(response.getStatusLine().getStatusCode()==200){
                String result = EntityUtils.toString(response.getEntity());
                if(StringUtils.isBlank(result)){
                    throw new Exception("请求异常!");
                }
                retobj = JSON.parseObject(result);
                System.out.println("返回的结果====="+ retobj.toJSONString());
                EntityUtils.consume(response.getEntity());


            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if(retobj != null){
            if(retobj.get("status").equals(1)){
                //todo 查询出来结果以后绑定到表格中展示
                DefaultTableModel tableModel = (DefaultTableModel) custom_list_tb.getModel();
                tableModel.setRowCount(0);
                JSONArray jsonArray =(JSONArray) retobj.get("data");
                for(Object item:jsonArray){
                    JSONObject customItem = (JSONObject)item;
                    Vector rowdata = new Vector();
                    rowdata.add(customItem.get("id"));
                    rowdata.add(customItem.get("name"));
                    rowdata.add(StringUtils.equalsIgnoreCase(customItem.get("sex").toString(),"1")?"男":"女");
                    rowdata.add("22");
                    rowdata.add(customItem.get("body_height"));
                    rowdata.add(customItem.get("weight"));
                    tableModel.addRow(rowdata);
                }


            }else{
                JOptionPane.showMessageDialog(this,retobj.get("error"));
            }
        }else{
            JOptionPane.showMessageDialog(this,"系统异常，请稍后再试");
        }


    }


    protected void hideColumn(JTable table,int index){
        TableColumn tc= table.getColumnModel().getColumn(index);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setMinWidth(0);
        tc.setWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }

    /**
     * 选择客户进行检测
     * @param e
     */
    private void teast_btnActionPerformed(ActionEvent e) {
        //todo 判断表格是否有被选中行
        int [] selectrows =  custom_list_tb.getSelectedRows();
        if(selectrows == null || selectrows.length ==0){
            JOptionPane.showMessageDialog(this,"请在表格中选择需要检测的客户!");
            return;
        }
        DefaultTableModel tableModel = (DefaultTableModel) custom_list_tb.getModel();
        for(int index:selectrows){
            System.out.println(tableModel.getValueAt(index,0).toString());
            //todo 跳转到检测界面
            new TeastFrm().setVisible(true);
        }


    }

    private void custom_jiance_btnActionPerformed(ActionEvent e) {
//        JOptionPane.showMessageDialog(this,"体检检测按钮");
        cardLayout.show(card_layout,"card1");
    }

    private void system_config_btnActionPerformed(ActionEvent e) {
//        JOptionPane.showMessageDialog(this,"系统设置按钮");
        cardLayout.show(card_layout,"card2");
    }

    private void normal_ques_btnActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this,"功能开发中！");
    }

    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("com.jianyi.view.resources.message");
        panel1 = new JPanel();
        custom_jiance_btn = new JButton();
        system_config_btn = new JButton();
        normal_ques_btn = new JButton();
        card_layout = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        label1 = new JLabel();
        custom_name_txt = new JTextField();
        label2 = new JLabel();
        custom_phone_txt = new JTextField();
        panel5 = new JPanel();
        query_btn = new JButton();
        panel6 = new JPanel();
        teast_btn = new JButton();
        scrollPane1 = new JScrollPane();
        custom_list_tb = new JTable();
        panel8 = new JPanel();
        label3 = new JLabel();
        textField1 = new JTextField();
        label4 = new JLabel();
        textField2 = new JTextField();
        label5 = new JLabel();
        textField3 = new JTextField();
        label6 = new JLabel();
        comboBox1 = new JComboBox();
        cardLayout = new CardLayout();

        //======== this ========
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("\u4eba\u4f53\u673a\u80fd\u68c0\u6d4b\u8bc4\u4f30\u7cfb\u7edf");
        setMinimumSize(new Dimension(12, 28));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(2, 2, 2, 2), 0, 0));

        //======== panel1 ========
        {
            panel1.setBorder(null);
            panel1.setAutoscrolls(true);
            panel1.setBackground(new Color(0, 204, 204));
            panel1.setLayout(new GridLayoutManager(1, 5, new Insets(2, 2, 2, 2), 0, 0, true, true));

            //---- custom_jiance_btn ----
            custom_jiance_btn.setIcon(new ImageIcon(ImageView.class.getResource("/images/examine.png")));
            custom_jiance_btn.setHorizontalTextPosition(SwingConstants.CENTER);
            custom_jiance_btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            custom_jiance_btn.setRolloverIcon(new ImageIcon(ImageView.class.getResource("/images/examine-active.png")));
            custom_jiance_btn.setBorderPainted(false);
            custom_jiance_btn.setFocusPainted(false);
            custom_jiance_btn.setContentAreaFilled(false);
            custom_jiance_btn.setFocusable(true);
            custom_jiance_btn.setMargin(new Insets(0, 0, 0, 0));
            custom_jiance_btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    custom_jiance_btnActionPerformed(e);
                }
            });
            panel1.add(custom_jiance_btn, new GridConstraints(0, 1, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

            //---- system_config_btn ----
//            system_config_btn.setText(bundle.getString("MainFrm.system_config_btn.text"));
            system_config_btn.setIcon(new ImageIcon(ImageView.class.getResource("/images/setting.png")));
            system_config_btn.setHorizontalTextPosition(SwingConstants.CENTER);
            system_config_btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            system_config_btn.setRolloverIcon(new ImageIcon(ImageView.class.getResource("/images/setting-active.png")));
            system_config_btn.setBorderPainted(false);
            system_config_btn.setFocusPainted(false);
            system_config_btn.setContentAreaFilled(false);
            system_config_btn.setFocusable(true);
            system_config_btn.setMargin(new Insets(0, 0, 0, 0));
            system_config_btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    system_config_btnActionPerformed(e);
                }
            });
            panel1.add(system_config_btn, new GridConstraints(0, 2, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));

            //---- normal_ques_btn ----
            normal_ques_btn.setIcon(new ImageIcon(ImageView.class.getResource("/images/question.png")));
            normal_ques_btn.setHorizontalTextPosition(SwingConstants.CENTER);
            normal_ques_btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            normal_ques_btn.setRolloverIcon(new ImageIcon(ImageView.class.getResource("/images/question-active.png")));
            normal_ques_btn.setBorderPainted(false);
            normal_ques_btn.setFocusPainted(false);
            normal_ques_btn.setContentAreaFilled(false);
            normal_ques_btn.setFocusable(true);
            normal_ques_btn.setMargin(new Insets(0, 0, 0, 0));
            normal_ques_btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    normal_ques_btnActionPerformed(e);
                }
            });
            panel1.add(normal_ques_btn, new GridConstraints(0, 3, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
        }
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null, 0, true));

        //======== card_layout ========
        {
            card_layout.setLayout(cardLayout);

            //======== panel2 ========
            {
                panel2.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), 0, 0));

                //======== panel3 ========
                {
                    panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 0, 0));

                    //======== panel4 ========
                    {
                        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1, true, true));

                        //---- label1 ----
                        label1.setText(bundle.getString("MainFrm.label1.text_2"));
                        label1.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel4.add(label1, new GridConstraints(0, 0, 1, 1,
                                GridConstraints.ANCHOR_EAST, GridConstraints.FILL_BOTH,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                null, null, null, 1));

                        panel4.add(custom_name_txt, new GridConstraints(0, 1, 1, 1,
                                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                null, null, null));


                        //---- label2 ----
                        label2.setText(bundle.getString("MainFrm.label2.text"));
                        label2.setHorizontalTextPosition(SwingConstants.RIGHT);
                        label2.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel4.add(label2, new GridConstraints(1, 0, 1, 1,
                                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                null, null, null));
                        panel4.add(custom_phone_txt, new GridConstraints(1, 1, 1, 1,
                                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                null, null, null));
                    }
                    panel3.add(panel4, new GridConstraints(0, 0, 1, 1,
                            GridConstraints.ANCHOR_EAST, GridConstraints.FILL_BOTH,
                            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                            null, null, null));

                    //======== panel5 ========
                    {
                        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 0, 0));

                        //---- query_btn ----
                        query_btn.setText(bundle.getString("MainFrm.query_btn.text"));

                        query_btn.setPreferredSize(new Dimension(80, 40));// 设置按钮大小
                        query_btn.setFont(new Font("粗体", Font.PLAIN, 22));// 按钮文本样式
                        query_btn.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
                        query_btn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                query_btnActionPerformed(e);
                            }
                        });
                        panel5.add(query_btn, new GridConstraints(0, 0, 1, 1,
                                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                null, null, null));

                        //======== panel6 ========
                        {
                            panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 0, 0));

                            //---- teast_btn ----
                            teast_btn.setText(bundle.getString("MainFrm.teast_btn.text"));
                            teast_btn.setPreferredSize(new Dimension(80, 40));// 设置按钮大小
                            teast_btn.setFont(new Font("粗体", Font.PLAIN, 22));// 按钮文本样式
                            teast_btn.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
                            teast_btn.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    teast_btnActionPerformed(e);
                                }
                            });
                            panel6.add(teast_btn, new GridConstraints(0, 0, 1, 1,
                                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                    null, null, null));
                        }
                        panel5.add(panel6, new GridConstraints(0, 1, 1, 1,
                                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                null, null, null));
                    }
                    panel3.add(panel5, new GridConstraints(0, 1, 1, 1,
                            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                            null, null, null));
                }
                panel2.add(panel3, new GridConstraints(0, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                //======== scrollPane1 ========
                {

                    //---- custom_list_tb ----
                    custom_list_tb.setAutoCreateRowSorter(true);
                    custom_list_tb.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                    custom_list_tb.setRowHeight(35);
//                    custom_list_tb.setBorder(null);

                    JTableHeader header = custom_list_tb.getTableHeader();          //设置字体
                    header.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                    header.setPreferredSize(new Dimension(header.getWidth(), 40));

                    custom_list_tb.setModel(new DefaultTableModel(
                            new Object[][] {
                            },
                            new String[] {
                                    "id", "\u59d3\u540d", "\u6027\u522b", "\u5e74\u9f84", "\u8eab\u9ad8(cm)", "\u4f53\u91cd(kg)"
                            }
                    ) {
                        Class<?>[] columnTypes = new Class<?>[] {
                                Object.class, String.class, String.class, Integer.class, Integer.class, Double.class
                        };
                        boolean[] columnEditable = new boolean[] {
                                true, false, false, false, false, false
                        };
                        @Override
                        public Class<?> getColumnClass(int columnIndex) {
                            return columnTypes[columnIndex];
                        }
                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return columnEditable[columnIndex];
                        }
                    });
                    {
                        TableColumnModel cm = custom_list_tb.getColumnModel();
                        cm.getColumn(2).setCellEditor(new DefaultCellEditor(
                                new JComboBox(new DefaultComboBoxModel(new String[] {
                                        "\u7537",
                                        "\u5973"
                                }))));
                    }
                    custom_list_tb.setShowVerticalLines(false);
                    scrollPane1.setViewportView(custom_list_tb);
                }
                panel2.add(scrollPane1, new GridConstraints(2, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        new Dimension(0, 0), null, null, 0, true));
            }
            card_layout.add(panel2, "card1");

            //======== panel8 ========
            {
                panel8.setLayout(new FormLayout(
                        "6*(default, $lcgap), default",
                        "8*(default, $lgap), default"));

                //---- label3 ----
                label3.setText(bundle.getString("MainFrm.label3.text"));
                panel8.add(label3, CC.xy(11, 5));
                panel8.add(textField1, CC.xy(13, 5, CC.FILL, CC.DEFAULT));

                //---- label4 ----
                label4.setText(bundle.getString("MainFrm.label4.text"));
                panel8.add(label4, CC.xy(11, 9));
                panel8.add(textField2, CC.xy(13, 9));

                //---- label5 ----
                label5.setText(bundle.getString("MainFrm.label5.text"));
                panel8.add(label5, CC.xy(11, 13));
                panel8.add(textField3, CC.xy(13, 13));

                //---- label6 ----
                label6.setText(bundle.getString("MainFrm.label6.text"));
                panel8.add(label6, CC.xy(11, 17));
                panel8.add(comboBox1, CC.xy(13, 17));
            }
            card_layout.add(panel8, "card2");
        }
        contentPane.add(card_layout, new GridConstraints(2, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JButton custom_jiance_btn;
    private JButton system_config_btn;
    private JButton normal_ques_btn;
    private JPanel card_layout;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JLabel label1;
    private JTextField custom_name_txt;
    private JLabel label2;
    private JTextField custom_phone_txt;
    private JPanel panel5;
    private JButton query_btn;
    private JPanel panel6;
    private JButton teast_btn;
    private JScrollPane scrollPane1;
    private JTable custom_list_tb;
    private JPanel panel8;
    private JLabel label3;
    private JTextField textField1;
    private JLabel label4;
    private JTextField textField2;
    private JLabel label5;
    private JTextField textField3;
    private JLabel label6;
    private JComboBox comboBox1;
    private CardLayout cardLayout;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    /**
      * Fix the bug "jframe undecorated cover taskbar when maximized". See:
      * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4737788
      *
      * @param state
      */


}
