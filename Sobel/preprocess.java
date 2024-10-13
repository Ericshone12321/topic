import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class preprocess {
    public static void main(String args[]) {
        String path = "grayImg\\fold7_0.png";
        String sidepath = "bottleSide\\side7.png";
        String newPath = "removeSide\\";
        BufferedImage img, newImg, sideImg;
        int data[][][], newData[][][], sideData[][][];
        img = Util.loadImg(path);
        sideImg = Util.loadImg(sidepath);
        data = Util.makeRGBData(img);
        sideData = Util.makeRGBData(sideImg);
        data = Util.sobel(data, 71);
        //newData = Util.stretch(data);
        //newData = Util.median_filter(data);
        newData = Util.cutside(data, sideData);

        newImg = Util.makeImg(newData);
        try {
            File newFile = new File(newPath + "cutside7.png");              
            ImageIO.write(newImg, "png", newFile);
            System.out.println("已完成!");

        } catch(IOException e) {
            System.out.println("IO exception");
        }

    }

    
}
