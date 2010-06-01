package Project.gui.List.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import Project.StaticManager;
import Project.gui.BlackList;
import Project.gui.ListPanel;

public class MenuActionListener implements ActionListener{
    private BlackList frame;
    public MenuActionListener(BlackList frame) {
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem)e.getSource();
        int index = Integer.parseInt(item.getName());
        switch (index) {
        case StaticManager.BLACKLIST: //黑名單
        case StaticManager.WHITELIST: //白名單
        case StaticManager.GRAYLIST: //灰名單
            frame.updateListPane(index);
            break;
        case StaticManager.REMOVEBLACKLIST: //清除黑白名單
            frame.clearBlackList();
            break;
        default:
            System.err.println("更新ListPane錯誤!");
        }
    }
}
