package Sobel;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Frame  extends JFrame {
	int count;
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelThreshold = new JPanel();
	
	JPanel cotrolPanelBin = new JPanel();
	JPanel cotrolPanelSobel = new JPanel();
	JPanel imagePanelOrg;
	JPanel imagePanelRight;
	JButton btnShow;
	JSlider sliderThreshold;
	JButton btnDilate;
	JButton btnErode;
	JButton btnOpen;
	JButton btnClose;
	JButton btnReset;
	JLabel lbCount;
	JTextField tfCount;
	JLabel lbOpenCount;
	JTextField tfOpenCount;
	JLabel lbCloseCount;
	JTextField tfCloseCount;
	JLabel lbIOU;
	JTextField tfIOU;
	JLabel lbThreshold = new JLabel("Threshold ");
	JLabel ThresholdBeging = new JLabel("0");
	JLabel ThresholdEnd = new JLabel("300");
	JTextField tfHueValue = new JTextField(3);
	
	String maskPath = "Sobel\\answer7\\fold1_0.png";
	BufferedImage maskImg = Util.loadImg(maskPath);
	int[][][] mask = Util.makeRGBData(maskImg);
	int[][][] data;
	int[][][] resultData;
	int height, width;
	static BufferedImage imgGrayScale = null;
	static BufferedImage imgSobel = null;
	
	Frame() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		setTitle("Sobel");
		btnShow = new JButton("Show GrayScale Image");
		tfHueValue.setText("0");
		tfHueValue.setEditable(false);
		btnDilate = new JButton("Dilate");
		btnErode = new JButton("Erode");
		lbCount = new JLabel("Count");
		tfCount = new JTextField(5);
		tfCount.setEditable(false);
		btnOpen = new JButton("Open");
		lbOpenCount = new JLabel("Times (Open)");
		tfOpenCount = new JTextField(5);
		btnClose = new JButton("Close");
		lbCloseCount = new JLabel("Times (Close)");
		tfCloseCount = new JTextField(5);
		btnReset = new JButton("Reset");
		lbIOU = new JLabel("IOU");
		tfIOU = new JTextField(7);
		tfIOU.setEditable(false);

		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(6, 1));
		sliderThreshold = new JSlider(0, 300, 0);
		cotrolPanelThreshold.add(btnShow);
		cotrolPanelThreshold.add(ThresholdBeging);
		cotrolPanelThreshold.add(sliderThreshold);
		cotrolPanelThreshold.add(ThresholdEnd);
		cotrolPanelThreshold.add(tfHueValue);
		cotrolPanelThreshold.add(lbThreshold);
		cotrolPanelThreshold.add(btnDilate);
		cotrolPanelThreshold.add(btnErode);
		cotrolPanelThreshold.add(lbCount);
		cotrolPanelThreshold.add(tfCount);
		cotrolPanelThreshold.add(btnOpen);
		cotrolPanelThreshold.add(lbOpenCount);
		cotrolPanelThreshold.add(tfOpenCount);
		cotrolPanelThreshold.add(btnClose);
		cotrolPanelThreshold.add(lbCloseCount);
		cotrolPanelThreshold.add(tfCloseCount);
		cotrolPanelThreshold.add(btnReset);
		cotrolPanelThreshold.add(lbIOU);
		cotrolPanelThreshold.add(tfIOU);

		cotrolPanelMain.add(cotrolPanelThreshold);
		cotrolPanelMain.add(cotrolPanelSobel);
		cotrolPanelMain.add(cotrolPanelBin);
		cotrolPanelMain.setBounds(0, 0, 1500, 200);
		getContentPane().add(cotrolPanelMain);

		imagePanelOrg = new ImagePanelLeft();
		imagePanelOrg.setBounds(0, 70, 700, 700);
		getContentPane().add(imagePanelOrg);
		imagePanelRight = new ImagePanelRight();
		imagePanelRight.setBounds(750, 70, 700, 700);
		getContentPane().add(imagePanelRight);

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImg();
				Graphics g = imagePanelOrg.getGraphics();
				g.drawImage(imgGrayScale, 0, 0, null);
			}
		});

		btnDilate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scale(255);
				tfIOU.setText(IOU(resultData));
				BufferedImage imgDilate = Util.makeImg(resultData);
				Graphics g = imagePanelRight.getGraphics();
				g.drawImage(imgDilate, 0, 0, null);
				count += 1;
				tfCount.setText(count + "");
			}
		});

		btnErode.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				scale(0);
				tfIOU.setText(IOU(resultData));
				BufferedImage imgErode = Util.makeImg(resultData);
				Graphics g = imagePanelRight.getGraphics();
				g.drawImage(imgErode, 0, 0, null);
				count -= 1;
				tfCount.setText(count + "");
			}
		});

		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int num = Integer.parseInt(tfOpenCount.getText());
				for(int i = 0; i < num; i++) {
					scale(0);
				}
				for(int j = 0; j < num; j++) {
					scale(255);
				}
				tfIOU.setText(IOU(resultData));
				BufferedImage imgOpen = Util.makeImg(resultData);
				Graphics g = imagePanelRight.getGraphics();
				g.drawImage(imgOpen, 0, 0, null);
			}
		});

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int num = Integer.parseInt(tfCloseCount.getText());
				for(int i = 0; i < num; i++) {
					scale(255);
				}
				for(int j = 0; j < num; j++) {
					scale(0);
				}
				tfIOU.setText(IOU(resultData));
				BufferedImage imgClose = Util.makeImg(resultData);
				Graphics g = imagePanelRight.getGraphics();
				g.drawImage(imgClose, 0, 0, null);
				
			}
		});

		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doSobel();
				count = 0;
				tfCount.setText(count + "");
			}
		});
	    
		sliderThreshold.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfHueValue.setText(sliderThreshold.getValue() + "");
				doSobel();
			}
		});
	}
     
	void loadImg() {
		imgGrayScale = Util.loadImg("Sobel\\grayImg\\fold1_0.png");
		Util.imgLeft = imgGrayScale;
		data = Util.makeRGBData(imgGrayScale);
		height = data.length;
		width = data[0].length;
	}

	void doSobel() {
		double thresholdOffset = sliderThreshold.getValue();
		int [][][] newData = AdjSobel(data, thresholdOffset);
		resultData = newData;
		tfIOU.setText(IOU(resultData));
		imgSobel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Util.imgRight = imgSobel;
		imgSobel = Util.makeImg(resultData);
        Graphics g = imagePanelRight.getGraphics();
		g.drawImage(imgSobel, 0, 0, null);
	}

	int[][][] AdjSobel(int[][][] data, double thresholdOffset) {
		int[][][] newData = new int[height][width][3];
		for(int i = 1; i < height - 1; i++) {
			for(int j = 1; j < width - 1; j++) {
				double gx = (data[i - 1][j + 1][0] - data[i - 1][j - 1][0]) + 2 * (data[i][j + 1][0] - data[i][j - 1][0]) + (data[i + 1][j + 1][0] - data[i + 1][j - 1][0]);
				double gy = (data[i - 1][j - 1][0] - data[i + 1][j - 1][0]) + 2 * (data[i - 1][j][0] - data[i + 1][j][0]) + (data[i - 1][j + 1][0] - data[i + 1][j + 1][0]);

				double G = Math.sqrt(Math.pow(gx, 2) + Math.pow(gy, 2));
				if(G > thresholdOffset) {
					newData[i][j][0] = 255;
					newData[i][j][1] = 255;
					newData[i][j][2] = 255;
				}
			}
		}
		return newData;
	}

	void scale(int rgb) {
		int[][][] check = new int[height][width][1];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(resultData[y][x][0] == rgb) {
					check[y][x][0] = 1;
				}
			}
		}
		
		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				if(check[y][x][0] == 1) {
					for(int i = 0; i < 3; i++) {
						resultData[y - 1][x - 1][i] = rgb;
						resultData[y - 1][x][i] = rgb;
						resultData[y - 1][x + 1][i] = rgb;
						resultData[y][x - 1][i] = rgb;
						resultData[y][x + 1][i] = rgb;
						resultData[y + 1][x - 1][i] = rgb;
						resultData[y + 1][x][i] = rgb;
						resultData[y + 1][x + 1][i] = rgb;
					}
				}
			}
		}
	}

	String IOU(int[][][] data) {
		int tp = 0;
		int tn = 0;
		int fp = 0;
		int fn = 0;
		int[][] position = select_section(data);
		for(int i = position[0][1]; i < position[1][1]; i++) {
			for(int j = position[0][0]; j < position[1][0]; j++) {
				if(data[i][j][0] == 255 && mask[i][j][0] == 255) {
					tp += 1;
				}
				else if(data[i][j][0] == 0 && mask[i][j][0] == 255) {
					fn += 1;
				}
				else if(data[i][j][0] == 255 && mask[i][j][0] == 0) {
					fp += 1;
				}
				else if(data[i][j][0] == 0 && mask[i][j][0] == 0) {
					tn += 1;
				}
			}
		}
		double IOU_value = (double)tp / (double)(tp + fp + fn);
		return String.format("%.4f", IOU_value);
	}

	int[][] select_section(int[][][] data) {
        int x0 = data[0].length, x1 = 0, y0 = data.length, y1 = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j][0] == 255) {
                    x0 = Math.min(x0, j);
                    x1 = Math.max(x1, j);
                    y0 = Math.min(y0, i);
                    y1 = Math.max(y1, i);
                } 
            }
        }
        return new int[][] {{x0, y0}, {x1, y1}};
    }
	
	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
