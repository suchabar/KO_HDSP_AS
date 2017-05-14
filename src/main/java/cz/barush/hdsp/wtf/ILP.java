package cz.barush.hdsp.wtf;

import cz.barush.hdsp.entity.Food;
import gurobi.*;

import java.util.ArrayList;
import java.util.List;

public class ILP
{
    public static GRBModel model;
    static GRBVar[][] edges;

    public ILP(GRBVar[][] newEdges)
    {
        this.edges = newEdges;
    }
    //min jidel
    public static List<List<Double>> solveStiglersProblem(List<List<Food>> nutritionTable, int[] balancedNutrients) throws GRBException
    {
        GRBEnv env = new GRBEnv();
        model = new GRBModel(env);
        List<List<GRBVar>> X = new ArrayList<>();
        //addVar(lowerBound, upperBound, objectiveCoeff, variableType, name)
        for (int i = 0; i < nutritionTable.size(); i++)X.add(new ArrayList<>(nutritionTable.get(i).size()));
        for (int i = 0; i < X.size(); i++)
        {
            for (int j = 0; j < nutritionTable.get(i).size(); j++)
            {
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
        double[] ratioBalancesLowerBound = {0.40, 0.30, 0.20, 0.10};
        double[] ratioBalancesUpperBound = {0.50, 0.40, 0.30};

        //GO THROUGH ALL FOOD - Iterate i. set of food
        for (int i = 0; i < nutritionTable.size(); i++)
        {
            //Energy
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++)
            {
                cons1.addTerm(nutritionTable.get(i).get(j).getEnergy(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * (7*balancedNutrients[0]), "balanceG_Energy_i_" + i);
            //model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * (7*balancedNutrients[0]), "balanceL_Energy_i_" + i);

            //Carbs
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++)
            {
                cons1.addTerm(nutritionTable.get(i).get(j).getCarbs(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * (7*balancedNutrients[1]), "balanceG_Carbs_i_" + i);
            //model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * (7*balancedNutrients[1]), "balanceL_Carbs_i_" + i);

            //Proteins
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++)
            {
                cons1.addTerm(nutritionTable.get(i).get(j).getProteins(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * (7*balancedNutrients[2]), "balanceG_Proteins_i_" + i);
            //model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * (7*balancedNutrients[2]), "balanceL_Proteins_i_" + i);

            //Fat
            cons1 = new GRBLinExpr();
            for (int j = 0; j < nutritionTable.get(i).size(); j++)
            {
                cons1.addTerm(nutritionTable.get(i).get(j).getFats(), X.get(i).get(j));
            }
            model.addConstr(cons1, GRB.GREATER_EQUAL, ratioBalancesLowerBound[i] * (7*balancedNutrients[3]), "balanceG_Fat_i_" + i);
            //model.addConstr(cons1, GRB.LESS_EQUAL, ratioBalancesUpperBound[i] * (7*balancedNutrients[3]), "balanceL_Fat_i_" + i);

//                model.optimize();
//                List<List<Double>> gramsToBuy = new ArrayList<>();
//                for (int k = 0; k < X.size(); k++)
//                {
//                    gramsToBuy.add(new ArrayList<>());
//                    for (int j = 0; j < X.get(k).size(); j++)
//                    {
//                        gramsToBuy.get(k).add(X.get(k).get(j).get(GRB.DoubleAttr.X));
//                        System.out.print(nutritionTable.get(k).get(j).getName() + " " + X.get(k).get(j).get(GRB.DoubleAttr.X) + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println("Objective value - total price: " + (int) ILP.model.get(GRB.DoubleAttr.ObjVal));
        }

        // Solve the model.
        model.optimize();

        List<List<Double>> gramsToBuy = new ArrayList<>();
        for (int i = 0; i < X.size(); i++)
        {
            gramsToBuy.add(new ArrayList<>());
            for (int j = 0; j < X.get(i).size(); j++)
            {
                gramsToBuy.get(i).add(X.get(i).get(j).get(GRB.DoubleAttr.X));
                System.out.print(nutritionTable.get(i).get(j).getName() + " " + X.get(i).get(j).get(GRB.DoubleAttr.X) + " ");
            }
            System.out.println();
        }
        System.out.println("Objective value - total price: " + (int) ILP.model.get(GRB.DoubleAttr.ObjVal));

        return gramsToBuy;
    }
}