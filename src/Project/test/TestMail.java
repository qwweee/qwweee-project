package Project.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import Project.StaticManager;
class testMail {
    public static void main(String args[]) {
        try {
            Socket  s = new Socket("smtp.ncnu.edu.tw", 25);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(), StaticManager.MAILENCODE));
            PrintStream out = new PrintStream(s.getOutputStream(), false, "BIG5");
            out.println("HELO ");
            receiveMessage(br);
            out.println("MAIL FROM:<test@ncnu.edu.tw>");
            receiveMessage(br);
            out.println("RCPT TO:<s97213521@ncnu.edu.tw>");
            receiveMessage(br);
            out.println("DATA");
            receiveMessage(br);
            out.println("Subject: email test from java\nhello mail@otherdomain.ext\nfrom mail@domain.ext\n");
            receiveMessage(br);
            out.println("這是利用 Java 造的 fake mail\r\n.\r\n");
            receiveMessage(br);
            out.println("QUIT");
            receiveMessage(br);
            out.flush();
            out.close();
        } catch (Throwable e) {
            System.err.println(e);
        }
    }
    private static void receiveMessage(BufferedReader br) throws IOException {
        String msg = br.readLine();
        System.out.println(msg);
    }
}
