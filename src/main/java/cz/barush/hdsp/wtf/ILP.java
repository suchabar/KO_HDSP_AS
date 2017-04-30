package cz.barush.hdsp.wtf;

import cz.barush.hdsp.entity.Food;
import gurobi.*;

import java.util.ArrayList;
import java.util.List;

public class ILP {
    private static final int CARBS_KJ_G = 17;
    private static final int PROTEINS_KJ_G = 17;
    private static final int FAT_KJ_G = 38;
    private static final int FIBRES_KJ_G = 8;

    private static final double CARBS_RATIO = 0.4;
    private static final double PROTEINS_RATIO = 0.3;
    private static final double FAT_RATIO = 0.25;
    private static final double FIBRES_RATIO = 0.05;

    private static final double KCAL_TO_KJ = 4.2;

    public static List<List<Integer>> gramsToBuyBestSolution = new ArrayList<>();
    public static GRBModel model;
    public static List<Integer> bestSupermarketCombination = new ArrayList<>();
    public static int[][] distances;
    //static List<Supermarket> supermarketsNearby;
    static GRBVar[][] edges;

    public ILP(GRBVar[][] newEdges) {
        this.edges = newEdges;
    }

    public static List<List<Integer>> solveStiglersProblem(List<List<Food>> nutritionTable, int[] balancedNutrients) throws GRBException {
        GRBEnv env = new GRBEnv();
        model = new GRBModel(env);
        List<List<GRBVar>> X = new ArrayList<>();
        //addVar(lowerBound, upperBound, objectiveCoeff, variableType, name)
        for (int i = 0; i < X.size(); i++) {
            for (int j = 0; j < X.get(i).size(); j++) {
                if (!nutritionTable.get(i).get(j).isSelected())
                    X.get(i).add(j, model.addVar(0, 0, 1, GRB.INTEGER, "x_" + i + "_" + j));
                else X.get(i).add(j, model.addVar(0, 50000, 1, GRB.INTEGER, "x_" + i + "_" + j));
            }
        }

        model.update();

        //OBJECTIVE
        GRBLinExpr obj = new GRBLinExpr();
        for (int i = 0; i < X.size(); i++)
            for (int j = 0; j < X.get(i).size(); j++)
                obj.addTerm(nutritionTable.get(i).get(j).getMergedPrice(), X.get(i).get(j));
        model.setObjective(obj, GRB.MINIMIZE);

        //CONDITIONS
        GRBLinExpr cons1;
        double[] ratioBalancesLowerBound = {0.35, 0.25, 0.15, 0.05};
        double[] ratioBalancesUpperBound = {0.45, 0.35, 0.25, 0.15};

        //GO THROUGH ALL FOOD - Iterate i. set of food
        for (int i = 0; i < nutritionTable.size(); i++) {
            //Energy
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++) {
                cons1.addTerm(nutritionTable.get(i).get(j).getEnergy(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * balancedNutrients[0], "balanceG_Energy_i_" + i);
            model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * balancedNutrients[0], "balanceL_Energy_i_" + i);

            //Carbs
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++) {
                cons1.addTerm(nutritionTable.get(i).get(j).getCarbs(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * balancedNutrients[1], "balanceG_Carbs_i_" + i);
            model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * balancedNutrients[1], "balanceL_Carbs_i_" + i);

            //Proteins
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++) {
                cons1.addTerm(nutritionTable.get(i).get(j).getProteins(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * balancedNutrients[2], "balanceG_Proteins_i_" + i);
            model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * balancedNutrients[2], "balanceL_Proteins_i_" + i);

            //Fat
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++) {
                cons1.addTerm(nutritionTable.get(i).get(j).getFats(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * balancedNutrients[3], "balanceG_Fat_i_" + i);
            model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * balancedNutrients[3], "balanceL_Fat_i_" + i);

            //Fibres
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++) {
                cons1.addTerm(nutritionTable.get(i).get(j).getFibres(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * balancedNutrients[4], "balanceG_Fibres_i_" + i);
            model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * balancedNutrients[4], "balanceL_Fibres_i_" + i);
        }

        // Solve the model.
        model.optimize();

        List<List<Integer>> gramsToBuy = new ArrayList<>();
        for (int i = 0; i < X.size(); i++)
            for (int j = 0; j < X.get(i).size(); j++)
                gramsToBuy.get(i).set(j, ((int) X.get(i).get(j).get(GRB.DoubleAttr.X)));

        return gramsToBuy;
    }
}