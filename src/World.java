/**
 * @author piero
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class World{
	protected static final int UNIVERSALTIME = 250; 
	protected static final int OASIS = 4;
	protected static final int WOOD = 3;
	protected static final int DESERT = 2;
	protected static final int WATER = 1;
	protected static final int ALASKA = 0;
	protected static final int UNKNOWN = 5;
	protected static int POPULATION =70;//250;//deve sempre mantenere il numero esatto quindi va aumentata quando c'è una nascita, diminuita quando c'è una morte (tanto è statica) (-samu)
	protected static final int MATRIX_HEIGHT =250;
	protected static final int MATRIX_WIDTH=400;
	protected static final int MAXAGENTS = 10000;
	protected static long turni=0;
	protected static long TIME = 11;
	protected static double maxAreaSize=0;
	

	protected static Position[][] world = new Position[MATRIX_WIDTH][MATRIX_HEIGHT];//campo di gioco
	protected static ArrayList<Agent> agentsArray = new ArrayList<Agent>();
	protected static ArrayList<AreaInteresse> listaAree=new ArrayList<AreaInteresse>();
	protected static ArrayList<PseudoWorld> allWorlds=new ArrayList<PseudoWorld>();

	/**
	 * carico lo schema e controllo il gioco
	 * @throws GrowException 
	 */
	public World(String choice) throws GrowException{
		Scanner scan = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader(); //TODO MODIFICA SAMU PRENDO DA RISORSE E NON DA PATH COMPUTER
			URL url=null;
			if(choice.equals("NORMAL")) url= classLoader.getResource("Files/333.txt");
			else url= classLoader.getResource("Files/world12.txt");
			File file = new File(url.getFile());
			scan = new Scanner(new FileReader(file));
		} catch (Exception e) {

			e.printStackTrace();
		}

		if(choice.equals("NORMAL")){
			for(int y=0;y<MATRIX_HEIGHT;y++){
				String s = scan.nextLine();
				for(int x=0;x<MATRIX_WIDTH;x++){

					int p = Integer.parseInt(s.substring(x, x+1));
					world[x][y] = new Position(p,x,y);
				}
			}
		}
		else {		//TODO CREARE MONDO CON GRADIENTE

			for(int y=0;y<MATRIX_HEIGHT;y++){
				String s = scan.nextLine();
				for(int x=0;x<MATRIX_WIDTH;x++){

					int p = Integer.parseInt(s.substring(x, x+1));
					if(p!=0){
						allWorlds.add(new PseudoWorld(new Position(p,x,y)));
					}

				}
			}


			world=PseudoWorld.createWorld(allWorlds);
			listaAree= Utility.trovaAreeInteresse(world,MATRIX_WIDTH,MATRIX_HEIGHT);
			
			for(AreaInteresse area:listaAree){
				double size=area.size();
				if(maxAreaSize<size) maxAreaSize=size;
			}
		}
		//creo le risorse per tenere in vita le cellule
		//qui è dove metteremo gli ambienti
		/*
		for(int x=0;x<MATRIXDIMENSION;x++){
			for(int y=0;y<MATRIXDIMENSION;y++){
				int p = Utility.randInt(0,4);
				world[x][y] = new Position(p,x,y);
			}
		}
		 */
		//creo gli agenti

		for(int index=0;index<POPULATION;index++){
			int X = Utility.randInt(0,50);
			int Y = Utility.randInt(0,50);
			Agent p;
			if(!World.world[X][Y].isOccupied())
			{
				p = new Agent(X, Y,listaAree, 1,1,2,1,1,20); // socialGene, agentDensityGene, resourceGene,  growGene, maxResGene, distanceGene, optimismGene
				agentsArray.add(p);
			}
			else
				index--;
		}


		for(int i = 0; i < POPULATION; i++){
			//System.out.println("Agent "+agentsArray[i].getNome()+" creato in "+agentsArray[i].getX()+","+agentsArray[i].getY());
			//System.out.println("type = " + agentsArray[i].knownField[agentsArray[i].getX()][agentsArray[i].getY()].getType());

			//prendo le risorse nel range della agentsArray
			System.out.println("agente creato in " + agentsArray.get(i).getX() + " ," + agentsArray.get(i).getY());
			System.out.println(agentsArray.get(i).getMatrix());
			//System.out.println("posizione attuale: " + agentsArray[i].getX() +" , " + agentsArray[i].getY());
			//agentsArray[i].updatePosition();
			//System.out.println("posizione attuale: " + agentsArray[i].getX() +" , " + agentsArray[i].getY());

		}


		//System.out.println(""+ counter + " aggiornamenti di posizione calcolati in "+ (System.currentTimeMillis() - tmpTime) + " ms");

		/******* FINE TEST *******/


	}
	



}