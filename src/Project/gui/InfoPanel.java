package Project.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Project.StaticManager;
import Project.struct.BlackListStruct;
import Project.struct.SWRunTableStruct;

public class InfoPanel extends JPanel{
    private BlackList frame;
    private GridBagLayout gridBagLayout;
    private JLabel nameLabel;
    private JLabel pathLabel;
    private JLabel paramentLabel;
    private JLabel typeLabel;
    private JLabel mapLabel;
    private JLabel nameText;
    private JLabel pathText;
    private JLabel paramentText;
    private JLabel typeText;
    private JLabel mapText;
    private TimeMapIcon mapIcon;
    public InfoPanel(BlackList frame) {
        this.frame = frame;
        init();
    }
    private void init() {
        gridBagLayout = new GridBagLayout();
        nameLabel = new JLabel("執行檔");
        pathLabel = new JLabel("路徑");
        paramentLabel = new JLabel("參數");
        typeLabel = new JLabel("型態");
        mapLabel = new JLabel("時間表");
        nameText = new JLabel();
        pathText = new JLabel();
        paramentText = new JLabel();
        typeText = new JLabel();
        mapText = new JLabel();
        gridBagLayout.rowWeights = new double[] {1.0, 1.0, 1.0};
        gridBagLayout.rowHeights = new int[] {1, 1, 1};
        gridBagLayout.columnWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        gridBagLayout.columnWidths = new int[] {128, 128, 128, 128, 128, 128, 128, 128};
        nameLabel.setFont(StaticManager.default_font);
        pathLabel.setFont(StaticManager.default_font);
        paramentLabel.setFont(StaticManager.default_font);
        typeLabel.setFont(StaticManager.default_font);
        mapLabel.setFont(StaticManager.default_font);
        
        nameText.setFont(StaticManager.default_font);
        pathText.setFont(StaticManager.default_font);
        paramentText.setFont(StaticManager.default_font);
        typeText.setFont(StaticManager.default_font);
        mapText.setFont(StaticManager.default_font);
        
        nameText.setOpaque(true);
        pathText.setOpaque(true);
        paramentText.setOpaque(true);
        typeText.setOpaque(true);
        mapText.setOpaque(true);
        
        nameText.setBackground(Color.WHITE);
        pathText.setBackground(Color.WHITE);
        paramentText.setBackground(Color.WHITE);
        typeText.setBackground(Color.WHITE);
        mapText.setBackground(Color.WHITE);
        
        setLayout(gridBagLayout);
        setBackground(Color.GRAY);
        addComponent();
    }
    private void addComponent() {
        add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(pathLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(paramentLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(typeLabel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(mapLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        
        add(nameText, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(pathText, new GridBagConstraints(5, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(paramentText, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(typeText, new GridBagConstraints(5, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(mapText, new GridBagConstraints(1, 2, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }
    public void setProcessData(SWRunTableStruct data) {
        nameText.setText(data.Name);
        pathText.setText(data.Path);
        paramentText.setText(data.Parametes);
        typeText.setText(data.Type);
        // TODO 新增時間表圖片
        mapText.setIcon(null);
        if (mapIcon == null) {
            mapIcon = new TimeMapIcon(mapText.getWidth(), 38, data.map);
        } else {
            mapIcon.setData(data.map);
        }
        mapText.setIcon(mapIcon);
        mapText.setVisible(true);
        mapLabel.setVisible(true);
    }
    public void setBlackData(BlackListStruct data) {
        nameText.setText(data.Name);
        pathText.setText(data.Path);
        paramentText.setText(data.Parametes);
        typeText.setText(data.Type);
        mapText.setIcon(null);
        mapText.setVisible(false);
        mapLabel.setVisible(false);
    }
}
