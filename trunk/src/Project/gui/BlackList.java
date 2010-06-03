package Project.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import Project.StaticManager;
import Project.db.DBFunction;
import Project.gui.List.event.IPListActionListener;
import Project.gui.List.event.MenuActionListener;


/**
 * @author bbxp
 *
 */
public class BlackList extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -4957200494593514164L;
    private JPanel contentPane;
    private InfoPanel infoPane;
    private ProcessPanel processPane;
    private ListPanel listPane;
    private GridBagLayout gridBagLayout;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenu ipMenu;
    private JMenuItem blackItem;
    private JMenuItem whiteItem;
    private JMenuItem grayItem;
    private JMenuItem bootItem;
    private JMenuItem notBootItem;
    private JMenuItem ipItem;
    private JMenuItem removeListItem;
    private IPListActionListener ipListActionListener;
    private MenuActionListener menuActionListener;
    
    public static BorderLayout borderLayout = new BorderLayout();
    
    public String ip;
    public String listType;
    
    public BlackList() {
        this.contentPane = (JPanel) this.getContentPane();
        this.ip = "";
        this.listType = "黑名單";
        newComponents();
        init();
    }
    private void init() {
        gridBagLayout.rowWeights = new double[] {1, 1, 1, 1};
        gridBagLayout.rowHeights = new int[] {128, 128, 256, 128};
        gridBagLayout.columnWeights = new double[] {1, 1};
        gridBagLayout.columnWidths = new int[] {512, 512};
        contentPane.setLayout(gridBagLayout);
        blackItem.setFont(StaticManager.menu_font);
        whiteItem.setFont(StaticManager.menu_font);
        grayItem.setFont(StaticManager.menu_font);
        ipItem.setFont(StaticManager.menu_font);
        bootItem.setFont(StaticManager.menu_font);
        notBootItem.setFont(StaticManager.menu_font);
        menu.setFont(StaticManager.menu_font);
        ipMenu.setFont(StaticManager.menu_font);
        removeListItem.setFont(StaticManager.menu_font);
        blackItem.setName(String.valueOf(StaticManager.BLACKLIST));
        whiteItem.setName(String.valueOf(StaticManager.WHITELIST));
        grayItem.setName(String.valueOf(StaticManager.GRAYLIST));
        ipItem.setName(String.valueOf(StaticManager.IPLISTDIALOG));
        bootItem.setName(String.valueOf(StaticManager.BOOTLIST));
        notBootItem.setName(String.valueOf(StaticManager.NOTBOOTLIST));
        removeListItem.setName(String.valueOf(StaticManager.REMOVEBLACKLIST));
        addComponents();
        addListeners();
        this.setJMenuBar(menuBar);
        this.setSize(1024, 768);
        this.setMinimumSize(getSize());
        updateTitle();
    }
    private void newComponents() {
        infoPane = new InfoPanel(this);
        processPane = new ProcessPanel(this, infoPane);
        listPane = new ListPanel(this, infoPane);
        gridBagLayout = new GridBagLayout();
        menuBar = new JMenuBar();
        menu = new JMenu("檢視");
        blackItem = new JMenuItem("黑名單");
        whiteItem = new JMenuItem("白名單");
        grayItem = new JMenuItem("灰名單");
        bootItem = new JMenuItem("開機時清單");
        notBootItem = new JMenuItem("非開機時清單");
        removeListItem = new JMenuItem("清除黑白灰名單");
        ipMenu = new JMenu("清單");
        ipItem = new JMenuItem("IP清單");
        menuActionListener = new MenuActionListener(this);
        ipListActionListener = new IPListActionListener(this);
    }
    private void addComponents() {
        contentPane.add(listPane, new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        contentPane.add(processPane, new GridBagConstraints(1, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        contentPane.add(infoPane, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        menu.add(blackItem);
        menu.add(whiteItem);
        menu.add(grayItem);
        menu.addSeparator();
        menu.add(bootItem);
        menu.add(notBootItem);
        ipMenu.add(ipItem);
        ipMenu.addSeparator();
        ipMenu.add(removeListItem);
        menuBar.add(menu);
        menuBar.add(ipMenu);
    }
    private void addListeners() {
        blackItem.addActionListener(menuActionListener);
        whiteItem.addActionListener(menuActionListener);
        grayItem.addActionListener(menuActionListener);
        removeListItem.addActionListener(menuActionListener);
        ipItem.addActionListener(ipListActionListener);
        bootItem.addActionListener(ipListActionListener);
        notBootItem.addActionListener(ipListActionListener);
    }
    public void updateTitle() {
        this.setTitle(listType+" | "+ip);
    }
    public void updateListPane(int type) {
        listPane.updateList(type);
        this.updateTitle();
    }
    public void updateProcessPane(String ip, int type) {
        processPane.updateList(ip, type);
        this.updateTitle();
    }
    public void clearBlackList() {
        DBFunction.getInstance().clearBlackList();
        updateListPane(StaticManager.BLACKLIST);
    }
}
