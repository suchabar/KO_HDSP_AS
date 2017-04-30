package cz.barush.hdsp;

import cz.barush.hdsp.entity.ILPInput;
import cz.barush.hdsp.entity.ILPResponse;
import cz.barush.hdsp.entity.Input;
import cz.barush.hdsp.entity.TSPResponse;
import cz.barush.hdsp.wtf.ILP;
import cz.barush.hdsp.wtf.TSP;
import gurobi.GRB;
import gurobi.GRBException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("api")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class Endpoint {

    @POST
    @Path("compute")
    public Response getInput(Input input) {
        String output = "Jersey say : ";
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("wut")
    public Response get() {
        String output = "Jersey say : ";
        return Response.status(200).entity(output).build();
    }

    @POST
    @Path("stigler")
    public Response getInput(ILPInput input) {
        try {
            final List<List<Integer>> lists = ILP.solveStiglersProblem(input.getAllFood(), input.getBalancedNutrients());
            int obj = (int) ILP.model.get(GRB.DoubleAttr.ObjVal);
            System.out.println("");
            return Response.status(200).entity(new ILPResponse()).build();
        } catch (GRBException e) {
            return Response.status(500).entity("daco sa pojebalo").build();
        }

    }

    @POST
    @Path("tsp")
    public Response getInput(TSPResponse input) {
//        try {
//            final List<List<Integer>> lists = ILP.solveStiglersProblem(input.getAllFood(), input.getBalancedNutrients());
//            int obj = (int) ILP.model.get(GRB.DoubleAttr.ObjVal);
//            System.out.println("");
//
//        } catch (GRBException e) {
//            e.printStackTrace();
//        }
        return Response.status(200).entity(new ILPResponse()).build();
    }

}