
//------------------------------------------------------------------
// Author: Pranav Srinivas, MVHS,   Date: 5/3/2013
//------------------------------------------------------------------

// package signalstorm;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*; 
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


//------------------------------------------------------------------
// Main Panel where game is played. Also functions as Keylistener
// for user key events and ActionListener for timer events
//
// The key information is sored in a 30 x 30 2D array of
// ProteinElements. It also stores an Image of DNA in nucleus
//-------------------------------------------------------------------

class CellPanel extends JPanel implements MouseListener, KeyListener, ActionListener 
{
    private ScorePanel score;  // reference to the Score Panel
    private int width;         // width of the cell panel
    private int height;        // height of the cell panel
    
    private ProteinElement [][] network; // 2D array of Protein Elements
    private final int SIZE = 30;         
    
    private boolean oncoOpen;        // Oncogenic signal jaw open or close 
    private boolean suppressorOpen;  // Suppresor signal jaw open or close
    
    private Timer oncoMoveTimer;     // Timer to move oncogenic signals
    private Timer oncoFlashTimer;    // Timer to blink oncogenic signals
    private Timer suppressorTimer;   // Timer to blink suppressors
    
    private Random rand;             // for random number generation
    
    private boolean gameOn;          // Is game being played or not
    private boolean gamePaused;      // Is game temporarily paused
    
    private Pathway []  pathways;      // Array of all pathways (size 9)  
    private Suppressor [] suppressors; // Array of suppressors (size 4)
    
    private boolean []  pathwaysPreference;    // User indicated pathway preference
    private boolean []  alterationsPreference; // User indicated alteration preference
    
    private static final int NUM_PATHWAYS    = 9; // number of signaling pathways
    private static final int NUM_ALTERATIONS = 4; // number of alterations
    
    private static final int NUM_SUPPRESSORS = 4; // number of suppressors
            
    Image dnaFigure;  // DNA Image in the cell nucleus
    
    private final String dnaImageName = "dna.jpeg"; // DNA imgae file name
            
    //-----------------------------------------------------------
    // CellPanel constructor. A reference to ScorePanel is
    // kept in CellPanel for easy access and score manipuation
    //-----------------------------------------------------------
    
    public CellPanel(ScorePanel scor)
    {
        super();     
        score = scor;
        score.setCellPanel(this);  // ScorePanel also needs to know about CellPanel
        
        try {
            dnaFigure = ImageIO.read(new File(dnaImageName));
        } catch (IOException ex) {
            System.out.println("Warning: File not found - " + dnaImageName);
        }
        
        rand = new Random();
        
        pathwaysPreference    = new boolean [NUM_PATHWAYS];
        alterationsPreference = new boolean [NUM_ALTERATIONS];
        
        // arrays of 9 pathways
        pathways = new Pathway [NUM_PATHWAYS];
            
        // Default pathway preference is GPCR
        setPathwaysPreference(4, true);
      
        // Default alteration preference is Mutation
        setAlterationsPreference(0, true);
          
        // Protein Element Network (create and initialize)
        network = new ProteinElement[SIZE][SIZE];
        for (int row = 0; row < network.length; row++)
        {
            for (int col = 0; col < network[row].length; col++)
            {
                network[row][col] = new ProteinElement(row, col);
            }
        }
        
        // Suppressors (create and initialize)
        suppressors = new Suppressor [NUM_SUPPRESSORS];
        for (int i = 0; i < NUM_SUPPRESSORS; i++) 
        {
            suppressors[i] = new Suppressor(0, 0, network, score);
        }
        
        addMouseListener(this);  // register self as mouse listener
        addKeyListener(this);    // register self as key listener
        
        setBackground(Color.BLACK);
        
        oncoMoveTimer = new Timer(4000, this);   // Timer to move onco signals
        oncoMoveTimer.start();      
        
        oncoFlashTimer = new Timer(200, this);   // Timer to flash onco signals
        oncoFlashTimer.start();
        
        suppressorTimer = new Timer(300, this);  // Timer to flash suppressors
        suppressorTimer.start();       
        
        //------------------------------------------------------------
        // Create all 9 signaling pathways that the game comes with.
        // Create proteins and their connections for each pathways
        //------------------------------------------------------------
        createAllSignalingPathways();
        
        initializeGame();      // initialize the game
    }
    
    
    //-----------------------------------------------------------------
    // Create all signaling pathways for the game. For easy access
    // references to 'score' and 'network' are passed to each pathway
    //-----------------------------------------------------------------
    
    public void createAllSignalingPathways()
    {        
        for (int pathwayIndex = 0; pathwayIndex < NUM_PATHWAYS; pathwayIndex++) 
        {
            Pathway p = null;
            
            switch(pathwayIndex)
            {
                case 0:
                    p = new Pathway(pathwayIndex, "HedgeHog", "HedgeHog", network, score);
                    break;
                case 1:
                    p = new Pathway(pathwayIndex, "Notch", "Notch", network, score);
                    break;    
                case 2:
                    p = new Pathway(pathwayIndex, "Wnt", "Wnt", network, score);
                    break;       
                case 3:
                    p = new Pathway(pathwayIndex, "Jak/STAT", "Jak/STAT", network, score);
                    break;      
                case 4:
                    p = new Pathway(pathwayIndex, "GPCR", "GPCR", network, score);
                    break;        
                case 5:
                    p = new Pathway(pathwayIndex, "Ras", "Ras", network, score);
                    break;   
                case 6:
                    p = new Pathway(pathwayIndex, "PI3K/Akt", "PI3K/Akt", network, score);
                    break;  
                case 7:
                    p = new Pathway(pathwayIndex, "NF-kB", "NF-kB", network, score);
                    break;  
                case 8:
                    p = new Pathway(pathwayIndex, "TGF-B", "TGF-B", network, score);
                    break;  
                default:
                    break;
            }
            
            pathways[pathwayIndex] = p;
        }
    } 
    
    
    //------------------------------------------------------
    // initialize the Game
    //------------------------------------------------------
    
    public void initializeGame()
    {
        gameOn = gamePaused = false;
        
        initializeProteinGrid();  // clear all signals
        
        initializeSuppressors();  // set suppressor location
        
        initializePathways();     // deactivate all pathways
        
        repaint();
    }   
    
    
    //------------------------------------------------------
    // Initialize protein grid by clearing out all
    // oncogenic and suppressor signal
    //------------------------------------------------------
    
    public void initializeProteinGrid()
    {
        for (int row = 0; row < network.length; row++)
        {
            for (int col = 0; col < network[row].length; col++)
            {
                network[row][col].setOncogenicSignal(false);  // clear onco signal
                network[row][col].setSuppressorSignal(false); // clear suppressor
                network[row][col].setActive(false);           // inactive
            }
        }
    }
    
    
    //------------------------------------------------------------
    // Initialize 4 suppressor locations on 4 corners of nucleus
    //------------------------------------------------------------
    
    public void initializeSuppressors()
    {
        for (int i = 0; i < NUM_SUPPRESSORS; i++) 
        {
            int row = 0, col = 0;
            switch(i)
            {
                case 0:  row = 12; col = 6;  break;
                case 1:  row = 12; col = 23; break;
                case 2:  row = 18; col = 6;  break;
                case 3:  row = 18; col = 23; break;
                default: row = col = SIZE/2; break;
            }
            suppressors[i].set(row, col);
        }  
    }
     
    
    //------------------------------------------------------------
    // Initialize all pathways by deactivating all cancer signals
    //------------------------------------------------------------
    public void initializePathways()
    {
        for (int i = 0; i < NUM_PATHWAYS; i++) 
        {
            pathways[i].deactivate();
        }
    }
    
    //---------------------------------------------------------------------.
    // Simulate pathways based on Level, Selected Signaling and Alterations
    //----------------------------------------------------------------------
    
    public void simulatePathways()
    {
        // return if game is not being played
        if (!gameOn) return;
        
        // current game level
        int level = score.getCurrentLevel();
           
        // Create 'level' number of simulated pathways
        int [] simulatedPathways = new int [level]; 
        
        // Decide pathways to simulate based on level number and user preference
        decideSimulatedPathways(simulatedPathways);
        
        // Activate the selected pathways
        activatePathways(simulatedPathways);
    }
    
    
    //-------------------------------------------------------------
    // Decide which pathways to simulate for the game based on 
    // current game level and user indicated preferences
    // 
    // The decision is communicated through passed array of ints
    //-------------------------------------------------------------
    
    public void decideSimulatedPathways(int [] simulatedPathways)
    {
        int level = simulatedPathways.length;
        int levelIndex = 0;      

        // Select 'level' number of pathways based on user indicated pathways preference
        for (int pathwayIndex = 0; pathwayIndex < NUM_PATHWAYS; pathwayIndex++) 
        {
            // only look at user preference first
            if (pathwaysPreference[pathwayIndex] == true)
            {   
                simulatedPathways[levelIndex] = pathwayIndex;  // record pathway index
                levelIndex++;
                if (levelIndex == level) break;
            }    
        }
        
        // User pathways preference is less than current game level
        // Randomly fill remaining pathways
        while (levelIndex < level)
        {
            int i = rand.nextInt(NUM_PATHWAYS);
            
            // Check if pathway index i is already selected
            boolean alreadySelected = false;
            for (int k = 0; k < levelIndex; k++)
            {
                if (i == simulatedPathways[k]) 
                {
                    alreadySelected = true;
                    break;
                }
            }
 
            if (alreadySelected == false)
            {
                simulatedPathways[levelIndex] = i;
                levelIndex++;
            }   
        }
    }
    
    
    //------------------------------------------------------
    // Activate oncogenic signals in the selected pathways.
    // The selected pathway indices are passed.
    //------------------------------------------------------
    
    public void activatePathways(int [] simulatedPathways)
    {
        for (int i = 0; i < simulatedPathways.length; i++) 
        {
            int pathwayIndex = simulatedPathways[i];  // index of simulated pathway
            Pathway p = pathways[pathwayIndex];
            p.activate();
            
            // Inform Score Panel about activated pathway mutated 
            // genes/proteins and Cancers
            score.recordPathwayProteinsAndCancers(p);
        }
    }
    
    
    //-----------------------------------------------------------
    // Move oncogenic signals along the the activated pathways 
    //-----------------------------------------------------------
    
    public void moveOncogenicSignals()
    {
        for (int pathwayIndex = 0; pathwayIndex < NUM_PATHWAYS; pathwayIndex++) 
        {
            // For active pathways move onco signals one step forward
            if (pathways[pathwayIndex].isActive())
                pathways[pathwayIndex].moveOncoOneStep();
        }  
    
        // repaint after moving signals
        repaint();
    }
    
    
    // Set/Clear preference for pathway indexed by pIndex
    public void setPathwaysPreference(int pIndex, boolean selected)
    {
        pathwaysPreference[pIndex] = selected;
    }
    
    // Set/Clear preference for alteration index by aIndex
    public void setAlterationsPreference(int aIndex, boolean selected)
    {
        alterationsPreference[aIndex] = selected;
    }
    
    // Get the focus to move suppressor
    public void mousePressed(MouseEvent e)
    { 
        requestFocus();
    }
    
    public void mouseReleased(MouseEvent e) { }
    public void mouseClicked(MouseEvent e)  { }
    public void mouseEntered(MouseEvent e)  { }    
    public void mouseExited(MouseEvent e)   { }
    
    
    //----------------------------------------------------------
    // Handle Key events in the Cell Panel (move suppressors)
    //----------------------------------------------------------
    
    public void keyPressed (KeyEvent e) 
    {   
        int code = e.getKeyCode();
        
        // If game is not being played return
        if (!gameOn) return;
        
        switch (code)
        {
            // Top Left Suppressor Agent (0)
            case KeyEvent.VK_3: suppressors[0].moveUp();    break;  // Up
            case KeyEvent.VK_2: suppressors[0].moveDown();  break;  // Down
            case KeyEvent.VK_1: suppressors[0].moveLeft();  break;  // Left
            case KeyEvent.VK_4: suppressors[0].moveRight(); break;  // Right
                    
            // Top Right Suppressor Agent (1)   
            case KeyEvent.VK_9:  suppressors[1].moveUp();    break;  // Up
            case KeyEvent.VK_8:  suppressors[1].moveDown();  break;  // Down
            case KeyEvent.VK_7: suppressors[1].moveLeft();   break;  // Left
            case KeyEvent.VK_0: suppressors[1].moveRight();  break;  // Right
               
  
            // Bottom Left Suppressor Agent (2)    
            case KeyEvent.VK_W: suppressors[2].moveUp();    break;  // Up
            case KeyEvent.VK_S: suppressors[2].moveDown();  break;  // Down
            case KeyEvent.VK_A: suppressors[2].moveLeft();  break;  // Left
            case KeyEvent.VK_D: suppressors[2].moveRight(); break;  // Right
                                          
            // Bottom Right Suppressor Agent (3)   
            case KeyEvent.VK_UP:    suppressors[3].moveUp();    break;  // Up
            case KeyEvent.VK_DOWN:  suppressors[3].moveDown();  break;  // Down
            case KeyEvent.VK_LEFT:  suppressors[3].moveLeft();  break;  // Left
            case KeyEvent.VK_RIGHT: suppressors[3].moveRight(); break;  // Right
                          
            default: return; // if some other key is pressed then don't do anything
        }
        
        repaint();
    }
    
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e)    { }
     
    //--------------------------------------------------------------------
    // Action Listener for timer events, button events
    //--------------------------------------------------------------------
    
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();  // get command string with event source
        Object src = (Object) e.getSource();    // get event source
        
        if (src == oncoFlashTimer)
        {
            oncoOpen = !oncoOpen;               // Oncogenic Signal animation
        }
        else if (src == suppressorTimer)
        {
            suppressorOpen = !suppressorOpen;   // Suppressor animation
        }
        else if (src == oncoMoveTimer)
        {
            if (gameOn) moveOncogenicSignals(); // Move cancer signals along pathways
        }
        
        // 'command' will be null for timer events, so repaint and return
        if (command == null) 
        {
            repaint();
            return;
        }
        
        // Handle button events
        if (command.equals("Play"))
        {
            // If game is paused then 'Play' is like 'Resume'
            if (gamePaused)
            {
                gameOn = true;
                gamePaused = false;
            }
            else 
            {
                gameOn = true;
                simulatePathways(); // start the game
            }
        }
        else if (command.equals("Pause"))
        {
            if (gameOn)
            {
                gameOn = false;
                gamePaused = true;
            }
        }
        else if (command.equals("Resume"))
        {
            if (gamePaused)
            {
                gameOn = true;
                gamePaused = false;
            }
        } 
        
        repaint();
    }
    
    
    // Draw grind the the cell panel
    public void drawGrid(Graphics g)
    {
        // step size in pixels
        int xStep = width / SIZE;
        int yStep = height / SIZE;
         
        // draw vertical lines
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < width; i += xStep)
        {
            g.drawLine(i, 0, i, height);
        }
        
        // draw horizontal lines
        for (int i = 0; i < height; i += yStep)
        {   
            g.drawLine(0, i, width, i);
        }  
    }
    
    
    // Draw cell and nucleus outline. Draw DNA image inside nucleus
    public void drawCellAndNucleus(Graphics g)
    {
        // nucleus outline
        g.setColor(Color.RED);
        g.drawOval(width/2 - 80, height/2 - 60, 160, 120);
        
        // DNA within nucleus
        g.drawImage(dnaFigure, width/2 - 50, height/2 - 40, 100, 80, this);
        
        // cell outline
        g.setColor(Color.CYAN);
        g.drawOval(30, 20, width - 60, height - 60);   
        
        // drawGrid(g);
    }
    
    // Show cell graphics guts
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        // Get width and height of panel
        width  = getWidth();
        height = getHeight();
        
        // Draw grid, cell and nucleus
        drawCellAndNucleus(g);
        
        int xStep = width / SIZE;
        int yStep = height / SIZE;
        
        // Draw cell intra-cellular signaling network
        for (int row = 0; row < network.length; row++)
        {
            for (int col = 0; col < network[row].length; col++)
            {
                int x = xStep * col;  // top left x-coordinate of (row, col) box
                int y = yStep * row;  // top left y-coordinate of (row, col) box
                
                // Draw occupants in (row, col) location
                network[row][col].Draw(x, y, g, xStep, yStep, oncoOpen, suppressorOpen); 
            }
        }         
    }
}
