import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class ropeTexture {
    public static void main(String[] args) {
        String path = "D:\\Topic\\grayImg\\";              
        String newPath = "C:\\Users\\User\\Desktop\\result\\";
        File file = new File(path);
        String[] arr = file.list();
        int done = 0;
        for(String fileName: arr) {
            BufferedImage img, newImg;
            int grayData[][][], newData[][][];
            String filePath = path + "\\" + fileName;
            img = Util.loadImg(filePath);
            grayData = Util.makeRGBData(img);
            int threshold = 10;
            newData = new int[grayData.length][grayData[0].length][3];

            for(int i  = 0; i < grayData.length; i++) {
                int width = 0;
                boolean reach = false;
                for(int j = 0; j < grayData[i].length - 1; j++) {
                    if(grayData[i][j][0] == 254) continue;

                    if(reach) {
                        if(Math.abs(grayData[i][j][0] - grayData[i][j + 1][0]) < threshold) {
                            width += 1;
                            continue;
                        }
                        else {
                            reach = false;
                        }
                        for(int k = 0; k < width; k ++) {
                            newData[i][j]
                        }
                    }
                    else {
                        if(Math.abs(grayData[i][j][0] - grayData[i][j + 1][0]) > threshold) {
                            reach = true;
                        }
                    }

                }
            }

            
            

            /*newImg = Util.makeImg();
            try {
                File newFile = new File(newPath + fileName);              
                ImageIO.write(newImg, "png", newFile);
                System.out.println("已完成:" + fileName);
                done += 1;
            } catch(IOException e) {
                System.out.println("IO exception");
            }*/

        }
        System.out.println("共完成:" + done + "項");
    }

    static int[][][] check(int[][][] data) {
        return data;
    } 
}

