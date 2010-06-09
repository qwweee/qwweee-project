package Project.test;

/*$Id: getSnmpTable.src,v 1.3.2.4 2009/01/28 12:45:56 prathika Exp $*/
/*
 * @(#)getSnmpTable.java
 * Copyright (c) 1996-2009 AdventNet, Inc. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 */

/** 
 *  This is an example of using the SnmpTable class.
 *  This is a command line application that does not require JFC/swing
 *  components.  See the swingapps directory for examples that need
 *  and use swing components.
 *
 * [-d]                - Debug output. By default off.
 * [-c] <community>    - community String. By default "public".
 * [-p] <port>         - remote port no. By default 161.
 * [-t] <Timeout>      - Timeout. By default 5000ms.
 * [-r] <Retries>      - Retries. By default 0.      
 * [-v] <version>      - version(v1 / v2 / v3). By default v1.
 * [-u] <username>     - The v3 principal/userName
 * [-a] <autProtocol>  - The authProtocol(MD5/SHA). Mandatory if authPassword is specified
 * [-w] <authPassword> - The authentication password.
 * [-s] <privPassword> - The privacy protocol password. Must be accompanied with auth password and authProtocol fields.
 * [-n] <contextName>  - The snmpv3 contextName to be used.
 * [-i] <contextID>    - The snmpv3 contextID to be used.
 * [-pp] <privProtocol> - The privacy protocol. Must be accompanied with auth,priv password and authProtocol fields.
 * host Mandatory      - The RemoteHost (agent).Format (string without double qoutes/IpAddress).
 * tableOID  Mandatory - Give the Object Identifier of a Table.
 * mibs                - The mibs to be loaded.Mandatory.
 */

import Project.StaticManager;
import Project.config.Config;

import com.adventnet.snmp.beans.*;
import com.adventnet.snmp.mibs.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TestSnmpget implements SnmpTableListener {
    private int count;
    public TestSnmpget() {
        this.count = 0;
    }

    public static void main(String args[]) throws InterruptedException {
        Config.Load();
        System.out.println(Thread.activeCount());
        Thread[] array = new Thread[Thread.activeCount()];
        System.out.println(Thread.enumerate(array));
        for (int i = 0 ; i < array.length ; i ++) {
            Thread t = array[i];
            Class<? extends Thread> c = array[i].getClass();
            System.out.println(String.format("%4s\t%s\t%s\t%s",t.isAlive(),t.getName(),c.getSimpleName(),""));
        }
    // instantiate an SnmpTable instance
        String ip = "163.22.32.101";
    SnmpTable table = new SnmpTable();
    System.out.println(Thread.activeCount());
    array = new Thread[Thread.activeCount()];
    System.out.println(Thread.enumerate(array));
    for (int i = 0 ; i < array.length ; i ++) {
        Thread t = array[i];
        Class<? extends Thread> c = array[i].getClass();
        System.out.println(String.format("%4s\t%s\t%s\t%s",t.isAlive(),t.getName(),c.getSimpleName(),""));
    }
     //To load MIBs from compiled file
     table.setLoadFromCompiledMibs(true);
     table.setTargetHost(ip);
     table.setCommunity("public");
     try {
         table.loadMibs("D:/工作區/Eclipse_Project/snmp/Trapd_Program/bin/mib/RFC1213-MIB");
         table.loadMibs("D:/工作區/Eclipse_Project/snmp/Trapd_Program/bin/mib/HR-MIB");
         table.setTableOID(Config.TCPCONNECT);
     } catch (FileNotFoundException e) {
         e.printStackTrace();
         System.exit(1);
     } catch (MibException e) {
         e.printStackTrace();
         System.exit(1);
     } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
     } catch (DataException e) {
         e.printStackTrace();
     }
     TestSnmpget gettable = new TestSnmpget();
     table.setPollInterval(1);
     table.addSnmpTableListener(gettable);
     System.out.println(table);
     System.out.println(Thread.activeCount());
     array = new Thread[Thread.activeCount()];
     System.out.println(Thread.enumerate(array));
     for (int i = 0 ; i < array.length ; i ++) {
         Thread t = array[i];
         Class<? extends Thread> c = array[i].getClass();
         System.out.println(String.format("%4s\t%s\t%s\t%s",t.isAlive(),t.getName(),c.getSimpleName(),""));
     }
    }


    /* This is the listener method which is notified of table changes **/
    public void tableChanged(SnmpTableEvent e) {

    SnmpTable table = (SnmpTable)e.getSource();
    if (e.isEndOfTable()) { // 最後抓到資料的時候
        StaticManager.printDate(System.currentTimeMillis());
        //System.out.println(count);
        count ++;
        if (count == 2) {
            table.setPollInterval(2);
        }
        if (count == 3) {
            table.stopPollingTable();
            table.removeSnmpTableListener(this);
            System.out.println(Thread.activeCount());
            Thread[] array = new Thread[Thread.activeCount()];
            System.out.println(Thread.enumerate(array));
            for (int i = 0 ; i < array.length ; i ++) {
                Thread t = array[i];
                Class<? extends Thread> c = array[i].getClass();
                System.out.println(String.format("%4s\t%s\t%s\t%s",t.isAlive(),t.getName(),c.getSimpleName(),t.getId()));
            }
            System.gc();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            for (int i = 0 ; i < array.length ; i ++) {
                Thread t = array[i];
                Class<? extends Thread> c = array[i].getClass();
                System.out.println(String.format("%4s\t%s\t%s\t%s",t.isAlive(),t.getName(),c.getSimpleName(),""));
//                try {
//                    t.join();
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
            }
        }
    }
    if( e.isEndOfTable() || e.getType() == 2){
        if (table.getRowCount() == 0) { // no rows and we're being notified 關機時
            System.err.println(table.getErrorString());
            System.out.println(table);
        }
        return;
    }
/*
    StringBuffer sb = new StringBuffer();
        
    if (e.getFirstRow() == 0) {  // we're being notified of first row
      for (int i=0;i<table.getColumnCount();i++)  // print column names
        sb.append(table.getColumnName(i)+" \t");
      System.out.println(sb.toString());
    }

    // print the rows we're getting
    sb = new StringBuffer();
    for (int j=e.getFirstRow();j<=e.getLastRow();j++) { 
        for (int i=0;i<table.getColumnCount();i++) 
            sb.append(table.getValueAt(j,i)+" \t");
    }
    System.out.println(sb.toString());*/
    }
}
