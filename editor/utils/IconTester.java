// -----------------------------
// File: src/editor/utils/IconTester.java
// -----------------------------
package editor.utils;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility application to test and display available system icons
 */
public class IconTester extends JFrame {
    
    public IconTester() {
        super("System Icon Tester");
        initializeUI();
    }
    
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create main panel with scroll pane
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        
        // Add title
        JLabel titleLabel = new JLabel("Available System Icons", SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);
        
        // Test system icons
        addSection(mainPanel, "UIManager Icons", getUIManagerIcons());
        addSection(mainPanel, "Custom Geometric Icons", getCustomIcons());
        addSection(mainPanel, "Look and Feel Info", getLookAndFeelInfo());
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh Icons");
        refreshButton.addActionListener(e -> {
            mainPanel.removeAll();
            addSection(mainPanel, "UIManager Icons", getUIManagerIcons());
            addSection(mainPanel, "Custom Geometric Icons", getCustomIcons());
            addSection(mainPanel, "Look and Feel Info", getLookAndFeelInfo());
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void addSection(JPanel parent, String title, List<IconInfo> icons) {
        // Section header
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        parent.add(sectionLabel);
        
        // Icons grid
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconPanel.setBorder(BorderFactory.createEtchedBorder());
        
        for (IconInfo iconInfo : icons) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            if (iconInfo.icon != null) {
                JLabel iconLabel = new JLabel(iconInfo.icon);
                iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemPanel.add(iconLabel);
            } else {
                JLabel noIconLabel = new JLabel("(null)");
                noIconLabel.setForeground(Color.RED);
                noIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemPanel.add(noIconLabel);
            }
            
            JLabel nameLabel = new JLabel(iconInfo.name);
            nameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemPanel.add(nameLabel);
            
            if (iconInfo.size != null) {
                JLabel sizeLabel = new JLabel(iconInfo.size);
                sizeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
                sizeLabel.setForeground(Color.GRAY);
                sizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemPanel.add(sizeLabel);
            }
            
            iconPanel.add(itemPanel);
        }
        
        parent.add(iconPanel);
    }
    
    private List<IconInfo> getUIManagerIcons() {
        List<IconInfo> icons = new ArrayList<>();
        String[] iconKeys = {
            "FileView.directoryIcon",
            "FileView.fileIcon", 
            "FileView.computerIcon",
            "FileView.hardDriveIcon",
            "FileView.floppyDriveIcon",
            "FileChooser.newFolderIcon",
            "FileChooser.upFolderIcon",
            "FileChooser.homeFolderIcon",
            "FileChooser.detailsViewIcon",
            "FileChooser.listViewIcon",
            "Tree.openIcon",
            "Tree.closedIcon",
            "Tree.leafIcon",
            "OptionPane.informationIcon",
            "OptionPane.warningIcon",
            "OptionPane.errorIcon",
            "OptionPane.questionIcon",
            "InternalFrame.closeIcon",
            "InternalFrame.maximizeIcon",
            "InternalFrame.minimizeIcon",
            "InternalFrame.iconifyIcon"
        };
        
        for (String key : iconKeys) {
            Icon icon = UIManager.getIcon(key);
            String size = null;
            if (icon != null) {
                size = icon.getIconWidth() + "x" + icon.getIconHeight();
            }
            icons.add(new IconInfo(key, icon, size));
        }
        
        return icons;
    }
    
    private List<IconInfo> getCustomIcons() {
        List<IconInfo> icons = new ArrayList<>();
        String[] operations = {"new", "open", "save", "cut", "copy", "paste", 
                              "undo", "redo", "find", "replace", "terminal"};
        
        for (String op : operations) {
            Icon icon = SystemIconHelper.getBestIcon(op);
            String size = icon.getIconWidth() + "x" + icon.getIconHeight();
            icons.add(new IconInfo(op, icon, size));
        }
        
        return icons;
    }
    
    private List<IconInfo> getLookAndFeelInfo() {
        List<IconInfo> info = new ArrayList<>();
        
        // Current Look and Feel
        LookAndFeel laf = UIManager.getLookAndFeel();
        info.add(new IconInfo("Current L&F: " + laf.getName(), null, laf.getClass().getSimpleName()));
        
        // Available Look and Feels
        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lafInfo : lafs) {
            info.add(new IconInfo(lafInfo.getName(), null, lafInfo.getClassName()));
        }
        
        return info;
    }
    
    private static class IconInfo {
        final String name;
        final Icon icon;
        final String size;
        
        IconInfo(String name, Icon icon, String size) {
            this.name = name;
            this.icon = icon;
            this.size = size;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new IconTester().setVisible(true);
        });
    }
    
    /**
     * Test method to print available icons to console
     */
    public static void printSystemIcons() {
        System.out.println("\n=== SYSTEM ICON TEST ===");
        System.out.println("Current Look and Feel: " + UIManager.getLookAndFeel().getName());
        System.out.println();
        
        String[] iconKeys = {
            "FileView.directoryIcon", "FileView.fileIcon", "FileView.computerIcon",
            "FileView.hardDriveIcon", "FileView.floppyDriveIcon",
            "FileChooser.newFolderIcon", "FileChooser.upFolderIcon", "FileChooser.homeFolderIcon",
            "Tree.openIcon", "Tree.closedIcon", "Tree.leafIcon",
            "OptionPane.informationIcon", "OptionPane.warningIcon", "OptionPane.errorIcon"
        };
        
        System.out.println("Available UIManager Icons:");
        for (String key : iconKeys) {
            Icon icon = UIManager.getIcon(key);
            if (icon != null) {
                System.out.printf("  %-25s: %dx%d\n", key, icon.getIconWidth(), icon.getIconHeight());
            } else {
                System.out.printf("  %-25s: (not available)\n", key);
            }
        }
        
        System.out.println("\nCustom Icons:");
        String[] operations = {"new", "open", "save", "cut", "copy", "paste", "undo", "redo", "find", "replace", "terminal"};
        for (String op : operations) {
            Icon icon = SystemIconHelper.getBestIcon(op);
            System.out.printf("  %-10s: %dx%d\n", op, icon.getIconWidth(), icon.getIconHeight());
        }
        
        System.out.println("\n=== END TEST ===\n");
    }
}