import Graphe.Graphe;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String argc[]) {


        String filename = "queen5_5.txt";

        Graphe graph = new Graphe(filename);

        //graph.print();
        System.out.println("Graphe \'" + graph.getNom() + "\' :");

        graph.colorationWelshPowell("croissant");
        graph.colorationWelshPowell("decroissant");

        graph.colorationWelshPowellAleatoire(100);

        System.out.println();

        graph.colorationGreedy("croissant");
        graph.colorationGreedy("decroissant");
        graph.colorationGreedyAleatoire(100);


        System.out.println();

        graph.colorationDSAT();


    }
}
