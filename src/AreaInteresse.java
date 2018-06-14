import java.util.ArrayList;


public class AreaInteresse {
	ArrayList<Position> positionList=new ArrayList<Position>();
	private static final  double coeffRes=5; //FORSE NON FINAL
	private ArrayList<Position> rootsList=new ArrayList<Position>();
	private ArrayList<Position> frontiera=new ArrayList<Position>();
	private double resSum=0;
	private int numOfAgents=0;
	private int ID;
	private boolean imKnown=false;
	private long lastUpdateTimeMillis=0;
	protected final boolean KNOWN=true;
	protected final boolean UNKNOWN=false;
	
	//private String nome;


	//COSTRUTTORI CREAZIONE MONDO
	public AreaInteresse(Position root){
		rootsList.add(root);
		int rootX=root.getX();
		int rootY=root.getY();
		//nome="Io sono la "+String.valueOf(nomeint++);
		double minRes=(double) Position.MAXRESOASIS/coeffRes;


		for(int y=0; y<World.MATRIX_HEIGHT;y++){
			for(int x=0; x<World.MATRIX_WIDTH;x++){
				Position pos=World.world[x][y];
				int posX = pos.getX();
				int posY=pos.getY();
				double d=Math.sqrt((rootX-posX)*(rootX-posX)+(rootY-posY)*(rootY-posY));

				if(pos!=null)
					if(d<20&&World.world[x][y].getMaxResources()>minRes)
						positionList.add(World.world[x][y]);

			}
		}

	}




	public AreaInteresse(AreaInteresse area1,AreaInteresse area2){

		for(Position pos:area2.positionList){
			if(!area1.positionList.contains(pos)) area1.positionList.add(pos);
		}
		this.positionList=area1.positionList;

		for(Position pos: this.positionList){
			if(pos.imRoot()) rootsList.add(pos);
		}
		imKnown=false;

	}
	//////////////////////////////

	
	public double freeCoeff(){
		double freeCoeff=(double) (positionList.size()-numOfAgents)/positionList.size();
		if(freeCoeff<0) freeCoeff=0;
		return freeCoeff;
	}
	
	public double agentDensity(){
		double density=((double)(numOfAgents))/positionList.size();
		if(density>1)density=1;
		return density;
	}
	public double getResources(){
		double sum=0;
		for(Position pos: positionList){
			sum+=pos.getResources();
		}
		return sum*freeCoeff();
	}

	public double getMaxResSum(){
		double sum=0;
		for(Position pos: positionList){
			sum+=pos.getMaxResources();
		}
		return sum*freeCoeff();
	}

	//MEDIE 
	public double getMediumMaxRes(){
		double sum=0;
		for(Position pos:positionList){
			sum+=(double) pos.getMaxResources()/Position.MAXRESOASIS;

		}
		return (sum/positionList.size());
	}
	public double getMediumGrow(){
		double sum=0;
		for(Position pos:positionList){
		//	sum+=(double) (Position.MAXGROWTIME-pos.getGrowTime())/Position.MAXGROWTIME;		
		}
		return sum/positionList.size();
	}
	public double getMediumRes(){
		double sum=0;
		for(Position pos:positionList){
			sum+=(double) pos.getResources();///Position.MAXRESOASIS;		
		}
		return (sum/positionList.size());
	}
	////

	public double size(){return positionList.size();}


	public void setResSum(double resSum){this.resSum=resSum;}

	public void addPosAFrontiera(Position pos){frontiera.add(pos);}

	public ArrayList<Position> getFrontiera(){return frontiera;}

	public boolean contieneNellaFrontiera(Position pos){
		int x=pos.getX();
		int y=pos.getY();
		if(!positionList.contains(World.world[x-1][y-1])) return true;
		if(!positionList.contains(World.world[x][y-1])) return true;
		if(!positionList.contains(World.world[x+1][y-1])) return true;
		if(!positionList.contains(World.world[x-1][y])) return true;
		if(!positionList.contains(World.world[x+1][y])) return true;
		if(!positionList.contains(World.world[x-1][y+1])) return true;
		if(!positionList.contains(World.world[x][y+1])) return true;
		if(!positionList.contains(World.world[x+1][y+1])) return true;

		return false;
	}





	public Position getRoot(){return rootsList.get(0);}
	public ArrayList<Position> getRoots(){return rootsList;}
	public void addAgent(){numOfAgents++;}
	public void togliAgent(){numOfAgents--;}
	public int getNumOfAgents(){return numOfAgents;}
	public boolean imKnown(){return imKnown;}
	public void setKnown(){imKnown=true;}
	public void setUnknown(){imKnown=false;}
	public long getLastUpdateTimeMillis(){return lastUpdateTimeMillis;}



	//CONFRONTA QUALITA' DELL'AREA IN FUNZIONE DELLA QUALITA MASSIMA DELL'AREA STESSA
	/*public double evaluateAbsoluteAreaQuality(Agent agent){

		double sum=0;
		for(Position pos: positionList){
			sum+=pos.evaluateAbsoluteQuality();
		}
		double absoluteQuality= sum/positionList.size();
		double agentQuality=(double) (positionList.size()-numOfAgents)/positionList.size();
		return  (absoluteQuality*agentQuality)/bestAbsoluteQuality;
	}*/





	public ArrayList<Position> getPositionList(){return positionList;}

	public boolean intersects(AreaInteresse area){
		ArrayList<Position> positionList=area.getPositionList();

		for(Position pos:this.positionList){
			if(positionList.contains(pos)) return true;
		}

		return false;
	}


	public boolean containsPosition(Position pos){
		return positionList.contains(pos);
	}
	
	
	public Position randomPositionInFrontiera(){
		return frontiera.get(Utility.randInt(0, frontiera.size()-1));
	}




}
