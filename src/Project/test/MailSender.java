package Project.test;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

class MailSender extends MailFrame implements ActionListener {
    BufferedReader in1;
    BufferedWriter out1;

    public MailSender(String F, String S) {
        super("Mail Sender");
        setFrom(F);
        setSMTP(S);
        setWindow();
        command.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)// 按下命令鈕時的事件程式
    {
        if (e.getSource() == command) {
            try {
                Socket sk1 = new Socket(tf3.getText(), 25);
                in1 = new BufferedReader(new InputStreamReader(sk1
                        .getInputStream()));
                out1 = new BufferedWriter(new OutputStreamWriter(sk1
                        .getOutputStream()));
                receiveMessage();
                sendMessage("HELO ");
                receiveMessage();
                sendMessage("MAIL FROM: <" + tf1.getText() + ">");
                receiveMessage();
                sendMessage("RCPT TO: <" + tf2.getText() + ">");
                receiveMessage();
                sendMessage("DATA");
                receiveMessage();
                sendMessage("Subject: " + tf4.getText());
                StringTokenizer token1 = new StringTokenizer(ta1.getText(),
                        "\n");
                while (token1.hasMoreTokens())
                    sendMessage(token1.nextToken());
                sendMessage("\n.");
                sendMessage("QUIT");
                receiveMessage();
                sk1.close();
            } catch (IOException e2) {
                System.out.println("發生錯誤");
            }
        }
    }

    public void sendMessage(String str1)// 傳送訊息的方法
    {
        try {
            ta2.append(str1);
            ta2.append("\n");
            out1.write(str1);
            out1.write("\r\n");
            out1.flush();
        } catch (Exception e3) {
            System.out.println("傳送資料發生錯誤");
        }
    }

    public void receiveMessage() throws IOException// 接收訊息的方法
    {
        String str1 = in1.readLine();
        if (str1 != null) {
            ta2.append(str1);
            ta2.append("\n");
        }
    }

    public static void main(String[] args) {
        MailSender a = new MailSender("plyeh@ncnu.edu.tw",
                "smtp.ncnu.edu.tw");
        a.setVisible(true);// 顯示視窗
    }
}
