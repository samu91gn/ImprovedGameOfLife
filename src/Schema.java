/**
 * @author piero+
 *
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Random;
import java.lang.Thread;

public class Schema extends JFrame{

	//private boolean attiva = true;
	private static MyPanel panel;
	private static PanelAgent pnlAgent;
	private JPanel opzioni = new JPanel();
	private JButton aumenta = new JButton("Aumenta Tempo");
	private JButton diminuisci = new JButton("Diminuisci Tempo");
	private JButton dialogo = new JButton("Visualizza");
	protected final static String choice="NORMA";
	
	
	public Schema(final Dimension d){

		super("Life Cycling");
		panel = new MyPanel(d);
		pnlAgent = new PanelAgent(d);
		int w = (int) d.width;
		opzioni.setLayout(null);
		opzioni.setBounds(w-200,0,200,d.height);
		add(opzioni);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		add(panel, null);
		//agent = new Agent(2,4);
		//riempita la matrice con le positions
		aumenta.setBounds(25,150,150,50); //TODO MODIFICA SAMU (CAMBIATE LE POSIZIONI DEI DUE PULSANTI)
		aumenta.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				World.TIME += 10;
			}
		});
		opzioni.add(aumenta);
		diminuisci.setBounds(25,210,150,50);
		diminuisci.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(World.TIME-10>0)
				World.TIME -= 10;
			}
		});
		opzioni.add(diminuisci);
		dialogo.setBounds(25,270,150,50);
		dialogo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				visualizzaDialog(d);
			}
		});
		opzioni.add(dialogo);
		setVisible(true);
		setSize(d);
	}

	public void paint(Graphics g){


		panel.paintComponent(g);
	}


	public void visualizzaDialog(Dimension d){
		JDialog dialog = new JDialog();
		dialog.setLayout(null);
		dialog.add(pnlAgent);
		dialog.setSize(200, 200);
		dialog.setVisible(true);
	}
	

	static int counter = 0;
	public static void main(String[] args) throws GrowException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.height = (int)screenSize.getHeight()-20;
		screenSize.width = (int)screenSize.getWidth()-100;
		World w = new World(choice);
		final Schema s = new Schema(screenSize);
		
		
		
		Thread t = new Thread(new Runnable(){
			@Override
			public void run()
			{
				while(true){
					counter++;
					World.turni++;/*update delle position che se non c'è l'agente e non sono max aumentano di una risorsa ecc*/
					
					for(int y = 0; y < World.MATRIX_HEIGHT; y++)
					{
						for(int x = 0; x < World.MATRIX_WIDTH; x++)
						{ 
								World.world[x][y].addResources(1);
						}
					}
					
					for(int i = 0; i < World.agentsArray.size(); i++){
						if(World.agentsArray.size() > i && World.agentsArray.get(i) != null){
							System.out.println(World.agentsArray.get(i).getMatrix());
							int fromX = World.agentsArray.get(i).getX();
							int fromY = World.agentsArray.get(i).getY();
						
								World.agentsArray.get(i).updatePosition();
						
							
						
							int toX = World.agentsArray.get(i).getX();
							int toY = World.agentsArray.get(i).getY();
							if(World.agentsArray.get(i).getX() == -1 || World.agentsArray.get(i).getY() == -1)
							{
								World.agentsArray.remove(i);
								World.POPULATION--;
							}
							
						}
					
						
						

						panel.repaint();
						pnlAgent.repaint();
						
					}

					try {
						Thread.sleep(World.TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		t.start();


	}



}