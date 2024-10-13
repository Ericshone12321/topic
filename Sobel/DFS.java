import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class DFS {
     static int[][][] check;
     static ArrayList <int[]> position;
    public static void main(String args[]) {
        String path = "removeSide\\cutside1.png";
        String newPath = "DFSpicture\\";
        BufferedImage img, newImg;
        int data[][][], newData[][][];
        img = Util.loadImg(path);
        data = Util.makeRGBData(img);
        check = new int[data.length][data[0].length][1];
        newData = new int[data.length][data[0].length][3];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                if(data[i][j][0] == 255) {
                    check[i][j][0] = 1;
                }
            }
        }

        for(int i = 1; i < data.length - 1; i++) {
            for(int j = 1; j < data[0].length - 1; j++) {
                position = new ArrayList<int[]>();
                if(check[i][j][0] == 1) {
                    change(i, j);
                    dfs(i, j);
                }

                if(position.size() > 30) {
                    for(int[] p: position) {
                        newData[p[0]][p[1]][0] = 255;
                        newData[p[0]][p[1]][1] = 255;
                        newData[p[0]][p[1]][2] = 255;
                    }
                }
            }
        }

        newImg = Util.makeImg(newData);
        try {
            File newFile = new File(newPath + "dfs1.png");              
            ImageIO.write(newImg, "png", newFile);
            System.out.println("已完成!");

        } catch(IOException e) {
            System.out.println("IO exception");
        }
    }

    static void dfs(int i, int j) {
        if(check[i - 1][j - 1][0] == 1) {
            change(i - 1, j - 1);
            dfs(i - 1, j - 1);
        }
        if(check[i - 1][j][0] == 1) {
            change(i - 1, j);
            dfs(i - 1, j);
        }
        if(check[i - 1][j + 1][0] == 1) {
            change(i - 1, j + 1);
            dfs(i - 1, j + 1);
        }
        if(check[i][j - 1][0] == 1) {
            change(i, j - 1);
            dfs(i, j - 1);
        }
        if(check[i][j + 1][0] == 1) {
            change(i, j + 1);
            dfs(i, j + 1);
        }
        if(check[i + 1][j - 1][0] == 1) {
            change(i + 1, j - 1);
            dfs(i + 1, j - 1);
        }
        if(check[i + 1][j][0] == 1) {
            change(i + 1, j);
            dfs(i + 1, j);
        }
        if(check[i + 1][j + 1][0] == 1) {
            change(i + 1, j + 1);
            dfs(i + 1, j + 1);
        }
    }

    static void change(int i, int j) {
        check[i][j][0] = 2;
        int[] temp = {i, j};
        position.add(temp);
    }
}