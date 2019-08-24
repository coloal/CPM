import javax.swing.*;    
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;

class GraphicPanelCPM extends JPanel{
  private static final long serialVersionUID = 1L;//REVISAR
  int dimension_;
  int steps_;
  CPMLatticeMio tumor_;
  boolean verdad;
  Object l;

  public GraphicPanelCPM(int newXMax, int newYMax, int newMcs, int newMcSubsteps, int newSigmaMax,double newInitialMatrixDensity,
            /*CPMcalculations*/
            double jEcm, double jLightCells,double jDarkCells, double jOtherCells, double lambdaArea,
            double targetAreaFactorLight, double targetAreaFactorDark, int darkCellDecrease, double temperature,
            int ratioDarkToLightCells )
  {
    super(new BorderLayout());
    tumor_ = new CPMLatticeMio(newXMax,newYMax,newMcs,newMcSubsteps,newSigmaMax,newInitialMatrixDensity,
                                          jEcm, jLightCells,jDarkCells, jOtherCells, lambdaArea,targetAreaFactorLight,
                                          targetAreaFactorDark, darkCellDecrease, temperature, ratioDarkToLightCells);
    tumor_.initializeLattice();
    verdad = true;
  }

  public void parar(){
        verdad=false;
        int[] areas = tumor_.getArea();
        System.out.println("------------------------------------------FIN--------------------------------------------");
        for(int i = 0 ; i<areas.length ; ++i)
          System.out.println("Area"+ i +"= "+areas[i]);

  }

  public void dibujar()
  {
    int[] areas = tumor_.getArea();
    System.out.println("------------------------------------------INICIO--------------------------------------------");
    for(int i = 0 ; i<areas.length ; ++i)
          System.out.println("Area"+i+"= "+areas[i]);
    verdad = true;
    Thread  updateThread = new Thread(){
      public void run(){
        while(verdad){
          
          repaint();
          /*try{
            Thread.sleep(10);
          }catch(InterruptedException e){}*/
          tumor_.computeCPM();
        }
      }
    };
    updateThread.start();
  }

  public void  paintComponent(Graphics g)
  {
    super.paintComponent(g);  
    
    for(int y=1; y<tumor_.getYMax();++y)
    {
      for(int x=1; x<tumor_.getXMax(); ++x)
      {
        switch(tumor_.sigma[x][y])
        {
          case 0: g.setColor(Color.white);
          break;
          case 1: g.setColor(Color.red);
          break;
          case 2: g.setColor(Color.blue);
          break;
          case 3: g.setColor(Color.green);
          break;
          case 4: g.setColor(Color.black);
          break;
          default:
          break; 
        }
        
          g.fillRect(x,y,1,1);
      }
    }
  }

}