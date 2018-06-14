/**
 * 

 * @author samu
 * @version 1.00 2014/11/14
 * 
 */
import java.util.ArrayList;


public class Segment {

	private ArrayList<Position> list;
	private int number;
	int [] firstCellCoords;
	int[] origin;
	int numberQuad;

	public Segment( int n, int[]or, int nq)
	{
		list = new ArrayList<Position>();
		number = n;
		origin = or;
		numberQuad = nq;

	}

	public int[] getFirstCellCoord(Position p)
	{
		int[] ret = null;
		//restituisco la posizione in cui si trovava prima se ci sono ancora risorse
		if(p.getResources()!=0&&false)
		{
			final int ret00X = p.getX();
			final int ret00Y = p.getY();
			int[] ret00 = {ret00X, ret00Y};
			return ret00;
		}

		int[] ret11 = {origin[0], origin[1]-1};
		int[] ret12 = {origin[0]+1, origin[1]-1};
		int[] ret13 = {origin[0]+1, origin[1]};
		int[] ret21 = {origin[0], origin[1]-1};
		int[] ret22 = {origin[0]-1, origin[1]-1};
		int[] ret23 = {origin[0]-1, origin[1]};
		int[] ret31 = {origin[0], origin[1]+1};
		int[] ret32 = {origin[0]-1, origin[1]+1};
		int[] ret33 = {origin[0]-1, origin[1]};
		int[] ret41 = {origin[0], origin[1]+1};
		int[] ret42 = {origin[0]+1, origin[1]+1};
		int[] ret43 = {origin[0]+1, origin[1]};

		if(numberQuad == 1 && number == 1)
			ret = ret11;
		else if(numberQuad == 1 && number == 2)
			ret = ret12;
		else if(numberQuad == 1 && number == 3)
			ret = ret13;
		else if(numberQuad == 2 && number == 1)
			ret = ret21;
		else if(numberQuad == 2 && number == 2)
			ret = ret22;
		else if(numberQuad == 2 && number == 3)
			ret = ret23;
		else if(numberQuad == 3 && number == 1)
			ret = ret31;
		else if(numberQuad == 3 && number == 2)
			ret = ret32;
		else if(numberQuad == 3 && number == 3)
			ret = ret33;
		else if(numberQuad == 4 && number == 1)
			ret = ret41;
		else if(numberQuad == 4 && number == 2)
			ret = ret42;
		else if(numberQuad == 4 && number == 3)
			ret = ret43;
		return ret;
	}
	public int getNumber()
	{
		return this.number;
	}
	public int getQuadrantNumber()
	{
		return this.numberQuad;
	}
	public void add(Position p)
	{
		list.add(p);
	}
	public ArrayList<Position> getList()
	{
		return this.list;
	}
	public Position get(int i)
	{
		return this.list.get(i);
	}
	public Position remove(int i)
	{
		return this.list.remove(i);
	}
}
