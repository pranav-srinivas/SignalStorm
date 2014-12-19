//------------------------------------------------------------------
// Author: Pranav Srinivas, MVHS,   Date: 5/28/2013
//------------------------------------------------------------------

// package signalstorm;


//------------------------------------------------------------------------
// The Suppressor class has current location of suppressor as well as
// a reference to Signaling network in CellPanel. The reference copy
// is needed to manipulate Protein Element suppressor signal.
//------------------------------------------------------------------------

class Suppressor
{
    public Location loc;           // suppressor location
    ProteinElement [][] network;   // Reference to Signaling network in CellPanel
    ScorePanel score;              // Reference to Score Panel
    
    public ProteinElement protein; // ProteinElement where the suppressor is
    
    public Suppressor(int row, int col, ProteinElement [][] ntk, ScorePanel scorePanel)
    {
        loc = new Location(row, col);
        network = ntk;
        score = scorePanel;
    }
    
    // Set suppressor signal at (row, col)
    public void set(int row, int col)
    {
        loc.set(row, col);
        setSuppressorSignal(true);
    }
    
    private void setSuppressorSignal(boolean value) 
    {
        int row = loc.getRow();
        int col = loc.getCol();
        
        ProteinElement pe = network[row][col];
        
        pe.setSuppressorSignal(value); 
    }
    
    //-------------------------------------------------------------------
    // Update score based on suppressor movement. If suppressor
    // lands on a protein element with oncogenic signal, record a win!!
    //-------------------------------------------------------------------
    public void updateScore()
    {
        int row = loc.getRow();
        int col = loc.getCol();
        
        ProteinElement pe = network[row][col]; 
        
        if (pe.hasOncogenicSignal())
            score.recordWin();
    }
    
    // Move up, guard against restricted zone
    public void moveUp()    
    { 
        setSuppressorSignal(false); 
        loc.moveUp();
        
        if (loc.inRestrictedZone()) loc.moveDown();
        
        setSuppressorSignal(true); 
        updateScore();
    }   
   
    
    // Move down, guard against restricted zone
    public void moveDown()    
    { 
        setSuppressorSignal(false); 
        loc.moveDown();
        
        if (loc.inRestrictedZone()) loc.moveUp();
        
        setSuppressorSignal(true); 
        updateScore();
    }    
    
    
    // move right, guard against restricted zone
    public void moveRight()    
    { 
        setSuppressorSignal(false); 
        loc.moveRight();
        
        if (loc.inRestrictedZone()) loc.moveLeft();
        
        setSuppressorSignal(true); 
        updateScore();
    }
    
    // move left, guard against restricted zone
    public void moveLeft()    
    { 
        setSuppressorSignal(false); 
        loc.moveLeft();
        
        if (loc.inRestrictedZone()) loc.moveRight();
        
        setSuppressorSignal(true); 
        updateScore();
    }
}
  
