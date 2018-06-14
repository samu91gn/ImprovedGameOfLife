import java.util.ArrayList;
class GrowException extends Exception{
	public GrowException(String message){
		super(message);
		
	}
	
}

public class PseudoWorld {
	PseudoPosition[][] pseudoWorld=new PseudoPosition[World.MATRIX_WIDTH][World.MATRIX_HEIGHT];
	Position root;

	public PseudoWorld(Position root) throws GrowException{
		this.root=root;
		root.setRoot();;
	//	int growTime=Position.getGrowTime(root.getType());
		double maxResources=Position.getMaxResPos(root.getType());
		int rootX=root.getX();
		int rootY=root.getY();

		for(int y=0;  y<World.MATRIX_HEIGHT;y++){
			for(int x=0;x<World.MATRIX_WIDTH;x++){

				double d=Math.sqrt((x-rootX)*(x-rootX)+(y-rootY)*(y-rootY));
				if(d<1) d=1;
				
				double dGrow=Math.pow(d,.01);
				double dRes=Math.pow(d, 1.5);
				int newMaxResources=0;
				/*	if(maxResources-dRes>0)
					newMaxResources=(int) Math.round(maxResources-dRes);*/
				
				if(Math.round(maxResources-dRes)<2) newMaxResources=1;
				else newMaxResources=(int) Math.round(maxResources-dRes);
				
				//int newGrowTime=growTime;//(int) Math.round(growTime+dGrow);
				//if(newGrowTime>Position.MAXGROWTIME)newGrowTime=Position.MAXGROWTIME;
				
				//if(newMaxResources!=0) newGrowTime=(int) Math.round((50/newMaxResources));

				pseudoWorld[x][y]=new PseudoPosition(newMaxResources, Position.SEAGROWTIME,dGrow,dRes, x,y);
			}

		}
	}

	public Position getRoot() {return root;}
	private  PseudoPosition getPosition(int x, int y){return pseudoWorld[x][y];}

	
	
	
	
	public static Position[][] createWorld(ArrayList<PseudoWorld> allWorlds){//TODO DANIEL GRADIENTI

		Position[][] world=new Position[World.MATRIX_WIDTH][World.MATRIX_HEIGHT];

		for(int y=0;  y<World.MATRIX_HEIGHT;y++){
			for(int x=0;x<World.MATRIX_WIDTH;x++){
		
				double  maxRes=0;
				//int  minGrow= allWorlds.get(0).getPosition(x, y).getGrowTime();

				for(PseudoWorld pWorld: allWorlds){
					PseudoPosition pos=pWorld.getPosition(x, y);
					if(pos.getMaxResources()>maxRes) maxRes=pos.getResources();
					//if(pos.getGrowTime()<minGrow)minGrow=pos.getGrowTime(); 
			
				}
		
				world[x][y]=new Position( maxRes,Position.SEAGROWTIME, x,  y);
				
				for(PseudoWorld pWorld: allWorlds){
					Position pos=pWorld.getRoot();
					world[pos.getX()][pos.getY()]=pos;
				}

			}
		}

		return world;
	}


	class PseudoPosition extends Position{
		int x;
		int y;
		int	growTime;
		int maxResources;
		double dGrow;
		double dRes;


		public PseudoPosition(int maxResources,int growTime, double dGrow, double dRes, int x ,int y){

			super(maxResources, growTime, x, y);
			this.dGrow=dGrow;
			this.dRes=dRes;
		}
		public double getDGrow(){return dGrow;}
		public double getDRes(){return dRes;}

	}
}



