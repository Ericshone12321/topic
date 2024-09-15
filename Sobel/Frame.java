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
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelThreshold = new JPanel();
	
	JPanel cotrolPanelBin = new JPanel();
	JPanel cotrolPanelSobel = new JPanel();
	JPanel imagePanelOrg;
	JPanel imagePanelSobel;
	JButton btnShow;
	JSlider sliderThreshold;
	JLabel lbThreshold = new JLabel("    Threshold");
	JLabel ThresholdBeging = new JLabel("0");
	JLabel ThresholdEnd = new JLabel("300");
	JTextField tfHueValue = new JTextField(3);

	int[][][] data;
	static BufferedImage imgGrayScale = null;
	static BufferedImage imgSobel = null;
	
	Frame() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		setTitle("Sobel");
		btnShow = new JButton("Show GrayScale Image");
		tfHueValue.setText("0");
		tfHueValue.setEditable(false);

		
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(6, 1));
		sliderThreshold = new JSlider(0, 300, 0);
		cotrolPanelShow.add(btnShow);
		cotrolPanelThreshold.add(ThresholdBeging);
		cotrolPanelThreshold.add(sliderThreshold);
		cotrolPanelThreshold.add(ThresholdEnd);
		cotrolPanelThreshold.add(tfHueValue);
		cotrolPanelThreshold.add(lbThreshold);

		cotrolPanelMain.add(cotrolPanelShow);
		cotrolPanelMain.add(cotrolPanelThreshold);

		cotrolPanelMain.add(cotrolPanelSobel);
		cotrolPanelMain.add(cotrolPanelBin);
		cotrolPanelMain.setBounds(0, 0, 1200, 200);
		getContentPane().add(cotrolPanelMain);

		imagePanelOrg = new ImagePanelLeft();
		imagePanelOrg.setBounds(0, 220, 700, 700);
		getContentPane().add(imagePanelOrg);
		imagePanelSobel = new ImagePanelRight();
		imagePanelSobel.setBounds(750, 220, 700, 700);
		getContentPane().add(imagePanelSobel);

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImg();
				Graphics g = imagePanelOrg.getGraphics();
				g.drawImage(imgGrayScale, 0, 0, null);
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
		imgGrayScale = Util.loadImg("Sobel\\grayImg\\fold6_0.png");
		Util.imgLeft = imgGrayScale;
		data = Util.makeRGBData(imgGrayScale);
	}

	void doSobel() {
		double thresholdOffset = sliderThreshold.getValue();
		int [][][] newData = AdjSobel(data, thresholdOffset);
		imgSobel = new BufferedImage(newData[0].length, newData.length, BufferedImage.TYPE_INT_ARGB);
		Util.imgRight = imgSobel;
		for (int y=0; y < newData.length; y++) {
        	for (int x=0; x < newData[0].length; x++) {
        		int rgb = Util.makeColor(newData[y][x][0],
        								 newData[y][x][1], 
        								 newData[y][x][2]);
        		imgSobel.setRGB(x, y, rgb);
        	}
        }
        Graphics g = imagePanelSobel.getGraphics();
		g.drawImage(imgSobel, 0, 0, null);

	}

	int[][][] AdjSobel(int[][][] data, double thresholdOffset) {
		int[][][] newData = new int[data.length][data[0].length][3];
		for(int i = 1; i < data.length - 1; i++) {
			for(int j = 1; j < data[i].length - 1; j++) {
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
	
	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
