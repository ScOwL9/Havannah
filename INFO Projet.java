import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
import java.io.IOException;

public class ProjetInfo {

  public static class Structure {

    String nom;
    int[][] forme;

    Structure(String nom, int[][] forme) {
      this.nom = nom;
      this.forme = forme;
    }
  }

  public static class Sauvegarder {

    char[][] save_hex;
    char[][] save_gemhex;
    int[] save_score_points;
    int save_tour;

    Sauvegarder(char[][] save_hex, char[][] save_gemhex, int[] save_score_points, int save_tour) {
      this.save_hex = save_hex;
      this.save_gemhex = save_gemhex;
      this.save_score_points = save_score_points;
      this.save_tour = save_tour;
    }
  }

  

  public static char[][] Plateu() {
    char[][] hex = new char[11][13];
    for (int i = 0; i < 11; i++) {
        for (int j = 0; j < 13; j++) {
            hex[i][j] = ' ';
        }
    }
    for (int r = 0; r < 9; r++) {
        int espace = Math.abs(4 - r);
        int place  = 7 - espace;
        for (int k = 0; k < place; k++)
            hex[r][espace + k * 2] = '0';
    }

  ///       0 0 0    
  ///      0 0 0 0   
 ///      0 0 0 0 0  
 ///     0 0 0 0 0 0 
///     0 0 0 0 0 0 0
///      0 0 0 0 0 0 
///       0 0 0 0 0  
 ///       0 0 0 0   
  ///       0 0 0 
    
    return hex;
}

  public static char[][] Gemmes(char[][] hex, Random random) {

    char[][] gemhex = new char[11][13];
    for (int i = 0; i < 11; i++) {
        for (int j = 0; j < 13; j++) {
            gemhex[i][j] = '*';
        }
    }
    int gemmescaches = 0;
    
    while (gemmescaches < 10) {
      int r = random.nextInt(9);
      int c = random.nextInt(9);
      if (hex[r][c] == '0' && gemhex[r][c] == '*') {
        gemhex[r][c] = (random.nextInt(4) == 0) ? 'R' : 'S';
        gemmescaches++; 
      }
    }
    
    return gemhex;
  }

  public static ArrayList<Structure> BibliothequeStructures() {

    // listes avec les structures possibles

    ArrayList<Structure> bibliotheque = new ArrayList<>();

    Structure triangle = new Structure(
      "Triangle",
      new int[][]{
        {0,0},
        {0,2},
        {1,1}
      }
    );
    bibliotheque.add(triangle);
    
    ///   * *
    ///    *

    Structure triangle_inverse = new Structure(
      "TriangleInverse",
      new int[][]{
        {0,1},
        {1,0},
        {1,2}
      }
    );
    bibliotheque.add(triangle_inverse);

    ///   *
    ///  * *

    Structure etoile = new Structure(
      "Etoile",
      new int[][]{
        {0,1},
        {0,3},
        {1,0},
        {1,4},
        {2,1},
        {2,3}
      }
    );
    bibliotheque.add(etoile);

    ///     * *
    ///    *   * 
    ///     * *

    Structure ligne_h = new Structure(
      "LigneH",
      new int[][] {
        {0,0},{0,2},{0,4},{0,6},{0,8}
      }
    );
    bibliotheque.add(ligne_h);

    //    * * * * *

    Structure ligne_diag1 = new Structure(
      "LigneDiag\\",
      new int[][] {
        {0,0},{1,-1},{2,-2},{3,-3},{4,-4}
      }
    );
    bibliotheque.add(ligne_diag1);

    ///   *           
    ///    *           
    ///     *          
    ///      *     
    ///       *

    Structure ligne_diag2 = new Structure(
      "LigneDiag/",
      new int[][] {
        {0,0},{1,1},{2,2},{3,3},{4,4}
      }
    );
    bibliotheque.add(ligne_diag2);

    ///       *           
    ///      *           
    ///     *          
    ///    *     
    ///   *
    
    return bibliotheque;
  }

  public static String DetecterStructures (char[][] hex, char joueur, char marque) {

    ArrayList<Structure> bibliotheque = BibliothequeStructures();

    for (Structure s : bibliotheque) {
      for (int r = 0; r<9; r++) {
        for (int c = 0; c<13; c++) {
          boolean valide = true;
          for (int[] coord : s.forme) {
            int nr = r + coord[0];
            int nc = c + coord[1];
            if (nr < 0 || nr >= 9 || nc < 0 || nc >=13 || (hex[nr][nc] != joueur && hex[nr][nc] != marque)) {
              valide = false;
              break;
            }
          }

          if (valide) {
            for (int[] coord : s.forme) {
              hex[r + coord[0]][c + coord[1]] = marque;
            }
            return s.nom + " " + r + " " + c; 
          }
        }
      }
    }
    return null;
  }

  public static void VerifierVoisins(char[][] hex, char[][] gemhex, char joueur, char marque, int[] score_points, int tour) {
    
    for (int r = 0; r < 9; r++) {
      for (int c = 0; c < 13; c++) {
        if (hex[r][c] == joueur) {

          int[][] voisins = {
            {r,c-2},{r,c+2},{r-1,c+1},{r-1,c-1},{r+1,c-1},{r+1,c+1}
          };

          for (int[] v : voisins) {
            int vr = v[0];
            int vc = v[1];
            if (vr >= 0 && vr < 9 && vc >= 0 && vc < 13 && hex[vr][vc] == marque) {
              hex[r][c] = marque;
              if (gemhex[r][c] == 'S') { 
                score_points[tour - 1] += 1; 
                gemhex[r][c] = '*'; 
              } else if (gemhex[r][c] == 'R') { 
                score_points[tour - 1] += 2; 
                gemhex[r][c] = '*'; 
              }
              break;
            }
          }
        }
      }
    }
  }
  
  public static int VerifierGemmes(char[][] hex, char[][] gemhex, int tour, String resultat, Random random) {
    
    if (resultat == null) {
      return 0;
    }

    String[] parts = resultat.split(" ");
    String nom = parts[0];
    int r = Integer.parseInt(parts[1]);
    int c = Integer.parseInt(parts[2]);
    int score_points = 0;

    ArrayList<Structure> bibliotheque = BibliothequeStructures();
    for (Structure s : bibliotheque) {
      if (s.nom.equals(nom)) {
        for (int[] coord : s.forme) {
          int sr = r + coord[0];
          int sc = c + coord[1];
            if (gemhex[sr][sc] == 'S') { 
              score_points += 1; 
              gemhex[sr][sc] = '*'; 
            } else if (gemhex[sr][sc] == 'R') { 
              score_points += 2; 
              gemhex[sr][sc] = '*'; 
            }
          }

          if (nom.equals("Etoile")) {
            if (gemhex[r+1][c+2] != '*') {
              hex[r+1][c+2] = 'G';
            }

          } else if (nom.contains("Triangle")) {
            ArrayList<int[]> tousVoisins = new ArrayList<>();
            for (int[] coord : s.forme) {
              int sr = r + coord[0];
              int sc = c + coord[1];
              int[][] voisinsCell = {
                {sr,sc-2},{sr,sc+2},{sr-1,sc+1},{sr-1,sc-1},{sr+1,sc-1},{sr+1,sc+1}
              };
              
              for (int[] v : voisinsCell) {
                if (v[0] >= 0 && v[0] < 9 && v[1] >= 0 && v[1] < 13 && gemhex[v[0]][v[1]] != '*') {
                  tousVoisins.add(v);
                }
              }
            }
            
            if (!tousVoisins.isEmpty()) {
              int[] choix = tousVoisins.get(random.nextInt(tousVoisins.size()));
              hex[choix[0]][choix[1]] = 'G';
            }
            
          } else if (nom.contains("Ligne")) {
            for (int[] coord : s.forme) {
              int sr = r + coord[0];
              int sc = c + coord[1];
              int[][] voisinsCell = {
                {sr,sc-2},{sr,sc+2},{sr-1,sc+1},{sr-1,sc-1},{sr+1,sc-1},{sr+1,sc+1}
              };
              for (int[] v : voisinsCell) {
                int vr = v[0];
                int vc = v[1];
                if (vr >= 0 && vr < 9 && vc >= 0 && vc < 13 && gemhex[vr][vc] != '*'){
                  hex[vr][vc] = 'G';
                }
              }
            }
          }
          break;
        }
      }
      return score_points;
  }

  public static void JoueurTour (char[][] hex, char[][] gemhex, int tour, int[] score_points, Scanner scanner, Random random, Sauvegarder[] save_slots) {

    for (int i = 0; i < 13; i++) {
     System.out.print(i);
    }
    System.out.println();
   
    for (int r = 0; r < 9; r++) {
      for (int c = 0; c < 13; c++) {
        System.out.print(hex[r][c]);
      }
      System.out.println();
    }

    System.out.println("Joeur 1 =" + score_points[0] + "  " + "Joeur 2 =" + score_points[1]);
    System.out.println();

    // debug gemmes

    for (int j = 0; j < 13; j++) {
     System.out.print(j);
    }
    System.out.println();
   
    for (int kr = 0; kr < 9; kr++) {
      for (int kc = 0; kc < 13; kc++) {
        System.out.print(gemhex[kr][kc]);
      }
      System.out.println();
    }

    System.out.print("Joueur " + tour + ", entrez les coordonnees (ligne et colonne): ");
    int r = scanner.nextInt();
    int c = scanner.nextInt();

    if (r < 0 || r >= 9 || c < 0 || c >= 13 || hex[r][c] != '0' && hex[r][c] != 'G') {
      System.out.println("Place invalide, reessayez.");
      JoueurTour(hex, gemhex, tour, score_points, scanner, random, save_slots);
      return;
    }

    hex[r][c] = (tour == 1) ? '1' : '2';

    char joueur = (tour == 1) ? '1' : '2';
    char marque = (tour == 1) ? 'A' : 'B';

    String resultat = DetecterStructures(hex, joueur, marque);
    if (resultat != null) {
      score_points[tour - 1] += VerifierGemmes(hex, gemhex, tour, resultat, random);
    }

    VerifierVoisins(hex, gemhex, joueur, marque, score_points, tour);

    for (int i = 0; i < 13; i++) {
     System.out.print(i);
    }
    System.out.println();
   
    for (r = 0; r < 9; r++) {
      for (c = 0; c < 13; c++) {
        System.out.print(hex[r][c]);
      }
      System.out.println();
    }

    System.out.println("Joeur 1 =" + score_points[0] + "  " + "Joeur 2 =" + score_points[1]);
    System.out.println();

    // debug gemmes

    for (int j = 0; j < 13; j++) {
     System.out.print(j);
    }
    System.out.println();
   
    for (int kr = 0; kr < 9; kr++) {
      for (int kc = 0; kc < 13; kc++) {
        System.out.print(gemhex[kr][kc]);
      }
      System.out.println();
    }

    PauseMenu (hex, gemhex, (tour == 1) ? 2 : 1, score_points, scanner, random, save_slots);
  }

  public static void SauvegarderFichiers(char[][] hex, char[][] gemhex, int tour, int[] score_points, int choix_slot) {

    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter("slot"+choix_slot+".txt"));
      bw.write("tour:"+tour);
      bw.newLine();
      bw.write("scores:"+score_points[0]+","+score_points[1]);
      
      bw.newLine();
      bw.write("hex:");
      bw.newLine();
      for (int r = 0; r<9;r++) {
        bw.write(new String(hex[r]));
        bw.newLine();
      }
      
      bw.write("gemhex:");
      bw.newLine();
      for (int r = 0; r<9;r++) {
        bw.write(new String(gemhex[r]));
        bw.newLine();
      }
      
      bw.close();
      System.out.println("Partie sauvegarde dans slot"+choix_slot+".txt");
    } catch (IOException e) {
      System.out.println("Erreur de sauvegarde: "+e.getMessage());
    }
  }

   public static Sauvegarder ChargeFichiers(int choix_slot) {

    try {
      BufferedReader br = new BufferedReader(new FileReader("slot"+choix_slot+".txt"));

      String ligne = br.readLine();
      String[] partie = ligne.split(":");
      int tour = Integer.parseInt(partie[1]);
      
      String ligne = br.readLine();
      String[] partie = ligne.split(":");
      String[] score = partie[1].split(",");
      int[] score_points = {Integer.parseInt(score[0]), Integer.parseInt(score[1])};

      br.readLine();
      char[][] hex = new char[9][];
      for (int r = 0; r < 9; r++) {
        hex[r] = br.readLine().toCharArray();
      }

      br.readLine();
      char[][] gemhex = new char[9][];
      for (int r = 0; r < 9; r++) {
        gemhex[r] = br.readLine().toCharArray();
      }

      br.close()
      return new Sauvegarder(hex, gemhex, score_points, tour);
        
    } catch (IOException e) {
      System.out.println("Erreur de chargement: "+e.getMessage());
      return null;
    }
  }

  public static Sauvegarder[] Sauvergarde (char[][] hex, char[][] gemhex, int tour, int[] score_points, Sauvegarder[] save_slots, Scanner scanner) {
    
    System.out.println("Quel slot voulez-vous utiliser? (0-4)");
    int choix_slot = scanner.nextInt();
      
    if (save_slots[choix_slot] != null) {
      System.out.println("Slot occupe. Ecraser?");
      System.out.println("0 - Oui");
      System.out.println("1 - Non");
      int conf = scanner.nextInt();
      if (conf == 0) {
        save_slots[choix_slot] = new Sauvegarder(hex, gemhex, score_points, tour);
        SauvegarderFichiers(hex, gemhex, tour, score_points, choix_slot);
        System.out.println("Match auvegarde.");
      } else {
        Sauvergarde(hex, gemhex, tour, score_points, save_slots, scanner);
      }
    } else {
      save_slots[choix_slot] = new Sauvegarder(hex, gemhex, score_points, tour);
      SauvegarderFichiers(hex, gemhex, tour, score_points, choix_slot);
      System.out.println("Match auvegarde.");
      return save_slots;
    }
    return save_slots;
  }

  public static void PauseMenu (char[][] hex, char[][] gemhex, int tour, int[] score_points, Scanner scanner, Random random, Sauvegarder[] save_slots) {
    
    System.out.println();
    System.out.println("Joueur " + tour + ", que voulez-vous faire?");
    System.out.println("0 - Jouer");
    System.out.println("1 - Passer.");
    System.out.println("2 - Sauvegarder ");
    System.out.println("3 - Quitter");
    System.out.println();

    int choix = scanner.nextInt();

    if (choix == 0) {
      JoueurTour(hex, gemhex, tour, score_points, scanner, random, save_slots);
    } else if (choix == 1) {
      JoueurTour(hex, gemhex, (tour == 1) ? 2 : 1, score_points, scanner, random, save_slots);
      passer++;
    } else if (choix == 2) {
      Sauvergarde (hex, gemhex, tour, score_points, save_slots, scanner);
      PauseMenu (hex, gemhex, tour, score_points, scanner, random, save_slots);
    } else if (choix == 3) {
      System.exit(0);
    } else if (choix > 3) {
      PauseMenu (hex, gemhex, tour, score_points, scanner, random, save_slots);
    }
  }

  public static void MenuPrincipal (Scanner scanner, Random random) {

    System.out.println();
    System.out.println("HexaConquest");
    System.out.println();
    System.out.println("0 - Nouvelle partie");
    System.out.println("1 - Charger une partie");
    System.out.println("2 - Quitter");
    System.out.println();

    Sauvegarder[] save_slots = new Sauvegarder[5];    
    int choix = scanner.nextInt();

    if (choix == 0) {
      char[][] hex = Plateu();
      char[][] gemhex = Gemmes(hex, random);
      int[] score_points = {0, 0};
      int passer = 0;
      PauseMenu (hex, gemhex, 1, score_points, scanner, random, save_slots);
      
    } else if (choix == 1) {
      System.out.println();
      System.out.println("Choisir le match (0-4):");

      for (int i = 0; i < 5; i++) {
        java.io.File f = new java.io.File("slot"+i+".txt");
        System.out.println(i + " - " + (f.exists() ? "Match" + (i+1) : "Vide"));
      }
      
      int choix_slot = scanner.nextInt();
      Sauvegarder s = ChargerFichiers(choix_slot);
      
      if (s != null) {
        save_slots[choix_slot] = s;
        PauseMenu (s.save_hex, s.save_gemhex, s.save_tour, s.save_score_points, scanner, random, save_slots);
      } else {
        System.out.println("Slot Vide!");
        MenuPrincipal(scanner, random);
      }
      
    } else if (choix == 2) {
      System.exit(0);
    } else if (choix > 2) {
      MenuPrincipal(scanner, random);
    }
  }

  public static void main(String[] args) {
    Random random = new Random();
    Scanner scanner = new Scanner(System.in);
    MenuPrincipal(sacnner, random);
  }
}
