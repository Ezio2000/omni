package org.omni.toolkit.design.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * @author Xieningjun
 * @date 2025/2/12 10:11
 * @description
 */
// if-else 内部节点
public class IfElseNode implements Executable {
    private final BooleanSupplier condition;
    private final List<Executable> thenSteps = new ArrayList<>();
    private final List<Executable> elseSteps = new ArrayList<>();

    public IfElseNode(BooleanSupplier condition) {
        this.condition = condition;
    }

    public void addThen(Executable e) {
        thenSteps.add(e);
    }

    public void addElse(Executable e) {
        elseSteps.add(e);
    }

    @Override
    public void execute() {
        if (condition.getAsBoolean()) {
            for (Executable step : thenSteps) {
                step.execute();
            }
        } else {
            for (Executable step : elseSteps) {
                step.execute();
            }
        }
    }

}
