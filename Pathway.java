//------------------------------------------------------------------
// Author: Pranav Srinivas, MVHS,   Date: 5/7/2013
//------------------------------------------------------------------

// package signalstorm;

//--------------------------------------------------------------------
// The Pathway class is used to initialize game with specific 
// cancer signaling pathways
//
// Each pathway is characterized by an array of connected proteins and
// cell surface receptor proteins that activate the pathway
//--------------------------------------------------------------------

class Pathway
{
    private int       id;        // Pathway Index
    private String    name;      // Pathway Name
    private String    family;    // Pathway family name
    
    private String [] relatedCancers;      // pathway related cancers 
    private int       relatedCancerCount;  // related cancer count
    
    ProteinElement [][] network;   // reference to Signaling network in CellPanel
    ScorePanel          score;     // reference to Score panel for easy score manipulation
    
    private ProteinElement [] proteins;  // Array of Protein Element references in this pathway
    private int proteinCount;            // Protein Count in this pathway
    
    ProteinElement []  receptor;         // Cell surface receptors for this pathway
    
    private boolean    active;           // is it a currently simulated pathway
     
    public static final int MAX_POSSIBLE_CANCERS = 25;
    public static final int MAX_PATHWAY_PROTEINS = 20;
    
    public Pathway(int Id, String Name, String Family, ProteinElement [][] ntk, ScorePanel theScore)
    {
        id     = Id;
        name   = new String(Name);
        family = new String(Family);
        relatedCancers = new String [MAX_POSSIBLE_CANCERS];
        relatedCancerCount = 0;
        network = ntk;
        score   = theScore;
        proteins = new ProteinElement [MAX_PATHWAY_PROTEINS];
        proteinCount = 0;
        
        receptor = new ProteinElement[2]; // 2 cell surface receptor
        active = false;
        
        // ccreate simplistic signaling network for the game
        createSignalingNetwork();
    }
    
    // Get protein count and protein names
    public int    getProteinCount()     { return proteinCount;          }
    public String getProteinName(int i) { return proteins[i].getName(); }
    
    // Get cancer count and cancer names
    public int    getCancerCount()     { return relatedCancerCount; }
    public String getCancerName(int i) { return relatedCancers[i];  }
    
    // Get pathway name
    public String getName()            { return name; }
    
    // Activate ProteinElement pe and add its reference to proteins array
    public void addProtein(ProteinElement pe)
    {
        pe.setActive(true);
        proteins[proteinCount] = pe;
        proteinCount++;
    }
    
    
    // Connect pe1 to pe2 in activating/inhibiting way
    public void connectProtein(ProteinElement pe1, ProteinElement pe2, boolean activating)
    {   
        int row_2 = pe2.getRow();
        int col_2 = pe2.getCol();
        
        pe1.createConnection(row_2, col_2, activating);  
    }
    
    public boolean isActive() { return active; }
    
    //------------------------------------------------------------------------
    // Deactivate this pathway by setting all proteins active status to false
    //------------------------------------------------------------------------
    public void  deactivate() 
    { 
        active = false; 
        
        for (int i = 0; i < proteinCount; i++)
            proteins[i].setActive(false);
    }
    
    
    //------------------------------------------------------------------
    // Activate the pathway and set oncogenic signals at the receptors
    //------------------------------------------------------------------
    public void activate()
    {
        active = true;  // activate me!
        
        // activate individual proteins in this pathway
        for (int i = 0; i < proteinCount; i++)
            proteins[i].setActive(true);
        
        // Inject oncogenic signals at the receptors (mostly 1 recepor)
        for (int i = 0; i < 2; i++)
        {
            if (receptor[i] != null)
                receptor[i].setOncogenicSignal(true);
        }
    }
    
    
    //----------------------------------------------------------
    // Move Oncogenic signal one step forward along the pathway
    //----------------------------------------------------------
    
    public void moveOncoOneStep()
    {
        if (!active) return;  // if pathway is not active, return
        
        ProteinElement [] oncoProteins = new ProteinElement[MAX_PATHWAY_PROTEINS];
        int oncoCount = 0;
        
        //-----------------------------------------------------------
        // Collect all proteins of this pathway that have oncogenic 
        // signals and move them one step forward outside the loop. 
        // This is done to prevent double moving one signal!
        //-----------------------------------------------------------
        for (int i = 0; i < proteinCount; i++)
        {
            if (proteins[i].hasOncogenicSignal())
            {
                oncoProteins[oncoCount] = proteins[i];
                oncoCount++;
            }
        }
        
        for (int i = 0; i < oncoCount; i++)
        {
            oncoProteins[i].moveOnco(network, score);
        }      
    }
    
    
    //------------------------------------------------------------
    // Create simplistic signaling network for the pathway 
    //------------------------------------------------------------
    
    public void createSignalingNetwork()
    {   
        ProteinElement pe0, pe1, pe2, pe3, pe4, pe5, pe6, pe7;
        ProteinElement pe8, pe9, pe10, pe11, pe12, pe13, pe14;
        ProteinElement pe15, pe16;
        
        switch(id)    
        {    
            case 0:
                // HedgeHog
                System.out.println("Creating HedgeHog Signaling Network ......");
                
                relatedCancers[0] = new String("Brain");
                relatedCancers[1] = new String("Lung");
                relatedCancers[2] = new String("ALL");
                relatedCancers[3] = new String("Colon");
                relatedCancers[4] = new String("Liver");
                relatedCancerCount = 5;
                
                pe0 = network[28][19]; pe0.setProtein("PTCH");
                pe1 = network[28][17]; pe1.setProtein("SMO");
                pe2 = network[21][17]; pe2.setProtein("GLI");
                pe3 = network[16][16]; pe3.setProtein("CDK4");
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;
                
                pe3.setNuclearProtein(true);
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                
                connectProtein(pe0, pe1, false);
                connectProtein(pe1, pe2, true);
                connectProtein(pe2, pe3, true);
                        
                break;

            case 1:
                // "Notch"
                
                System.out.println("Creating Notch Signaling Network ......");
                
                relatedCancers[0] = new String("Skin");
                relatedCancers[1] = new String("Bone");
                relatedCancers[2] = new String("Liver");
                relatedCancers[3] = new String("Non-Hodgekin's Lymphoma");
                relatedCancers[4] = new String("Lung");
                relatedCancerCount = 5;
                
                pe0 = network[25][24]; pe0.setProtein("NOTCH");
                pe1 = network[25][19]; pe1.setProtein("CNTCH");  // Cleaved Notch
                pe2 = network[21][19]; pe2.setProtein("NIC");
                pe3 = network[19][19]; pe3.setProtein("CSL");
                pe4 = network[16][16]; pe4.setProtein("CDK4");
                pe5 = network[22][24]; pe5.setProtein("DSH");
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;
                
                pe4.setNuclearProtein(true);
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                addProtein(pe4);
                addProtein(pe5);
                
                connectProtein(pe5, pe0, false);
                connectProtein(pe0, pe1, true);
                connectProtein(pe1, pe2, true);
                connectProtein(pe2, pe3, true);
                connectProtein(pe3, pe4, true);
                
                break;  
                    
            case 2:
                // "Wnt"
                
                System.out.println("Creating Wnt Signaling Network ......");
                
                relatedCancers[0] = new String("Melanoma");
                relatedCancers[1] = new String("Colorectal");
                relatedCancers[2] = new String("Kidney");
                relatedCancers[3] = new String("CML");
                relatedCancers[4] = new String("Bladder");
                relatedCancerCount = 5;
                
                pe0 = network[24][4];  pe0.setProtein("Wnt");
                pe1 = network[24][7];  pe1.setProtein("DSH");
                pe2 = network[21][7];  pe2.setProtein("GSK3B");
                pe3 = network[19][10]; pe3.setProtein("BCATN"); // Beta Catenin
                pe4 = network[16][4];  pe4.setProtein("Ras");
                pe5 = network[18][4];  pe5.setProtein("PI3K");
                pe6 = network[21][4];  pe6.setProtein("Akt");
                pe7 = network[19][12]; pe7.setProtein("SOX");
                pe8 = network[22][10]; pe8.setProtein("RAR");
                pe9 = network[14][14]; pe9.setProtein("CDK4");
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                addProtein(pe4);
                addProtein(pe5);
                addProtein(pe6);
                addProtein(pe7);
                addProtein(pe8);
                addProtein(pe9);
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;
                
                pe9.setNuclearProtein(true);
                
                connectProtein(pe0, pe1, true);
                connectProtein(pe1, pe2, false);
                connectProtein(pe2, pe3, true);
                connectProtein(pe3, pe9, true);
                
                connectProtein(pe4, pe5, true);
                connectProtein(pe5, pe6, true);
                connectProtein(pe6, pe2, false);
                
                connectProtein(pe7, pe3, false);
                connectProtein(pe8, pe3, false);
                
                break;  
                   
            case 3:
                // "Jak/STAT"
                
                System.out.println("Creating Jak/STAT Signaling Network ......");
                
                relatedCancers[0] = new String("AML");
                relatedCancers[1] = new String("Kidney");
                relatedCancers[2] = new String("CML");
                relatedCancers[3] = new String("Thyroid");
                relatedCancers[4] = new String("Liver");
                relatedCancerCount = 5;
                
                pe0 = network[0][16];  pe0.setProtein("CKNR");  // Cytokine Receptor
                pe1 = network[5][16];  pe1.setProtein("JAK");
                pe2 = network[8][16];  pe2.setProtein("STAT");
                pe3 = network[13][16]; pe3.setProtein("STAT");
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;
                
                pe3.setNuclearProtein(true);
                
                connectProtein(pe0, pe1, true);
                connectProtein(pe1, pe2, true);
                connectProtein(pe2, pe3, true);
                
                
                break;      
                    
            case 4:
                // "GPCR"  
                
                System.out.println("Creating GPCR Signaling Network ......");
                
                relatedCancers[0] = new String("Pancreas");
                relatedCancers[1] = new String("Lung");
                relatedCancers[2] = new String("ALL");
                relatedCancers[3] = new String("Colon");
                relatedCancers[4] = new String("Breast");
                relatedCancerCount = 5;
                
                pe0 = network[14][0]; pe0.setProtein("GABA");
                pe1 = network[14][1]; pe1.setProtein("GPCR");
                pe2 = network[14][3]; pe2.setProtein("PLCB");  // PLC-B
                pe3 = network[14][7]; pe3.setProtein("PKC");
                pe4 = network[12][7]; pe4.setProtein("RGAP");   // RasGAP  
                pe5 = network[12][10]; pe5.setProtein("Ras");
                pe6 = network[12][14]; pe6.setProtein("ERK1");  // ERK1/2                
                pe7 = network[14][14]; pe7.setProtein("CDK4");
                
                pe8 = network[14][10]; pe8.setProtein("RGRP");  // RasGRP
                pe9 = network[9][3]; pe9.setProtein("CAMKII");
                pe10 = network[9][10]; pe10.setProtein("SGAP"); // SynGAP
                pe11 = network[10][7]; pe11.setProtein("RGRF"); // RasGRF
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                addProtein(pe4);
                addProtein(pe5);
                addProtein(pe6);
                addProtein(pe7); 
                addProtein(pe8);
                addProtein(pe9);
                addProtein(pe10);
                addProtein(pe11);
               
                connectProtein(pe0, pe1, true);
                connectProtein(pe1, pe2, true);
                connectProtein(pe2, pe3, true);                
                connectProtein(pe3, pe4, false);
                connectProtein(pe4, pe5, false);
                connectProtein(pe5, pe6, true);
                connectProtein(pe6, pe7, true);
                
                connectProtein(pe3, pe8, true);
                connectProtein(pe8, pe5, true);
                
                connectProtein(pe2, pe9, true);
                connectProtein(pe9, pe10, true);
                connectProtein(pe9, pe11, true);
                connectProtein(pe10, pe5, false);
                connectProtein(pe11, pe5, true);
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;

                pe7.setNuclearProtein(true);

                break;   
                    
            case 5:
                // "Ras"
                
                System.out.println("Creating RAS Signaling Network ......");
                
                relatedCancers[0] = new String("Breast");
                relatedCancers[1] = new String("Ovarian");
                relatedCancers[2] = new String("Colorectal");
                relatedCancers[3] = new String("AML");
                relatedCancers[4] = new String("Liver");
                relatedCancerCount = 5;
                
                pe0 = network[5][4]; pe0.setProtein("EGFR");
                pe1 = network[5][5]; pe1.setProtein("Shc");
                pe2 = network[5][6]; pe2.setProtein("Grb2");
                pe3 = network[7][6]; pe3.setProtein("Sos");
                pe4 = network[7][8]; pe4.setProtein("Ras");  
                pe5 = network[7][12]; pe5.setProtein("Raf");
                pe6 = network[10][12]; pe6.setProtein("MEK1");  // MEK1/2 
                
                pe7 = network[12][14]; pe7.setProtein("ERK1");  // ERK1/2                
                pe8 = network[14][14]; pe8.setProtein("CDK4");
                
                pe9  = network[4][8]; pe9.setProtein("RGEF");  // RalGEF
                pe10 = network[3][10]; pe10.setProtein("RalA");
                pe11 = network[5][10]; pe11.setProtein("RalB");
                pe12 = network[3][12]; pe12.setProtein("RalBP1");
                pe13 = network[5][12]; pe13.setProtein("Cdc42");
                pe14 = network[3][14]; pe14.setProtein("Rac");
                
                pe15 = network[8][14];  pe15.setProtein("JNK");
                pe16 = network[12][15]; pe16.setProtein("Fos");
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                addProtein(pe4);
                addProtein(pe5);
                addProtein(pe6);
                addProtein(pe7); 
                addProtein(pe8);
                addProtein(pe9);
                addProtein(pe10);
                addProtein(pe11);
                addProtein(pe12);
                addProtein(pe13);
                addProtein(pe14);
                addProtein(pe15);
                addProtein(pe16);
                
                connectProtein(pe0, pe1, true);
                connectProtein(pe1, pe2, true);
                connectProtein(pe2, pe3, true);
                connectProtein(pe3, pe4, true);
                connectProtein(pe4, pe5, true);
                connectProtein(pe5, pe6, true);
                connectProtein(pe6, pe7, true);
                connectProtein(pe7, pe8, true);
                
                connectProtein(pe4, pe9, true);
                connectProtein(pe9, pe10, true);
                connectProtein(pe9, pe11, true);
                connectProtein(pe10, pe12, false);
                connectProtein(pe11, pe12, false);
                connectProtein(pe12, pe13, false);
                connectProtein(pe12, pe14, false);
                
                connectProtein(pe14, pe15, true);
                connectProtein(pe15, pe16, true);
                connectProtein(pe16, pe8,  true);
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;
                
                pe8.setNuclearProtein(true);
                pe16.setNuclearProtein(true);
                
                break;  
                    
            case 6:
                // "PI3K/Akt"
                
                System.out.println("Creating PI3K/Akt Signaling Network ......");
                
                relatedCancers[0] = new String("Bladder");
                relatedCancers[1] = new String("Lung");
                relatedCancers[2] = new String("CML");
                relatedCancers[3] = new String("Colon");
                relatedCancers[4] = new String("Pancreas");
                relatedCancerCount = 5;
                
                pe0 = network[4][24];  pe0.setProtein("PDGF");
                pe1 = network[8][24];  pe1.setProtein("PI3K");
                pe2 = network[8][26];  pe2.setProtein("Ras");
                pe3 = network[8][22]; pe3.setProtein("PIP3");
                pe4 = network[8][20];  pe4.setProtein("Akt");
                pe5 = network[15][20]; pe5.setProtein("MDM2");
                pe6 = network[15][17]; pe6.setProtein("P53");
                
                pe7 = network[11][18]; pe7.setProtein("GSK3B");
                pe8 = network[16][16]; pe8.setProtein("CDK4");
                
                pe9 = network[5][20]; pe9.setProtein("P21");
                pe10 = network[6][18]; pe10.setProtein("mTOR");
                pe11 = network[11][22]; pe11.setProtein("PTEN");
                pe12 = network[8][18]; pe12.setProtein("Bad");
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                addProtein(pe4);
                addProtein(pe5);
                addProtein(pe6);
                addProtein(pe7);
                addProtein(pe8);
                addProtein(pe9);
                addProtein(pe10);
                addProtein(pe11);
                addProtein(pe12);
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;
                
                pe6.setNuclearProtein(true);
                pe8.setNuclearProtein(true);
                
                connectProtein(pe0, pe1, true);
                connectProtein(pe2, pe1, true);
                connectProtein(pe1, pe3, true);
                connectProtein(pe3, pe4, true);
                connectProtein(pe4, pe5, true);
                connectProtein(pe5, pe6, false);
                connectProtein(pe4, pe7, true);
                connectProtein(pe7, pe8, false);
                connectProtein(pe4, pe9, false);
                connectProtein(pe4, pe10, true);
                connectProtein(pe11, pe3, false);
                connectProtein(pe4, pe12, false);

                
                
                break;  
                    
            case 7:
                // "NF-kB"
                
                System.out.println("Creating NF-kB Signaling Network ......");
                
                relatedCancers[0] = new String("Bone");
                relatedCancers[1] = new String("Lung");
                relatedCancers[2] = new String("Hodgkin's Lymphoma");
                relatedCancers[3] = new String("Liver");
                relatedCancers[4] = new String("Brain");
                relatedCancerCount = 5;
                
                pe0 = network[15][29]; pe0.setProtein("DR"); // Death Receptor
                pe1 = network[15][27]; pe1.setProtein("TRADD");
                pe2 = network[13][27]; pe2.setProtein("FADD");
                pe3 = network[13][23]; pe3.setProtein("Casp8");
                pe4 = network[16][23]; pe4.setProtein("Casp3");
                pe5 = network[16][16]; pe5.setProtein("CDK4");
                
                pe6 = network[19][28]; pe6.setProtein("TNFR");
                pe7 = network[19][26]; pe7.setProtein("TRAF");
                pe8 = network[17][26]; pe8.setProtein("NIK");
                
                pe9 = network[17][21]; pe9.setProtein("NEMO");
                pe10 = network[20][23]; pe10.setProtein("IKKA");
                pe11 = network[17][19]; pe11.setProtein("IkB");
                
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                addProtein(pe4);
                addProtein(pe5);
                addProtein(pe6);
                addProtein(pe7);
                addProtein(pe8);
                addProtein(pe9);
                addProtein(pe10);
                addProtein(pe11);
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;
                
                pe6.setIsReceptor(true);
                receptor[1] = pe6;
                
                pe5.setNuclearProtein(true);
                
                connectProtein(pe0, pe1, true);
                connectProtein(pe1, pe2, true);
                connectProtein(pe2, pe3, true);
                connectProtein(pe3, pe4, true);
                connectProtein(pe4, pe5, true);
                
                connectProtein(pe6, pe7, true);
                connectProtein(pe7, pe8, true);
                connectProtein(pe8, pe9, true);
                connectProtein(pe8, pe10, true);
                connectProtein(pe9, pe11, true);
                connectProtein(pe11, pe5, true);
                
                break;  
                    
            case 8:
                // "TGF-B"
                System.out.println("Creating TGF-B Signaling Network ......");
                
                relatedCancers[0] = new String("Liver");
                relatedCancers[1] = new String("Lung");
                relatedCancers[2] = new String("Breast");
                relatedCancers[3] = new String("Ovarian");
                relatedCancers[4] = new String("Pancreas");
                relatedCancerCount = 5;
                
                pe0 = network[27][8];  pe0.setProtein("TGFB"); // TGFB-R1 and R2
                pe1 = network[27][15]; pe1.setProtein("Smad2");
                pe2 = network[25][15]; pe2.setProtein("Smad3");
                pe3 = network[22][15]; pe3.setProtein("Smad4");
                pe4 = network[17][15]; pe4.setProtein("Myc");
                
                pe5 = network[25][13]; pe5.setProtein("ERK");
                pe6 = network[24][10]; pe6.setProtein("TAK");
                pe7 = network[24][12]; pe7.setProtein("P38");
                pe8 = network[21][12]; pe8.setProtein("JNK");
                pe9 = network[17][14]; pe9.setProtein("Fos");
                
                addProtein(pe0);
                addProtein(pe1);
                addProtein(pe2);
                addProtein(pe3);
                addProtein(pe4);
                addProtein(pe5);
                addProtein(pe6);
                addProtein(pe7);
                addProtein(pe8);
                addProtein(pe9);
                
                pe0.setIsReceptor(true);
                receptor[0] = pe0;
                
                pe4.setNuclearProtein(true);
                pe9.setNuclearProtein(true);
                
                connectProtein(pe0, pe1, true);
                connectProtein(pe1, pe2, true);
                connectProtein(pe2, pe3, true);
                connectProtein(pe2, pe4, false);
                connectProtein(pe5, pe2, true);
                connectProtein(pe0, pe6, true);
                connectProtein(pe6, pe7, true);
                connectProtein(pe6, pe8, true);
                connectProtein(pe7, pe4, true);
                connectProtein(pe8, pe9, true);
                
                break;  
                    
            default:
                break;
        }
    }
}
