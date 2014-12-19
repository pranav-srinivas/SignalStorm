//------------------------------------------------------------------
// Author: Pranav Srinivas, MVHS,   Date: 5/26/2013
//------------------------------------------------------------------

// package signalstorm;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

//----------------------------------------------------------------
// Utilities class contains utility functions to be 
// used in rest of the program
//----------------------------------------------------------------

public class Utilities {
     
   private String [] help =
           
   {"Signal Storm Rules \n\nSignalStorm is an original interactive game " +
    "that simulates human cancer signaling network. Players can learn about " +
    "various signal receptors and their corresponding intra-cellular signaling pathways.\n\n"   +
    "The game supports simulation of 9 cancer signaling pathways including "  +
    "HedgeHog, Notch, Wnt, PI3K/AKT, GPCR, Ras, TGF-B, Jak/Stat and NF-kB.\n\n"  +
           
    "The purpose of the game is to use four (4) supplied suppressor proteins to kill " +
    "oncogenic signals (originating at receptors) before they reach nucleus " +
    "and cause DNA Damage. Each kill is awarded two (2) points while a DNA damage by " +
    "an oncogenic signal results in one (1) point loss. The game begins when user " +
    "presses 'Play' button.\n\n" +
           
    "The four suppressors are controlled by 4 sets of four keys: " +
    "The suppressor in lower right region is controlled by four arrow keys. " +
    "The suppressor in upper right region is controlled by '7' (Left), '8' (Down), '9' (Up) and '0' (Right) keys. " +
    "The suppressor in upper left  region is controlled by '1' (Left), '2' (Down), '3' (Up) and '4' (Right) keys. " +
    "The suppressor in lower left  region is controlled by 'a' (Left), 's' (Down), 'w' (Up) and 'd' (Right) keys. \n\n" +
           
    "The game supports 6 different levels corresponding to increasing order of difficulty. "  +
    "Each level corresponds to number of pathways simulated for that level. For example at "  +
    "level 4 four pathways are simulated. Players can indicate their pathway preference "     +
    "by using the drop-down 'Signaling' menu. When selecting signaling pathways to simulate " +
    "for a level, Player indicated preference is given priority. If player indicated " +
    "pathway preference is less than the game level then other pathways are randomly picked. \n\n" +
           
    "The game can be paused any time by pressing the 'Pause' button and later resumed " +
    "by pressing the 'Resume' button. The game level can be changed by using the slider. " +
    "The game score can be saved by using the 'File' drop down menu and selecting Save or Save As. " +
    "Players can also erase their bad scores by using 'Reset' drop down menu and either " +
    "selecting 'Full' to reset scores for all level or 'Current' to reset score for current level.\n\n" +
           
    "The game level, level score and grand score along with mutated genes and " +
    "associated cancers are displayed in score panel. Suppressors are shown in blue while " +
    "flashing orange oval indicates oncogenic signal. Magenta is used for receptor proteins and " +
    "Pink for nuclear protein for easy identification. Proteins in inactive pathways are shown in white.\n\n" +
           
    "The game can be terminated by selecting 'Exit' under 'File' drop-down menu.",       
           
                                 
             
    "Cancer Signaling\n\n" +
    "SignalStorm supports simulation of 9 cancer signaling pathways:\n\n" +
           
    "HedgeHog Signaling: Binding of the Patched receptor by a Hedgehog ligand causes " +
    "Patched to release Smoothened protein from inhibition. Smoothened then emits downstream " +
    "signals that protect cytoplasmic Gli protein from cleavage. Intact Gli can then migrate " +
    "to the nucleus and functions as transcription activator.\n\n" +
           
    "Notch Signaling: Upon binding one of its ligands, Notch is cleaved twice, liberating a cytoplasmic " +
    "fragment that migrates to the nucleus and functions as part of a transcription factor complex.\n\n" +
           
    "Wnt Signaling: Acting through Frizzled receptors, Wnt suppresses the activity of GSK-3B, which " +
    "otherwise would phosphorylate several key substrates including Beta-catenin and cyclin D1 " +
    "tagging them for destruction. The spared Beta-catenin moves into the nucleus and activates " +
    "transcription of key growth stimulating genes.\n\n" +
           
    "Jak/STAT Signaling: Receptors for cytokines form complexes with tyrosine kinase of the Jak class. " +
    "which phosphorylate STATs (signal transducers and activators of transcription). The STATs form " +
    "dimers, and migrate to the nucleus, where they function as transcription factors.\n\n" +
   
    "GPCR Signaling: Upon binding theirextracellular ligands, G-protein-coupled receptors (GPCRs) " +
    "activate cytoplasmic hetero-trimeric G proteinswhose alpha subunit exchanges its GDP for GTP. " +
    "The G-alpha subunit then dissociates from its two partners (G-beta + G-gamma) and proceeds to " +
    "activate or inhibit a number of cytoplasmic enzymes having mitogenic or anti-mitogenic effects. " +
    "The (G-beta + G-gamma) dimer activatesits own effectors, including PI3K-gamma, PLC-beta, Src.\n\n " +
           
    "Ras Signaling: Three major downstream signaling cascades emanate from activated Ras via binding " +
    "of its effector loop with downstream signaling partners - Raf kinase, PI3K, and RalGEF. Raf " +
    "phosphorylates residues on MEK. The resulting activated MEK phosphorylates and activates Erk-1/2.\n\n" +
           
    "PI3K/Akt Signaling: The phosphatidylinositol 3-kinase (PI3K) pathway depends on kinases phosphorylating" +
    "a phospholipid and is important in suppressing apoptosis and in promoting the growth of cells.\n\n"+
           
    "NF-kB Signaling: The nuclear factor-kB signaling system depends on the formation of NF-kB homo- " +
    "and hetero-dimers in the cytoplasm. The inhibitor of NF-kB (IkB) usually sequesters NF-kB in the " +
    "cytoplasm but in response to signaling, is tagged for destruction by IkB kinase (IKK). This leaves " +
    "NF-kB free to migrate into the nucleus, where it activates expression of at least 150 genes, some " + 
    "of which specify key anti-apoptotic proteins.\n\n" +
    
    "TGF-B Signaling: The TGF-beta signaling pathway involves the dispatch of cytoplasmic Smad " +
    "transcription factors into the nucleus, where they help activate a large contingent of genes. " +
    "This pathway plays a major role in the pathogenesis of many carcinomas.\n\n\n" +
           
    "Source: The Biology of Cancer by Robert A Weinberg",
                                    
       
    
    "Genetic Alterations\n\n" +
    "Currently SignalStorm only supports Mutation alteration. Other possible alterations include " +
    "gene amplification, gene deletion and overexpression"       
             
   };
    
   // Show help in a separate frame window based on topic
   public void showHelp(int topic)
   {
       String title = "";
        
       if (topic == 0) title = new String("Game Rules");
       if (topic == 1) title = new String("Signaling");
       if (topic == 2) title = new String("Alterations");
        
       JFrame helpFrame = new JFrame(title);
        
       JTextArea helpArea = new JTextArea();
       helpArea.setLineWrap(true);
       helpArea.setRows(100);
       helpArea.setWrapStyleWord(true);
       helpArea.setEditable(false);
       helpArea.setBackground(Color.WHITE);
        
       // Set help text based on topic
       helpArea.setText(help[topic]);
                
       JScrollPane scrollPane = new JScrollPane(helpArea);
        
       helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       helpFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
       
       helpFrame.setSize(620, 650);
       helpFrame.setLocation(100, 100);
       helpFrame.setVisible(true);  
   }
   
}

// Simple location class to keep track of (row, col) locations
class Location
{
    private int row;
    private int col;
    
    public Location(int Row, int Col)
    {
        set(Row, Col);
    }
    
    public void set(int Row, int Col)
    {
        row = Row;
        col = Col;
    }
    
    public void setRow(int Row) { row = Row; }
    public void setCol(int Col) { col = Col; }
   
    public int getRow() { return row; }
    public int getCol() { return col; }        
    
    public void moveUp()
    {
        row -= 1;
        if (row < 1) row++;
    }
    
    public void moveDown()
    {
        row += 1;
        if (row == 30) row--;
    }
    
    public void moveRight()
    {
        col += 1;
        if (col == 30) col--;
    }
    
    public void moveLeft()
    {
        col -= 1;
        if (col < 1) col++;
    } 
    
    
    //---------------------------------------------------------------------
    // Outside of cell membrane and inside of cell nucleus is restricted
    // zone for suppressors
    //---------------------------------------------------------------------
    
    public boolean inRestrictedZone()
    {
        // Nucleus restricted zone
        int beginRow = 12, endRow = 17, beginCol = 12, endCol = 17;
        
        // in restricted rectangular area
        if (row >= beginRow && row <= endRow && col >= beginCol && col <= endCol)
        {
            // corners zones are ok (only elliptical area is restricted)
            if ( (row == beginRow || row == endRow) && (col == beginCol || col == endCol) )
                return false;
            else
                return true;            
        }

        // Cell Membrane restricted zone
        switch (row)
        {
            case 0: case 29:
                return true;
                
            case 1:  case 28:
                if (col >= 18) return true;
                if (col <= 11) return true;
                break;
            case 2:  case 27:
                if (col <= 8) return true;
                if (col >= 21) return true;
                break;
        
            case 3: case 4: case 5:   
                if (col <= 9-row || col >= 20+row) return true;
                break;
             
            case 24:
                if (col <= 3 || col >= 25) return true;
                break;

            case 25:  
                if (col <= 4 || col >= 24) return true;
                break;     

            case 26:  
                if (col <= 5 || col >= 23) return true;
                break;
                       
                
            case 6:  case 23:
                if (col <= 3 || col >= 27) return true;
                break;
                
            case 7: case 8:   case 21: case 22:
                if (col <= 2 || col >= 27) return true;
                break;
                
            case 9: case 10: case 11:    case 18: case 19:
                if (col <= 1 || col >= 29) return true;
                break;
                    
            case 20:
              if (col <= 1 || col >= 28) return true;
               break;  
                
            case 12: case 13: case 14: case 15: case 16: case 17:
                if (col == 0 || col == 29) return true;
                break;
        }
        
        return false;
    }
    
}
