package cz.barush.hdsp.wtf;

import cz.barush.hdsp.entity.Supermarket;
import gurobi.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * mergedPriceList - 1. row - GRAINS, 2. row - VEGETABLES, FRUIT, 3. row - DIARY, MEAT, 4. row - OILS, SWEET
 */

public class TSP {
//    public static List<List<Integer>> gramsToBuyBestSolution = new ArrayList<>();
//    public static GRBModel model;
//    public static List<Integer> bestSupermarketCombination = new ArrayList<>();
//    public static int[][] distances;
//    static List<Supermarket> supermarketsNearby;
//    static GRBVar[][] edges;
//
//    public TSP(GRBVar[][] newEdges) {
//        this.edges = newEdges;
//    }
//
//    //TSP
//    private static List<List<Supermarket>> TSPThroughAllUniqueSubsets(List<List<Supermarket>> uniqueSets) {
//
//    }
//
//    protected static int[] findSubTour(double[][] sol) {
//        int n = sol.length;
//        boolean[] seen = new boolean[n];
//        int[] tour = new int[n];
//        int bestind, bestlen;
//        int i, node, len, start;
//
//        for (i = 0; i < n; i++) seen[i] = false;
//
//        start = 0;
//        bestlen = n + 1;
//        bestind = -1;
//        node = 0;
//        while (start < n) {
//            for (node = 0; node < n; node++) if (!seen[node]) break;
//            if (node == n) break;
//            for (len = 0; len < n; len++) {
//                tour[start + len] = node;
//                seen[node] = true;
//                for (i = 0; i < n; i++) {
//                    if (sol[node][i] > 0.5 && !seen[i]) {
//                        node = i;
//                        break;
//                    }
//                }
//                if (i == n) {
//                    len++;
//                    if (len < bestlen) {
//                        bestlen = len;
//                        bestind = start;
//                    }
//                    start += len;
//                    break;
//                }
//            }
//        }
//
//        int result[] = new int[bestlen];
//        for (i = 0; i < bestlen; i++) result[i] = tour[bestind + i];
//        return result;
//    }
//
//    protected int[] solveTsp(List<Integer> indecesOfSupermarkets) throws GRBException {
//        int[] optimalTour = new int[indecesOfSupermarkets.size() + 2];
//        GRBEnv env = new GRBEnv();
//        env.set(GRB.IntParam.LazyConstraints, 1);
//        GRBModel model = new GRBModel(env);
//
//        edges = new GRBVar[optimalTour.length + 1][optimalTour.length + 1];
//        for (int i = 0; i < optimalTour.length + 1; i++) {
//            for (int j = 0; j < optimalTour.length + 1; j++) {
//                edges[i][j] = model.addVar(0.0, 1.0, 0, GRB.BINARY, "x" + i + "_" + j);
//            }
//        }
//
//        model.update();
//
//        //OBJECTIVE
//        GRBLinExpr obj = new GRBLinExpr();
//        for (int i = 0; i < optimalTour.length + 1; i++)
//            for (int j = 0; j < optimalTour.length + 1; j++)
//                obj.addTerm(distances[i][j], edges[i][j]);
//        model.setObjective(obj, GRB.MINIMIZE);
//
//
//        // Degree-2 constraints
//        for (int i = 0; i < optimalTour.length + 1; i++) {
//            GRBLinExpr expr = new GRBLinExpr();
//            for (int j = 0; j < optimalTour.length + 1; j++) expr.addTerm(1.0, edges[i][j]);
//            model.addConstr(expr, GRB.EQUAL, 1, "deg1_" + String.valueOf(i));
//        }
//
//        for (int i = 0; i < optimalTour.length + 1; i++) {
//            GRBLinExpr expr = new GRBLinExpr();
//            for (int j = 0; j < optimalTour.length + 1; j++) expr.addTerm(1.0, edges[j][i]);
//            model.addConstr(expr, GRB.EQUAL, 1, "deg2_" + String.valueOf(i));
//        }
//
//        // Forbid edge from node back to itself
//        for (int i = 0; i < optimalTour.length + 1; i++) edges[i][i].set(GRB.DoubleAttr.UB, 0.0);
//
//        model.setCallback(new TSP(edges));
//        model.optimize();
//
//        if (model.get(GRB.IntAttr.SolCount) > 0) {
//            int[] tour = findSubTour(model.get(GRB.DoubleAttr.X, edges));
//            assert tour.length == optimalTour.length + 1;
//
//            //System.out.print("Tour: ");
//            int[] tourWithoutDummyNode = new int[tour.length - 1];
//            ArrayList<Integer> tempList = new ArrayList<>();
//            for (int i = 0; i < tour.length; i++) tempList.add(tour[i]);
//            int startIndex = tempList.indexOf(numberOfStripes);
//            int index = 0;
//            for (int i = startIndex + 1; i < tour.length; i++) {
//                tourWithoutDummyNode[index++] = tour[i];
//                //System.out.print(tour[i] + " ");
//            }
//            for (int i = 0; i < startIndex; i++) {
//                tourWithoutDummyNode[index++] = tour[i];
//                //System.out.print(tour[i] + " ");
//            }
//            //System.out.println();
//            optimalTour = Arrays.copyOf(tourWithoutDummyNode, tourWithoutDummyNode.length);
//        }
//
//        // Dispose of model and environment
//        model.dispose();
//        env.dispose();
//
//        return optimalTour;
//    }
//
//    @Override
//    protected void callback() {
//        try {
//            if (where == GRB.CB_MIPSOL) {
//                // Found an integer feasible solution - does it visit every node?
//                int n = edges.length;
//                int[] tour = findSubTour(getSolution(edges));
//                if (tour.length < n) {
//                    // Add subtour elimination constraint
//                    GRBLinExpr expr = new GRBLinExpr();
//                    for (int i = 0; i < tour.length; i++)
//                        expr.addTerm(1.0, edges[tour[i]][tour[(i + 1) % tour.length]]);
//                    addLazy(expr, GRB.LESS_EQUAL, tour.length - 1);
//                }
//            }
//        } catch (GRBException e) {
//            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}