/**
 * 
 */
package Project.utils;

import java.io.IOException;
import org.xbill.DNS.DClass;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;

/**
 * @author bbxp
 *
 */
public class ReverseDNS {
    /**
     * dns反查，ip所對應的dns
     * @param ip ip位址
     * @return String 如能查到dns則回傳dns，否則回傳ip
     * @throws IOException 
     */
    public static String reverseDns(String ip) throws IOException {
        Resolver res = new ExtendedResolver();
        Name name = ReverseMap.fromAddress(ip);
        int type = Type.PTR;
        int dclass = DClass.IN;
        Record rec = Record.newRecord(name, type, dclass);
        Message query = Message.newQuery(rec);
        Message response = res.send(query);

        Record[] answers = response.getSectionArray(Section.ANSWER);
        if (answers.length == 0)
           return ip;
        else
           return answers[0].rdataToString();
    }
}
