//------------------------------------------------------------------
// Author: Pranav Srinivas, MVHS,   Date: 5/5/2013
//------------------------------------------------------------------

// package signalstorm;

import java.awt.*;

//-------------------------------------------------------------------------
// Class representing one protein element (occupant at each grid location)
//
//-------------------------------------------------------------------------
public class ProteinElement 
{
    private int row;                      // row number of this protein element
    private int col;                      // column number of this protein element
    
    private boolean hasProtein;           // does it have a real protein    ?
    private boolean hasOncogenicSignal;   // does it have a cancer signal   ?
    private boolean hasSuppressorSignal;  // is suppressor on this location ?
    
    private boolean isReceptor;           // is it a cell surface receptor protein ?
    private boolean nuclearProtein;       // is it a nucleus protein ?
    
    private String  proteinName;          // name of protein if it has one
    private boolean active;               // is it active or inactive
    
    private ConnectionElement [] connections; // array of connections to other protein elements
    private int connectionCount;              // protein connection count
    
    private Font font;
    
    private final int MAXCONNECTIONS = 10;    // number of max protein connections allowed 
            
    public ProteinElement(int Row, int Col)
    {   
        row = Row;
        col = Col;
        connections = new ConnectionElement [MAXCONNECTIONS];        
        font = new Font("Arial", Font.BOLD, 7);
    }
    
    // Set presence of protein and its name
    public void setProtein(String Name)
    {
        proteinName = new String(Name);
        hasProtein = true;
    }
    
    // Get protein name
    public String getName()
    {
        return proteinName;
    }
    
    public boolean getIsReceptor()       { return isReceptor; }  // true if receptor protein
    public void setIsReceptor(boolean v) { isReceptor = v;    }
    
    public void setActive(boolean value)
    {
        active = value;
    }
    
    public boolean getActive()
    {
        return active;
    } 
    
    public boolean isNuclearProtein()        { return nuclearProtein; }  // true if nucleus protein
    public void setNuclearProtein(boolean v) { nuclearProtein = v;    }
    
    public void setOncogenicSignal(boolean value)
    {
        hasOncogenicSignal = value;
    }
    
    public void setSuppressorSignal(boolean value)
    {
        hasSuppressorSignal = value;     
    }
    
    public boolean hasOncogenicSignal()
    {
        return hasOncogenicSignal;
    }
    
    public boolean hasSuppressorSignal()
    {
        return hasSuppressorSignal;
    }
   
    
    public int getRow() { return row; }
    public int getCol() { return col; }
    
    public void createConnection()
    {
        if (connectionCount == MAXCONNECTIONS) 
        {
            System.out.println("Error: Max amount of connections reached.");
            return;
        }
        
        connections [connectionCount] = new ConnectionElement();
        connectionCount++;
    }
    

    //--------------------------------------------------------------------
    // Create connection to protein at (endRow, endCol) location
    // The nature of connection activating/inhibiting is also specified
    //--------------------------------------------------------------------
    
    public void createConnection(int endRow, int endCol, boolean activating)
    {
        if (connectionCount == MAXCONNECTIONS) 
        {
            System.out.println("Error: Max amount of connections reached.");
            return;
        }
        
        connections [connectionCount] = new ConnectionElement(row, col, endRow, endCol, activating);
        connectionCount++;
    }
    
    //-------------------------------------------------------------
    // Draw one Protein Element and all its direct connections
    //-------------------------------------------------------------
    public void Draw(int x, int y, Graphics g, int xStep, int yStep, boolean oncoOpen, boolean suppressorOpen)
    {
        // Show protein if present
        if (hasProtein)
        {
            // Active protein is shown Green, inactive as white
            Color color = active ? Color.GREEN : Color.WHITE;
          
            // receptor protein is shown in magenta
            if (getIsReceptor()) color = Color.MAGENTA;
            
            // nuclear protein is shown in pink
            if (isNuclearProtein()) color = Color.PINK;
            
            g.setColor(color);     
            g.fillRect(x + 5, y + 3, xStep - 10, yStep - 6);  
            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(proteinName, x + 7, y + 2 + yStep/2);
        }
        
        // Show Oncogenic Signal if present
        if (hasOncogenicSignal)
        {
            g.setColor(Color.ORANGE);
            if (oncoOpen)
                g.drawOval(x + xStep * 2/3, y + yStep/3, xStep*2/5, yStep*2/5);
            else
                g.fillOval(x + xStep * 2/3, y + yStep/3, xStep*2/5, yStep*2/5);
        }
        
        // Show Suppressor Signal if present
        if (hasSuppressorSignal)
        {
            g.setColor(Color.BLUE);
            if (suppressorOpen)
                g.drawOval(x + xStep/6, y + yStep/6, xStep * 3/4, yStep * 3/4);
            else
                g.fillOval(x + xStep/6, y + yStep/6, xStep * 3/4, yStep * 3/4);
        }
        
        // Show all direct connections
        for (int i = 0; i < connectionCount; i++)
        {
            connections[i].Draw(g, xStep, yStep);   
        }
    }    
    
    
    //---------------------------------------------------------
    // Move oncogenic signal one step along its connections.
    // For easy access to all connected proteins this function
    // takes a reference to entire protein grid 'network'
    //---------------------------------------------------------
    
    public void moveOnco(ProteinElement [][] network, ScorePanel score)
    {   
        // for each connection
        for (int i = 0; i < connectionCount; i++)
        {
            // get (row, col) location of connected protein
            int toRow = connections[i].getToRow();
            int toCol = connections[i].getToCol();
            
            // get protein element at the 'to' location and set onco signal
            ProteinElement npe = network[toRow][toCol];
            npe.setOncogenicSignal(true);
            
            // If oncogenic signal reaches a nuclear protein, record a loss:-(
            if (npe.isNuclearProtein())
                score.recordLoss();
        }
        
        // After moving the oncogenic signal, set it to false for non-receptor proteins
        if (!isReceptor)
            this.setOncogenicSignal(false);       
    }
}
