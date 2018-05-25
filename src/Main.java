import Graphe.Graphe;

public class Main {

    public static void main(String argc[]) {

        String filename = "queen15_15.txt";

        Graphe graph = new Graphe(filename);


        graph.print();
        //graph.sort();
        graph.print();


    }
}
