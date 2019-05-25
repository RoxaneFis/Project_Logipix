import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LogipixFrame extends JFrame {

	public LogipixFrame(Cell[][] grid, int height, int width, int step) {
		MazeWindow window = new MazeWindow(grid, height, width, step);
		this.setTitle("Logipix");
		window.setPreferredSize(new Dimension(width * step + 1, height * step + 1));
		this.add(window, BorderLayout.CENTER);
		this.pack();
		this.add(window);
		this.addKeyListener(window);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

class MazeWindow extends JPanel implements KeyListener {
	private final int height, width, step;
	private Cell[][] grid;
	MazeWindow(Cell[][] grid, int height, int width, int step) {
		this.grid = grid;
		this.height = height;
		this.width = width;
		this.step = step;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		int w = this.width * this.step;
		int h = this.height * this.step;
		g.drawLine(0, 0, w, 0);
		g.drawLine(0, 0, 0, h);
		g.drawLine(w, 0, w, h);
		g.drawLine(0, h, w, h);

		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				// draw walls
				g2d.setColor(Color.BLACK);
				int numb =grid[i][j].clue;
				if(numb!=0) {
					String b = Integer.toString(numb);
					g2d.drawString(b,  (j)*(step), (i+1)*step);
					
				}
			
				if(!grid[i][j].hasDirection(Direction.North))
					g2d.drawLine(j*step, i*step, (j+1)*step, i*step);
				if(!grid[i][j].hasDirection(Direction.East))
					g2d.drawLine((j+1)*step, i*step, (j+1)*step, (i+1)*step);
				if(!grid[i][j].hasDirection(Direction.South))
					g2d.drawLine(j*step, (i+1)*step, (j+1)*step, (i+1)*step);
				if(!grid[i][j].hasDirection(Direction.West))
					g2d.drawLine(j*step, i*step, j*step, (i+1)*step);

				// draw mark
				g2d.setColor(Color.RED);

				if(grid[i][j].marked()) {
					int midx = j*step + step/2;
					int midy = i*step + step/2;
					g2d.fillOval(midx - step/4, midy - step/4, step/2, step/2);
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}

