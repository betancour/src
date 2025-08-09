// -----------------------------
// File: src/editor/ui/FindReplaceDialog.java
// -----------------------------
package editor.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FindReplaceDialog extends JDialog {
    private final JTextArea textArea;
    private final JTextField findField = new JTextField(20);
    private final JTextField replaceField = new JTextField(20);
    private final JCheckBox matchCaseBox = new JCheckBox("Match case");
    private final JCheckBox wholeWordBox = new JCheckBox("Whole word");
    private final JCheckBox regexBox = new JCheckBox("Regular expression");
    private final JButton findNextButton = new JButton("Find Next");
    private final JButton findPrevButton = new JButton("Find Previous");
    private final JButton replaceButton = new JButton("Replace");
    private final JButton replaceAllButton = new JButton("Replace All");
    private final JLabel statusLabel = new JLabel(" ");
    
    private int lastFoundIndex = -1;
    private String lastSearchText = "";
    
    public FindReplaceDialog(JFrame parent, JTextArea textArea) {
        super(parent, "Find and Replace", false);
        this.textArea = textArea;
        
        initComponents();
        setupLayout();
        setupActions();
        setupKeyBindings();
        
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        // Components are initialized in field declarations
        statusLabel.setForeground(Color.BLUE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Find section
        JPanel findPanel = new JPanel(new BorderLayout(5, 5));
        findPanel.setBorder(new TitledBorder("Find"));
        findPanel.add(new JLabel("Find:"), BorderLayout.WEST);
        findPanel.add(findField, BorderLayout.CENTER);
        
        // Replace section
        JPanel replacePanel = new JPanel(new BorderLayout(5, 5));
        replacePanel.setBorder(new TitledBorder("Replace"));
        replacePanel.add(new JLabel("Replace:"), BorderLayout.WEST);
        replacePanel.add(replaceField, BorderLayout.CENTER);
        
        // Options panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setBorder(new TitledBorder("Options"));
        optionsPanel.add(matchCaseBox);
        optionsPanel.add(wholeWordBox);
        optionsPanel.add(regexBox);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(findNextButton);
        buttonPanel.add(findPrevButton);
        buttonPanel.add(replaceButton);
        buttonPanel.add(replaceAllButton);
        
        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        
        // Layout components
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        mainPanel.add(findPanel, gbc);
        
        gbc.gridy = 1;
        mainPanel.add(replacePanel, gbc);
        
        gbc.gridy = 2;
        mainPanel.add(optionsPanel, gbc);
        
        gbc.gridy = 3;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void setupActions() {
        findNextButton.addActionListener(e -> findNext());
        findPrevButton.addActionListener(e -> findPrevious());
        replaceButton.addActionListener(e -> replace());
        replaceAllButton.addActionListener(e -> replaceAll());
        
        // Auto-search as user types
        findField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { resetSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { resetSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { resetSearch(); }
        });
        
        // Enable/disable buttons based on content
        ActionListener updateButtons = e -> updateButtonStates();
        matchCaseBox.addActionListener(updateButtons);
        wholeWordBox.addActionListener(updateButtons);
        regexBox.addActionListener(updateButtons);
    }
    
    private void setupKeyBindings() {
        // Enter key in find field = Find Next
        findField.addActionListener(e -> findNext());
        
        // Enter key in replace field = Replace
        replaceField.addActionListener(e -> replace());
        
        // ESC key closes dialog
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        // F3 = Find Next
        KeyStroke f3KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0, false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f3KeyStroke, "FIND_NEXT");
        getRootPane().getActionMap().put("FIND_NEXT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });
    }
    
    private void resetSearch() {
        lastFoundIndex = -1;
        lastSearchText = "";
        updateButtonStates();
    }
    
    private void updateButtonStates() {
        boolean hasText = !findField.getText().trim().isEmpty();
        findNextButton.setEnabled(hasText);
        findPrevButton.setEnabled(hasText);
        replaceButton.setEnabled(hasText && textArea.getSelectedText() != null);
        replaceAllButton.setEnabled(hasText);
    }
    
    private void findNext() {
        find(true);
    }
    
    private void findPrevious() {
        find(false);
    }
    
    private void find(boolean forward) {
        String searchText = findField.getText();
        if (searchText.isEmpty()) {
            setStatus("Search text is empty", Color.RED);
            return;
        }
        
        String content = textArea.getText();
        if (content.isEmpty()) {
            setStatus("Document is empty", Color.RED);
            return;
        }
        
        int startPos = forward ? textArea.getCaretPosition() : textArea.getSelectionStart();
        
        // If search text changed, reset position
        if (!searchText.equals(lastSearchText)) {
            lastFoundIndex = -1;
            lastSearchText = searchText;
        }
        
        int foundIndex = -1;
        
        try {
            if (regexBox.isSelected()) {
                foundIndex = findWithRegex(content, searchText, startPos, forward);
            } else {
                foundIndex = findWithString(content, searchText, startPos, forward);
            }
            
            if (foundIndex != -1) {
                highlightFound(foundIndex, searchText);
                setStatus("Found at position " + foundIndex, Color.BLUE);
                lastFoundIndex = foundIndex;
            } else {
                // Try wrapping around
                startPos = forward ? 0 : content.length();
                if (regexBox.isSelected()) {
                    foundIndex = findWithRegex(content, searchText, startPos, forward);
                } else {
                    foundIndex = findWithString(content, searchText, startPos, forward);
                }
                
                if (foundIndex != -1) {
                    highlightFound(foundIndex, searchText);
                    setStatus("Found at position " + foundIndex + " (wrapped)", Color.BLUE);
                    lastFoundIndex = foundIndex;
                } else {
                    setStatus("Text not found", Color.RED);
                }
            }
        } catch (PatternSyntaxException e) {
            setStatus("Invalid regular expression: " + e.getMessage(), Color.RED);
        }
        
        updateButtonStates();
    }
    
    private int findWithString(String content, String searchText, int startPos, boolean forward) {
        if (!matchCaseBox.isSelected()) {
            content = content.toLowerCase();
            searchText = searchText.toLowerCase();
        }
        
        if (wholeWordBox.isSelected()) {
            return findWholeWord(content, searchText, startPos, forward);
        } else {
            return forward ? content.indexOf(searchText, startPos) 
                           : content.lastIndexOf(searchText, startPos - 1);
        }
    }
    
    private int findWholeWord(String content, String searchText, int startPos, boolean forward) {
        int pos = startPos;
        while (true) {
            int foundPos = forward ? content.indexOf(searchText, pos)
                                   : content.lastIndexOf(searchText, pos - 1);
            if (foundPos == -1) break;
            
            // Check if it's a whole word
            boolean isWholeWord = true;
            if (foundPos > 0 && Character.isLetterOrDigit(content.charAt(foundPos - 1))) {
                isWholeWord = false;
            }
            if (foundPos + searchText.length() < content.length() && 
                Character.isLetterOrDigit(content.charAt(foundPos + searchText.length()))) {
                isWholeWord = false;
            }
            
            if (isWholeWord) {
                return foundPos;
            }
            
            pos = forward ? foundPos + 1 : foundPos - 1;
            if (pos < 0 || pos >= content.length()) break;
        }
        return -1;
    }
    
    private int findWithRegex(String content, String searchText, int startPos, boolean forward) throws PatternSyntaxException {
        int flags = matchCaseBox.isSelected() ? 0 : Pattern.CASE_INSENSITIVE;
        Pattern pattern = Pattern.compile(searchText, flags);
        
        if (forward) {
            java.util.regex.Matcher matcher = pattern.matcher(content);
            if (matcher.find(startPos)) {
                return matcher.start();
            }
        } else {
            // For backward search, find all matches up to startPos and take the last one
            java.util.regex.Matcher matcher = pattern.matcher(content);
            int lastMatch = -1;
            while (matcher.find() && matcher.start() < startPos) {
                lastMatch = matcher.start();
            }
            return lastMatch;
        }
        return -1;
    }
    
    private void highlightFound(int index, String searchText) {
        int length;
        if (regexBox.isSelected()) {
            try {
                int flags = matchCaseBox.isSelected() ? 0 : Pattern.CASE_INSENSITIVE;
                Pattern pattern = Pattern.compile(searchText, flags);
                java.util.regex.Matcher matcher = pattern.matcher(textArea.getText());
                if (matcher.find(index)) {
                    length = matcher.end() - matcher.start();
                } else {
                    length = searchText.length();
                }
            } catch (PatternSyntaxException e) {
                length = searchText.length();
            }
        } else {
            length = searchText.length();
        }
        
        textArea.setSelectionStart(index);
        textArea.setSelectionEnd(index + length);
        textArea.getCaret().setSelectionVisible(true);
        textArea.requestFocusInWindow();
    }
    
    private void replace() {
        String selectedText = textArea.getSelectedText();
        if (selectedText == null) {
            findNext();
            return;
        }
        
        String findText = findField.getText();
        String replaceText = replaceField.getText();
        
        // Check if selection matches find text
        boolean matches = false;
        if (regexBox.isSelected()) {
            try {
                int flags = matchCaseBox.isSelected() ? 0 : Pattern.CASE_INSENSITIVE;
                Pattern pattern = Pattern.compile(findText, flags);
                matches = pattern.matcher(selectedText).matches();
            } catch (PatternSyntaxException e) {
                setStatus("Invalid regular expression: " + e.getMessage(), Color.RED);
                return;
            }
        } else {
            String compareText = matchCaseBox.isSelected() ? selectedText : selectedText.toLowerCase();
            String compareFindText = matchCaseBox.isSelected() ? findText : findText.toLowerCase();
            matches = compareText.equals(compareFindText);
        }
        
        if (matches) {
            textArea.replaceSelection(replaceText);
            setStatus("Replaced 1 occurrence", Color.BLUE);
            findNext(); // Find next occurrence
        } else {
            findNext(); // Find the next occurrence
        }
    }
    
    private void replaceAll() {
        String findText = findField.getText();
        String replaceText = replaceField.getText();
        String content = textArea.getText();
        
        if (findText.isEmpty()) {
            setStatus("Find text is empty", Color.RED);
            return;
        }
        
        int count = 0;
        try {
            if (regexBox.isSelected()) {
                int flags = matchCaseBox.isSelected() ? 0 : Pattern.CASE_INSENSITIVE;
                Pattern pattern = Pattern.compile(findText, flags);
                java.util.regex.Matcher matcher = pattern.matcher(content);
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(sb, replaceText);
                    count++;
                }
                matcher.appendTail(sb);
                content = sb.toString();
            } else {
                String searchText = matchCaseBox.isSelected() ? findText : findText.toLowerCase();
                String searchContent = matchCaseBox.isSelected() ? content : content.toLowerCase();
                
                StringBuilder sb = new StringBuilder();
                int lastIndex = 0;
                int index = 0;
                
                while ((index = searchContent.indexOf(searchText, lastIndex)) != -1) {
                    if (wholeWordBox.isSelected()) {
                        // Check if it's a whole word
                        boolean isWholeWord = true;
                        if (index > 0 && Character.isLetterOrDigit(content.charAt(index - 1))) {
                            isWholeWord = false;
                        }
                        if (index + findText.length() < content.length() && 
                            Character.isLetterOrDigit(content.charAt(index + findText.length()))) {
                            isWholeWord = false;
                        }
                        
                        if (!isWholeWord) {
                            lastIndex = index + 1;
                            continue;
                        }
                    }
                    
                    sb.append(content.substring(lastIndex, index));
                    sb.append(replaceText);
                    lastIndex = index + findText.length();
                    count++;
                }
                sb.append(content.substring(lastIndex));
                content = sb.toString();
            }
            
            if (count > 0) {
                textArea.setText(content);
                textArea.setCaretPosition(0);
                setStatus("Replaced " + count + " occurrence(s)", Color.BLUE);
            } else {
                setStatus("No occurrences found to replace", Color.RED);
            }
        } catch (PatternSyntaxException e) {
            setStatus("Invalid regular expression: " + e.getMessage(), Color.RED);
        }
    }
    
    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        
        // Clear status after 3 seconds
        Timer timer = new Timer(3000, e -> statusLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    
    public void showWithText(String selectedText) {
        if (selectedText != null && !selectedText.trim().isEmpty()) {
            findField.setText(selectedText);
            findField.selectAll();
        }
        setVisible(true);
        findField.requestFocusInWindow();
    }
    
    public void showDialog() {
        setVisible(true);
        findField.requestFocusInWindow();
    }
}