import com.jianyi.view.LoginFrm;
import com.jianyi.view.TestFrm;
import com.jtattoo.plaf.smart.SmartLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * Created by liwanzhong on 2016/3/2.
 */
public class Main {

    public static void main(String[] args){
        Font font = new Font("Dialog", Font.PLAIN, 12);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }

        Properties props = new Properties();

        props.put("logoString", "my company");
        props.put("licenseKey", "INSERT YOUR LICENSE KEY HERE");

        props.put("selectionBackgroundColor", "180 240 197");
        props.put("menuSelectionBackgroundColor", "180 240 197");

        props.put("controlColor", "218 254 230");
        props.put("controlColorLight", "218 254 230");
        props.put("controlColorDark", "180 240 197");

        props.put("buttonColor", "218 230 254");
        props.put("buttonColorLight", "255 255 255");
        props.put("buttonColorDark", "244 242 232");

        props.put("rolloverColor", "218 254 230");
        props.put("rolloverColorLight", "218 254 230");
        props.put("rolloverColorDark", "180 240 197");

        props.put("windowTitleForegroundColor", "0 0 0");
        props.put("windowTitleBackgroundColor", "180 240 197");
        props.put("windowTitleColorLight", "218 254 230");
        props.put("windowTitleColorDark", "180 240 197");
        props.put("windowBorderColor", "218 254 230");

        // set your theme
//        SmartLookAndFeel.setCurrentTheme(props);
        // select the Look and Feel
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        LoginFrm loginFrm = new LoginFrm();
        loginFrm.setVisible(true);
    }
}
