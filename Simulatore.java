import javax.swing.*;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;        
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.LayoutStyle.ComponentPlacement.*;
import javax.swing.*;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;        
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.LayoutStyle.ComponentPlacement.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.math.BigInteger;
import java.util.concurrent.*;
import java.lang.Math;


public class Simulatore implements ActionListener{
    static JFrame frame_inicio,frame_simulacion;
    JPanel pane; 
    GraphicPanelCPM graphicPane;

    JTextField Field_newXMax, Field_newYMax, Field_newMcs, Field_newMcSubsteps, Field_ratioDarkLightCells, Field_newInitialMatrixDensity,
              Field_jEcm,Field_jLightCells, Field_jDarkCells, Field_jOtherCells, Field_lambdaArea, Field_targetAreaFactorLight,
              Field_targetAreaFactorDark, Field_temperature, Field_newSigmaMax;
    
    static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    /*CPM parameters*/
    int newXMax, newYMax, newMcs, newMcSubsteps, newSigmaMax, ratioDarkLightCells, darkCellDecrease; 
    double newInitialMatrixDensity, jEcm,  jLightCells, jDarkCells,  jOtherCells,  lambdaArea,
             targetAreaFactorLight,  targetAreaFactorDark,  temperature;

    Simulatore()
    {
      newXMax = 400;
      newYMax = 400;
      newMcs  = 50;
      newMcSubsteps = 50;
      newSigmaMax  = 2;
      ratioDarkLightCells = 5;
      darkCellDecrease = 0;/*NO HAY BOTON PARA ESTO TODAVIAAAAAAA*/
      newInitialMatrixDensity = 0.9;
      jEcm = 20;
      jLightCells = 15;
      jDarkCells = 2;
      jOtherCells = 10;
      lambdaArea = 0.05;
      targetAreaFactorLight = 0.25;
      targetAreaFactorDark = 0.25;
      temperature = 20;
    }

    public void actionPerformed(ActionEvent e){
      
      if("simular".equals(e.getActionCommand()))
      {
        frame_simulacion = new JFrame("Simulacion");
        frame_simulacion.setPreferredSize(new Dimension(newXMax,newYMax));     
        graphicPane = new GraphicPanelCPM(newXMax,newYMax,newMcs,newMcSubsteps,newSigmaMax,newInitialMatrixDensity,
                                          jEcm, jLightCells,jDarkCells, jOtherCells, lambdaArea,targetAreaFactorLight,
                                          targetAreaFactorDark, darkCellDecrease, temperature, ratioDarkLightCells);

        frame_simulacion.setContentPane(graphicPane);
        frame_simulacion.pack();
        frame_simulacion.setVisible(true);
        
      }
      else if("iniciar".equals(e.getActionCommand()))
      {
        graphicPane.dibujar(); 
      }
      else if("fin".equals(e.getActionCommand()))
      {
        graphicPane.parar();

      }
      else if("newXMax".equals(e.getActionCommand()))
      {
        newXMax=Integer.parseInt(Field_newXMax.getText());
        System.out.println("newXMax"+newXMax);
      }
      else if("newYMax".equals(e.getActionCommand()))
      {
        newYMax=Integer.parseInt(Field_newYMax.getText());
        System.out.println("newYMax"+newYMax);
      }
      else if("newMcs".equals(e.getActionCommand()))
      {
        newMcs=Integer.parseInt(Field_newMcs.getText());
        System.out.println("newMcs"+newMcs);
      }
      else if("newMcSubsteps".equals(e.getActionCommand()))
      {
        newMcSubsteps=Integer.parseInt(Field_newMcSubsteps.getText());
        System.out.println("newMcSubsteps"+newMcSubsteps);
      }
      else if("newSigmaMax".equals(e.getActionCommand()))
      {
        newSigmaMax=Integer.parseInt(Field_newSigmaMax.getText());
        System.out.println("newSigmaMax"+ newSigmaMax);
      }
      else if("ratioDarkLightCells".equals(e.getActionCommand()))
      {
        ratioDarkLightCells=Integer.parseInt(Field_ratioDarkLightCells.getText());
        System.out.println("ratioDarkLightCells"+ ratioDarkLightCells);
      }
      else if("newInitialMatrixDensity".equals(e.getActionCommand()))
      {
        newInitialMatrixDensity=Double.parseDouble(Field_newInitialMatrixDensity.getText());
        System.out.println("newInitialMatrixDensity"+ newInitialMatrixDensity);
      }
      else if("jEcm".equals(e.getActionCommand()))
      {
        jEcm=Double.parseDouble(Field_jEcm.getText());
        System.out.println("jEcm"+ jEcm);
      }
      else if("jLightCells".equals(e.getActionCommand()))
      {
        jLightCells=Double.parseDouble(Field_jLightCells.getText());
        System.out.println("jLightCells"+ jLightCells);
      }
      else if("jDarkCells".equals(e.getActionCommand()))
      {
        jDarkCells=Double.parseDouble(Field_jDarkCells.getText());
        System.out.println("jDarkCells"+ jDarkCells);
      }
      else if("jOtherCells".equals(e.getActionCommand()))
      {
        jOtherCells=Double.parseDouble(Field_jOtherCells.getText());
        System.out.println("jOtherCells"+ jOtherCells);
      }
      else if("lambdaArea".equals(e.getActionCommand()))
      {
        lambdaArea=Double.parseDouble(Field_lambdaArea.getText());
        System.out.println("lambdaArea"+ lambdaArea);
      }
      else if("targetAreaFactorLight".equals(e.getActionCommand()))
      {
        targetAreaFactorLight=Double.parseDouble(Field_targetAreaFactorLight.getText());
        System.out.println("targetAreaFactorLight"+ targetAreaFactorLight);
      }
      else if("targetAreaFactorDark".equals(e.getActionCommand()))
      {
        targetAreaFactorDark=Double.parseDouble(Field_targetAreaFactorDark.getText());
        System.out.println("targetAreaFactorDark"+ targetAreaFactorDark);
      }
      else if("temperature".equals(e.getActionCommand()))
      {
        temperature=Double.parseDouble(Field_temperature.getText());
        System.out.println("temperature"+ temperature);
      }
    }
   

    private void createPaneInicio()
    {
      pane = new JPanel(new BorderLayout());

      JButton simularButton = new JButton("create");
      simularButton.setActionCommand("simular");
      JButton iniciarButton = new JButton("go");
      iniciarButton.setActionCommand("iniciar");
      JButton finButton = new JButton("stop");
      finButton.setActionCommand("fin");

      iniciarButton.addActionListener(this);
      simularButton.addActionListener(this);
      finButton.addActionListener(this);

      JLabel Label_newXMax = new JLabel("Dim X");
      JLabel Label_newYMax = new JLabel("Dim Y");
      JLabel Label_newMcs = new JLabel("MonteCarlo steps");
      JLabel Label_newMcSubsteps = new JLabel("MonteCarlo substeps");
      JLabel Label_newSigmaMax = new JLabel("Sigma max");
      JLabel Label_ratioDarkLightCells = new JLabel("ratioDarkLightCells");
      JLabel Label_newInitialMatrixDensity = new JLabel("initial matrix density");      
      JLabel Label_jEcm = new JLabel("ecm energy");
      JLabel Label_jLightCells = new JLabel("light cells energy");
      JLabel Label_jDarkCells = new JLabel("dark cells energy");
      JLabel Label_jOtherCells = new JLabel("other cells energy");
      JLabel Label_lambdaArea = new JLabel("lambda area");
      JLabel Label_targetAreaFactorLight = new JLabel("light cell's target area factor");
      JLabel Label_targetAreaFactorDark = new JLabel("dark cell's target area factor");
      JLabel Label_temperature = new JLabel("Temperature");
      
      

     Field_newXMax = new JTextField("400");
     Field_newXMax.addActionListener(this);
     Field_newXMax.setActionCommand("newXMax");

     Field_newYMax = new JTextField("400");
     Field_newYMax.addActionListener(this);
     Field_newYMax.setActionCommand("newYMax");

     Field_newMcs = new JTextField("50");
     Field_newMcs.addActionListener(this);
     Field_newMcs.setActionCommand("newMcs");
     
     Field_newMcSubsteps = new JTextField("50");
     Field_newMcSubsteps.addActionListener(this);
     Field_newMcSubsteps.setActionCommand("newMcSubsteps");

     Field_newSigmaMax = new JTextField("2");
     Field_newSigmaMax.addActionListener(this);
     Field_newSigmaMax.setActionCommand("newSigmaMax");


     Field_ratioDarkLightCells = new JTextField("5");
     Field_ratioDarkLightCells.addActionListener(this);
     Field_ratioDarkLightCells.setActionCommand("ratioDarkLightCells");

     Field_newInitialMatrixDensity = new JTextField("0.9");
     Field_newInitialMatrixDensity.addActionListener(this);
     Field_newInitialMatrixDensity.setActionCommand("newInitialMatrixDensity");

     Field_jEcm = new JTextField("20");
     Field_jEcm.addActionListener(this);
     Field_jEcm.setActionCommand("jEcm");

     Field_jLightCells = new JTextField("15");
     Field_jLightCells.addActionListener(this);
     Field_jLightCells.setActionCommand("jLightCells");

     Field_jDarkCells = new JTextField("2");
     Field_jDarkCells.addActionListener(this);
     Field_jDarkCells.setActionCommand("jDarkCells");

     Field_jOtherCells = new JTextField("10");
     Field_jOtherCells.addActionListener(this);
     Field_jOtherCells.setActionCommand("jOtherCells");

     Field_lambdaArea = new JTextField("0.05");
     Field_lambdaArea.addActionListener(this);
     Field_lambdaArea.setActionCommand("lambdaArea");

     Field_targetAreaFactorLight = new JTextField("0.25");
     Field_targetAreaFactorLight.addActionListener(this);
     Field_targetAreaFactorLight.setActionCommand("targetAreaFactorLight");

     Field_targetAreaFactorDark = new JTextField("0.25");
     Field_targetAreaFactorDark.addActionListener(this);
     Field_targetAreaFactorDark.setActionCommand("targetAreaFactordark");

     Field_temperature = new JTextField("20");
     Field_temperature.addActionListener(this);
     Field_temperature.setActionCommand("temperature");



      
      GroupLayout layout = new GroupLayout(pane);
      layout.setAutoCreateGaps(true);
      layout.setAutoCreateContainerGaps(true);
      pane.setLayout(layout);


      layout.setHorizontalGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(LEADING)
          
          .addComponent(Label_newXMax)
          .addComponent(Label_newYMax)
          .addComponent(Label_newMcs)
          .addComponent(Label_newMcSubsteps)
          .addComponent(Label_newSigmaMax)
          .addComponent(Label_ratioDarkLightCells)
          .addComponent(Label_newInitialMatrixDensity)
          .addComponent(Label_jEcm)
          .addComponent(Label_jLightCells)
          .addComponent(Label_jDarkCells)
          .addComponent(Label_jOtherCells)
          .addComponent(Label_lambdaArea)
          .addComponent(Label_targetAreaFactorLight)
          .addComponent(Label_targetAreaFactorDark)
          .addComponent(Label_temperature)
        )
        .addGroup(layout.createParallelGroup(LEADING)
          .addComponent(Field_newXMax)
          .addComponent(Field_newYMax)
          .addComponent(Field_newMcs)
          .addComponent(Field_newMcSubsteps)
          .addComponent(Field_newSigmaMax)       
          .addComponent(Field_ratioDarkLightCells)
          .addComponent(Field_newInitialMatrixDensity)
          .addComponent(Field_jEcm)
          .addComponent(Field_jLightCells)
          .addComponent(Field_jDarkCells)
          .addComponent(Field_jOtherCells)
          .addComponent(Field_lambdaArea)
          .addComponent(Field_targetAreaFactorLight)
          .addComponent(Field_targetAreaFactorDark)
          .addComponent(Field_temperature)

          .addComponent(simularButton)
          .addComponent(iniciarButton)
          .addComponent(finButton)
        )

      );      

      layout.setVerticalGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_newXMax)
          .addComponent(Field_newXMax)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_newYMax)
          .addComponent(Field_newYMax)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_newMcs)
          .addComponent(Field_newMcs)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_newMcSubsteps)
          .addComponent(Field_newMcSubsteps)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_newSigmaMax)
          .addComponent(Field_newSigmaMax)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_ratioDarkLightCells)
          .addComponent(Field_ratioDarkLightCells)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_newInitialMatrixDensity)
          .addComponent(Field_newInitialMatrixDensity)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_jEcm)
          .addComponent(Field_jEcm)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_jLightCells)
          .addComponent(Field_jLightCells)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_jDarkCells)
          .addComponent(Field_jDarkCells)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_jOtherCells)
          .addComponent(Field_jOtherCells)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_lambdaArea)
          .addComponent(Field_lambdaArea)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_targetAreaFactorLight)
          .addComponent(Field_targetAreaFactorLight)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_targetAreaFactorDark)
          .addComponent(Field_targetAreaFactorDark)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(Label_temperature)
          .addComponent(Field_temperature)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(simularButton)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(iniciarButton)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(finButton)
        )
      );

      Border borde = BorderFactory.createLineBorder(new Color(51,134,202));
      TitledBorder borde_titulo = BorderFactory.createTitledBorder(borde,"Juego de la vida");
    
      pane.setBorder(borde_titulo);
      pane.setBackground(new Color(176,201,247));
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        Simulatore sim = new Simulatore();
        /*FRAME OPTIONS*/
        frame_inicio = new JFrame("SimulatoreInicio");
        frame_inicio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_inicio.setPreferredSize(new Dimension(400,600));

        
        int x = (int)(dim.getWidth()/2);
        int y = (int)(dim.getHeight()/3);
        
        frame_inicio.setLocation(x,y);
        sim.createPaneInicio();
        frame_inicio.setContentPane(sim.pane);

        frame_inicio.pack();
        frame_inicio.setVisible(true);  
    }
 
    public static void main(String[] args) {
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
