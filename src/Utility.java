import java.util.ArrayList;
import java.util.Random;

public class Utility
{

	public static int randInt(int min, int max) {


		Random rand = new Random();


		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}



	public static ArrayList<AreaInteresse> trovaAreeInteresse(Position[][] matrix, int width, int height){//TODO DANIEL tutto perche non abbiamo una vera classe mondo 
		ArrayList<AreaInteresse> listaAree=new ArrayList<AreaInteresse>();
		for(int y=0;y<height;y++){

			for(int x=0;x<width;x++){

				if(matrix[x][y].imRoot()) listaAree.add(new AreaInteresse(matrix[x][y]));

			}
		}

		for(int current=0;current<listaAree.size();current++){
			int j=current+1;
			while(j<listaAree.size()){
				AreaInteresse area1=listaAree.get(current);
				AreaInteresse area2=listaAree.get(j);
				if(area1.intersects(area2)){
					listaAree.remove(area1);
					listaAree.remove(area2);
					listaAree.add(current,new AreaInteresse(area1,area2));
					j=current+1;
				}
				else j++;
			}

		}
		
		for(AreaInteresse area: listaAree){
			for(Position pos:area.getPositionList()){
				if(area.contieneNellaFrontiera(pos)) {
					area.addPosAFrontiera(pos);
					pos.setInFrontiera();
				}
			}
		}	
		
		for(AreaInteresse area:listaAree){
			for(Position pos:area.getPositionList()){
				pos.setGrowTime(Position.GROWTIMEOASIS);
			}
		}
		
		return listaAree;
	}

	
	public  static ArrayList<Position> copiaLista(ArrayList<Position> listaDaCopiare){
		ArrayList<Position> listaInCuiCopiare=new ArrayList<Position>(listaDaCopiare.size());	
		for(int i=0;i<listaDaCopiare.size();i++){
			listaInCuiCopiare.add(new Position(listaDaCopiare.get(i)));
		}
		return listaInCuiCopiare;
		
	}

	
	
}