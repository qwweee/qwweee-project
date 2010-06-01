package Project.gui.List.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import Project.StaticManager;
import Project.db.DBFunction;
import Project.gui.BlackList;
import Project.gui.ProcessPanel;

public class IPListActionListener implements ActionListener{
    private BlackList frame;
    public IPListActionListener(BlackList frame) {
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem)e.getSource();
        processAction(Integer.parseInt(item.getName()));
        
    }
    private void processAction(int type) {
        switch (type) {
        case StaticManager.IPLISTDIALOG:
            showDialog();
            break;
        case StaticManager.BOOTLIST:
        case StaticManager.NOTBOOTLIST:
            if (frame.ip.equalsIgnoreCase("")) {
                return;
            }
            frame.updateProcessPane(frame.ip, type);
            break;
        default:
            System.err.println("更新ProcessPane錯誤!");
        }
    }
    private void showDialog() {
        String[] ipList = DBFunction.getInstance().GetAllIPList();
        if (ipList.length != 0) {
            String s = (String) JOptionPane.showInputDialog(frame, "請選擇要查看的IP的處理程序？", "IP清單",
                    JOptionPane.PLAIN_MESSAGE, null, ipList, ipList[0]);
            if (s == null) {
                frame.ip = "";
                return;
            } else {
                frame.ip = s;
            }
        } else {
            StaticManager.showWarningMsg(frame, "目前沒有監測資料！", "IP清單");
            return;
        }
        frame.updateProcessPane(frame.ip, StaticManager.BOOTLIST);
    }
}
