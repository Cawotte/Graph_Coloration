import Graphe.Graphe;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String argc[]) {


        float moyenneColorationAleatoire;
        int nbColorationAleatoire;
        int miniColorationAleatoire;
        int maxColorationAleatoire;
        int nbColorationAleatoireMax = 100;
        int nbCouleur;

        String filename = "queen5_5.txt";

        Graphe graph = new Graphe(filename);

        //graph.print();

        graph.colorationWelshPowell(true);
        graph.colorationWelshPowell(false);

        graph.colorationWelshPowellAleatoire(200);

        System.out.println();

        graph.colorationGreedy(true);
        graph.colorationGreedy(false);
        graph.colorationGreedyAleatoire(200);


    }
}
