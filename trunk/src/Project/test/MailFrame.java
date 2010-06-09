package Project.test;

import java.awt.*;
import javax.swing.*;

public class MailFrame extends JFrame {
    public String From;
    public String SMTPsrv;
    String DST;
    String Subject;
    public JButton command;
    Container c;
    public JTextField tf1, tf2, tf3, tf4;
    public JTextArea ta1, ta2;

    public MailFrame(String title) {
        super(title);
    }

    public void setWindow() {
        c = getContentPane();
        c.setLayout(null);
        JLabel lb1 = new JLabel("寄件者E_Mail:");
        lb1.setSize(100, 20);
        lb1.setLocation(10, 10);

        tf1 = new JTextField(From);
        tf1.setSize(370, 20);
        tf1.setLocation(110, 10);

        JLabel lb2 = new JLabel("收件者E_Mail:");
        lb2.setSize(100, 20);
        lb2.setLocation(10, 40);

        tf2 = new JTextField(DST);
        tf2.setSize(370, 20);
        tf2.setLocation(110, 40);

        JLabel lb3 = new JLabel("SMTP Server:");
        lb3.setSize(100, 20);
        lb3.setLocation(10, 70);

        tf3 = new JTextField(SMTPsrv);
        tf3.setSize(370, 20);
        tf3.setLocation(110, 70);

        JLabel lb4 = new JLabel("主旨:");
        lb4.setSize(100, 20);
        lb4.setLocation(10, 100);

        tf4 = new JTextField(Subject);
        tf4.setSize(370, 20);
        tf4.setLocation(110, 100);
        ta1 = new JTextArea("信紙");
        ta1.setSize(470, 120);
        ta1.setLocation(10, 130);

        ta2 = new JTextArea("回傳 message !");
        ta2.setSize(470, 150);
        ta2.setLocation(10, 260);

        c.add(lb1);
        c.add(tf1);
        c.add(lb2);
        c.add(tf2);
        c.add(lb3);
        c.add(tf3);
        c.add(lb4);
        c.add(tf4);
        c.add(ta1);
        c.add(ta2);

        command = new JButton("傳送郵件");
        command.setSize(100, 20);
        command.setLocation(200, 420);
        c.add(command);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 480); // 設定視窗大小
    }

    public void setFrom(String f) {
        From = f;
    }

    public void setSMTP(String s) {
        SMTPsrv = s;
    }

    public static void main(String args[]) {
        MailFrame a = new MailFrame("Send Mail");
        a.setFrom("test@msa.hinet.net");
        a.setSMTP("msa.hinet.net");
        a.setWindow();
        a.setVisible(true);
    }
}
