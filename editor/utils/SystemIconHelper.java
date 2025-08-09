// -----------------------------
// File: src/editor/utils/SystemIconHelper.java
// -----------------------------
package editor.utils;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class SystemIconHelper {
    
    /**
     * Gets system icons for common operations, with fallbacks to created icons
     */
    public static Icon getNewFileIcon() {
        Icon icon = IkonliIconProvider.getNewFileIcon(16, Color.GREEN);
        if (icon != null) {
            return icon;
        }
        icon = UIManager.getIcon("FileView.fileIcon");
        if (icon == null) {
            icon = createTextIcon("N", Color.GREEN);
        }
        return icon;
    }

    public static Icon getOpenFileIcon() {
        Icon icon = IkonliIconProvider.getOpenFileIcon(16, Color.BLUE);
        if (icon != null) {
            return icon;
        }
        icon = UIManager.getIcon("FileChooser.upFolderIcon");
        if (icon == null) {
            icon = UIManager.getIcon("Tree.openIcon");
        }
        if (icon == null) {
            icon = createTextIcon("O", Color.BLUE);
        }
        return icon;
    }

    public static Icon getSaveFileIcon() {
        Icon icon = IkonliIconProvider.getSaveFileIcon(16, new Color(0, 150, 0));
        if (icon != null) {
            return icon;
        }
        icon = UIManager.getIcon("FileView.floppyDriveIcon");
        if (icon == null) {
            // Try to get a document icon
            try {
                File tempFile = File.createTempFile("temp", ".txt");
                icon = FileSystemView.getFileSystemView().getSystemIcon(tempFile);
                tempFile.delete();
            } catch (Exception e) {
                // Fallback to created icon
            }
        }
        if (icon == null) {
            icon = createTextIcon("S", new Color(0, 150, 0));
        }
        return icon;
    }

    public static Icon getCutIcon() {
        Icon icon = IkonliIconProvider.getCutIcon(16, new Color(200, 50, 50));
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric scissors icon
        return createGeometricIcon("cut", new Color(200, 50, 50));
    }

    public static Icon getCopyIcon() {
        Icon icon = IkonliIconProvider.getCopyIcon(16, new Color(50, 100, 200));
        if (icon != null) {
            return icon;
        }
        // Try system icon first
        icon = UIManager.getIcon("FileView.fileIcon");
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric copy icon
        return createGeometricIcon("copy", new Color(50, 100, 200));
    }

    public static Icon getPasteIcon() {
        Icon icon = IkonliIconProvider.getPasteIcon(16, new Color(150, 100, 50));
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric clipboard icon
        return createGeometricIcon("paste", new Color(150, 100, 50));
    }

    public static Icon getUndoIcon() {
        Icon icon = IkonliIconProvider.getUndoIcon(16, new Color(150, 50, 150));
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric undo arrow (left pointing)
        return createGeometricIcon("undo", new Color(150, 50, 150));
    }

    public static Icon getRedoIcon() {
        Icon icon = IkonliIconProvider.getRedoIcon(16, new Color(150, 50, 150));
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric redo arrow (right pointing)
        return createGeometricIcon("redo", new Color(150, 50, 150));
    }

    public static Icon getFindIcon() {
        Icon icon = IkonliIconProvider.getFindIcon(16, new Color(80, 80, 180));
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric magnifying glass
        return createGeometricIcon("find", new Color(80, 80, 180));
    }

    public static Icon getReplaceIcon() {
        Icon icon = IkonliIconProvider.getReplaceIcon(16, new Color(150, 100, 50));
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric replace icon
        return createGeometricIcon("replace", new Color(150, 100, 50));
    }

    public static Icon getTerminalIcon() {
        Icon icon = IkonliIconProvider.getTerminalIcon(16, Color.BLACK);
        if (icon != null) {
            return icon;
        }
        icon = UIManager.getIcon("FileView.computerIcon");
        if (icon == null) {
            // Terminal icon - command prompt symbol
            icon = createSymbolIcon("⌘", Color.BLACK);
        }
        return icon;
    }
    
    public static Icon getLineNumbersIcon() {
        Icon icon = IkonliIconProvider.getLineNumbersIcon(16, new Color(100, 100, 100));
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric line numbers icon
        return createGeometricIcon("linenumbers", new Color(100, 100, 100));
    }

    /**
     * Creates a simple text-based icon
     */
	private static Icon createTextIcon(String text, Color color) {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
				);

				// Background circle with padding for 24x24 button
				int padding = 2;
				int size = 20 - (padding * 2);
				g2.setColor(color.brighter());
				g2.fillOval(x + padding, y + padding, size, size);

				// Border
				g2.setColor(color.darker());
				g2.drawOval(x + padding, y + padding, size, size);

				// Text
				g2.setColor(Color.WHITE);
				g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
				FontMetrics fm = g2.getFontMetrics();
				int textX = x + padding + (size - fm.stringWidth(text)) / 2;
				int textY =
					y + padding + (size - fm.getHeight()) / 2 + fm.getAscent();
				g2.drawString(text, textX, textY);

				g2.dispose();
			}

			@Override
			public int getIconWidth() {
				return 20;
			}

			@Override
			public int getIconHeight() {
				return 20;
			}
		};
	}

	/**
	 * Creates an icon using Unicode symbols
	 */
	private static Icon createSymbolIcon(String symbol, Color color) {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
				);
				g2.setRenderingHint(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON
				);

				g2.setColor(color);
				g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
				FontMetrics fm = g2.getFontMetrics();

				int textX = x + (20 - fm.stringWidth(symbol)) / 2;
				int textY = y + (20 - fm.getHeight()) / 2 + fm.getAscent();
				g2.drawString(symbol, textX, textY);

				g2.dispose();
			}

			@Override
			public int getIconWidth() {
				return 20;
			}

			@Override
			public int getIconHeight() {
				return 20;
			}
		};
	}

	/**
	 * Creates a simple geometric icon for operations that don't have good Unicode symbols
	 */
	public static Icon createGeometricIcon(String operation, Color color) {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
				);
				g2.setColor(color);

				// Add 2px padding for better proportion in 24x24 buttons
				int padding = 2;
				int centerX = x + 10;
				int centerY = y + 10;

				switch (operation.toLowerCase()) {
					case "cut":
						// Draw simple scissors icon - centered and natural
						g2.setStroke(new BasicStroke(1.5f));

						int bladeLength = 8;
						int handleOffsetY = 5;
						int handleRadius = 5;

						// Left blade
						g2.drawLine(
							centerX,
							centerY,
							centerX - bladeLength,
							centerY - bladeLength / 2
						);
						g2.drawLine(
							centerX,
							centerY,
							centerX - bladeLength,
							centerY + bladeLength / 2
						);

						// Right blade
						g2.drawLine(
							centerX,
							centerY,
							centerX + bladeLength,
							centerY - bladeLength / 2
						);
						g2.drawLine(
							centerX,
							centerY,
							centerX + bladeLength,
							centerY + bladeLength / 2
						);

						// Handle rings (symmetrical)
						g2.drawOval(
							centerX - bladeLength - handleRadius / 2,
							centerY + handleOffsetY,
							handleRadius,
							handleRadius
						);
						g2.drawOval(
							centerX + bladeLength - handleRadius / 2,
							centerY + handleOffsetY,
							handleRadius,
							handleRadius
						);

						// Center screw
						g2.fillOval(centerX - 1, centerY - 1, 2, 1);
						break;
					case "copy":
						// Draw two overlapping rectangles
						g2.setStroke(new BasicStroke(1.5f));
						g2.drawRect(x + padding, y + padding, 10, 10);
						g2.drawRect(x + padding + 4, y + padding + 4, 10, 10);
						break;
					case "paste":
						// Draw clipboard shape
						g2.setStroke(new BasicStroke(1.5f));
						g2.drawRect(x + padding + 1, y + padding, 14, 16);
						g2.drawRect(x + padding + 5, y + padding - 1, 6, 3);
						g2.drawLine(
							x + padding + 3,
							y + padding + 6,
							x + padding + 13,
							y + padding + 6
						);
						g2.drawLine(
							x + padding + 3,
							y + padding + 9,
							x + padding + 13,
							y + padding + 9
						);
						g2.drawLine(
							x + padding + 3,
							y + padding + 12,
							x + padding + 10,
							y + padding + 12
						);
						break;
					case "find":
						// Draw magnifying glass
						g2.setStroke(new BasicStroke(2.0f));
						g2.drawOval(x + padding, y + padding, 10, 10);
						g2.drawLine(
							x + padding + 8,
							y + padding + 8,
							x + 18 - padding,
							y + 18 - padding
						);
						break;
					case "replace":
						// Draw find and replace arrows
						g2.setStroke(new BasicStroke(1.8f));
						// Left arrow
						g2.drawLine(
							x + padding,
							y + centerY - 3,
							x + padding + 8,
							y + centerY - 3
						);
						g2.drawLine(
							x + padding,
							y + centerY - 3,
							x + padding + 3,
							y + centerY - 6
						);
						g2.drawLine(
							x + padding,
							y + centerY - 3,
							x + padding + 3,
							y + centerY
						);
						// Right arrow
						g2.drawLine(
							x + padding + 6,
							y + centerY + 3,
							x + 20 - padding,
							y + centerY + 3
						);
						g2.drawLine(
							x + 20 - padding,
							y + centerY + 3,
							x + 17 - padding,
							y + centerY
						);
						g2.drawLine(
							x + 20 - padding,
							y + centerY + 3,
							x + 17 - padding,
							y + centerY + 6
						);
						break;
					case "undo":
						// Draw left-pointing arrow
						g2.setStroke(new BasicStroke(2.0f));
						// Arrow shaft
						g2.drawLine(
							x + padding + 2,
							centerY,
							x + 20 - padding - 2,
							centerY
						);
						// Arrow head (pointing left)
						g2.drawLine(
							x + padding + 2,
							centerY,
							x + padding + 6,
							centerY - 4
						);
						g2.drawLine(
							x + padding + 2,
							centerY,
							x + padding + 6,
							centerY + 4
						);
						break;
					case "redo":
						// Draw right-pointing arrow
						g2.setStroke(new BasicStroke(2.0f));
						// Arrow shaft
						g2.drawLine(
							x + padding + 2,
							centerY,
							x + 20 - padding - 2,
							centerY
						);
						// Arrow head (pointing right)
						g2.drawLine(
							x + 20 - padding - 2,
							centerY,
							x + 20 - padding - 6,
							centerY - 4
						);
						g2.drawLine(
							x + 20 - padding - 2,
							centerY,
							x + 20 - padding - 6,
							centerY + 4
						);
						break;
					case "linenumbers":
						// Draw line numbers icon (list with numbers)
						g2.setStroke(new BasicStroke(1.5f));
						// Draw three lines representing text
						g2.drawLine(x + padding + 6, y + padding + 4, x + 20 - padding, y + padding + 4);
						g2.drawLine(x + padding + 6, y + padding + 8, x + 18 - padding, y + padding + 8);
						g2.drawLine(x + padding + 6, y + padding + 12, x + 16 - padding, y + padding + 12);
						// Draw line numbers
						g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
						g2.drawString("1", x + padding + 1, y + padding + 6);
						g2.drawString("2", x + padding + 1, y + padding + 10);
						g2.drawString("3", x + padding + 1, y + padding + 14);
						break;
					default:
						// Default to a simple rectangle
						g2.drawRect(x + padding, y + padding, 16, 16);
						break;
				}

				g2.dispose();
			}

			@Override
			public int getIconWidth() {
				return 20;
			}

			@Override
			public int getIconHeight() {
				return 20;
			}
		};
	}

	/**
	 * Try to get the best available system icon, with fallback hierarchy
	 */
	public static Icon getBestIcon(String operation) {
		switch (operation.toLowerCase()) {
			case "new":
				return getNewFileIcon();
			case "open":
				return getOpenFileIcon();
			case "save":
				return getSaveFileIcon();
			case "cut":
				return getCutIcon();
			case "copy":
				return getCopyIcon();
			case "paste":
				return getPasteIcon();
			case "undo":
				return getUndoIcon();
			case "redo":
				return getRedoIcon();
			case "find":
				return getFindIcon();
			case "replace":
				return getReplaceIcon();
			case "terminal":
				return getTerminalIcon();
			default:
				return createTextIcon("?", Color.GRAY);
		}
	}

	/**
	 * Get system look and feel specific icons where available
	 */
	public static void printAvailableSystemIcons() {
		System.out.println("Available System Icons:");
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
		};

		for (String key : iconKeys) {
			Icon icon = UIManager.getIcon(key);
			if (icon != null) {
				System.out.println(
					"  " +
					key +
					": Available (" +
					icon.getIconWidth() +
					"x" +
					icon.getIconHeight() +
					")"
				);
			}
		}
	}
}
