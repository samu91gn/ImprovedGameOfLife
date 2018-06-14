/**
 * 
 * @author samu
 * @version 1.00 2014/11/14
 * 
 */

import java.util.ArrayList;

public class Quadrant
{
	private Position[][] matrix;
	private int number;
	private int agentsNumber;
	private int maxGrowTime;
	private long minLastUpdateTimeMillis = Long.MAX_VALUE;
	private int width;
	private int height;
	private int[] origin = new int[2];
	private Segment firstSegment;
	private Segment secondSegment;
	private Segment thirdSegment;
	Agent agent;
	Position position;

	public Quadrant(Agent a, boolean b, int n, int w, int h, int[] o, Position position)
	{
		number = n;
		width = w;
		height = h;
		matrix = new Position[w][h];
		origin = o;
		this.firstSegment = new Segment(1, origin, this.number);
		this.secondSegment = new Segment(2, origin, this.number);
		this.thirdSegment = new Segment(3, origin, this.number);
		agent = a;
		this.position = position;
	}


	public int getAgentsNumber()
	{
		return this.agentsNumber;
	}

	public int getMaxGrowTime()
	{
		return this.maxGrowTime;
	}

	public long getMinLastUpdateTimeMillis()
	{
		return this.minLastUpdateTimeMillis;
	}


	public int getNumber()
	{
		return this.number;
	}

	//aggiunge un nuovo elemento posizione al quadrante
	public void add(Position p, int x, int y)
	{
		matrix[x][y] = p;
		if(p.isOccupied() == true)
			this.agentsNumber++;
		if(p.getLastUpdateTimeMillis() < this.minLastUpdateTimeMillis)
			this.minLastUpdateTimeMillis = p.getLastUpdateTimeMillis();
		if(p.getGrowTime() > this.maxGrowTime)
			this.maxGrowTime = p.getGrowTime();
	}

	public double getWeightedAverage(ArrayList<Position> l)
	{
		double sum = 0;
		int dev = 1;
		double devW=0;
		if(l.isEmpty())
			return sum;
		for(int i = 1; i < l.size(); i++)
		{
			sum +=l.get(i).evaluateQualityBis(agent,1/*VA MESSA DISTANZA MAX*/, agentsNumber, minLastUpdateTimeMillis, maxGrowTime);//agent.evaluatePositionResourcesQuality(l.get(i));//,1/*VA MESSA LA MASSIMA DISTANZA*/,agentsNumber, minLastUpdateTimeMillis, maxGrowTime);//*( l.get(i).getResources());//l.get(i).getResources();
			dev += l.get(i).getResources(); //da sostiture con getWeight
			//devW+=l.get(i).evaluateQuality(agent, agentsNumber, minLastUpdateTimeMillis, maxGrowTime);
		
		}

		return sum/*/devW*/;
	}

	public double getWeightedAverage() //usando la parte commentata è il metodo daniel, mi viene dato sempre il primo quadrante come migliore o.O
	{
		double sum = 0;
		int dev = 1;
		double devW=0;//somma pesi
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				//sum+=matrix[j][i].getResources();
				if(true)//matrix[j][i] != null)
				{
					
				sum +=matrix[j][i].evaluateQualityBis(agent,1/*VA MESSA DISTANZA MAX*/, agentsNumber, minLastUpdateTimeMillis, maxGrowTime);//*(matrix[j][i].getResources());/*getResources()*matrix[j][i].getType();*///matrix[j][i].evaluateQuality(agent, agentsNumber, minLastUpdateTimeMillis, maxGrowTime) * matrix[j][i].getResources();
				dev +=matrix[j][i].getResources();// matrix[j][i].evaluateQuality(agent, agentsNumber, minLastUpdateTimeMillis, maxGrowTime);
				//devW+=matrix[j][i].evaluateQuality(agent, agentsNumber, minLastUpdateTimeMillis, maxGrowTime);
			
				}
				/*else
				{
					Position nullPos = new Position(5,-1,-1);
					nullPos.setStateOccupied(null);
					
					sum += nullPos.evaluateQuality(agent, agentsNumber, minLastUpdateTimeMillis, maxGrowTime) * matrix[j][i].getResources();
					dev += nullPos.getResources();
					devW += nullPos.evaluateQuality(agent, agentsNumber, minLastUpdateTimeMillis, maxGrowTime);
				
				}*/
			}
		}

		return sum/*/devW*/;
	}

	public int[] bestStep() //se fanno tutte cagare restituisce -1, -1
	{

		int[] coords = {-1,-1};

		//metodo che riempie i segmenti del quadrante
		fillSegments(this.number);

		//prima di tutto verifico se il primo elemento in cui steppare del segment è crossabile, se non lo è o se è nullo lascio perdere quel segment
		ArrayList<Segment> segmentsList = new ArrayList<Segment>();
		segmentsList.add(firstSegment);segmentsList.add(secondSegment);segmentsList.add(thirdSegment);

		for(int i = 0; i < segmentsList.size(); i++)
		{
			if(segmentsList.get(i).getList().size() < 2){
				
				try{
					int number = segmentsList.get(i).getNumber();
					int quadrantNumber = segmentsList.get(i).getQuadrantNumber();
					segmentsList.remove(i);
					i--;
					System.out.println("rimosso segment " + number + " del quadrante " + quadrantNumber);
				}
				catch(Exception e)
				{
					segmentsList.remove(i);
					i--;
					System.out.println("rimosso un segment per null");
				}
				
			}
			else{
				Position p = World.world[segmentsList.get(i).get(1).getX()][segmentsList.get(i).get(1).getY()]; //punta a una copia che non è la matrice in world originale
				if(p == null || !agent.isCrossable(p))
				{     		
					System.out.println("rimosso segment " + segmentsList.get(i).getNumber() + " del quadrante " + segmentsList.get(i).getQuadrantNumber());
					segmentsList.remove(i);
					i--;
				}
			}
		}

		if(segmentsList.size() == 0)
			return coords;

		if(segmentsList.size() == 1)
		{
			return segmentsList.get(0).getFirstCellCoord(position);
		}
		else if(segmentsList.size() == 2)
		{
			if(getWeightedAverage(segmentsList.get(0).getList()) >= getWeightedAverage(segmentsList.get(1).getList()))
			{
				return segmentsList.get(0).getFirstCellCoord(position);	 
			}
			else 
			{
				return segmentsList.get(1).getFirstCellCoord(position);	 
			}
		}


		if(getWeightedAverage(firstSegment.getList()) >= Math.max(getWeightedAverage(secondSegment.getList()), getWeightedAverage(thirdSegment.getList())))
		{
			coords = firstSegment.getFirstCellCoord(position);
		}
		else if(getWeightedAverage(secondSegment.getList()) >= Math.max(getWeightedAverage(firstSegment.getList()), getWeightedAverage(thirdSegment.getList())))
		{
			coords = secondSegment.getFirstCellCoord(position);
		}
		else if(getWeightedAverage(thirdSegment.getList()) >= Math.max(getWeightedAverage(secondSegment.getList()), getWeightedAverage(firstSegment.getList())))
		{
			coords = thirdSegment.getFirstCellCoord(position);
		}


		return coords; 
	}

	private int varX(int quadrantNumber, int x)
	{

		if(quadrantNumber == 1)
		{
			return x;
		}
		else if(quadrantNumber == 2)
		{
			return width-1-x;
		}
		else if(quadrantNumber == 3)
		{
			return width-1-x;
		}
		else 
		{
			return x;
		}
	}

	private int varY(int quadrantNumber, int y)
	{

		if(quadrantNumber == 1)
		{
			return y;
		}
		else if(quadrantNumber == 2)
		{
			return y;
		}
		else if(quadrantNumber == 3)
		{
			return height-1-y;
		}
		else 
		{
			return height-1-y;
		}
	}

	private void fillSegments(int quadrantNumber)
	{

		int x = 0;
		int y = 0;


		//riempiamo firstSegment
		//firstSegment.add(position);
		int yMin = height-1;
		while(yMin >= 0 && x < width){
			y = yMin;
			while(y >= 0){
				firstSegment.add(matrix[varX(quadrantNumber,x)][varY(quadrantNumber,y)]);
				y--;
			}
			x++;
			yMin-=2;
		}

		//riempiamo secondSegment


		int yMax = height-3; //max escluso, min incluso
		yMin = height-2;
		x = 1;
		secondSegment.add(position);
		while(yMax >= 0 && x < width)
		{
			y = yMin;
			while(y > yMax)
			{
				secondSegment.add(matrix[varX(quadrantNumber,x)][varY(quadrantNumber,y)]);  
				y--;
			}
			x++;
			yMax -= 2;
			if(x % 2 == 0)
				yMin -= 1;
		}

		//riempiamo thirdSegment

		yMax = height-2;
		x = 0;
		while(yMax >= 0 && x < width)
		{
			y = height-1;
			while(y > yMax)
			{
				thirdSegment.add(matrix[varX(quadrantNumber,x)][varY(quadrantNumber,y)]);
				y--;
			}
			x++;
			if(x % 2 == 0)
				yMax -= 1;

		}
	}

}