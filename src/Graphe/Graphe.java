package Graphe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@SuppressWarnings("Duplicates")

public class Graphe
{

    private String nom;
    private int nbSommets;
    private int nbArcs;

    private HashMap< Integer, ArrayList<Integer> > listeAdjacence;

    /*
        key : Numero du sommet
        value : Listes des sommets voisins.
     */

    public Graphe(String nom, int nbSommets, int nbArcs, HashMap<Integer, ArrayList<Integer>> listeAdjacence) {
        this.nom = nom;
        this.nbSommets = nbSommets;
        this.nbArcs = nbArcs;
        this.listeAdjacence = listeAdjacence;
    }

    public Graphe(String filename) {
        if ( !readGraphe(filename) )
            System.out.println("Erreur lecture fichier!");
    }


    /**
     * Affiche la liste d'adjacence du graphe dans la console.
     */
    public void print() {

        System.out.println("Liste d'adjacence du Graphe " + nom + " :");
        System.out.println("-------------------------------------------");

        //Pour chaque sommets, on récupère ses adjacences et les affiche
        for ( HashMap.Entry<Integer, ArrayList<Integer>> entry : listeAdjacence.entrySet()) {
            int sommet = entry.getKey();
            ArrayList<Integer> voisins = entry.getValue();
            System.out.print(" " + sommet + "  |  ");
            for (Integer voisin : voisins) {
                System.out.print(voisin + " ");
            }
            System.out.println();
        }

        System.out.println("-------------------------------------------");
        System.out.println();
    }

    public void sort() {

        for ( ArrayList<Integer> listeAdj : listeAdjacence.values() ) {
            Collections.sort(listeAdj);
        }
    }


    public boolean readGraphe(String fname) {
        Scanner sc;
        Scanner scLine;

        int som, voisin;
        String line;


        //On essaye d'ouvrir le fichier à lire
        try {
            sc = new Scanner(new File(fname));
        } catch (FileNotFoundException fnfe) {
            return false;
        }

        //On lit le fichier en s'attendant à un format précis.

        //Première ligne, le nom.
        line = sc.nextLine();
        nom = line.substring(5); //On coupe les premiers caractères "Nom: ";
        System.out.println("\'" + nom + "\'");

        //Seconde ligne, orienté Oui/non. On ignore car pas pris en charge
        //System.out.println(sc.nextLine());
        sc.nextLine();

        //Troisième ligne, nbSommets, on le récupère
        while (!sc.hasNextInt())
            sc.next();
        nbSommets = sc.nextInt();
        System.out.println("nbSommets = " + nbSommets);
        sc.nextLine();

        //Quatrième ligne, nbValSommets, pas pris en charge, on skip
        //System.out.println( sc.nextLine() );
        sc.nextLine();

        //Cinquième ligne, nbArcs, on récupère
        while (!sc.hasNextInt())
            sc.next();
        nbArcs = sc.nextInt();
        System.out.println("nbArcs = " + nbArcs);
        sc.nextLine();

        //Sixième ligne, nbValArcs, pas pris en charge
        //System.out.println( sc.nextLine() );
        sc.nextLine();

        //Prochaine ligne, début de la liste des sommets, on a juste besoin de leur nombre donc on peut les ignorer
        //System.out.println("printf sommets : " + sc.nextLine() );
        sc.nextLine();
        for ( int i = 0; i < nbSommets; i++)
            sc.nextLine();

        //Debut de la liste des arcs
        sc.nextLine();

        //On initialise une Hashmap
        listeAdjacence = new HashMap<>();
        //Pour chaque sommet on lui donne une liste d'adjacence vide
        for (int i = 0; i < nbSommets; i++) {
            listeAdjacence.put(i, new ArrayList<>());
        }

        //On les affiche juste pour voir
        /*
        for ( int i = 0; i < nbArcs; i++)
            System.out.println(sc.nextLine());
        */

        for ( int i = 0; i < nbArcs; i++) {

            line = sc.nextLine();
            scLine = new Scanner(line);
            som = scLine.nextInt();
            voisin = scLine.nextInt();

            //System.out.println(som + " " + voisin);

            (listeAdjacence.get(som)).add(voisin);
        }

        System.out.println("---- Fichier lu ! ----\n");


        return true;
    }

    public void isNull() {

        for (int i = 0; i < nbSommets; i++) {
            if ( listeAdjacence.get(i).isEmpty() )
                System.out.println("sommet "+ i + " : Liste vide !");

        }
    }

    //Coloration de Graphe

    /**
     * Renvoie le nombre de couleur d'une coloration du graph par WelshPowell, décroissant ou croissant ou aléatoire en
     * fonction des booléens données en argument.
     * @param croissant
     * @param aleatoire
     * @return
     */
    public int colorationWelshPowell(boolean croissant, boolean aleatoire) {

        /*
        Repérer le degré de chaque sommet.
        Ranger les sommets par ordre de degrés décroissants (dans certains cas plusieurs possibilités).
        Attribuer au premier sommet (A) de la liste une couleur.
        Suivre la liste en attribuant la même couleur au premier sommet (B) qui ne soit pas adjacent à (A).
                Suivre (si possible) la liste jusqu'au prochain sommet (C) qui ne soit adjacent ni à A ni à B.
        Continuer jusqu'à ce que la liste soit finie.
        Prendre une deuxième couleur pour le premier sommet (D) non encore coloré de la liste.
                Répéter les opérations 4 à 7.
        Continuer jusqu'à avoir coloré tous les sommets.
                */


        ArrayList<Integer> sommets = trierSommetsParDegres(croissant, aleatoire, !aleatoire);

        int couleur = 1;

        int[] couleurs = new int[nbSommets];
            //Utile seulement si on veut connaitre la coloration exacte de chaque sommet.

        ArrayList<Integer> sommetsMemeCouleur;
            //On listera les sommets de meme couleurs pour vérifier les adjacences avec tout ses sommets.
        ArrayList<Integer> aRetirer;
            //On retira les sommets colorés de la liste à la fin du parcours pour éviter une erreur d'accès concurrentiel.
        int sommetActuel;

        //Tant que la liste de sommets n'est pas vide
        while ( !sommets.isEmpty() ) {

            sommetActuel = sommets.get(0); //On récupère le 1er sommet
            couleurs[sommetActuel] = couleur; //On lui donne la couleur actuelle
            sommets.remove(0); //On le retire de la liste

            //On vide les listes de sommets coloré et à enlever.
            sommetsMemeCouleur = new ArrayList<>(); aRetirer = new ArrayList<>();
            //Le sommet choisi est coloré.
            sommetsMemeCouleur.add(sommetActuel);

            //Pour chaque sommets restants
            for ( int sommet : sommets ) {
                //Si ce sommet n'est adjacent a aucun sommet de meme couleur
                if ( !sontAdjacents(sommet, sommetsMemeCouleur) ) {
                    //On le colorie, et on modifie nos listes en conséquences.
                    couleurs[sommet] = couleur;
                    sommetsMemeCouleur.add(sommet);
                    aRetirer.add(sommet);
                }
            }
            //On retire les sommets coloriés de la liste.
            sommets.removeAll(aRetirer);

            couleur++;
        }

        //region affichage
        /*
        System.out.print("sommets : ");
        for ( int i = 0; i < nbSommets; i++)
            System.out.print(i + " | ");
        System.out.println();
        System.out.print("degres  : ");
        for ( int i = 0; i < nbSommets; i++)
            System.out.print(degres[i] + " | ");
        System.out.println();
        System.out.print("couleur : ");
        for ( int i = 0; i < nbSommets; i++)
            System.out.print(couleurs[i] + " | ");
        System.out.println(); */
        //endregion

        //Affichage d'informations
        if ( !aleatoire ) {

            String ordre;
            if ( croissant )
                ordre = "croissant";
            else
                ordre = "decroissant";

            System.out.println("Coloration minimale du graphe " + "\'" + nom + "\'" + " par coloration WeshPowell d'ordre " + ordre + " : " + couleur + " couleur.");

        }

        return couleur-1;
    }

    public int colorationWelshPowell(boolean croissant) {
        return colorationWelshPowell(croissant, false);
    }
    /**
     * Execute l'algorithme de WellshPowell nbEchantille fois et affiche une petite statistique dessus.
     * @param nbEchantillon
     */
    public void colorationWelshPowellAleatoire(int nbEchantillon) {

        float moyenne_NbCoul = 0;
        int max_NbCoul = 0;
        int min_NbCoul = 0;
        int nbCouleur;

        for ( int i = 0; i < nbEchantillon; i++) {
            nbCouleur = colorationWelshPowell(false, true);
            if ( min_NbCoul == 0) {
                min_NbCoul = nbCouleur;
                max_NbCoul = nbCouleur;
            }
            else {
                if ( min_NbCoul > nbCouleur )
                    min_NbCoul = nbCouleur;
                else if ( max_NbCoul < nbCouleur)
                    max_NbCoul = nbCouleur;
            }
            moyenne_NbCoul += (float)nbCouleur;
        }

        moyenne_NbCoul = moyenne_NbCoul / (float)nbEchantillon;
        System.out.println("Algorithme de Welsh Powell Aleatoire : ");
        System.out.println("\tTaille échantillon : " + nbEchantillon);
        System.out.println("\tNombre de couleurs moyenne = " + moyenne_NbCoul);
        System.out.println("\tNombre de couleurs minimal atteint = " + min_NbCoul);
        System.out.println("\tNombre de couleurs maximal atteint = " + max_NbCoul);
    }

    public int colorationGreedy(boolean croissant, boolean aleatoire) {

        int couleurMax = -1;

        ArrayList<Integer> sommets = trierSommetsParDegres(croissant, aleatoire, !aleatoire);

        HashMap< Integer, ArrayList<Integer> > listeColoriees = new HashMap<>();
        for (int i = 0; i < nbSommets; i++) {
            sommets.add(i);
            listeColoriees.put(i, new ArrayList<>());
        }

        //On assigne la première couleur valide à chaque sommet de la liste.
        for ( int sommet : sommets ) {
            int couleur = 1;
            while ( sontAdjacents(sommet, listeColoriees.get(couleur) ) ) {
                couleur++;
            }
            listeColoriees.get(couleur).add(sommet);
            if ( couleurMax < couleur ) {
                couleurMax = couleur;
            }
        }

    //Affichage d'informations
        if ( !aleatoire ) {

            String ordre;
            if ( croissant )
                ordre = "croissant";
            else
                ordre = "decroissant";

            System.out.println("Coloration minimale du graphe " + "\'" + nom + "\'" + " par coloration Greedy d'ordre " + ordre + " : " + couleurMax + " couleur.");

        }
        return couleurMax;

    }

    public int colorationGreedy(boolean croissant) {
        return colorationGreedy(croissant, false);
    }

    public void colorationGreedyAleatoire(int nbEchantillon) {
        float moyenne_NbCoul = 0;
        int max_NbCoul = 0;
        int min_NbCoul = 0;
        int nbCouleur;

        for ( int i = 0; i < nbEchantillon; i++) {
            nbCouleur = colorationGreedy(false, true);
            if ( min_NbCoul == 0) {
                min_NbCoul = nbCouleur;
                max_NbCoul = nbCouleur;
            }
            else {
                if ( min_NbCoul > nbCouleur )
                    min_NbCoul = nbCouleur;
                else if ( max_NbCoul < nbCouleur)
                    max_NbCoul = nbCouleur;
            }
            moyenne_NbCoul += (float)nbCouleur;
        }

        moyenne_NbCoul = moyenne_NbCoul / (float)nbEchantillon;
        System.out.println("Algorithme Greedy Aleatoire : ");
        System.out.println("\tTaille échantillon : " + nbEchantillon);
        System.out.println("\tNombre de couleurs moyenne = " + moyenne_NbCoul);
        System.out.println("\tNombre de couleurs minimal atteint = " + min_NbCoul);
        System.out.println("\tNombre de couleurs maximal atteint = " + max_NbCoul);
    }

    private ArrayList<Integer> trierSommetsParDegres(boolean croissant, boolean aleatoire, boolean affichageDebug) {

        int[] degres = new int[nbSommets];
        for (int i = 0; i < nbSommets; i++)
            degres[i] = listeAdjacence.get(i).size();

        ArrayList<Integer> sommets = new ArrayList<>();
        sommets.add(0);

        for (int i = 1; i < nbSommets; i++) {

            int j = 0;
            if ( !aleatoire ) {
                //Si par ordre croissant
                if ( croissant ) {
                    while ( degres[sommets.get(j)] < degres[i] && j < sommets.size()-1 )
                        j++;
                } //si décroissant
                else {
                    while ( degres[sommets.get(j)] > degres[i] && j < sommets.size()-1 )
                        j++;
                }
            }
            if ( j == sommets.size()-1 )
                sommets.add(i);
            else
                sommets.add(j, i);
        }
        //Si on trie par ordre aléatoire on mélange juste toute les valeurs.
        if ( aleatoire )
            Collections.shuffle(sommets);

        if (affichageDebug) {
            String bar;
            System.out.print("sommets : ");
            for (int i = 0; i < sommets.size(); i++) {
                bar = " | ";
                if ( degres[sommets.get(i)] >= 10 && i < 10 )
                    bar += "  " ;
                if ( i >= 100 )
                    bar += " " ;
                if ( i >= 1000 )
                    bar += " " ;
                System.out.print(sommets.get(i) + bar);
            }
            System.out.println();
            System.out.print("degres  : ");
            bar = " | ";
            for (int i = 0; i < sommets.size(); i++) {
                bar = " | ";
                if ( degres[sommets.get(i)] >= 10 && i < 10 )
                    bar += "  " ;
                if ( i >= 100 )
                    bar += " " ;
                if ( i >= 1000 )
                    bar += " " ;
                System.out.print(degres[sommets.get(i)] + bar);
            }
            System.out.println();
        }

        return sommets;
    }

    /**
     * Renvoie vrai si les sommets A et B sont adjacents, faux sinon.
     * @param sommetA
     * @param sommetB
     * @return
     */
    public boolean sontAdjacents(int sommetA, int sommetB) {
        for ( int som : listeAdjacence.get(sommetA) ) {
            if ( som == sommetB )
                return true;
        }
        return false;
    }

    /**
     * Renvoie vrai si le sommets donné est adjacent à un des sommets de la liste donnée, false sinon.
     */
    public boolean sontAdjacents(int sommet, ArrayList<Integer> sommets) {

        if (sommets.isEmpty())
            return false;

        for ( int sommetB : sommets) {
            for ( int som : listeAdjacence.get(sommet) ) {
                if ( som == sommetB )
                    return true;
            }
        }
        return false;
    }

    //Getters/Setters


    public HashMap<Integer, ArrayList<Integer>> getListeAdjacence() {
        return listeAdjacence;
    }
}
