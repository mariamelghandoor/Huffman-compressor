import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.io.FileInputStream;

public class App {

    public static double computeEntropy(Map<Character, Integer> frequencyMap) {
        if (frequencyMap == null || frequencyMap.isEmpty()) {
            return 0.0;
        }

        int totalOccurrences = frequencyMap.values().stream().mapToInt(Integer::intValue).sum();
        double entropy = 0.0;

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            double probability = (double) entry.getValue() / totalOccurrences;
            entropy += probability * (Math.log(1.0 / probability) / Math.log(2)); 
        }

        return entropy;
    }
    
public static byte[] binaryStringToBytes(String binary) {
    int length = (binary.length() + 7) / 8;
    byte[] byteArray = new byte[length];

    for (int i = 0; i < binary.length(); i += 8) {
        String byteString = binary.substring(i, Math.min(i + 8, binary.length()));
        while (byteString.length() < 8) byteString += "0";  
        byteArray[i / 8] = (byte) Integer.parseInt(byteString, 2);
    }
    return byteArray;
}


    public static void saveMapToBinaryFile(Map<Character, String> huffmanMap, String encodedMessage, String fileName) {
        StringBuilder binaryData = new StringBuilder();

        for (Map.Entry<Character, String> entry : huffmanMap.entrySet()) {
            char letter = entry.getKey();
            String huffmanCode = entry.getValue();

            binaryData.append(String.format("%8s", Integer.toBinaryString(letter)).replace(' ', '0'));
            binaryData.append(String.format("%5s", Integer.toBinaryString(huffmanCode.length())).replace(' ', '0'));
            binaryData.append(huffmanCode);
        }

        binaryData.append("00000000");
        binaryData.append(encodedMessage);

        byte[] byteArray = binaryStringToBytes(binaryData.toString());
        
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static PriorityQueue<Huffman_Tree.Node> sort_letters_by_prob(String code) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : code.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        double entropy = computeEntropy(map);
        System.out.println("Expected size using Entropy: " + entropy * code.length() + " bits");
        PriorityQueue<Huffman_Tree.Node> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.value, b.value));
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            pq.add(new Huffman_Tree.Node(entry.getValue(), entry.getKey()));
        }
        return pq;
    }

    public static String Compressing_turn_to_short_binary(String code, Map<Character, String> map) {
        StringBuilder binaryCode = new StringBuilder();
        for (char c : code.toCharArray()) {
            binaryCode.append(map.get(c));
        }
        return binaryCode.toString();
    }
    
    public static void Compress(String inputFile, String outputFile) {
        try {
            String code = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(inputFile)));
    
            PriorityQueue<Huffman_Tree.Node> pq = sort_letters_by_prob(code);
            Huffman_Tree tree = new Huffman_Tree();
            Huffman_Tree.Node root = tree.build_tree(pq);
            tree.assign_code(root, "");
            Map<Character, String> map = tree.build_mapping_dict(root);
    
            String binary_code = Compressing_turn_to_short_binary(code, map);
    
            saveMapToBinaryFile(map, binary_code, outputFile);
            System.out.println("Huffman Tree: ");
            tree.print_tree(root);
            System.out.println("Compression Done: " + outputFile);
            System.out.println("Compressed in " + binary_code.length() + " bits");
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    public static String bytesToBinaryString(byte[] bytes) {
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return binary.toString();
    }

    public static Map<String, Character> extractHuffmanMap(String binaryData, int[] endIndex) {
        Map<String, Character> huffmanMap = new HashMap<>();
        int index = 0;
    
        while (index + 8 + 5 <= binaryData.length()) {
            String charBinary = binaryData.substring(index, index + 8);
    
            if (charBinary.equals("00000000")) {
                index += 8;
                break;
            }
    
            char letter = (char) Integer.parseInt(charBinary, 2);
            index += 8;
    
            int codeLength = Integer.parseInt(binaryData.substring(index, index + 5), 2);
            index += 5;
    
            String binaryCode = binaryData.substring(index, index + codeLength);
            index += codeLength;
    
            huffmanMap.put(binaryCode, letter);
        }
    
        endIndex[0] = index; 
        return huffmanMap;
    }
    
    public static void Decode_binaryfile(String fileName, String outputfileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            byte[] fileBytes = fis.readAllBytes();
            String binaryData = bytesToBinaryString(fileBytes);
            System.out.println("Binary Data: " + binaryData);
    
            int[] endIndex = new int[1];
            Map<String, Character> huffmanMap = extractHuffmanMap(binaryData, endIndex);
            String encodedBinary = binaryData.substring(endIndex[0]); 
    
            System.out.println("Extracted Huffman Map: " + huffmanMap);
            System.out.println("Encoded Binary String: " + encodedBinary);
    
            StringBuilder decodedMessage = new StringBuilder();
            String currentCode = "";
            for (int i = 0; i < encodedBinary.length(); i++) {
                currentCode += encodedBinary.charAt(i);
    
                if (huffmanMap.containsKey(currentCode)) {
                    decodedMessage.append(huffmanMap.get(currentCode));
                    currentCode = "";
                }
            }
    
            try (FileWriter writer = new FileWriter(outputfileName)) {
                writer.write(decodedMessage.toString());
                System.out.println("Successfully written to file.");
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public static void Decompress(String inputFile, String outputFile) {
        Decode_binaryfile(inputFile, outputFile);
        System.out.println("Decompression Done: " + outputFile);
    }

    public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    
    Files.createDirectories(Paths.get("resources"));

    while (true) {
        System.out.println("\nChoose an option:");
        System.out.println("1. Compress 2. Decompress 3. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        if (choice == 1) {
            System.out.print("Enter the input file path: ");
            String inputPath = "resources/" + scanner.nextLine();
            System.out.print("Enter the output compressed file path: ");
            String outputPath = "resources/" + scanner.nextLine();

            try {
                Compress(inputPath, outputPath);
                System.out.println("Compression successful!");
            } catch (Exception e) {
                System.out.println("Error during compression: " + e.getMessage());
            }

        } else if (choice == 2) {
            System.out.print("Enter the compressed file path: ");
            String inputPath = "resources/" + scanner.nextLine();
            System.out.print("Enter the output decompressed file path: ");
            String outputPath = "resources/" + scanner.nextLine();

            try {
                Decompress(inputPath, outputPath);
                System.out.println("Decompression successful!");
            } catch (Exception e) {
                System.out.println("Error during decompression: " + e.getMessage());
            }

        } else if (choice == 3) {
            System.out.println("Exiting...");
            break;

        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
    scanner.close();
}
}
