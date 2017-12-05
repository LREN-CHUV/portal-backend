package eu.hbp.mip.utils;

import eu.hbp.mip.woken.messages.external.VariableId;
import eu.hbp.mip.model.AlgorithmParam;
import eu.hbp.mip.model.Variable;
import scala.collection.immutable.HashMap;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mirco on 06.01.17.
 */
public class TypesConvert {

    public static List<VariableId> variablesToVariableIds(List<Variable> vars) {
        List<VariableId> varIds = new LinkedList<>();
        for (Variable v: vars
                ) {
            varIds.add(new VariableId(v.getCode()));
        }
        return varIds;
    }

    public static HashMap<String, String> algoParamsToHashMap(List<AlgorithmParam> aps) {
        HashMap<String, String> params = new HashMap<>();
        for (AlgorithmParam ap: aps
                ) {
            params = params.updated(ap.getCode(), ap.getValue());
        }
        return params;
    }

}
