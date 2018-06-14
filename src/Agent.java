/**
 /**
 * 
 * @author 
 * @version 1.00 2014/11/14
 * 

 */
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Comparator;
import java.util.Collections;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Agent {

	protected static final int VISUAL_LENGTH = 15; //ATTENZIONE METTERE SEMPRE NUMERO DISPARI
	private final int VISUAL_HEIGHT = VISUAL_LENGTH;
	private int x,y;
	private static Color c = Color.BLACK;  
	private String nome;
	private static final int HEIGHT = World.MATRIX_HEIGHT;//World.MATRIXDIMENSION; // TODO PROVO  A SETTARLA CON LA VISUAL
	private static final int WIDTH=World.MATRIX_WIDTH;
	protected Position[][] visualField = new Position[VISUAL_LENGTH][VISUAL_HEIGHT];
	//protected Position[][] knownField = new Position[DIMENSION][DIMENSION];
	private Agent agent = this;
	private boolean isSon=false;
	public boolean getIsSon(){return isSon;}
	private static final int MAXSOCIAL = 235;
	private int turniSocial=0;
	private int maxGrowTime = 0;
	private static final int MAXFERTILITY=235;
	private int fertility =15;
	private boolean busy;	
	private Agent currentPartner=null;
	private Agent lastPartner=null;
	private boolean imInOasi=false;
	private AreaInteresse currentOasi=null;
	private AreaInteresse lastOasi=null;
	private boolean isPregnant=false;
	private ArrayList<AreaInteresse> listaAree=null;
	private final boolean UNKNOWN=false;
	private final boolean KNOWN=true;
	private double internalResources=99;
	private final int MAXINTERNALRESOURCES=200;
	private final double RISORSECONSUMATEPERTURNO=1.75;
	private final int RISORSEIMMAGAZZINATEPERTURNO=1;
	private final static int MAXTURNIVITA=950;
	private  int turniVita=0;
	private FIS fis=null;




	//Roba Noiosa	

	protected int maxFirstQuadrantGrowTime;
	protected int maxSecondQuadrantGrowTime;
	protected int maxThirdQuadrantGrowTime;
	protected int maxFourthQuadrantGrowTime;

	protected long minLastUpdateTimeMillis;
	protected long minFirstQuadrantLastUpdateTimeMillis;
	protected long minSecondQuadrantLastUpdateTimeMillis;
	protected long minThirdQuadrantLastUpdateTimeMillis;
	protected long minFourthQuadrantLastUpdateTimeMillis;


	protected int numberOfAgent;
	protected int firstQuadrantAgent;
	protected int secondQuadrantAgent;
	protected int thirdQuadrantAgent;
	protected int fourthQuadrantAgent;

	//Caratteri agente

	private double socialGene=0;
	private double agentDensityGene=0;
	private double resourceGene=0;
	private double growGene=0;
	private double maxResGene=0; 		
	private double distanceGene=0;	
	private double minMediumResGene=0;




	// socialGene, agentDensityGene, resourceGene,  growGene, maxResGene, distanceGene, optimismGene

	public Agent(int x,int y, ArrayList<AreaInteresse> listaAree,double socialGene, double agentDensityGene, double resourceGene, double growGene,double maxResGene,double distanceGene) {

		this.turniSocial=0;	
		this.fertility=0;
		this.x = x;
		this.y = y;
		this.socialGene=socialGene;
		this.agentDensityGene=agentDensityGene;
		this.resourceGene=resourceGene;
		this.growGene=growGene;
		this.maxResGene=maxResGene;
		this.distanceGene=distanceGene;
		minMediumResGene=Utility.randInt(10, 20);
		this.busy=false;
		String fileName=null;
		int rand=Utility.randInt(1,3);
		if(rand==1)
			fileName = "fcl/oasiQuality11.fcl";
		if(rand==2)
			fileName = "fcl/oasiQuality12.fcl";
		if(rand==3)
			fileName = "fcl/oasiQuality15.fcl";
		//fileName = "C:\\Users\\ASUS\\workspaceJavaFx\\LifeFinaleConosciuto\\src\\Files\\fcl\\oasiQuality3su.fcl";
		fis = FIS.load(fileName,true);
		World.world[x][y].setStateOccupied(this); // (cioè passo l'agente che daniel pirla non inizializzava)
		this.listaAree=listaAree;//creaListaAree(listaAree); //TODO LISTAAAAA


		updateVisual();
	}

	public void updateVisual()
	{
		for(int ix = x - (int)((VISUAL_LENGTH-1)/2), xx = 0; ix < x + (VISUAL_LENGTH-1)/2 +1; ix++, xx++) //PERCHè +1 ?????
		{
			for(int iy = y - (int)((VISUAL_HEIGHT-1)/2), yy = 0; iy <  y + (VISUAL_LENGTH-1)/2 +1; iy++, yy++)
			{
				if(!(ix < 0 || iy < 0 || ix >= WIDTH || iy >= HEIGHT))
				{
					visualField[xx][yy] = World.world[ix][iy];
				}
				else
					visualField[xx][yy] = null;

			}
		}

		//	mergeMatrixes(x-(VISUAL_LENGTH-1)/2, y-(VISUAL_HEIGHT-1)/2);
		//	int i = 0;
	}





	public int getVisualFieldDimension(){return VISUAL_LENGTH;}
	public AreaInteresse getMaxPositionDistance(){return  currentOasi;}

	/*	private void mergeMatrixes(int xStart, int yStart) 
	{ 
		for(int ix = 0; ix < VISUAL_LENGTH; ix++)
		{
			for(int iy = 0; iy < VISUAL_HEIGHT; iy++)
			{
				if(this.visualField[ix][iy] != null)
				{
					Position tmp = visualField[ix][iy];
					//knownField[xStart + ix][yStart + iy]=new Position(tmp);
					knownField[xStart + ix][yStart + iy] = new Position(tmp.getType(), tmp.getX(), tmp.getY()); 
					knownField[xStart + ix][yStart + iy].setResources(tmp.getResources());
				} 
			}
		}

	}*/

	public boolean isCrossable(Position pos)
	{
		return  !pos.isOccupied()&& (internalResources+pos.getResources()-RISORSECONSUMATEPERTURNO>35)&&turniVita<MAXTURNIVITA;

	}

	public void updatePosition()  {
		/**
		 *aggiorna i valori di x e y basandosi su metodo quadranti:
		 *
		 */
		turniVita++;
		
		if(turniVita>MAXTURNIVITA||!existValidPosition()){
			World.world[getX()][getY()].setStateFree();
			World.agentsArray.remove(this);
		}
		else{
			eatResources(World.world[x][y]);
			updateVisual();
			if(fertility<MAXFERTILITY)
				fertility++;
			if(turniSocial<MAXSOCIAL)
				turniSocial++;	


			if(imInOasi)esploraOasi();
			else goToNewArea();

		}


	}
	
	private  boolean existValidPosition(){
		if(x-1>=0&&y-1>=0){
			if(isCrossable(World.world[x-1][y-1])) return isCrossable(World.world[x-1][y-1]);
		}
		if(y-1>=0){
			if(isCrossable(World.world[x][y-1])) return isCrossable(World.world[x][y-1]);
		}
		if(x+1<World.MATRIX_WIDTH&&y-1>=0){
			if(isCrossable(World.world[x+1][y-1])) return isCrossable(World.world[x+1][y-1]);
		}
		if(x-1>=0){
			if(isCrossable(World.world[x-1][y])) return isCrossable(World.world[x-1][y]);
		}
		if(x+1<World.MATRIX_WIDTH){
			if(isCrossable(World.world[x+1][y])) return isCrossable(World.world[x+1][y]);
		}
		if(x-1>=0&&y+1<World.MATRIX_HEIGHT){
			if(isCrossable(World.world[x-1][y+1])) return isCrossable(World.world[x-1][y+1]);
		}
		if(y+1<World.MATRIX_HEIGHT){
			if(isCrossable(World.world[x][y+1])) return isCrossable(World.world[x][y+1]);
		}
		if(x+1<World.MATRIX_WIDTH&&y+1<World.MATRIX_HEIGHT){
			if(isCrossable(World.world[x+1][y+1])) return isCrossable(World.world[x+1][y+1]);
		}
		if(true){
			if(isCrossable(World.world[x][y])) return isCrossable(World.world[x][y])|| (internalResources+World.world[x][y].getResources()-RISORSECONSUMATEPERTURNO>0);
		}
		return false;
	}
	
	
	public void eatResources(Position pos)
	{
		double posRes=pos.getResources();

		if(posRes-RISORSECONSUMATEPERTURNO<0) {
			pos.setResources(0);
			internalResources=internalResources-(RISORSECONSUMATEPERTURNO-posRes);		
		}
		else{
			pos.setResources(posRes-RISORSECONSUMATEPERTURNO);
			posRes=pos.getResources();
			if(posRes-RISORSEIMMAGAZZINATEPERTURNO>=0)
				if(RISORSEIMMAGAZZINATEPERTURNO+internalResources<=MAXINTERNALRESOURCES){
					internalResources+=RISORSEIMMAGAZZINATEPERTURNO;
					pos.setResources(posRes-RISORSEIMMAGAZZINATEPERTURNO);
				}
				else{
					pos.setResources(posRes-(MAXINTERNALRESOURCES-internalResources));
					internalResources=MAXINTERNALRESOURCES;
				}

			else{
				if(posRes+internalResources<=MAXINTERNALRESOURCES){
					internalResources+=posRes;
					pos.setResources(0);
				}
				else{
					pos.setResources(posRes-(MAXINTERNALRESOURCES-internalResources));
					internalResources=MAXINTERNALRESOURCES;
				}
			}
		}




		/*if(pos.getResources()>0)
		{
			pos.setResources(pos.getResources()-1);
			//lastUpdateTimeMillis = System.currentTimeMillis();
			pos.setUltimoTurnoModificaRisorse();
		}*/
	}



	private void goToNewArea(){ //TODO DANIEL

		AreaInteresse bestArea=getBestArea();
		//Position pos=bestArea.randomPositionInFrontiera();//getPositionForArea(bestArea);//bestArea.getRoot();
		Position pos=getPositionForArea(bestArea);
		if(isInRange(bestArea)){
			imInOasi=true;
			currentOasi=bestArea;
			lastOasi=bestArea;
			currentOasi.addAgent();
			//goToBestPosition(getBestPosition(currentOasi));
			esploraOasi();
		}
		else{
			Position nextPos=getNextPosForPosBis(pos);
			if(nextPos!=null){//newX>=0&&newX<World.MATRIXDIMENSION&&newY>=0&&newY<World.MATRIXDIMENSION){
				if(isCrossable(nextPos)){
					World.world[x][y].setStateFree();
					x=nextPos.getX();
					y=nextPos.getY();
					World.world[x][y].setStateOccupied(this);

				}
				else goToResource(visualField,VISUAL_LENGTH,VISUAL_LENGTH);
			}
			else goToResource(visualField,VISUAL_LENGTH,VISUAL_LENGTH);

		}

	}




	private void esploraOasi(){ //TODO DANIEL
		if(isPregnant) generateSon();

		if(!World.agentsArray.contains(currentPartner)){ busy=false; currentPartner=null;}

		if(!busy&&turniSocial==MAXSOCIAL) getPartner();      

		if(busy) goToThePartner();
		else
		{
			AreaInteresse bestArea=getBestArea();

			if(!currentOasi.equals(bestArea)&&internalResources>170){//currentOasi.evaluateAbsoluteAreaQuality(this)<minQuality) {
				currentOasi.togliAgent();
				currentOasi=null;
				imInOasi=false;
				goToNewArea();
			}
			else {

				goToResource(visualField,VISUAL_LENGTH,VISUAL_LENGTH);	/*goToBestPosition(currentOasi.getBestPosition(this));*/

			}

		}

	}

	private AreaInteresse getBestArea(){
		double bestQuality=0;
		double maxMaxRes=0;
		double maxDistance=0;
		double maxNumOfAgents=0;
		double maxResources=0;
		double maxMediumRes=0;
		double minDistance=99999999;
		AreaInteresse bestArea=null;
		/*	for(AreaInteresse area: listaAree){
			double distance=distanceFromArea(area);
			if(minDistance>distance)maxDistance=distance;
			bestArea=area;
		}*/

		/*for(AreaInteresse area: listaAree){
			double distance=distanceFromArea(area);
			if(maxDistance<distance)maxDistance=distance;		

			double maxRes=area.getMaxResSum();
			if(maxMaxRes<maxRes) maxMaxRes=maxRes;	

			double numOfAgents=area.getNumOfAgents();
			if(maxNumOfAgents<numOfAgents)maxNumOfAgents=numOfAgents;

			double resources=area.getResources();
			if(maxResources<resources)maxResources=resources;

			double mediumRes=area.getMediumRes();
			if(maxMediumRes<mediumRes) maxMediumRes=mediumRes;	
		}*/

		for(AreaInteresse area: listaAree){
			double quality=evaluateRelativeAreaQualityFuzzy(area);//evaluateRelativeAreaQuality(area,maxMediumRes, maxMaxRes,maxDistance,maxNumOfAgents);

			if(bestQuality<quality){//TODO DIO OCCHIO AL LAST
				bestArea=area;
				bestQuality=quality;
			}	
		}
		return bestArea;
	}

	//CALCOLA QUALITA AREA INTERESSE
	public double evaluateRelativeAreaQuality(AreaInteresse area, double maxMediumRes, double maxMaxRes, double maxDistance, double maxAgents){
		double agentDensityQuality=0, agentSocialQuality=0,distanceQuality=0, resourceQuality=0,maxResQuality=0,growTimeQuality=0,sizeQuality=0;
		ArrayList<Position> positionList=area.getPositionList();	
		int numOfAgents=area.getNumOfAgents();


		//RISORSE
		//double resources=area.getResources()*area.freeCoeff();
		//double maxRes=area.getMaxResSum()*area.freeCoeff();
		double mediumRes=area.getMediumRes();

		if(mediumRes<minMediumResGene)mediumRes=0;
		resourceQuality=Math.pow(mediumRes/maxMediumRes,resourceGene);
		//maxResQuality=Math.pow(maxRes/maxMaxRes,maxResGene);


		//DIMENSIONE
		double size=area.size();
		sizeQuality=Math.pow(size/World.maxAreaSize,.5);





		//AGENTI
		double agentSocial=(double) numOfAgents/maxAgents;

		agentSocialQuality=Math.pow(agentSocial, socialGene);
		//double agentASocialQuality=1-agentSocialQuality;

		//DISTANZA
		double distance=(maxDistance-distanceFromPosition(getPositionForArea(area)))/maxDistance; //TODO OCCHIO A CURRENT OASI
		distanceQuality=Math.pow(distance,distanceGene);
		if(distanceFromArea(area)>(internalResources/(RISORSECONSUMATEPERTURNO))) distanceQuality=0;
		double fertilityCoeff=fertility/MAXFERTILITY;
		double internalResCoeff=internalResources/MAXINTERNALRESOURCES;
		double mediumCoeff=(fertilityCoeff+internalResCoeff)/2;
		//		TODO MEDIE O PRODOTTI?
		return distanceQuality*(agentSocial*fertilityCoeff+sizeQuality*mediumCoeff+resourceQuality*(1-internalResCoeff))/( fertilityCoeff+mediumCoeff+1-internalResCoeff);
		//if(maxAgents!=0)	return (internalResources*(resourceQuality)+distanceQuality)/(internalResources+1);
		/*else*/
		//resource*maxRes*growTime*agentDensity*distance;
		//resourceQuality*agentDensityQuality*distanceQuality*maxResQuality;
		//((100*maxResQuality+resourceQuality+agentSocialQuality+distanceQuality)/103);



	}

	public double evaluateRelativeAreaQualityFuzzy(AreaInteresse area){

		double distance=distanceFromArea(area);
		double agentDensity=area.agentDensity();
		double resourceDensity=area.getMediumRes();

		fis.setVariable("distance", distance);
		fis.setVariable("agentDensity", agentDensity);
		fis.setVariable("resourceDensity",resourceDensity);

		fis.evaluate();
		Variable oasiQuality=fis.getVariable("oasiQuality");
		return oasiQuality.getValue();//defuzzify();

	}
	/*public double evaluateRelativeCurrentAreaQuality(AreaInteresse area, double maxAgents){
		double  agentDensityQuality=0, agentSocialQuality=0, resourceQuality=0, agentQuality=0,maxResQuality=0,growTimeQuality=0;
		ArrayList<Position> positionList=area.getPositionList();	
		int numOfAgents=area.getNumOfAgents();


		//RISORSE
		resourceQuality=area.getMediumRes();
		maxResQuality=area.getMediumMaxRes();
		growTimeQuality=area.getMediumGrow();

		//AGENTI
		agentDensityQuality=(double) (positionList.size()-numOfAgents)/positionList.size();
		agentSocialQuality=(double) numOfAgents/maxAgents;
		if(agentDensityQuality<0)agentDensityQuality=0;
		agentQuality=Math.min(agentDensityQuality, agentSocialQuality);//TODO MINIMO????

		//		TODO MEDIE O PRODOTTI?
		return  resourceQuality*agentQuality*maxResQuality*growTimeQuality;//*agentSocialQuality;
		//return (resourceQuality*fame+agentQuality*greed)/(fame+greed);

	}*/


	public double evaluatePositionResourcesQuality(Position pos){//TODO DANIEL LASCIAMO PERDERE CI SARà ANCHE DENSITa' AGENTI
		double growQuality=0, resourceQuality=0, typeQuality=0, fame=5;

		double	distanceQuality=1/distanceFromPosition(pos);
		//GROWQUALITY
		growQuality= (double)((Position.MAXGROWTIME-pos.getGrowTime())/(Position.MAXGROWTIME-Position.GROWTIMEOASIS));

		//RESOURCEQUALITY
		resourceQuality=(double) pos.getResources()/Position.MAXRESOASIS;

		//TYPEQUALITY
		typeQuality=(double) pos.getMaxResources()/Position.MAXRESOASIS;

		//TODO MEDIE O PRODOTTI?
		//return (distanceQuality*initiative+growQuality*greed+ resourceQuality*fame)/(initiative+greed+fame);
		return growQuality*resourceQuality*typeQuality*distanceQuality; 

	}


	public void updateCurrentAreaResSum(){
		double resSum=0;
		for(Position pos: currentOasi.getPositionList()){
			resSum+=evaluatePositionResourcesQuality(pos);
		}
		currentOasi.setResSum(resSum);
	}


	//TROVA MIGLIORE POSIZIONE NELL'OASI
	public Position getBestPosition(AreaInteresse area){
		ArrayList<Position> positionList=area.getPositionList();
		Position bestPosition=positionList.get(0);
		double bestQuality=0;//bestPosition.evaluateQualityBis(agent,this.getMaxDistanceForAgent(agent),1, 1, 1)*bestPosition.getResources();

		for(Position pos: positionList){

			double quality=evaluatePositionResourcesQuality(pos);
			if(quality>bestQuality) {bestQuality=quality;bestPosition=pos;}

		}

		return bestPosition;
	}


	public boolean isInRange(AreaInteresse area){
		for(Position pos: area.getPositionList()){
			double distance= distanceFromPosition(pos);
			if(distance<=1) return true;
		}
		return false;
	}

	//RESTITUISCE POSIZIONE PIU VICINA DI UN AREA

	private Position getPositionForArea(AreaInteresse area){
		double 	minDistance=999999999;
		ArrayList<Position> frontiera=area.getFrontiera();
		Position minPos=frontiera.get(0);
		for(Position pos:frontiera){
			double 	distance=distanceFromPosition(pos);
			if(distance<minDistance) {
				minDistance=distance;
				minPos=pos;
			}	
		}
		return minPos;
	}


	//RESTITUISCE DISTANZA DA UN AREA CONOSCIUTA
	public double distanceFromArea(AreaInteresse area){

		double 	minDistance=999999999;

		ArrayList<Position> frontiera=area.getFrontiera();

		for(Position pos:frontiera){
			double 	distance=distanceFromPosition(pos);
			if(distance<minDistance) {
				minDistance=distance;
			}
		}
		return minDistance;	
	}

	private Position getNextPosForPosBis(Position pos){
		int posX=pos.getX();
		int posY=pos.getY();
		int oldX=x;
		int oldY=y;
		int newX=x;
		int newY=y;


		if(posX>x){
			newX=oldX+1;
		}
		else {
			if(posX<x)
				newX=oldX-1;
		}

		if(posY>y){
			newY=oldY+1;
		}
		else{
			if(posY<y)newY=oldY-1;
		}

		return World.world[newX][newY];

	}

	private Position getNextPosForPos(Position pos){

		double distance=0;
		double minDistance=999999999;
		Position bestPos=World.world[x-1][y-1];
		for(int posX=x-1; posX<=x+1;posX++){
			distance=World.world[posX][y-1].distanceFromPositions(pos);
			if(distance<minDistance){
				minDistance=distance;
				bestPos=World.world[posX][y-1];
			}
		}

		distance=World.world[x-1][y].distanceFromPositions(pos);
		if(distance<minDistance){
			minDistance=distance;
			bestPos=World.world[x-1][y];
		}
		distance=World.world[x+1][y].distanceFromPositions(pos);
		if(distance<minDistance){
			minDistance=distance;
			bestPos=World.world[x+1][y];
		}

		for(int posX=x-1; posX<=x+1;posX++){
			distance=World.world[posX][y+1].distanceFromPositions(pos);
			if(distance<minDistance){
				minDistance=distance;
				bestPos=World.world[posX][y+1];
			}
		}

		return bestPos;
	}


	public double getMaxDistanceForAgent(AreaInteresse area){
		double 	maxDistance=0;
		for(Position pos:area.getPositionList()){
			double 	distance=distanceFromPosition(pos);
			if(distance>maxDistance) maxDistance=distance;
		}
		return maxDistance;
	}


	public double distanceFromPosition(Position pos){
		return Math.sqrt((x-pos.getX())*(x-pos.getX())+(y-pos.getY())*(y-pos.getY()));
	}

	private void goToBestPosition(Position pos){

		int posX=pos.getX();
		int posY=pos.getY();
		int oldX=x;
		int oldY=y;
		int newX=x;
		int newY=y;


		if(posX>x){newX=oldX+1;}
		else {if(posX<x)newX=oldX-1;}

		if(posY>y){newY=oldY+1;}
		else{if(posX<y)newY=oldY-1;}

		if(newX>=0&&newX<World.MATRIX_WIDTH&&newY>=0&&newY<World.MATRIX_HEIGHT){
			if( isCrossable(World.world[newX][newY])){
				World.world[oldX][oldY].setStateFree();
				this.x=newX;
				this.y=newY;
				World.world[newX][newY].setStateOccupied(this);

			}
			else goToResource(visualField,VISUAL_LENGTH,VISUAL_LENGTH);
		}
		else goToResource(visualField,VISUAL_LENGTH,VISUAL_LENGTH);


	}
	private void goToResource(Position[][] matrix, int w, int h){

		int oldX = x;
		int oldY = y;
		//updateMaxValues(); TODO occhio


		int[] coordinates = {-1,-1};
		int matrixLength = matrix.length;
		coordinates = updatePositionQuadrant(matrix, w, h/*World.world*/); //TODO MATRICE DA CAMBIARE 


		this.x = coordinates[0];
		this.y = coordinates[1];
		if(x != -1 && y != -1)
			World.world[x][y].setStateOccupied(this);
		if(x!=oldX  || y!=oldY)
			World.world[oldX][oldY].setStateFree();


	}

	//Se Non E' Impegnato Cerca Una Partner Nella KnownField
	private void getPartner(){
		double minPartnerDistance=999;
		int middle=VISUAL_LENGTH/2;
		Agent tmpPartner=null;
		for(int x=0;x<VISUAL_LENGTH; x++){
			for(int y=0;y<VISUAL_LENGTH;y++){
				Position position=visualField[x][y];

				if(position!=null){
					if(position.isOccupied()){
						Agent agent=position.getAgent();
						if(!agent.equals(this) && !agent.getBusy() && !agent.equals(lastPartner))
						{
							double distance=Math.sqrt((x- middle)*(x- middle)+(y-middle)*(y-middle));
							if(distance<minPartnerDistance){
								minPartnerDistance=distance;
								tmpPartner=agent;
							}
						}
					}
				}
			}
		}
		if(tmpPartner!=null){
			currentPartner=lastPartner=tmpPartner;
			busy=true;
			tmpPartner.setCurrentPartner(this);	
			tmpPartner.setLastPartner(this);
			tmpPartner.setBusy(true);
		}
	}

	public Position getCurrentPosition(Position[][] matrix){return matrix[x][y];}

	private Position findFreePosition(){ //TODO ECCEZIONE INDEXOUTOFBOUND
		if(isCrossable(World.world[x-1][y-1])) return World.world[x-1][y-1];
		if(isCrossable(World.world[x][y-1])) return World.world[x][y-1];
		if(isCrossable(World.world[x+1][y-1])) return World.world[x+1][y-1];
		if(isCrossable(World.world[x-1][y])) return World.world[x-1][y];
		if(isCrossable(World.world[x+1][y])) return World.world[x+1][y];
		if(isCrossable(World.world[x-1][y+1])) return World.world[x-1][y+1];
		if(isCrossable(World.world[x][y+1])) return World.world[x][y+1];
		if(isCrossable(World.world[x+1][y+1])) return World.world[x+1][y+1];

		return null;
	}

	private void generateSon(){
		Position pos=findFreePosition();
		/*int yson=Utility.randInt(0, World.MATRIXDIMENSION-1);
		int xson=Utility.randInt(0, World.MATRIXDIMENSION-1);

		while(World.world[xson][yson].isOccupied()==true){
			yson=Utility.randInt(0,World.MATRIXDIMENSION-1 );
			xson=Utility.randInt(0, World.MATRIXDIMENSION-1);
		}
		 */
		if(pos!=null){
			int yson=pos.getY();
			int xson=pos.getX();
			Agent son=new Agent(xson, yson,listaAree, socialGene, agentDensityGene, resourceGene,  growGene, maxResGene, distanceGene);///piazzare figlio
			son.setSon();
			World.world[xson][yson].setStateOccupied(son);
			son.setFertility(0);
			World.agentsArray.add(son);
			World.POPULATION++;
			fertility=0;
			turniSocial=0;
			currentPartner.setFertility(0);
			isPregnant=false;
		}
	}

	public void goToThePartner()
	{


		int partnerX=currentPartner.getX();
		int partnerY=currentPartner.getY();

		if(Math.floor(Math.sqrt((x-partnerX)*(x-partnerX)+(y-partnerY)*(y-partnerY)))<=1)
		{
			//this.mergeListaAree(currentPartner);
			turniSocial=0;	
			currentPartner.setSocial(0);
			if(currentPartner.getFertility()==MAXFERTILITY && fertility==MAXFERTILITY&&World.agentsArray.size()<=World.MAXAGENTS &&!isPregnant){ 
				isPregnant=true;
				generateSon();
			}
			busy=false;
			currentPartner.setBusy(false);
			currentPartner.setCurrentPartner(null);
			currentPartner=null;
			goToResource(visualField,VISUAL_LENGTH,VISUAL_LENGTH);
		}
		else {
			int oldX=x;
			int oldY=y;
			int newX=x;
			int newY=y;
			//Nuove cooredinate	
			if(partnerX>x){newX=oldX+1;}
			else {if(partnerX<x)newX=oldX-1;}

			if(partnerY>y){newY=oldY+1;}
			else{if(partnerX<y)newY=oldY-1;}

			if(newX>=0&&newX<World.MATRIX_WIDTH&&newY>=0&&newY<World.MATRIX_HEIGHT){
				if(/*!World.world[newX][newY].isOccupied()&&*/ isCrossable(World.world[newX][newY])){
					World.world[oldX][oldY].setStateFree();
					this.x=newX;
					this.y=newY;
					World.world[newX][newY].setStateOccupied(this);

				}
				else goToResource(visualField,VISUAL_LENGTH,VISUAL_LENGTH);
			}
			else goToResource(visualField,VISUAL_LENGTH,VISUAL_LENGTH);
		}

	}





	private int[] updatePositionQuadrant(Position[][] matrixx, int width, int height) //TODO vediamo
	{

		int firstQuadrantWidth=0;
		int fourthQuadrantWidth = 0;
		int secondQuadrantWidth = 0;
		int thirdQuadrantWidth = 0;
		int firstQuadrantHeight = 0;
		int secondQuadrantHeight = 0;
		int thirdQuadrantHeight = 0;
		int fourthQuadrantHeight = 0;

		int firstOffsetWidth=0;
		int firstOffsetHeight=0;
		int secondOffsetWidth=0;
		int secondOffsetHeight=0;
		int thirdOffsetWidth=0;
		int thirdOffsetHeight=0;
		int fourthOffsetWidth=0;
		int fourthOffsetHeight=0;

		int halfWidth = width%2==0? width/2+1 : ((width-1)/2)+1;
		int halfHeight = height%2==0? height/2+1 : ((height-1)/2)+1;

		if(World.MATRIX_WIDTH-x < halfWidth)
		{
			firstQuadrantWidth = World.MATRIX_WIDTH-x;
			firstOffsetWidth = halfWidth - (World.MATRIX_WIDTH-x);
		}
		else
			firstQuadrantWidth = halfWidth;
		fourthQuadrantWidth = firstQuadrantWidth;
		fourthOffsetWidth = firstOffsetWidth;

		if(x + 1 < halfWidth)
		{
			secondQuadrantWidth = x + 1;
			secondOffsetWidth = halfWidth - (x + 1);
		}
		else
			secondQuadrantWidth = halfWidth;
		thirdQuadrantWidth = secondQuadrantWidth;
		thirdOffsetWidth = secondOffsetWidth;


		if(y + 1 < halfHeight)
		{
			firstQuadrantHeight = y + 1;
			firstOffsetHeight = halfHeight - (y + 1);
		}
		else
			firstQuadrantHeight = halfHeight;
		secondQuadrantHeight = firstQuadrantHeight;
		secondOffsetHeight = firstOffsetHeight;


		if(World.MATRIX_HEIGHT-y < halfHeight)
		{
			thirdQuadrantHeight = World.MATRIX_HEIGHT-y;
			thirdOffsetHeight = halfHeight - (World.MATRIX_HEIGHT-y);
		}
		else
			thirdQuadrantHeight = halfHeight;
		fourthQuadrantHeight = thirdQuadrantHeight;

		fourthOffsetHeight = thirdOffsetHeight;






		/*opera sulle matrici "knownField" e "visualField" e la divide in quadranti, sceglie il migliore ecc*/
		//per prima cosa determina quale è il quadrante migliore nella matrice knownField
		int[] coords = {x,y};
		Quadrant firstQuadrantKnown = new Quadrant(agent, false, 1, firstQuadrantWidth, firstQuadrantHeight, coords, World.world[x][y]);
		Quadrant secondQuadrantKnown = new Quadrant(agent, false, 2, secondQuadrantWidth, secondQuadrantHeight, coords, World.world[x][y]);
		Quadrant thirdQuadrantKnown = new Quadrant(agent, false, 3, thirdQuadrantWidth, thirdQuadrantHeight, coords, World.world[x][y]);
		Quadrant fourthQuadrantKnown = new Quadrant(agent, false, 4, fourthQuadrantWidth, fourthQuadrantHeight, coords, World.world[x][y]);



		int colonna = 0;
		int riga = 0;
		for(int row = firstOffsetHeight; row <= halfHeight-1; row++, riga++){
			for(int column = halfWidth-1; column < width - firstOffsetWidth; column++, colonna++) //TODO sistemare firstoffsetwidth
			{
				firstQuadrantKnown.add(matrixx[column][row], colonna, riga);

			}
			colonna = 0;
		}
		colonna = 0;
		riga = 0;
		for(int row = secondOffsetHeight; row <= halfHeight-1; row++, riga++){
			for(int column = secondOffsetWidth+0; column <= halfWidth-1; column++, colonna++)
			{
				secondQuadrantKnown.add(matrixx[column][row], colonna, riga);
			}
			colonna = 0;
		}
		colonna = 0;
		riga = 0;
		for(int row =  halfHeight-1; row < height - thirdOffsetHeight; row++, riga++){
			for(int column = thirdOffsetWidth+ 0; column <= halfWidth-1; column++, colonna++)
			{
				thirdQuadrantKnown.add(matrixx[column][row], colonna, riga);
			}
			colonna = 0;
		}
		colonna = 0;
		riga = 0;
		for(int row = halfHeight-1; row < height - fourthOffsetHeight; row++, riga++){
			for(int column = halfWidth-1; column < width - fourthOffsetWidth; column++, colonna++)
			{
				fourthQuadrantKnown.add(matrixx[column][row], colonna, riga);
			}
			colonna = 0;
		}


		//calcola le medie pesate e sceglie il quadrante migliore
		ArrayList<Quadrant> sortedQuadrants = getSortedQuadrants(firstQuadrantKnown, secondQuadrantKnown, thirdQuadrantKnown, fourthQuadrantKnown);

		System.out.println("Best Quadrant is: " + sortedQuadrants.get(0).getNumber());
		/*for(Quadrant p : sortedQuadrants)
    	  p.print();*/

		int[] coordsTmp = {-1,-1};
		int counter = 0;
		while(counter < 4){
			coordsTmp = sortedQuadrants.get(counter).bestStep();
			System.out.println("fatti " + counter + " cicli nella lista quadranti per trovare il migliore");
			if(coordsTmp[0] != -1 && coords[1] != -1)
				break;
			counter++;
		}


		return coordsTmp;
		// return chosenQuadrantKnown.bestStep();



	}

	private ArrayList<Quadrant> getSortedQuadrants(Quadrant fi,Quadrant se,Quadrant th,Quadrant fo)
	{

		ArrayList<Quadrant> ret = new ArrayList<Quadrant>();

		ret.add(fi);ret.add(se);ret.add(th);ret.add(fo);
		Collections.sort(ret, new Comparator<Quadrant>() {
			@Override
			public int compare(Quadrant q1, Quadrant q2) {
				System.out.println("comparing quadrant " + q1.getNumber() + " with quadrant " + q2.getNumber());
				return q1.getWeightedAverage() - q2.getWeightedAverage() <0 ? 1 : -1; //invertito per ottenere una lista ordinata in modo che in posizione zero ci sia il migliore
			}
		});
		return ret;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public String getNome(){
		return nome;
	}

	public String getMatrix(){

		String ret = "";
		for(int i=0;i<visualField.length;i++){
			for(int j=0;j<visualField.length;j++){
				if(visualField[j][i]!= null)
					ret += ""+visualField[j][i].getType();
				else ret+="@";
			}
			ret += "\n";
		}
		return ret;
	}

	//Fonde Le Matrici dei Due Agenti
	/*	public void mergeAgentKnownField(Agent agent){ //TODO MADONNA DIO

		for(int x=0;x<World.MATRIXDIMENSION;x++){
			for(int y=0;y<World.MATRIXDIMENSION;y++){

				if((this.knownField[x][y].getType() == World.UNKNOWN && agent.knownField[x][y].getType()!= World.UNKNOWN)|| (this.knownField[x][y].getLastUpdateTimeMillis()<=agent.knownField[x][y].getLastUpdateTimeMillis()&&agent.knownField[x][y].getType()!=World.UNKNOWN))
					this.knownField[x][y]=new Position(agent.knownField[x][y]);

				else if((agent.knownField[x][y].getType() == World.UNKNOWN && this.knownField[x][y].getType()!= World.UNKNOWN)|| (agent.knownField[x][y].getLastUpdateTimeMillis()<=this.knownField[x][y].getLastUpdateTimeMillis()&&this.knownField[x][y].getType()!=World.UNKNOWN))
					agent.knownField[x][y]=new Position(this.knownField[x][y]);

			}

		}
	}*/

	/*public void updateMax(){
		firstQuadrantAgent=0;
		secondQuadrantAgent=0;
		thirdQuadrantAgent=0;
		fourthQuadrantAgent=0;

		maxGrowTime=0;
		maxFirstQuadrantGrowTime=0;
		maxSecondQuadrantGrowTime=0;
		maxThirdQuadrantGrowTime=0;
		maxFourthQuadrantGrowTime=0;

		minLastUpdateTimeMillis=knownField[0][0].getLastUpdateTimeMillis();
		minFirstQuadrantLastUpdateTimeMillis=knownField[0][0].getLastUpdateTimeMillis();
		minSecondQuadrantLastUpdateTimeMillis=knownField[DIMENSION/2][0].getLastUpdateTimeMillis();
		minThirdQuadrantLastUpdateTimeMillis=knownField[0][DIMENSION/2].getLastUpdateTimeMillis();
		minFourthQuadrantLastUpdateTimeMillis=knownField[DIMENSION/2][DIMENSION/2].getLastUpdateTimeMillis();

		for(x=0;x<DIMENSION/2;x++){
			for(y=0;y<DIMENSION/2;y++){
				minFirstQuadrantLastUpdateTimeMillis=Math.min(minFirstQuadrantLastUpdateTimeMillis, knownField[x][y].getLastUpdateTimeMillis());
				maxFirstQuadrantGrowTime=Math.max(maxFirstQuadrantGrowTime,knownField[x][y].getGrowTime());
				if(knownField[x][y].isOccupied())
					firstQuadrantAgent++;
			}

		}
		for(x=0;x<DIMENSION/2;x++){
			for(y=DIMENSION/2;y<DIMENSION;y++){
				minThirdQuadrantLastUpdateTimeMillis=Math.min(minThirdQuadrantLastUpdateTimeMillis, knownField[x][y].getLastUpdateTimeMillis());
				maxThirdQuadrantGrowTime=Math.max(maxThirdQuadrantGrowTime,knownField[x][y].getGrowTime());
				if(knownField[x][y].isOccupied())
					thirdQuadrantAgent++;
			}
		}
		for(x=DIMENSION/2;x<DIMENSION;x++){
			for(y=0;y<DIMENSION/2;y++){
				minSecondQuadrantLastUpdateTimeMillis=Math.min(minSecondQuadrantLastUpdateTimeMillis, knownField[x][y].getLastUpdateTimeMillis());
				maxSecondQuadrantGrowTime=Math.max(maxSecondQuadrantGrowTime,knownField[x][y].getGrowTime());
				if(knownField[x][y].isOccupied())
					secondQuadrantAgent++;
			}

		}
		for(x=DIMENSION/2;x<DIMENSION;x++){
			for(y=DIMENSION/2;y<DIMENSION;y++){
				minFourthQuadrantLastUpdateTimeMillis=Math.min(minFourthQuadrantLastUpdateTimeMillis, knownField[x][y].getLastUpdateTimeMillis());
				maxFourthQuadrantGrowTime=Math.max(maxFourthQuadrantGrowTime,knownField[x][y].getGrowTime());
				if(knownField[x][y].isOccupied())
					fourthQuadrantAgent++;
			}
		}


	}*/

	public void setSon(){isSon=true;}

	public int turniVita(){return turniVita;}

	/*public double getCuriosity(){return curiosity;}
	public double getSociality(){return sociality;}
	public double getGreed(){return greed;}
	public double getInitiative(){return initiative;}
	public double getCaution(){return caution;}*/
	public int getFertility(){return fertility;}
	public void setFertility(int fertility){this.fertility=fertility;}
	public void setBusy(boolean busy){ this.busy=busy;}
	public boolean getBusy(){return busy;}
	private boolean isPregnant(){return isPregnant;}
	public void setCurrentPartner(Agent agent){this.currentPartner=agent;}
	public void setLastPartner(Agent agent){this.lastPartner=agent;}
	public Agent getCurrentPartner(){return currentPartner;}
	public Agent getLastPartner(){return lastPartner;}
	//public Position[][] getKnownField(){return knownField;}
	public void setSocial(int turni){this.turniSocial=turni;}
	public void setCurrentOasi(AreaInteresse area){ this.currentOasi=area;}//TODO DANIEL
	public AreaInteresse getCurrentOasi(){ return currentOasi;}


}