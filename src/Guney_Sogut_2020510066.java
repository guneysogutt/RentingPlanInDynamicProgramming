import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Guney_Sogut_2020510066 {



    // read the given file and assign the elements into an array
    public static int[]  readFile(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);

        int counter = -1;

        while (br.readLine()!=null) {
            counter++;
        }

        int[] array = new int[counter];

        fr = new FileReader(fileName);
        br = new BufferedReader(fr);

        br.readLine();//skip the header
        for(int i = 0; i < array.length; i++) {
            String line = br.readLine();

            array[i] = Integer.parseInt(line.substring(line.indexOf("\t") + 1));
        }

        return array;
    }


    public static void DP(int[] playerSalary, int[] yearlyPlayerDemand, int n, int p, int c) {

        int columnSize = 0;
        for(int i = 0; i < n; i++)
            columnSize += yearlyPlayerDemand[i]; // DP arrays column size is the total demand in the given years

        int[][] DP = new int[n + 1][columnSize + 1]; // initialize the array

        for(int i = 1; i <= columnSize; i++){ // insert the player salaries into the array
            DP[0][i] = playerSalary[i-1];
        }

        // first year is a special case that create the club initially(you have no players before that year)
        int demand = yearlyPlayerDemand[0]; // hold the demand for the first year
        for(int i = 0 ; i <= columnSize; i++) {
            if(demand > p){
                DP[1][i] = (demand + i - p) * c + DP[0][i];
            }
            else{
                DP[1][i] = DP[0][i];
            }
        }


       // fill the DP table
        for(int year = 2; year <= n; year++){ // fill the array starting from 2 year because of mentioned above
             demand = yearlyPlayerDemand[year-1];// hold the current demand year by year
            for(int column = 0; column <= columnSize; column++){ // iterate the player numbers column by column
                int requiredPlayers; // holds the required players array

                if (demand > p){ // if the demand is grater than the max players to be promoted
                    requiredPlayers = demand + column - p; // calculate the required players for the scenario
                    ArrayList<Integer> totalCosts = new ArrayList<Integer>();
                    for(int i = 0; i <= column + 1; i++){ // iterate the columns for compare and calculate for the cases
                        if(requiredPlayers < 0||i == columnSize-1)
                            break;
                        int totalCost = (c * requiredPlayers) + DP[year-1][i]; // get the total cost
                        totalCost += DP[0][column]; // add salary for each scenario
                        totalCosts.add(totalCost); // add the value to the array
                        requiredPlayers--;
                    }
                    // find the minimum cost for the year then insert the value to the DP array
                    int minValue = Collections.min(totalCosts);
                    DP[year][column] = minValue;
                }
                else{ // if the demand less than the number of max players to be involved
                    requiredPlayers = column + demand; // calculate the required players
                    boolean check = true; // create a flag to check the scenario that required players greater than the p or less than the p
                    if (requiredPlayers > p)
                        check = false;
                    ArrayList<Integer> totalCosts = new ArrayList<Integer>();

                    for(int i = 0; i <= column + 1; i++){ // iterate the columns for compare and calculate the min cost
                        if(requiredPlayers < 0 ||i == columnSize-1)
                            break;
                        int totalCost;

                        if(check){ // if required players less than the max players to be promoted
                            totalCost = DP[year-1][i];
                        }
                        else{  // if required players greater than the max players to be promoted
                            int coachCount = requiredPlayers - p; // calculate the coach amount
                            totalCost = coachCount * c + DP[year-1][i];
                        }
                        totalCost += DP[0][column]; // add the salary
                        totalCosts.add(totalCost); // add the value to the array
                        requiredPlayers--;
                    }
                        // calculate the min value then insert it to the DP array
                        int minValue = Collections.min(totalCosts);
                        DP[year][column] = minValue;
                }
            }
        }

        // print the min cost
        System.out.println("The minimum cost for the given values : " + DP[n][0]);
    }


    public static void main(String[] args) throws IOException {
        int n = 3, p = 5, c = 5; // initial values


        //create the demand and salary arrays
        int[] playerSalary = readFile("players_salary.txt");
        int[] yearlyPlayerDemand = readFile("yearly_player_demand.txt");


        // call the function
        DP(playerSalary,yearlyPlayerDemand,n,p,c);

    } // end main
}
