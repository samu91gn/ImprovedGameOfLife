/**
 * @(#)Risorsa.java
 *
 *

 * Type 0 = Oasi
 * Type 1 = Bosco
 * Type 2 = Deserto
 * Type 3 = Alaska
 * Type 4 = Acqua 
 *da fare:
 *un thread interno a Agent che la sposta e fa tutto in base a quello che vede
 *un thread in world che aggiorna le risorse e aggiorna la UI, contiene matrice e array agenti.
 */


public class Position{

	private double resources;
	private int type;
	private double maxResources;
	private int growTime;
	private int x;
	private int y;
	private boolean isRoot=false;//TODO DANIEL

	private Agent agent;
	/**final values*/
	public final static double MAXRESOASIS = 60;
	private final static double MAXRESALASKA = 0;
	final static int GROWTIMEOASIS =27;
	final static int GROWTIMEWATER = 1000;
	private final static int GROWTIMEALASKA = 100;
	public final static int MAXGROWTIME=100000;//TODO DANIEL
	public final static int SEAGROWTIME=100;
	private long lastUpdateTimeMillis;
	private long ultimoTurnoModificaRisorse=0;
	private boolean isOccupied = false;
	private boolean imInFrontiera=false;

	public Position (int type, int x, int y) {
		this.x = x;
		this.y = y;
		this.type = type;
		this. lastUpdateTimeMillis = System.currentTimeMillis(); 
		switch(type)
		{
		case 4: 
			maxResources = MAXRESOASIS;
			resources = MAXRESOASIS;
			growTime = GROWTIMEOASIS;
			break;
		case 3:
			/*maxResources = MAXRESWOOD;
			resources = MAXRESWOOD;
			growTime = GROWTIMEOASIS;*/
			break;
		case 2:
			/*maxResources = MAXRESDESERT;
			resources = MAXRESDESERT;*/
			growTime = GROWTIMEOASIS;
			break;
		case 1:
			/*maxResources = MAXRESWATER;
			resources = MAXRESWATER;*/
			growTime = GROWTIMEOASIS;

			break;
		case 0:
			maxResources = MAXRESALASKA;
			resources = MAXRESALASKA;
			growTime = GROWTIMEALASKA;
			break;
		case 5:
			maxResources = 1;
			resources = 1;
			growTime = 20;
			break;
		}
	}




	//Costruttore clone
	public Position(Position pos){
		this(pos.getMaxResources(), pos.getGrowTime(), pos.getX(),pos.getY());

		//this.isOccupied=pos.isOccupied();
		this.resources=pos.getResources();
		this.lastUpdateTimeMillis=pos.getLastUpdateTimeMillis();
		this.isRoot=pos.imRoot();
		this.imInFrontiera=pos.imInFrontiera;
		

	}

	public Position(double maxResources, int growTime, int x, int y){ //TODO GRADIENTI DANIEL
		this.maxResources=maxResources;
		this.growTime=growTime;
		this.x=x;
		this.y=y;
		this.resources=maxResources;
	}


	public Position copyOf(){

		return new Position(this);

	}


	public double getMaxResources(){return maxResources;}

	public static double getMaxResPos(int type)
	{
		switch(type)
		{
		case 4: 
			return MAXRESOASIS;

		case 3:
			//return MAXRESWOOD;

		case 2:
			//return MAXRESDESERT;

		case 1:
			//return MAXRESWATER;

		case 0:
			return MAXRESALASKA;

		case 5:
			return 1;

		}
		return -1;
	}

	public boolean imRoot(){return isRoot;}
	public void setRoot(){this.isRoot=true;}
	public void setNotRoot(){this.isRoot=false;}
	public void setGrowTime(int growTime){this.growTime=growTime;}
	public static int getGrowTime(int type){
		switch(type)
		{
		case 4: 
			return GROWTIMEOASIS;
		case 3:
			return GROWTIMEOASIS;
		case 2:
			return GROWTIMEOASIS;
		case 1:
			return GROWTIMEOASIS;
		case 0:
			return GROWTIMEOASIS;
		case 5:
			return 20;
		}
		return -1;
	}


	public static int getTimeMultiplierPos(int type)
	{
		switch(type)
		{
		case 4: 
			return 2;

		case 3:
			return 4;

		case 2:
			return 6;

		case 1:
			return 8;

		case 0:
			return 10;

		case 5:
			return 1;

		}
		return -1;
	}


	public int getType()
	{
		return this.type;
	}


	public void setType(int t)
	{
		this.type = t;
	}


	public Agent getAgent(){return agent;}


	public Object clone() throws CloneNotSupportedException {
		Position pos = (Position)super.clone();
		return pos;
	}


	public double evaluateQuality(Agent agent, int quadrantPopulation,  long minLastUpdateTimeMillis,long maxGrowTime){
		int maxPopulation = World.POPULATION;
		int agentX=agent.getX(), agentY=agent.getY();
		double maxDistance, distanceQuality=1, timeQuality, growQuality=1, populationQuality, resourceQuality=1;
		long currentTime=System.currentTimeMillis();
		double fame=1;
		//Caratterizzazione Agente
	//	double curiosity=agent.getCuriosity(),greed=agent.getGreed(),sociality=agent.getSociality(),initiative=agent.getInitiative(),caution=agent.getCaution();
		double average;

		double d=distanceFromPositions(agent.getCurrentPosition(World.world));

		if(d<1) d=1;


		distanceQuality=1/(d);
		resourceQuality=(double)resources/MAXRESOASIS;
		if(Schema.choice.equals("NORMAL")) growQuality=((GROWTIMEWATER-growTime)/(GROWTIMEWATER-GROWTIMEOASIS));
		else growQuality=((MAXGROWTIME-growTime)/(MAXGROWTIME-GROWTIMEOASIS));
		growQuality=(double) (MAXGROWTIME-growTime)/(MAXGROWTIME-GROWTIMEOASIS);


		return (distanceQuality+growQuality+ resourceQuality*fame)/(2+fame); 
		//return growQuality*distanceQuality*resourceQuality;


	}



	public double evaluateQualityBis(Agent agent, double maxDistance,int quadrantPopulation,  long minLastUpdateTimeMillis,long maxGrowTime){
		int maxPopulation = World.POPULATION;
		int matrixDimension=agent.getVisualFieldDimension(), agentX=agent.getX(), agentY=agent.getY();
		double  distanceQuality, timeQuality, growQuality=0, populationQuality, resourceQuality=0, typeQuality=0;
		long currentTime=System.currentTimeMillis();
		double fame=8;
		//Caratterizzazione Agente
		//double curiosity=agent.getCuriosity(),greed=agent.getGreed(),sociality=agent.getSociality(),initiative=agent.getInitiative(),caution=agent.getCaution();
		//double average;

		//Calcolo maxDistance
		/*	if(agentX>matrixDimension/2)
			if(agentY>matrixDimension/2) maxDistance=Math.sqrt(agentX*agentX+agentY*agentY);
			else maxDistance=Math.sqrt(matrixDimension*matrixDimension+(matrixDimension-agentY)*(matrixDimension-agentY));
		else
			if(agentY>matrixDimension/2) maxDistance=Math.sqrt(agentY*agentY+(matrixDimension-agentX)*(matrixDimension-agentX));
			else maxDistance=Math.sqrt((matrixDimension-agentX)*(matrixDimension-agentX)+(matrixDimension-agentY)*(matrixDimension-agentY));
		 */
		//Esponente esprime velocità con cui decresce il tipo di qualità. Sarà un delirio....
		//distanceQuality=Math.pow(1- Math.sqrt((x-agentX)*(x-agentX)+(y-agentY)*(y-agentY))/maxDistance, 2);
		double d=Math.sqrt((x-agentX)*(x-agentX)+(y-agentY)*(y-agentY));

		if(d<1) d=1;
		distanceQuality=1/d;
		resourceQuality=(double) resources/MAXRESOASIS;		
		if(Schema.choice.equals("NORMAL")) growQuality=((GROWTIMEWATER-growTime)/(GROWTIMEWATER-GROWTIMEOASIS));
		else  growQuality=(double)((MAXGROWTIME-growTime)/(MAXGROWTIME-GROWTIMEOASIS));
		growQuality=(double) (MAXGROWTIME-growTime)/(MAXGROWTIME-GROWTIMEOASIS);
		typeQuality=(double) maxResources/MAXRESOASIS;
		//double weightSum=initiative+greed;
		//BESTIA
		//if(type==World.UNKNOWN) return ((distanceQuality*initiative+growQuality*greed)/(initiative+greed))*curiosity;
		/*else// return (distanceQuality*initiative+growQuality*greed)/(initiative+greed);*/
		if(false) return 0;
		else //return (distanceQuality*initiative+growQuality*greed+resourceQuality*fame)/(initiative+greed+fame);
			//return Math.mix(distanceQuality*initiative,growQuality*greed);
			////return growQuality; 
			return distanceQuality*resourceQuality;
		//return distanceQuality;
		//return 1;

	}



	public double distanceFromPositions(Position pos1){
		double pos1X=pos1.getX();
		double pos1Y=pos1.getY();

		return Math.sqrt((pos1X-x)*(pos1X-x)+(pos1Y-y)*(pos1Y-y));
	}

	/*public boolean isCrossable()
	{
		return  !isOccupied()&& getResources()!=0;

	}*/


	public int getGrowTime()
	{
		return this.growTime;
	}


	public long getLastUpdateTimeMillis()
	{
		return this.lastUpdateTimeMillis;
	}


	public boolean isOccupied()
	{
		return this.isOccupied;
	}


	public void setStateOccupied(Agent a)
	{
		this.isOccupied = true;
		agent = a;
	}

	public boolean imInFrontiera(){return imInFrontiera;}
	public void setInFrontiera(){imInFrontiera =true;}
	public void setNotInFrontiera(){imInFrontiera=false;}

	public int getX(){
		return x;
	}


	public int getY(){
		return y;
	}


	public void setStateFree()
	{
		this.isOccupied = false;
		this.agent = null;
	}


	public double getResources(){
		return resources;
	}


	public void setResources(double r){
		resources = r;
		lastUpdateTimeMillis = System.currentTimeMillis();
	}


	public void addResources(int r) {
		

		if(resources < maxResources && (World.turni-ultimoTurnoModificaRisorse)>growTime && !isOccupied()){

			resources+=r;
			//	lastUpdateTimeMillis = temp;
			ultimoTurnoModificaRisorse=World.turni;
		}


	}

	public void setUltimoTurnoModificaRisorse(){
		ultimoTurnoModificaRisorse=World.turni;
	}

}


