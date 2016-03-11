import com.jianyi.view.LoginFrm;

import javax.swing.*;
import java.awt.*;

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
        LoginFrm loginFrm = new LoginFrm();
        loginFrm.setVisible(true);
    }
}
