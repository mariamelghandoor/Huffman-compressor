# Huffman Coding Compression Algorithm
A Java implementation of the Huffman coding algorithm for lossless data compression.
## Description
This project implements Huffman coding, a popular algorithm for lossless data compression. The algorithm works by assigning variable-length codes to input characters based on their frequencies - characters that occur more frequently are assigned shorter codes.
## Features

* Text file compression using Huffman coding
* Binary file decompression to restore original data
* Entropy calculation to estimate theoretical compression limits
* Command-line interface for easy interaction
* Storage of Huffman tree in compressed file for proper decompression

## How It Works
1. The algorithm first calculates the frequency of each character in the input file
2. It builds a Huffman tree where characters with higher frequencies get shorter binary codes
3. The original text is encoded using these variable-length binary codes
4. For decompression, the Huffman map is stored in the compressed file

## Usage
The program provides a simple command-line interface with three options:
###Choose an option:
1. Compress 2. Decompress 3. Exit
### Compression
* Enter the input file path: example.txt
* Enter the output compressed file path: example.bin
### Decompression
* Enter the compressed file path: example.bin
* Enter the output decompressed file path: restored.txt
## Implementation Details
The implementation consists of two main classes:

* App.java - Handles file I/O and the compression/decompression process
* Huffman_Tree.java - Implements the Huffman tree data structure and coding algorithm

## Getting Started

1. Clone the repository:
   
```bash
git clone https://github.com/yourusername/huffman-compression.git
```

2. Run the application:
   
```
java App
```

3. Place your input text files in the 'resources' directory
4. Follow the prompts to compress or decompress files

### Example Output

Expected size using Entropy: 4721.0 bits
Huffman Tree: 

```
Letter: T, Code: 000
Letter: h, Code: 001
Letter: i, Code: 010
Letter: s, Code: 011
Letter:  , Code: 100
Letter: a, Code: 101
Letter: t, Code: 110
Letter: e, Code: 111

```
Compression Done: example.bin
Compressed in 4728 bits
## Performance
Huffman coding is most efficient for files with highly skewed character distributions. The algorithm achieves compression ratios close to the theoretical entropy limit for the given data.
## License
MIT
