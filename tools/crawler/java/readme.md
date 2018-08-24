[![experimental](http://badges.github.io/stability-badges/dist/experimental.svg)](http://github.com/badges/stability-badges)
# Overview of the TestCase Crawler to produce the current TestCase state

## Requirements
* Java Compiler for Java >1.8
* evtl. development environment (e.g. Eclipse, www.eclipse.org)
* Apache Commons IO (https://commons.apache.org/io/)
* A local version of the files to process (best is to use GIT to clone the TestCase Repository)
* A class with a main-function (see below)

## Starter Class for the Belt-Crawler

```java
public class BeltCrawlerStarter {
	public static void main(String[] args) {
		String folder = "c:\\\\Users\\user\\github\\where\\ever\\you\\have\\your\\files\\";
		BeltCrawler bc = new BeltCrawler();
		bc.startCrawling(folder);
	}
}
```

## Starter Class for the Matrix-Crawler

```java
public class MatrixCrawlerStarter {
	public static void main(String[] args) {
		String folder = "c:\\\\Users\\user\\github\\where\\ever\\you\\have\\your\\files\\";
		MatrixCrawler mc = new MatrixCrawler();
		mc.startCrawling(folder);
	}
}
```

## How to start
1. Compile the code
2. Run the code
3. Copy the code from the console 
4. Paste the code to Github or whereever you like to have it
5. The console code is producing markdown (.md) text. You can use PanDoc (https://pandoc.org/) to transform it to whatever you want. 

## Have fun!
