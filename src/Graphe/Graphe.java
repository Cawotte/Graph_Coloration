package Graphe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

    //Getters/Setters


    public HashMap<Integer, ArrayList<Integer>> getListeAdjacence() {
        return listeAdjacence;
    }
}
