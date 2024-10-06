import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class test {
    public static void main(String args[]) {
        String path = "grayImg\\fold7_0.png";
        String newPath = "bottleSide\\";
        BufferedImage img, newImg;
        int data[][][], newData[][][];
        img = Util.loadImg(path);
        data = Util.makeRGBData(img);
        //newData = Util.stretch(data);
        //newData = Util.median_filter(data);
        newData = Util.sobel(data, 280);
        newImg = Util.makeImg(newData);
        try {
            File newFile = new File(newPath + "side7.png");              
            ImageIO.write(newImg, "png", newFile);
            System.out.println("已完成!");

        } catch(IOException e) {
            System.out.println("IO exception");
        }

    } 
}
