import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Util {
	static BufferedImage imgLeft;
	static BufferedImage imgRight;
	
	final static int checkPixelBounds(int value){
		if (value >255) return 255;
		if (value <0) return 0;
		return value;
 	} 

	final static int checkImageBounds(int value, int length){
		 if (value>length-1) return length-1;
		 else if (value <0) return 0;
		 else return value;
	}

	//get red channel from colorspace (4 bytes)
	final static int getR(int rgb){
		  return checkPixelBounds((rgb & 0x00ff0000)>>>16);	
    }

	//get green channel from colorspace (4 bytes)
	final static int getG(int rgb){
	  return checkPixelBounds((rgb & 0x0000ff00)>>>8);
	}
	
	//get blue channel from colorspace (4 bytes)
	final static int getB(int rgb){
		  return  checkPixelBounds(rgb & 0x000000ff);
	}
	
	final static int makeColor(int r, int g, int b){
		return (255<< 24 | r<<16 | g<<8 | b);
		
	}
	
	final static int covertToGray(int r, int g, int b){
		return checkPixelBounds((int) (0.2126 * r + 0.7152 * g + 0.0722 * b));		
	}
	
	final static double [] affine(double[][] a, double[] b){
		int aRow = a.length;
		int bRow = b.length;
		double[] result = new double[aRow];
       	
		for (int i=0; i<aRow; i++){
			for (int j=0; j<bRow; j++){
					result[i] +=  a[i][j]*b[j]; 
			}
		}
		return result;
	}
	
	final static int bilinear(int leftTop, 
			                  int rightTop, 
			                  int leftBottom, 
			                  int rightBottom,
			                  double alpha,
			                  double beta){
		double left = linear(leftTop, leftBottom, alpha);
		double right = linear(rightTop, rightBottom, alpha);
		double value = linear(left, right, beta);
		return checkPixelBounds((int)value);
	}
			
	final static double linear(double v1, double v2, double weight){
		return v1+(v2-v1)* weight;
	}

	
	
	static double getS(double min, double max, double l){
		// If Luminance is smaller then 0.5, then Saturation = (max-min) / (max+min)
		// If Luminance is bigger then 0.5. then Saturation = ( max-min) / (2.0-max-min)

		if (min == max)
			return 0;
		if(l<=0.5)
			return (max-min) / (max+min);
		else 
			return  (max-min) / (2.0-max-min);
	}

	static BufferedImage makeImg(int[][][] newData){
		int height = newData.length;
		int width =  newData[0].length;
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
        for (int y=0; y<height; y++) {
        	for (int x=0; x<width; x++) {
        		int rgb = Util.makeColor(newData[y][x][0],
        								 newData[y][x][1], 
        								 newData[y][x][2]);
        		newImg.setRGB(x, y, rgb);
        	}
        	
        }
        return newImg;
	}
	
	public static int[][][] makeRGBData(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int[][][] data = new int[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
			}
		}
		return data;
	}

	static BufferedImage loadImg(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filename));

		} catch (IOException e) {
			System.out.println("IO exception");
		}
		return img;
	}

	static int[][][] median_filter(int[][][] data) {
		for(int i = 1; i < data.length - 1; i++) {
			for(int j = 1; j < data[i].length - 1; j++) {
				int[] target = new int[9];
				target[0] = data[i - 1][j - 1][0];
				target[1] = data[i - 1][j][0];
				target[2] = data[i - 1][j + 1][0];
				target[3] = data[i][j - 1][0];
				target[4] = data[i][j][0];
				target[5] = data[i][j + 1][0];
				target[6] = data[i + 1][j - 1][0];
				target[7] = data[i + 1][j][0];
				target[8] = data[i + 1][j + 1][0];
				Arrays.sort(target);
				data[i][j][0] = target[4];
				data[i][j][1] = target[4];
				data[i][j][2] = target[4];
			}
		}
		return data;
	}

	static int[][][] stretch(int[][][] data) {
		int height = data.length;
		int width = data[0].length;
		int[][][] newData = new int[height][width][3];
		double[] cdf = new double[256];
		int[] histogram = new int[256];
		int total = width * height;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				histogram[data[j][i][0]] += 1;
			}
		}

		int sum = 0;
		for (int i = 0; i < 256; i++) {
			sum += histogram[i];
			cdf[i] = (double) sum / total;
			cdf[i] = Math.round(cdf[i] * 255);
		}

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int oldPixelValue = data[j][i][0];
				int newPixelValue = Util.checkPixelBounds((int) cdf[oldPixelValue]);
				int newValue = Util.checkPixelBounds((int) Math.round(newPixelValue));
				for (int c = 0; c < 3; c++) {
					newData[j][i][c] = newValue;
				}
			}
		}
		return newData;
	}
	
	static int[][][] sobel(int[][][] data, int threshold) {
		int height = data.length;
		int width = data[0].length;
		int[][][] newData = new int[height][width][3];
		for(int i = 1; i < height - 1; i++) {
			for(int j = 1; j < width - 1; j++) {
				double gx = (data[i - 1][j + 1][0] - data[i - 1][j - 1][0]) + 2 * (data[i][j + 1][0] - data[i][j - 1][0]) + (data[i + 1][j + 1][0] - data[i + 1][j - 1][0]);
				double gy = (data[i - 1][j - 1][0] - data[i + 1][j - 1][0]) + 2 * (data[i - 1][j][0] - data[i + 1][j][0]) + (data[i - 1][j + 1][0] - data[i + 1][j + 1][0]);

				double G = Math.sqrt(Math.pow(gx, 2) + Math.pow(gy, 2));
				if(G > threshold) {
					newData[i][j][0] = 255;
					newData[i][j][1] = 255;
					newData[i][j][2] = 255;
				}
			}
		}
		return newData;
	}
}