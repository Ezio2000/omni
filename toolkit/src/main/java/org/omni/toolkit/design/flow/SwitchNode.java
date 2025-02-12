package org.omni.toolkit.design.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2025/2/12 10:19
 * @description
 */
// switch-case 内部节点实现
public class SwitchNode implements Executable {
    private final int switchValue;
    private final List<Case> cases = new ArrayList<>();
    private FluentControlFlow defaultFlow;

    public SwitchNode(int value) {
        this.switchValue = value;
    }

    public void addCase(int caseValue, FluentControlFlow flow) {
        cases.add(new Case(caseValue, flow));
    }

    public void setDefault(FluentControlFlow flow) {
        this.defaultFlow = flow;
    }

    @Override
    public void execute() {
        boolean matched = false;
        for (Case c : cases) {
            if (switchValue == c.caseValue) {
                c.flow.execute();
                matched = true;
                break;
            }
        }
        if (!matched && defaultFlow != null) {
            defaultFlow.execute();
        }
    }

    private static class Case {
        int caseValue;
        FluentControlFlow flow;
        public Case(int caseValue, FluentControlFlow flow) {
            this.caseValue = caseValue;
            this.flow = flow;
        }
    }
}
