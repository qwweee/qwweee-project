package Project.email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import Project.LogStream;
import Project.StaticManager;
import Project.config.Config;
import Project.utils.StreamUtil;

public class SendMail {
    private BufferedReader br;
    private BufferedWriter bw;
    private static SendMail instance;
    public static SendMail getInstance() {
        return (instance == null)?(instance = new SendMail()):instance;
    }
    private SendMail() {
    }
    public void sendMail(String message, String type, String option) {
        /*System.out.println(type);
        System.out.println("-------------------------------------------------");
        System.out.println(message);
        System.out.println("-------------------------------------------------");*/
        LogStream.getInstance().eventPrint("="+type+"=\n");
        LogStream.getInstance().eventPrint(message);
        return ;
        /*Socket smtpSocket = null;
        try {
            smtpSocket = new Socket(Config.SMTPSERVER, Config.SMTPPORT);
            br = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream(), StaticManager.MAILENCODE));
            bw = new BufferedWriter(new OutputStreamWriter(smtpSocket.getOutputStream(), StaticManager.MAILENCODE));
            
            receiveMessage();
            sendMessage("HELO ");
            receiveMessage();
            sendMessage("MAIL FROM: <"+Config.SENDERMAIL+">");
            receiveMessage();
            sendMessage("RCPT TO: <"+Config.MANAGERMAIL+">");
            receiveMessage();
            sendMessage("DATA");
            receiveMessage();
            sendMessage(String.format("Subject: [%s] %s \n", option, type));
            StringTokenizer token = new StringTokenizer(message, "\n");
            while (token.hasMoreTokens())
                sendMessage(token.nextToken());
            sendMessage("\n.");
            sendMessage("QUIT");
            receiveMessage();
            
            smtpSocket.close();
            StreamUtil.close(br, bw);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StreamUtil.close(br, bw);*/
    }
    private void sendMessage(String msg) throws IOException {
        bw.write(msg);
        bw.write("\r\n");
        bw.flush();
    }
    private void receiveMessage() throws IOException {
        String msg = br.readLine();
        //System.out.println(msg);
    }
}
