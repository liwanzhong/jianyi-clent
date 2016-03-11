/*
 * Created by JFormDesigner on Wed Mar 02 01:30:14 CST 2016
 */

package com.jianyi.view;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author llll
 */
public class LoginFrm extends JFrame {
    public LoginFrm() {
        initComponents();
    }

    private void login_btnActionPerformed(ActionEvent e) {
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
        mapdata.put("username", usernametxt.getText());
        mapdata.put("password", new String(passwordpwd.getPassword()));
        // 签名
        try {
            mapdata.put("signature",RSASignature.sign(MapWithStringConvert.coverMap2String(mapdata)));
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



        HttpUriRequest reqMethod  = RequestBuilder.post().setUri("http://localhost:8080/citfc/client_call/login.shtml")
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
                //todo 保存用户信息到域
                ClientConfigLoader.getProperties().put("client.userid","3");
                this.dispose();
                new MainFrm().setVisible(true);
            }else{
                JOptionPane.showMessageDialog(this,retobj.get("error"));
            }
        }else{
            JOptionPane.showMessageDialog(this,"系统异常，请稍后再试");
        }

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("com.jianyi.view.resources.message");
        lab_username = new JLabel();
        lab_password = new JLabel();
        usernametxt = new JTextField();
        passwordpwd = new JPasswordField();
        login_title = new JLabel();
        login_btn = new JButton();
        reset_btn = new JButton();
        separator1 = new JSeparator();
        separator2 = new JSeparator();

        //======== this ========
        setResizable(false);
        setAlwaysOnTop(true);
        setTitle("\u4eba\u4f53\u673a\u80fd\u68c0\u6d4b\u8bc4\u4f30\u7cfb\u7edf");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();

        //---- lab_username ----
        lab_username.setText(bundle.getString("LoginFrm2.lab_username.text"));

        //---- lab_password ----
        lab_password.setText(bundle.getString("LoginFrm2.lab_password.text"));

        //---- login_title ----
        login_title.setText(bundle.getString("LoginFrm2.login_title.text"));
        login_title.setBackground(new Color(0, 102, 102));
        login_title.setFont(new Font("\u65b9\u6b63\u59da\u4f53", Font.BOLD, 22));

        //---- login_btn ----
        login_btn.setText(bundle.getString("LoginFrm2.login_btn.text"));
        login_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login_btnActionPerformed(e);
            }
        });

        //---- reset_btn ----
        reset_btn.setText(bundle.getString("LoginFrm2.reset_btn.text"));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(separator1, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(separator2, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(208, 208, 208)
                    .addComponent(login_btn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(28, 28, 28)
                    .addComponent(reset_btn)
                    .addGap(57, 57, 57))
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap(85, Short.MAX_VALUE)
                    .addComponent(login_title, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
                    .addGap(75, 75, 75))
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(lab_username)
                        .addComponent(lab_password))
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(usernametxt)
                        .addComponent(passwordpwd, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(27, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(login_title)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(33, 33, 33)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lab_username)
                        .addComponent(usernametxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(47, 47, 47)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lab_password)
                        .addComponent(passwordpwd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                    .addComponent(separator2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4, 4, 4)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(login_btn)
                        .addComponent(reset_btn))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel lab_username;
    private JLabel lab_password;
    private JTextField usernametxt;
    private JPasswordField passwordpwd;
    private JLabel login_title;
    private JButton login_btn;
    private JButton reset_btn;
    private JSeparator separator1;
    private JSeparator separator2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
