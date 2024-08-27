import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Sobel {
    public static void main(String[] args) {
        String path = "D:\\Topic\\picture7\\";              
        String newPath = "C:/Users/User/Desktop/sobel_300/";
        File file = new File(path);
        String[] arr = file.list();
        int done = 0;
        for(String fileName: arr) {
            BufferedImage img, newImg;
            int data[][][], newData[][][];
            String filePath = path + "\\" + fileName;
            img = Util.loadImg(filePath);
            data = Util.makeRGBData(img);
            newData = new int[data.length][data[0].length][3];
            int[][] Gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0 ,1}};
            int[][] Gy = {{1, 2, 1}, {0, 0, 0}, {-1, -2 ,1}};
            int threshold = 300;

            for(int i = 1; i < data.length - 1; i++) {
                for(int j = 1; j < data[i].length - 1; j++) {
                    int gx = Gx[0][0] * data[i - 1][j - 1][0] + Gx[0][1] * data[i][j - 1][0] + Gx[0][2] * data[i + 1][j - 1][0] +
                             Gx[1][0] * data[i - 1][j][0] + Gx[1][1] * data[i][j][0] + Gx[1][2] * data[i + 1][j][0] +
                             Gx[2][0] * data[i - 1][j + 1][0] + Gx[2][1] * data[i][j + 1][0] + Gx[2][2] * data[i + 1][j + 1][0];

                    int gy = Gy[0][0] * data[i - 1][j - 1][0] + Gy[0][1] * data[i][j - 1][0] + Gy[0][2] * data[i + 1][j - 1][0] +
                             Gy[1][0] * data[i - 1][j][0] + Gy[1][1] * data[i][j][0] + Gy[1][2] * data[i + 1][j][0] +
                             Gy[2][0] * data[i - 1][j + 1][0] + Gy[2][1] * data[i][j + 1][0] + Gy[2][2] * data[i + 1][j + 1][0];

                    int G = Math.abs(gx) + Math.abs(gy);

                    if(G > threshold) {
                        newData[i][j][0] = 255;
                        newData[i][j][1] = 255;
                        newData[i][j][2] = 255;
                    }

                }
            }
            
            newImg = Util.makeImg(newData);
            try {
                File newFile = new File(newPath + fileName);              
                ImageIO.write(newImg, "png", newFile);
                System.out.println("已完成:" + fileName);
                done += 1;
            } catch(IOException e) {
                System.out.println("IO exception");
            }

        }
        System.out.println("共完成:" + done + "項");
    }
}