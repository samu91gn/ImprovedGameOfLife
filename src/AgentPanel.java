import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AgentPanel extends JPanel {

	private final int MARGINTOP = 10;
	private final int MARGINLEFT = 800;
	private final int SQUARESDIMENSION = 2;
	private final int SQUARESDISTANCE = 3; //TODO AGGIUNTE VARIABILI FINAL (MODIFICA SAMU)
	private int startPoint;
	
	AgentPanel(Dimension d)
	{
		setSize((int)d.getWidth()-600,(int)d.getHeight());
		
		setVisible(true);
	}
	
	
	
	
	
	//int counter;
	//@Override
	/*public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		/*counter++;
		if(counter%10==0)setBackground(new Color(new Random().nextInt(10000000)));
		for(int y=0;y<World.MATRIXDIMENSION;y++){
			for(int x=0;x<World.MATRIXDIMENSION;x++){
				if(World.agentsArray.get(0).getKnownField()[x][y].getType() == 0)
					g.setColor(Color.red);
				else if(World.agentsArray.get(0).getKnownField()[x][y].getType() == 1)
					g.setColor(Color.blue);
				else if(World.agentsArray.get(0).getKnownField()[x][y].getType() == 2)
					g.setColor(Color.yellow);
				else if(World.agentsArray.get(0).getKnownField()[x][y].getType() == 3)
					g.setColor(Color.gray);
				else if(World.agentsArray.get(0).getKnownField()[x][y].getType() == 4)
					g.setColor(Color.green);
				
				//se risorse finite cancello il quadratino
				if(World.agentsArray.get(0).getKnownField()[x][y].getResources()==0)
					g.setColor(Color.WHITE);
				
				g.drawRect(x*SQUARESDISTANCE+MARGINLEFT,y*SQUARESDISTANCE+MARGINTOP,SQUARESDIMENSION,SQUARESDIMENSION); //TODO MODIFICA SAMU
			}
		}
		
	}*/
	
}
