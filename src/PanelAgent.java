import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class PanelAgent extends JPanel{

	private  int MARGINTOP = 10;
	private  int MARGINLEFT = 25;
	private  int SQUARESDIMENSION;
	private  int SQUARESDISTANCE;
	private int AGENTDIMENSION = 8; //TODO AGGIUNTE VARIABILI FINAL (MODIFICA SAMU)
	private int startPoint;
	private final double pixelBaseDaniel=1820;
	private final double pixelSchermoInUso=5000;
	private double pixelQuadratiDaniel=2;
	private double pixelDistanzaQuadratiDaniel=4;


	private Color[] colorsArray =
		{
			/*new Color(255,255,255),
			new Color(255,0,0),
			new Color(255,19,0),
			new Color(255,38,0),
			new Color(255,57,0),
			new Color(255,76,0),
			new Color(255,95,0),
			new Color(255,114,0),
			new Color(255,133,0),
			new Color(255,152,0),
			new Color(255,171,0),*/
			new Color(255,255,255),
			new Color(0,0,255),
			new Color(0,19,255),
			new Color(0,38,255),
			new Color(0,57,255),
			new Color(0,76,255),
			new Color(0,95,255),
			new Color(0,114,255),
			new Color(0,133,255),
			new Color(0,152,255),
			new Color(0,171,255),
			new Color(255,190,0),
			new Color(255,196,0),
			new Color(255,204,0),
			new Color(255,209,0),
			new Color(255,216,0),
			new Color(255,222,0),
			new Color(255,229,0),
			new Color(255,235,0),
			new Color(255,242,0),
			new Color(255,248,0),
			new Color(255,255,0),
			new Color(226,255,0),
			new Color(198,255,0),
			new Color(179,255,0),
			new Color(141,255,0),
			new Color(113,255,0),
			new Color(85,255,0),
			new Color(56,255,0),
			new Color(28,255,0),
			new Color(0,255,0),
		};


	PanelAgent(Dimension d)
	{
		//setSize((int)d.getWidth(),(int)d.getHeight());
		int w = (int) d.width;
		setBounds(0,0,w-200,d.height);

		setVisible(true);
		startPoint = -(AGENTDIMENSION-SQUARESDIMENSION)/2; //TODO modifica samu per centrare sempre l'agente indipendentemente dalle dimensioni di celle ecc.
		double width=d.getWidth();
		/*SQUARESDIMENSION=(int)(d.getWidth()*pixelQuadratiDaniel/pixelBaseDaniel);
		SQUARESDISTANCE=(int)(d.getWidth()*pixelDistanzaQuadratiDaniel/pixelBaseDaniel);*/
		SQUARESDIMENSION=(int)(pixelSchermoInUso*pixelQuadratiDaniel/pixelBaseDaniel);
	SQUARESDISTANCE=(int)(pixelSchermoInUso*pixelDistanzaQuadratiDaniel/pixelBaseDaniel);

	}





	//int counter;
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		/*counter++;
		if(counter%10==0)setBackground(new Color(new Random().nextInt(10000000)));*/

		/*for(int y=0;y<World.MATRIX_HEIGHT;y++){
			for(int x=0;x<World.MATRIX_WIDTH;x++){
				g.drawRect(x*SQUARESDISTANCE+MARGINLEFT,y*SQUARESDISTANCE+MARGINTOP,SQUARESDIMENSION,SQUARESDIMENSION); //TODO MODIFICA SAMU E DANIEL
				double res=World.world[x][y].getResources();
				res=30.0*res/Position.MAXRESOASIS;
				if(res<2)res=1;
				g.setColor(colorsArray[(int) res]);*/



				/*if(res==30)//getType() == 0)
					g.setColor(colorsArray[30]);
				else if(res==29)//getType() == 1)
					g.setColor(colorsArray[29]);//g.setColor(Color.blue);*/



				/*else if(0.4*Position.MAXRESOASIS<World.world[x][y].getResources())//getType() == 2)
					g.setColor(new Color(133,255,133));//g.setColor(Color.yellow);
				else if(0.2*Position.MAXRESOASIS<World.world[x][y].getResources())//getType() == 3)
					g.setColor(new Color(255,255,0));//g.setColor(Color.gray);
				else if(World.world[x][y].getResources()>0)//getType() == 4)
					g.setColor(new Color(0,0,255));//g.setColor(Color.gray);
				else g.setColor(new Color(255,255,255));*/

				//for(AreaInteresse area:World.listaAree)
				//	if(area.getFrontiera().contains(World.world[x][y])) g.setColor(new Color(255,0,0));

				//if(World.listaAree.get(3).containsPosition(World.world[x][y]))g.setColor(new Color(255,0,0));
				/*if(World.world[x][y].getType() == 0)
					g.setColor(Color.red);
				else if(World.world[x][y].getType() == 1)
					g.setColor(Color.blue);
				else if(World.world[x][y].getType() == 2)
					g.setColor(Color.yellow);
				else if(World.world[x][y].getType() == 3)
					g.setColor(Color.gray);
				else if(World.world[x][y].getType() == 4)
					g.setColor(Color.green);

				se risorse finite cancello il quadratino
				if(World.world[x][y].getResources()==0)
					g.setColor(Color.WHITE);*/

				//g.drawRect(x*SQUARESDISTANCE+MARGINLEFT,y*SQUARESDISTANCE+MARGINTOP,SQUARESDIMENSION,SQUARESDIMENSION); 

					/*if(0.9*Position.MAXRESOASIS<World.agentsArray.get(0).visualField[x][y].getResources())//getType() == 0)
					g.setColor(new Color(0,250,0));
				else if(0.8*Position.MAXRESOASIS<World.agentsArray.get(0).visualField[x][y].getResources())//getType() == 1)
					g.setColor(new Color(120,255,120));//g.setColor(Color.blue);
				else if(0.7*Position.MAXRESOASIS<World.agentsArray.get(0).visualField[x][y].getResources())//getType() == 2)
					g.setColor(new Color(204,255,204));//g.setColor(Color.yellow);
				else if(0.6*Position.MAXRESOASIS<World.agentsArray.get(0).visualField[x][y].getResources())//getType() == 3)
					g.setColor(new Color(255,255,0));//g.setColor(Color.gray);
				else if(0.5*Position.MAXRESOASIS<World.agentsArray.get(0).visualField[x][y].getResources())//getType() == 4)
					g.setColor(new Color(255,255,150));//g.setColor(Color.green);
				else if(0.4*Position.MAXRESOASIS<World.agentsArray.get(0).visualField[x][y].getResources())//getType() == 5)
					g.setColor(new Color(255,250,204));//g.setColor(Color.black);
				//se risorse finite cancello il quadratino
				if(World.agentsArray.get(0).visualField[x][y].getResources()==0)
					g.setColor(Color.WHITE);*/
				
				

				//	g.drawRect(x*3+800,y*3+MARGINTOP,2,2); //TODO MODIFICA SAMU

				//disegno la known del primo

			/*}
		}*/
		
		for(int y=0;y<Agent.VISUAL_LENGTH;y++){
			for(int x=0;x<Agent.VISUAL_LENGTH;x++){
				g.fillOval(x*SQUARESDISTANCE+MARGINLEFT,y*SQUARESDISTANCE+MARGINTOP,SQUARESDIMENSION,SQUARESDIMENSION); //TODO MODIFICA SAMU E DANIEL
				double res=World.agentsArray.get(0).visualField[x][y].getResources();
				res=30.0*res/Position.MAXRESOASIS;
				if(res<2)res=1;
				g.setColor(colorsArray[(int) res]);
				
				
				int newX = World.agentsArray.get(0).getX();
				int newY = World.agentsArray.get(0).getY();
				int xx = World.agentsArray.get(0).visualField[x][y].getX();
				int yy = World.agentsArray.get(0).visualField[x][y].getY();
				if(World.world[xx][yy].isOccupied()){
					g.setColor(Color.black);
					
					//if(World.agentsArray.get(i).getIsSon()&&World.agentsArray.get(i).turniVita()<400) g.setColor(Color.red);		
					g.fillOval(startPoint +(newX)*SQUARESDISTANCE+MARGINLEFT,startPoint+(newY)*SQUARESDISTANCE+MARGINTOP,AGENTDIMENSION,AGENTDIMENSION); //TODO MODIFICA SAMU
				}
			}
		}
		
		/*for(int i=0;i<World.agentsArray.size();i++){
			if(World.agentsArray.get(i) != null){
				int newX = World.agentsArray.get(i).getX();
				int newY = World.agentsArray.get(i).getY();

				g.setColor(Color.black);

				//if(World.agentsArray.get(i).getIsSon()&&World.agentsArray.get(i).turniVita()<400) g.setColor(Color.red);		
				g.fillOval(startPoint +newX*SQUARESDISTANCE+MARGINLEFT,startPoint+newY*SQUARESDISTANCE+MARGINTOP,AGENTDIMENSION,AGENTDIMENSION); //TODO MODIFICA SAMU


				System.out.println("agente disegnato in " + newX + ", "+ newY);
			}
		}*/


	}

}