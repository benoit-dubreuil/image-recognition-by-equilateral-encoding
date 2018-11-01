Image Recognition by Equilateral Encoding
=========================================

![Deer Comparison](/readme-images/DeerComparison.png)

Description
-----------

### Disclaimer

This project's purpose was only to practice and learn the equilateral encoding normalization technique. The code is not perfect, optimal and the same applies for its conception.

### Goal

At the end of the normalization chapter of the book [Artificial Intelligence for Humans, Volume 1: Fundamental Algorithms](https://www.heatonresearch.com/aifh/vol1/) by [Jeff Heaton](https://www.heatonresearch.com/about/), it was suggested that the equilateral encoding algorithm could be used in image recognition algorithms, although it was intended for normalization for neural networks. Even though this algorithm is not the one that gives the best comparison results and that it is overcomplicated for what it does without neural networks, I just wanted to learn how to use it and thus I programmed this application. In other words, I made this repository to learn how to code and use the equilateral encoding algorithm.

### Prerequisites

Java JRE 10 or greater.

### How to Use

Execute the .jar file under the following folder hierarchy : out/artifacts/ImageRecognitionByEquilateralEncoding_jar/

Load the image to the right by clicking on the buttons Image->Load Ref Image.  
Load the image to the left by clicking on the buttons Image->Load Image.

Once the left and right images are loaded, the percentage shown will be the actual resemblance of the two images.

### How It Works

The comparison is made on a pixel versus pixel basis and thus shapes are do not count towards the similarities between the two images.  
Also, the equilateral encoding algorithm's purpos is to encode categories, so I specified the following colors as categories :
* Red
* Green
* Blue
* White
* Black