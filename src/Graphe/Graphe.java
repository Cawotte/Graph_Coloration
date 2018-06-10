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

    private boolean affichageDebug = false;
    private boolean avecTempsExecution = true;

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
     * Renvoie le nombre de couleur d'une coloration du graph par WelshPowell, avec un tri de sommet selon le mode donnée :
     * "croissant", "decroissant", "aleatoire"
     * @return
     */
    public int colorationWelshPowell(String mode) {

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

        boolean croissant = true;
        boolean aleatoire = false;

        if ( mode.equals("aleatoire") )
            aleatoire = true;
        else if ( mode.equals("decroissant") )
            croissant = false;

        ArrayList<Integer> sommets = trierSommetsParDegres(mode);

        int couleur = 0;

        int[] couleurs = new int[nbSommets];
            //Utile seulement si on veut connaitre la coloration exacte de chaque sommet.

        ArrayList<Integer> sommetsMemeCouleur;
            //On listera les sommets de meme couleurs pour vérifier les adjacences avec tout ses sommets.
        ArrayList<Integer> aRetirer;
            //On retira les sommets colorés de la liste à la fin du parcours pour éviter une erreur d'accès concurrentiel.
        int sommetActuel;

        //Tant que la liste de sommets n'est pas vide
        while ( !sommets.isEmpty() ) {

            couleur++;

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

        }

        //Affichage d'informations
        if ( !aleatoire )
            System.out.println("Coloration minimale du graphe " + "\'" + nom + "\'" + " par coloration WeshPowell d'ordre " + mode + " : " + couleur + " couleur.");

        return couleur;
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
            nbCouleur = colorationWelshPowell("aleatoire");
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

    /**
     *
     * Renvoie le nombre de couleur d'une coloration du graph Greedy, avec un tri de sommet selon le mode donnée :
     * "croissant", "decroissant", "aleatoire"
     * @param mode
     * @return
     */
    public int colorationGreedy(String mode) {

        //mode croissant par défaut.
        boolean croissant = true;
        boolean aleatoire = false;

        if ( mode.equals("aleatoire") )
            aleatoire = true;
        else if ( mode.equals("decroissant") )
            croissant = false;

        int couleurMax = -1;

        ArrayList<Integer> sommets = trierSommetsParDegres(mode);

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

            System.out.println("Coloration minimale du graphe " + "\'" + nom + "\'" + " par coloration Greedy d'ordre " + mode + " : " + couleurMax + " couleur.");

        }
        return couleurMax;

    }


    public void colorationGreedyAleatoire(int nbEchantillon) {
        float moyenne_NbCoul = 0;
        int max_NbCoul = 0;
        int min_NbCoul = 0;
        int nbCouleur;

        for ( int i = 0; i < nbEchantillon; i++) {
            nbCouleur = colorationGreedy("aleatoire");
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

    /**
     * Renvoie le nombre de couleur d'une coloration du graph par DSAT
     * @return
     */
    public int colorationDSAT() {

        /*
        1. Ordonner les sommets par ordre décroissant de degrés.
        2. Colorer un sommet de degré maximum avec la couleur 1.
        3. Choisir un sommet avec DSAT maximum. En cas d'égalité, choisir un sommet de degré maximal.
        4. Colorer ce sommet avec la plus petite couleur possible
        5. Si tous les sommets sont colorés alors stop. Sinon aller en 3.
         */

        int dsat;
        int dsatMax;
        int sommetDsatMax;
        int couleurMin;
        int couleurMax = 1;

        ArrayList<Integer> sommetsMemeDsatMax = new ArrayList<>();

        int[] couleurs = new int[nbSommets];
        for (int i = 0; i < nbSommets; i++)
            couleurs[i] = 0;

        //1. Ordonner les sommets par ordre décroissant de degrés.
        ArrayList<Integer> sommets = trierSommetsParDegres("decroissant");

        ArrayList<Integer> sommetsColories = new ArrayList<>();

        //2. Colorer un sommet de degré maximum avec la couleur 1.
        couleurs[sommets.get(0)] = 1;

        sommetsColories.add(sommets.get(0));
        sommets.remove(0);

        //3. Choisir un sommet avec DSAT maximum. En cas d'égalité, choisir un sommet de degré maximal.
        while ( !sommets.isEmpty() ) {


            //On calcule le DSAT de chaque sommet restants
            dsatMax = 0;
            sommetsMemeDsatMax = new ArrayList<>();
            for ( int som : sommets ) {

                //Calcul DSAT.
                ArrayList<Integer> couleursDifferente = new ArrayList<>();
                for ( int sommet : listeAdjacence.get(som) ) {
                    if ( !couleursDifferente.contains(couleurs[sommet]) && couleurs[sommet] != 0 )
                        couleursDifferente.add(couleurs[sommet]);
                }
                dsat = couleursDifferente.size();

                //Si c'est un nouveau maximum, on l'enregistre
                if ( dsat > dsatMax ) {
                    dsatMax = dsat;
                    sommetsMemeDsatMax = new ArrayList<>();
                    sommetsMemeDsatMax.add(som);
                }
                //On garde une liste de tout les sommets de dsatMax de meme degré.
                else if ( dsat == dsatMax )
                    sommetsMemeDsatMax.add(som);
            }

            //On prend ce le sommets de DsatMax de plus haut degré.
            sommetDsatMax = sommetsMemeDsatMax.get(0);
            for ( int som : sommetsMemeDsatMax ) {
                if ( degre(sommetDsatMax) < degre(som) )
                    sommetDsatMax = som;
            }

            //4. Colorier ce sommet avec la plus petite couleur possible
            couleurMin = 1;
                //On liste les couleurs des voisins
            ArrayList<Integer> couleursDifferente = new ArrayList<>();
            for ( int voisin : listeAdjacence.get(sommetDsatMax) ) {
                if ( !couleursDifferente.contains(couleurs[voisin]) && couleurs[voisin] != 0 )
                    couleursDifferente.add(couleurs[voisin]);
            }
            //On récupère la valeur minimum non présente dans la liste
            couleurMin = 1;
            while ( couleursDifferente.contains(couleurMin) )
                couleurMin++;

            couleurs[sommetDsatMax] = couleurMin;
            if ( couleurMin > couleurMax )
                couleurMax = couleurMin;

            sommets.remove(new Integer(sommetDsatMax));
        }

        //Affichage
        System.out.println("Coloration minimale du graphe " + "\'" + nom + "\'" + " par coloration DSAT : " + couleurMax + " couleur.");

        return couleurMax;
    }


    /**
     * Trie les sommets selon le mode donné : "croissant", "decroissant", "aleatoire".
     * Si affichageDebug est sur vrai, affiche les résultats.
     * @param mode
     * @return
     */
    private ArrayList<Integer> trierSommetsParDegres(String mode) {

        //mode croissant par défaut.
        boolean croissant = true;
        boolean aleatoire = false;

        if ( mode.equals("aleatoire") )
            aleatoire = true;
        else if ( mode.equals("decroissant") )
            croissant = false;

        ArrayList<Integer> sommets = new ArrayList<>();
        sommets.add(0);

        for (int i = 1; i < nbSommets; i++) {

            int j = 0;
            if ( !aleatoire ) {
                //Si par ordre croissant
                if ( croissant ) {
                    while ( degre(sommets.get(j)) < degre(i) && j < sommets.size()-1 )
                        j++;
                } //si décroissant
                else {
                    while ( degre(sommets.get(j)) > degre(i) && j < sommets.size()-1 )
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
            boolean petitDegre, petitNombre;
            String bar;
            System.out.print("sommets : ");
            for (int i = 0; i < sommets.size(); i++) {
                bar = " | ";
                petitDegre = degre(sommets.get(i)) < 10;
                petitNombre = sommets.get(i) < 10;
                if ( !petitDegre && petitNombre )
                    bar = " " + bar ;
                System.out.print(sommets.get(i) + bar);
            }
            System.out.println();
            System.out.print("degres  : ");
            for (int i = 0; i < sommets.size(); i++) {
                bar = " | ";
                petitDegre = degre(sommets.get(i)) < 10;
                petitNombre = sommets.get(i) < 10;
                if ( petitDegre && !petitNombre )
                    bar = " " + bar ;
                System.out.print(degre(sommets.get(i)) + bar);
            }
            System.out.println();
        }

        return sommets;
    }

    private int degre(int sommet) {
        return listeAdjacence.get(sommet).size();
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


    public String getNom() {
        return nom;
    }

    public void setAffichageDebug(boolean affichageDebug) {
        this.affichageDebug = affichageDebug;
    }

    public HashMap<Integer, ArrayList<Integer>> getListeAdjacence() {
        return listeAdjacence;
    }
}
