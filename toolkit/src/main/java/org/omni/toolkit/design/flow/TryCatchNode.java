package org.omni.toolkit.design.flow;

/**
 * @author Xieningjun
 * @date 2025/2/12 14:49
 * @description
 */
// try-catch 内部节点实现
public class TryCatchNode implements Executable {
    private FluentControlFlow tryFlow = FluentControlFlow.of();
    private Class<? extends Throwable> catchType;
    private FluentControlFlow catchFlow = FluentControlFlow.of();
    private FluentControlFlow finallyFlow;

    public void setTryFlow(FluentControlFlow flow) {
        this.tryFlow = flow;
    }

    public void setCatch(Class<? extends Throwable> type, FluentControlFlow flow) {
        this.catchType = type;
        this.catchFlow = flow;
    }

    public void setFinally(FluentControlFlow flow) {
        this.finallyFlow = flow;
    }

    @Override
    public void execute() {
        try {
            tryFlow.execute();
        } catch (Throwable t) {
            if (catchType != null && catchType.isInstance(t)) {
                catchFlow.execute();
            } else {
                throw new RuntimeException("Unhandled exception in tryCatch", t);
            }
        } finally {
            if (finallyFlow != null) {
                finallyFlow.execute();
            }
        }
    }
}
