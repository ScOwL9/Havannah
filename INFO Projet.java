import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class ProjetInfo {

  public class Structure {

    String nom;
    int[][] forme;

    Structure(String nom, int[][] forme) {
      this.nom = nom;
      this.forme = forme;
    }
    
  }

  public static char[][] Plateu() {
    char[][] hex = new char[11][13];
    for (int i = 0; i < 11; i++)
        for (int j = 0; j < 13; j++)
            hex[i][j] = ' ';

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
    for (int i = 0; i < 11; i++)
        for (int j = 0; j < 13; j++)
            gemhex[i][j] = ' ';

    int gemmescaches = 0;

    while (gemmescaches < 10) {
      int r = random.nextInt(9);
      int c = random.nextInt(9);
      if (hex[r][c] == '0' && gemhex[r][c] == ' ') {
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
    ///    * * * 
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
            if (nr < 0 || nr >= 9 || nc < 0 || nc >=13 || hex[nr][nc] != joueur) {
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

  public static void VerifierVoisins (char[][] hex, char marque) {
    for (int r = 0; r<9; r++) {
      for (int c = 0; c<13; c++) {
        if (hex[r][c] == marque) {

          int[][] voisinsA = {
            {r, c-2}, {r, c+2}, {r-1,c+1}, {r-1,c-1}, {r+1,c-1}, {r+1,c+1}
          };

          for (int[] v:voisins) {
            int vr = v[0];
            int vc = v[1];
            if (vr > 0 && vr < 9 && vc > 0 && vc < 13 && hex[vr][vc] != ' ' && hex[vr][vc] != '0' && hex[vr][vc] != marque) {
              hex[vr][vc] = marque;
            }
          }
        }
      }
    }
  }
  
  public static int VerifierGemmes(char[][] gemhex, String resultat, Random random) {
    if (resultat == null) return 0;

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
            score_points += 1; gemhex[sr][sc] = ' '; 
          } else if (gemhex[sr][sc] == 'R') { 
            score_points += 2; gemhex[sr][sc] = ' '; 
          }
        }

        for (int[] coord : s.forme) {
          int sr = r + coord[0];
          int sc = c + coord[1];
          int[][] voisins = {
            {sr,sc-2},{sr,sc+2},{sr-1,sc+1},{sr-1,sc-1},{sr+1,sc-1},{sr+1,sc+1}
          };

          if (nom.equals("Etoile")) {
            if (gemhex[r+1][c+2] != ' ') gemhex[r+1][c+2] = 'G';

          } else if (nom.contains("Triangle")) {
            for (int i = random.nextInt(6), attempts = 0; attempts < 6; i = (i+1) % 6, attempts++) {
            int vr = voisins[i][0];
            int vc = voisins[i][1];
            if (vr >= 0 && vr < 9 && vc >= 0 && vc < 13 && gemhex[vr][vc] != ' ') {
              gemhex[vr][vc] = 'G';
              break;
            }
          }

          } else if (nom.contains("Ligne")) {
            for (int[] v : voisins) {
              int vr = v[0];
              int vc = v[1];
              if (vr >= 0 && vr < 9 && vc >= 0 && vc < 13 && gemhex[vr][vc] != ' ')
                gemhex[vr][vc] = 'G';
              }
            }
          }
        break;
      }
    }
    return score_points;
  }

  public static void JoueurTour (char[][] hex, char[][] gemhex, int tour, int[] score_points, Scanner scanner, Random random){

    for (int r = 0; r<9; r++) {
      for (int c = 0; c<13; c++) {
        System.out.print(hex[r][c]);
      }
      System.out.println();
    }

    System.out.print("Joueur " + tour + ", entrez les coordonnees (ligne et colonne): ");
    int r = scanner.nextInt();
    int c = scanner.nextInt();

    if (r < 0 || r >= 9 || c < 0 || c >= 13 || hex[r][c] != '0') {
      System.out.println("Place invalide, reessayez.");
      JoueurTour(hex, gemhex, tour, score_points, scanner, random);
      return;
    }

    hex[r][c] = (tour == 1) ? '1' : '2';

    char joueur = (tour == 1) ? '1' : '2';
    char marque = (tour == 1) ? 'A' : 'B';

    String resultat = DetecterStructures(hex, joueur, marque);
    VerifierVoisins(hex, marque);
    score_points[tour - 1] += VerifierGemmes(gemhex, resultat, random);

    JoueurTour(hex, gemhex, (tour == 1) ? 2 : 1, score_poitns, scanner, random);
  }

  public static void main(String[] args) {

    Random random = new Random();
    Scanner scanner = new Scanner(System.in);

    char[][] hex = Plateu();
    char[][] gemhex = Gemmes(hex, random);
    int[] score_points = {0, 0};
    
    JoueurTour(hex, gemhex, 1, score_points, scanner, random);
    
   for (int i = 0; i < 13; i++) {
     System.out.print(i);
   }
   System.out.println();
   
    for (int r = 0; r < 7; r++) {
      for (int c = 0; c < 13; c++) {
        System.out.print(hex[r][c]);
      }
      System.out.println();
    }
  }
}
