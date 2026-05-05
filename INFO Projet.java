import java.util.ArrayList;

public class ProjetInfo {

  public class Structure {

    String nom;
    int[][] forme;

    Structure(String nom, int[][] forme) {
      this.nom = nom;
      this.forme = forme;
    }
    
  }

  public static void Plateu() {

    // Une liste pour hexagone
    char[][] hex = new char[5][9];

    for (int i = 0 ; i < 5; i++) {
      for (int j = 0; j < 9; j++) {

        hex[i][j] = ' ';
        
      }
    }

    for (int r = 0; r < 5; r++) {
      int espace = Math.abs(2 - r);
      int place = 5 - espace;
      int debut = espace;

      for (int k = 0; k < place; k++) {
        hex[r][debut + k * 2] = '0';
      }
    }

    ///     0 0 0
    ///    0 0 0 0
    ///   0 0 0 0 0
    ///    0 0 0 0
    ///     0 0 0
    
  }

  public static ArrayList<Structure> BibliotequeStructures() {

    // listes avec les structures possibles

    ArrayList<Structure> bibliotheque = new ArrayList<>()

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
      "Triangle Inverse",
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
        {1,2},
        {1,4},
        {2,1},
        {2,3}
      }
    );
    bibliotheque.add(etoile);

    ///     * *
    ///    * * * 
    ///     * *
    
    return bibliotheque;
  }

  // on peut presented les deux joueurs comme ca:
  // il y a deux fonctions (par exemple) - JOUEURTOUR (fonction recursive), RESULTATS
  // si c'est au tour de joueur 1, int tour = 1
  // puis tour = 2 si c'est joueur 2
  // en fin de tour, joueurtour appele RESULTATS qui calcule quel joueur faire les mouvements et forme les figures

  public static void joueurtour (){

  }
  public static void resultats (){
 
    }
  }

  public static void main(Stringp[] args) {
    
   for (int i = 0; i < 9; i++) {
     System.out.print(i);
   }
     System.out.println();
   
    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 9; c++) {
        System.out.print(hex[r][c]);
      }
      System.out.println();
    }
  }
}        

/// *           
///  *           
///   *          
///    *     
///     *
