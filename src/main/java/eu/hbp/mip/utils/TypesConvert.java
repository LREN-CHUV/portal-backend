package eu.hbp.mip.utils;

import ch.chuv.lren.woken.messages.query.CodeValue;
import ch.chuv.lren.woken.messages.variables.FeatureIdentifier;
import ch.chuv.lren.woken.messages.variables.VariableId;
import eu.hbp.mip.model.AlgorithmParam;
import eu.hbp.mip.model.Variable;
import scala.collection.JavaConversions;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mirco on 06.01.17.
 */
public class TypesConvert {

    public static scala.collection.immutable.List<FeatureIdentifier> variablesToIdentifiers(List<Variable> vars) {
        List<FeatureIdentifier> varIds = new LinkedList<>();
        for (Variable v: vars) {
            if (!v.getCode().isEmpty()) {
                varIds.add(new VariableId(v.getCode()));
            }
        }
        return JavaConversions.asScalaBuffer(varIds).toList();
    }

    public static scala.collection.immutable.List<CodeValue> algoParamsToScala(List<AlgorithmParam> aps) {
        List<CodeValue> cvs = aps.stream().map(p -> new CodeValue(p.getCode(), p.getValue())).collect(Collectors.toList());
        return JavaConversions.asScalaBuffer(cvs).toList();
    }

}
