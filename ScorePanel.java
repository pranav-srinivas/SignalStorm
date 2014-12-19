//------------------------------------------------------------------
// Author: Pranav Srinivas, MVHS,   Date: 5/16/2013
//------------------------------------------------------------------

// package signalstorm;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*; 
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//---------------------------------------------------------------------------------
// ScorePanel class keeps track of all scores (wins/losses/level-score/grandScore)
// and displays them in designated area on the screen.
//---------------------------------------------------------------------------------

class ScorePanel extends JPanel implements ChangeListener
{
    private JTextArea   scoreArea;              // Score text area
    private JTextArea   mutatedGenesArea;       // Mutated Genes text area
    private JTextArea   susceptibleCancerArea;  // Susceptible Cancer text area
    private JSlider     levelSlider;            // Slider for game level selection
    private JScrollPane genesScroll;            // ScrollPane for long genes text
    private JScrollPane cancerScroll;           // ScrollPane for long cancer text
    private Font        currentFont;            // Font for drawing text
    
    private CellPanel   cell;          // reference to cell panel for easy method access
    
    private int         currentLevel;  // Current game level
    private int []      wins;          // Number of wins (kills) at each level
    private int []      losses;        // Number of losses (nucleus damage) at each level
    private int []      theScore;      // Score at each level
    private int         grandScore;    // Overall Grand Score
    
    private String[]    mutatedGenes;      // mutated genes string array
    private int         mutatedGeneCount;  // mutated genes count
    
    private String[]    susceptibleCancers;     // susceptible cancer string array
    private int         susceptibleCancerCount; // susceptible cancer count
    
    public static final int MAX_LEVEL            = 6;
    public static final int MAX_MUTATED_GENES    = 200;
    public static final int MAX_POSSIBLE_CANCERS = 50;
    
    private final int WIN_POINT  = 2;
    private final int LOSS_POINT = 1;
    
    public void setCellPanel(CellPanel cp) { cell = cp; }
    
    //----------------------------------------------------------------
    // Reset current level score and subtract it from grand score
    //----------------------------------------------------------------
    public void resetCurrentScore()
    {
        grandScore -= theScore[currentLevel];  
        wins[currentLevel] = 0;
        losses[currentLevel] = 0;
        theScore[currentLevel] = 0;
    
        mutatedGeneCount = 0;
        susceptibleCancerCount = 0;
        
        displayScore();
        displayMutatedGenes();
        displaySusceptibleCancers();
    }  
    
    //-------------------------------------------
    // Reset all level scores and grand core
    //-------------------------------------------
    public void resetAllScore()
    {
        for (int level = 1; level <= MAX_LEVEL; level++)
        {
            wins[level] = 0;
            losses[level] = 0;
            theScore[level] = 0;
        }  
        grandScore = 0;
        
        mutatedGeneCount = 0;
        susceptibleCancerCount = 0;
        
        displayScore();
        displayMutatedGenes();
        displaySusceptibleCancers();
    }
    
    // Display current score
    public void displayScore()
    {
        String tempStr = "Level: " + currentLevel + "\n" + 
                         "Level Score: " + theScore[currentLevel] + "\n" +
                         "Grand Score: " + grandScore;
        scoreArea.setFont(currentFont);
        scoreArea.setText(tempStr);
    }
    
    // Record a win and adjust various scores
    public void recordWin()
    {
        wins[currentLevel] += 1;
        theScore[currentLevel] += WIN_POINT;
        grandScore += WIN_POINT;   
        displayScore();
    }
    
    // Record a loss and adjust various scores
    public void recordLoss()
    {
        losses[currentLevel] += 1;
        theScore[currentLevel] -= LOSS_POINT;
        grandScore -= LOSS_POINT;
        displayScore();
    }
    
    
    // Record proteins and cancers of Pathway 'path' for display
    public void recordPathwayProteinsAndCancers(Pathway path)
    {
        int proteinCount = path.getProteinCount();
        for (int i = 0; i < proteinCount; i++)
        {
            mutatedGenes[mutatedGeneCount] = new String(path.getProteinName(i));
            mutatedGeneCount++;
        }
        
        int cancerCount = path.getCancerCount();
        for (int i = 0; i < cancerCount; i++)
        {
            susceptibleCancers[susceptibleCancerCount] = new String(path.getCancerName(i));
            susceptibleCancerCount++;
        }  
        
        displayMutatedGenes();
        displaySusceptibleCancers();
    }
    
    
    public int getCurrentLevel() { return currentLevel; }
    
    // Display mutated genes of the simulated pathways in the designated area
    public void displayMutatedGenes()
    {
        String tempStr = "Mutated Genes:                    " ;
        for (int i = 0; i < mutatedGeneCount; i++)
        {
            tempStr += mutatedGenes[i] + ", ";
        }
        mutatedGenesArea.setFont(currentFont);
        mutatedGenesArea.setText(tempStr);
    }
    
    // Display susceptible cancers of the simulated pathways in the designated area
    public void displaySusceptibleCancers()
    {
        String tempStr = "Possible Cancers:                 " ;
        for (int i = 0; i < susceptibleCancerCount; i++)
        {
            tempStr += susceptibleCancers[i] + ", ";
        }
        susceptibleCancerArea.setFont(currentFont);
        susceptibleCancerArea.setText(tempStr);
    }
    
    
    //---------------------------------------------------------
    // Constructor for the Score Panel. Create 3 text area and
    // a slider organized as 4x1 grid using GridLayout.
    //
    // Initialize wins, losses and theScore array
    //---------------------------------------------------------
    
    public ScorePanel()
    {
        currentLevel = 1; // current game level
        mutatedGenes       = new String [MAX_MUTATED_GENES];
        susceptibleCancers = new String [MAX_POSSIBLE_CANCERS];
        
        // Use one extra int to have wins/loss/theScore for
        // each level i in the corresponding index i
        wins     = new int [MAX_LEVEL+1];
        losses   = new int [MAX_LEVEL+1];
        theScore = new int [MAX_LEVEL+1];
        
        currentFont = new Font("Arial", Font.PLAIN, 20);
        
        this.setLayout(new GridLayout(4, 1, 2, 2) ); // 4x1 grid
         
        // Create and add scoreArea
        scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setBackground(Color.GREEN);
        this.add(scoreArea);
        
        // Create and add mutatedGenesArea
        mutatedGenesArea = new JTextArea();
        mutatedGenesArea.setLineWrap(true);
        mutatedGenesArea.setRows(30);
        mutatedGenesArea.setWrapStyleWord(true);
        mutatedGenesArea.setEditable(false);
        mutatedGenesArea.setBackground(Color.YELLOW);
        genesScroll = new JScrollPane(mutatedGenesArea);
        this.add(genesScroll);
        
        // Create and add susceptibleCancerArea
        susceptibleCancerArea = new JTextArea();
        susceptibleCancerArea.setLineWrap(true);
        susceptibleCancerArea.setRows(15);
        susceptibleCancerArea.setWrapStyleWord(true);
        susceptibleCancerArea.setEditable(false);
        susceptibleCancerArea.setBackground(Color.ORANGE);
        cancerScroll = new JScrollPane(susceptibleCancerArea);
        this.add(cancerScroll);
        
        // Create and add slder for game level control
        levelSlider = new JSlider(1, 6, 1);
        levelSlider.setMajorTickSpacing(2);
        levelSlider.setMinorTickSpacing(1);
        levelSlider.setPaintTicks(true);
        levelSlider.setLabelTable( levelSlider.createStandardLabels(1) );
        levelSlider.setPaintLabels(true);
        levelSlider.setSnapToTicks(true);   
        
        // Register self as the change listener for the slider
        levelSlider.addChangeListener(this);
        this.add(levelSlider);   
        
        resetAllScore();
        displayScore();
        displayMutatedGenes();
        displaySusceptibleCancers();
    }
    
    //-----------------------------------------------------
    // Handle game level selection by slider. Always ask for
    // user confirmation if level is changed
    //-----------------------------------------------------
    public void stateChanged(ChangeEvent e)
    {
        int newLevel = levelSlider.getValue();
        if (newLevel != currentLevel)
        {  
            if (!levelSlider.getValueIsAdjusting())
                askLevelConfirmation(newLevel);
        }
                
        displayScore();               // display score at the current level
        displayMutatedGenes();        // display mutated genes
        displaySusceptibleCancers();  // display susceptible cancers
    }
    
    
    //--------------------------------------------------------------
    // Dialog to ask user confirmation when game level is changed
    // using slider. If user confirms the choice then reinitialize
    // the game and wait for user to press the Play button
    //--------------------------------------------------------------
    
    public void askLevelConfirmation(int newLevel)
    {   
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to change game level and start over?\n");
            
        switch(response) {
            case JOptionPane.YES_OPTION:     
                currentLevel = newLevel;
                mutatedGeneCount = 0;
                susceptibleCancerCount = 0;
                cell.initializeGame();  // Re-initialize the cell panel upon level change
                break;
            case JOptionPane.NO_OPTION:
            case JOptionPane.CANCEL_OPTION: 
            case JOptionPane.CLOSED_OPTION: 
                levelSlider.setValue(currentLevel); // Reset the slider to old level
                break;
        }   
    }
    
    
    //---------------------------------------------------------
    // Save all score in a text file named 'fileName'
    //---------------------------------------------------------
    
    public void save(String fileName)
    {
        PrintWriter output;
        
        try       
        {
          File outfile = new File(fileName);
          output = new PrintWriter(outfile);
        } catch (IOException e)
        {
          System.err.println("ERROR: Cannot open file " + fileName);
          return;
        }
        
        // Write wins and losses for each game level
        for (int level = 1; level <= MAX_LEVEL; level++)
        {
            if (wins[level] > 0 || losses[level] > 0)
            {
                output.print("Level " + level + " score: " + theScore[level] +
                             " , " + wins[level] + " Kills and " +
                              losses[level] + " Damages \n");
            }
        }           
        
        // Write Grand score
        output.print("Grand Score: " + grandScore + "\n\n");
        
        output.close();
    }
}
