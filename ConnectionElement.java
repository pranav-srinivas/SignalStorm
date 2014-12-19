//------------------------------------------------------------------
// Author: Pranav Srinivas, MVHS,   Date: 5/5/2013
//------------------------------------------------------------------

// package signalstorm;

import java.awt.*;


//---------------------------------------------------------------------
// The ConnectionElement class represents a direct connection
// between two ProteinElements. Two types of connections are possible: 
// 
//      1. Stimulating connections are displayed as arrows (-->) 
//      2. Inhibiting  connections are displayed as T or -|
//---------------------------------------------------------------------


public class ConnectionElement 
{
    private int fromRow, fromCol; // connection origin
    private int toRow,   toCol;   // connection terminal
    private boolean stimulating;  // Stimulating or Inhibiting
    private boolean active;       // Active or inactive
    
    public ConnectionElement() { }
    
    public ConnectionElement(int beginRow, int beginCol, int endRow, int endCol, boolean activating)
    {
        fromRow = beginRow;
        fromCol = beginCol;
        toRow   = endRow;
        toCol   = endCol;
        stimulating = activating;
        active = false;
    }
    
    public boolean isActive()      { return active;      }
    public boolean isStimulating() { return stimulating; }
    public int getFromRow()        { return fromRow;     }
    public int getFromCol()        { return fromCol;     }
    public int getToRow()          { return toRow;       }
    public int getToCol()          { return toCol;       }
    
    public void setActive(boolean value) { active = value; }
    public boolean getActive()           { return active;  } 
    
    
    //------------------------------------------------------------------
    // Draw Arrowed line from (x1, y1) to (x2, y2) using simple tricks
    //
    // Not always perfect for diagonal arrows:-(
    //------------------------------------------------------------------
    public void drawArrow(Graphics g, int x1, int y1, int x2, int y2)
    {
        g.drawLine(x1, y1, x2, y2);
       
        int aw = 4; // arrow width;
        
        // Vertical Arrow
        if (x1 == x2)
        {
            if (y1 > y2)  // upward arrow
            {
                g.drawLine(x2, y2, x2-aw, y2+aw);
                g.drawLine(x2, y2, x2+aw, y2+aw);        
            }
            else          // downward arrow  
            {       
                g.drawLine(x2, y2, x2-aw, y2-aw);
                g.drawLine(x2, y2, x2+aw, y2-aw);
            }        
        }     
        
        // Horizontal Arrow
        else if (y1 == y2) 
        {
            if (x1 < x2)  // left to right arrow
            {       
                g.drawLine(x2, y2, x2-aw, y2-aw);
                g.drawLine(x2, y2, x2-aw, y2+aw); 
            }
            else          // right to left arrow
            {
                g.drawLine(x2, y2, x2+aw, y2-aw);
                g.drawLine(x2, y2, x2+aw, y2+aw);
            }        
        }
        
        // Diagonal Arrows
        
        else if (x1 < x2)
        {
            if (y1 > y2)  // first quadrant
            {
                g.drawLine(x2, y2, x2-aw, y2);
                g.drawLine(x2, y2, x2, y2+aw);        
            }
            else          // fourth quadrant 
            {       
                g.drawLine(x2, y2, x2-aw, y2);
                g.drawLine(x2, y2, x2, y2-aw);
            }        
        }
        else
        {
            if (y1 > y2)  // second quadrant
            {
                g.drawLine(x2, y2, x2+aw, y2);
                g.drawLine(x2, y2, x2, y2+aw);        
            }
            else          // third quadrant  
            {       
                g.drawLine(x2, y2, x2+aw, y2);
                g.drawLine(x2, y2, x2, y2-aw);
            }        
        }
    }
    
    
    // Draw the connection element (-> or T)
    public void Draw(Graphics g, int xStep, int yStep)
    {
        // Active connection is shown Green, inactive as Red    
        Color color = active ? Color.GREEN : Color.RED;
        g.setColor(color);
         
        int x1 = xStep * fromCol;   
        int y1 = yStep * fromRow;  
        
        int x2 = xStep * toCol;   
        int y2 = yStep * toRow;  
        
        // Draw horizontal or vertical lines
        if (fromCol == toCol || fromRow == toRow)
        {
            int delta = stimulating ? 4 : -2;
            
            if (fromCol == toCol)
            {
                x1 += xStep/2;
                x2 += xStep/2;
                
                if (y1 < y2)
                {
                    y1 += (yStep - delta);
                    y2 += delta;
                }
                else 
                {
                    y2 += (yStep - delta);
                    y1 += delta;
                }
            }
            
            if (fromRow == toRow)
            {
                y1 += yStep/2;
                y2 += yStep/2;  
                
                if (x1 < x2)
                {
                    x1 += (xStep - delta);
                    x2 += delta;
                }
                else
                {
                   x2 += (xStep - delta);
                   x1 += delta;  
                }
            }
          
            if (stimulating)
            {      
                drawArrow(g, x1, y1, x2, y2);
            }
            else
            {
                g.drawLine(x1, y1, x2, y2);
                
                if (x1 == x2)
                    g.fillRect(x1-5, y2, 10, 3); 
                else
                    g.fillRect(x2, y1-5, 3, 10);          
            }
        }
        
        // Draw Diagonal lines
        else {
            if (x1 < x2) x1 += xStep/2; 
            else         x2 += xStep/2;
            
            if (y1 < y2) y1 += yStep/2;
            else         y2 += yStep/2;
            
            if (stimulating)
                drawArrow(g, x1, y1, x2, y2);
            else
                g.drawLine(x1, y1, x2, y2);
        }
    }
}
