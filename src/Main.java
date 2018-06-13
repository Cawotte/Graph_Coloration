import java.util.ArrayList;

public class Main {

    public static void main(String argc[]) {

        int nbEchantillon = 300;

        System.out.println("\nCalculs de coloration de Graphes !\n");
        ArrayList<String> listFilenames = new ArrayList<>();

        //On ajoute tout les fichiers qu'on veut lire

        listFilenames.add("queen5_5.txt");
        listFilenames.add("queen7_7.txt");
        listFilenames.add("queen9_9.txt");
        listFilenames.add("queen11_11.txt");
        listFilenames.add("queen13_13.txt");
        listFilenames.add("queen15_15.txt");
        listFilenames.add("crown10.txt");

        for ( String filename : listFilenames ) {


            System.out.println("---------- Graphe \'" + filename + "\' ---------- ");

            Graphe graph = new Graphe(filename);

            //graph.print();

            System.out.println();
            graph.colorationGreedy("croissant");
            graph.colorationGreedy("decroissant");
            graph.colorationGreedyAleatoire(nbEchantillon);

            System.out.println();
            graph.colorationWelshPowell("croissant");
            graph.colorationWelshPowell("decroissant");
            graph.colorationWelshPowellAleatoire(nbEchantillon);

            System.out.println();
            graph.colorationDSAT();

            System.out.println("\n---------- ------------- ----------\n");

        }


    }
}
