import java.io.*;
import java.util.*;
import java.nio.file.Files;

public class HuffmanCompression {
    private static Map<Character, String> huffmanCodes = new HashMap<>();
    private static HuffmanNode root;

    public static HuffmanNode buildHuffmanTree(String text) {//تعريف فئة HuffmanCompression
        Map<Character, Integer> freqMap = getFrequencyMap(text);
        return buildHuffmanTreeFromFrequency(freqMap);
    }
    // بناء شجرة هوفمان
    private static Map<Character, Integer> getFrequencyMap(String text) {//انشاء خريطة تكرارات الاحرف
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }
    //بناء شجرة الهوفمان
    public static HuffmanNode buildHuffmanTreeFromFrequency(Map<Character, Integer> freqMap) {
        PriorityQueue<HuffmanNode> heap = new PriorityQueue<>();
        freqMap.forEach((c, f) -> heap.add(new HuffmanNode(c, f)));

        while (heap.size() > 1) {
            HuffmanNode left = heap.poll(), right = heap.poll();
            HuffmanNode merged = new HuffmanNode('\0', left.frequency + right.frequency);
            merged.left = left;
            merged.right = right;
            heap.add(merged);
        }
        return heap.poll();
    }
    //Malak2025
    // انشاء رموز هوفمان
    public static void buildHuffmanCodes(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.character != '\0') huffmanCodes.put(node.character, code);
        buildHuffmanCodes(node.left, code + "0");
        buildHuffmanCodes(node.right, code + "1");
    }
    public static String decompress(String encodedText) {
        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;

        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.character != '\0') { // إذا وصلنا إلى حرف فعلي
                decoded.append(current.character);
                current = root; // نرجع إلى بداية الشجرة
            }
        }

        return decoded.toString();
    }

    public static String convertByteArrayToBitString(byte[] byteArray, int bitCount) {//لعملية الضغط
        StringBuilder bitString = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            for (int j = 7; j >= 0; j--) {
                if (bitString.length() < bitCount) { // التأكد من عدم تجاوز الطول الأصلي
                    bitString.append(((byteArray[i] >> j) & 1) == 1 ? "1" : "0");
                }
            }
        }

        return bitString.toString();
    }
    public static byte[] convertBitStringToByteArray(String bitString) {// لفك الضغط
        int length = (bitString.length() + 7) / 8; // حساب عدد البايتات المطلوبة
        byte[] byteArray = new byte[length];

        for (int i = 0; i < bitString.length(); i++) {
            int byteIndex = i / 8;
            int bitIndex = 7 - (i % 8); // لكتابة البتات من اليسار إلى اليمين
            if (bitString.charAt(i) == '1') {
                byteArray[byteIndex] |= (1 << bitIndex);
            }
        }

        return byteArray;
    }
    public static void compressFile(File inputFile, File outputFile) throws IOException {
        String content = new String(Files.readAllBytes(inputFile.toPath()));
        root = buildHuffmanTree(content);//راح ارجع استخدمها لفك الملف
        huffmanCodes.clear();
        buildHuffmanCodes(root, "");

        StringBuilder encodedText = new StringBuilder();
        for (char c : content.toCharArray()) {
            encodedText.append(huffmanCodes.get(c));
        }

        byte[] compressedBytes = convertBitStringToByteArray(encodedText.toString());

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            out.writeObject(getFrequencyMap(content)); // حفظ تكرارات الحروف
            out.writeInt(encodedText.length()); // حفظ الطول الأصلي للبتات
            out.write(compressedBytes); // كتابة البيانات المضغوطة
        }
    }


    public static void decompressFile(File inputFile, File outputFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(inputFile))) {
            Map<Character, Integer> freqMap = (Map<Character, Integer>) in.readObject();
            int bitCount = in.readInt(); // قراءة عدد البتات الفعلية
            byte[] compressedBytes = new byte[(bitCount + 7) / 8]; // حجم البايتات الفعلية
            in.readFully(compressedBytes); // قراءة كل البيانات

            root = buildHuffmanTreeFromFrequency(freqMap);
            String encodedText = convertByteArrayToBitString(compressedBytes, bitCount);
            String decodedText = decompress(encodedText);

            Files.write(outputFile.toPath(), decodedText.getBytes());
        }
    }




}