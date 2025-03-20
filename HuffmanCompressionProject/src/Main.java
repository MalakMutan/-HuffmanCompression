import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Huffman Compression");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        // **ألوان مخصصة**
        Color pinkColor = new Color(255, 105, 180);
        Color darkGray = new Color(50, 50, 50);

        // **إنشاء اللوحة الرئيسية**
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // **العنوان الرئيسي**
        JLabel titleLabel = new JLabel("Huffman Compression");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(pinkColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // **اسم المساق**
        JLabel courseLabel = new JLabel("Course: Data Structures 2");
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        courseLabel.setForeground(darkGray);
        courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // **أزرار العمليات**
        JButton compressBtn = new JButton("Compress File");
        JButton decompressBtn = new JButton("Decompress File");
        styleButton(compressBtn, pinkColor);
        styleButton(decompressBtn, pinkColor);

        compressBtn.addActionListener(e -> processFile(true));
        decompressBtn.addActionListener(e -> processFile(false));

        // **إضافة الأزرار في لوحة أفقية**
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(compressBtn);
        buttonPanel.add(decompressBtn);

        // **إضافة اسم المطور**
        JLabel nameLabel = new JLabel("Developed by: Malak Mu'tan");
        nameLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        nameLabel.setForeground(Color.GRAY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // **إضافة العناصر إلى اللوحة الرئيسية**
        panel.add(Box.createVerticalStrut(15));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(courseLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(nameLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
    }

    public static void processFile(boolean isCompress) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // طلب اسم الملف الناتج من المستخدم
            String outputFileName = JOptionPane.showInputDialog(
                    null,
                    "Enter the output file name (without extension):",
                    "Save File",
                    JOptionPane.PLAIN_MESSAGE
            );

            // إذا كان الإدخال فارغًا، استخدم الاسم الافتراضي
            if (outputFileName == null || outputFileName.trim().isEmpty()) {
                outputFileName = isCompress ? "compressed" : "decompressed";
            }

            // إضافة الامتداد المناسب
            outputFileName += isCompress ? ".huff" : ".txt";
            File outputFile = new File(file.getParent(), outputFileName);

            try {
                if (isCompress) {
                    HuffmanCompression.compressFile(file, outputFile);
                } else {
                    HuffmanCompression.decompressFile(file, outputFile);
                }
                JOptionPane.showMessageDialog(null, "Operation Successful! Output: " + outputFile.getAbsolutePath());
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "File Error!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
