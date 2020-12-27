import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

public class AztecDiamond extends JPanel
{
	private int rows = 1, cols = 0;
	private final Random random = new Random(42);
	private char[][] data = new char[rows][cols];

	private void eliminate()
	{
		for (int i=0; i<rows; i++)
		{
			for (int j=0; j<cols-1; j++)
			{
				if (data[i][j] == 'R' && data[i][j+1] == 'L')
				{
					data[i][j] = '\0';
					data[i][j+1] = '\0';
				}
			}

			for (int j=1; j<cols; j++)
			{
				if (data[i][j] == 'D' && data[i][j-1] == 'U')
				{
					data[i][j] = '\0';
					data[i][j-1] = '\0';
				}
			}
		}
	}

	private void move()
	{
		char[][] newData = new char[rows+1][cols+1];

		for (int i=0; i<rows; i++)
		{
			for (int j=0; j<cols; j++)
			{
				char direction = data[i][j];
				switch (direction)
				{
					case 'L': newData[i][j] = direction; break;
					case 'R': newData[i+1][j+1] = direction; break;
					case 'U': newData[i][j+1] = direction; break;
					case 'D': newData[i+1][j] = direction; break;
				}
			}
		}

		data = newData;
		rows++; cols++;
	}

	private void create()
	{
		for (int i=0; i<rows-1; i++)
		{
			for (int j=0; j<cols; j++)
			{
				if (data[i][j] == '\0')
				{
					if (random.nextBoolean())
					{
						data[i][j] = 'U';
						data[i+1][j] = 'D';
					}
					else
					{
						data[i][j] = 'L';
						data[i+1][j] = 'R';
					}
				}
			}
		}
	}

	public void nextStep()
	{
		eliminate();
		move();
		create();
	}

	private char[][] straighten()
	{
		char[][] diamond = new char[cols*2][cols*2];
		for (int i=0; i<rows; i++)
		{
			for (int j=0; j<cols; j++)
			{
				int r = cols - 1 + i - j;
				int c = i + j;
				char direction = data[i][j];
				diamond[r][c] = direction;
				switch (direction)
				{
					case 'L': diamond[r+1][c] = direction; break;
					case 'R': diamond[r-1][c] = direction; break;
					case 'U': diamond[r][c+1] = direction; break;
					case 'D': diamond[r][c-1] = direction; break;
				}
			}
		}
		return diamond;
	}

	private Image getImage()
	{
		char[][] diamond = straighten();
		int sideLength = diamond.length;

		BufferedImage img = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_RGB);
		for (int i=0; i<sideLength; i++)
		{
			for (int j=0; j<sideLength; j++)
			{
				int direction = diamond[i][j];
				switch(direction)
				{
					case 'L': img.setRGB(j, i, Color.RED.getRGB()); break;
					case 'R': img.setRGB(j, i, Color.GREEN.getRGB()); break;
					case 'U': img.setRGB(j, i, Color.BLUE.getRGB()); break;
					case 'D': img.setRGB(j, i, Color.YELLOW.getRGB()); break;
				}
			}
		}

		return img;
	}

	public void paint(Graphics g)
	{
		Image img = getImage();
		Image scaledImage = img.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
		g.drawImage(scaledImage, 20,20,this);
	}

	public static void main(String[] args)
	{
		AztecDiamond aztecDiamond = new AztecDiamond();
		for (int i=0; i<2000; i++)
		{
			aztecDiamond.nextStep();
		}

		JFrame frame = new JFrame();
		frame.getContentPane().add(aztecDiamond);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setVisible(true);
	}
}
