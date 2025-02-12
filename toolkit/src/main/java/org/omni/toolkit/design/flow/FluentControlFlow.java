package org.omni.toolkit.design.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * FluentControlFlow 是一个延迟执行的控制结构 DSL，
 * 支持 if-else、switch-case、try-catch(-finally) 以及 for/while/do-while 循环，
 * 并且允许任意嵌套，只有调用 execute() 时才会真正运行之前构建的流程。
 */
public class FluentControlFlow implements Executable {

    // 存放当前流程中所有的节点（延迟执行）
    private final List<Executable> steps = new ArrayList<>();

    // === 工厂方法 ===
    public static FluentControlFlow of() {
        return new FluentControlFlow();
    }

    // 私有构造器，外部只能通过 create() 构建实例
    private FluentControlFlow() {}

    // === 添加普通动作 ===
    public FluentControlFlow action(Runnable action) {
        // 允许传入任意复杂逻辑（匿名类、lambda 内部多行语句都可以）
        steps.add(action::run);
        return this;
    }

    // ===================== if-else 控制结构 =====================

    /**
     * 重载版本：直接传入条件、then 与 otherwise 的动作（支持多行逻辑，可在 Runnable 内自行处理）。
     */
    public FluentControlFlow ifElse(BooleanSupplier condition, Runnable thenAction, Runnable elseAction) {
        IfElseNode node = new IfElseNode(condition);
        // 将动作包装为子流程
        FluentControlFlow thenFlow = FluentControlFlow.of().action(thenAction);
        FluentControlFlow elseFlow = FluentControlFlow.of().action(elseAction);
        node.addThen(thenFlow);
        node.addElse(elseFlow);
        steps.add(node);
        return this;
    }

    /**
     * Builder 版本：用于构建更复杂嵌套的 if-else 逻辑。
     */
    public IfElseBuilder ifElse(BooleanSupplier condition) {
        IfElseNode node = new IfElseNode(condition);
        steps.add(node);
        return new IfElseBuilder(node);
    }

    /**
     * if-else 构建器（嵌套复杂逻辑时使用），增加了重载的 then/otherwise 方法，
     * 允许直接传入 Runnable，从而避免手动创建子流程。
     */
    public class IfElseBuilder {
        private final IfElseNode node;

        public IfElseBuilder(IfElseNode node) {
            this.node = node;
        }

        /**
         * 传入 Consumer 版本：当需要在 then 分支中嵌套更多控制结构时使用。
         */
        public IfElseBuilder then(Consumer<FluentControlFlow> thenBlock) {
            FluentControlFlow subFlow = FluentControlFlow.of();
            thenBlock.accept(subFlow);
            node.addThen(subFlow);
            return this;
        }

        /**
         * 重载版本：直接传入 Runnable，适用于单行或简单多行逻辑，无需自己调用 create()。
         */
        public IfElseBuilder then(Runnable action) {
            FluentControlFlow subFlow = FluentControlFlow.of().action(action);
            node.addThen(subFlow);
            return this;
        }

        /**
         * Consumer 版本的 otherwise 分支。
         */
        public FluentControlFlow otherwise(Consumer<FluentControlFlow> elseBlock) {
            FluentControlFlow subFlow = FluentControlFlow.of();
            elseBlock.accept(subFlow);
            node.addElse(subFlow);
            return FluentControlFlow.this;
        }

        /**
         * 重载版本：直接传入 Runnable，适用于简单逻辑。
         */
        public FluentControlFlow otherwise(Runnable action) {
            FluentControlFlow subFlow = FluentControlFlow.of().action(action);
            node.addElse(subFlow);
            return FluentControlFlow.this;
        }
    }

    // ===================== switch-case 控制结构 =====================

    /**
     * 重载版本：仅指定一个默认动作（若仅关注默认情况）。
     */
    public FluentControlFlow switchCase(int value, Runnable defaultAction) {
        SwitchNode node = new SwitchNode(value);
        FluentControlFlow defaultFlow = FluentControlFlow.of().action(defaultAction);
        node.setDefault(defaultFlow);
        steps.add(node);
        return this;
    }

    /**
     * Builder 版本：用于定义多个 case 分支与默认分支。
     */
    public SwitchBuilder switchCase(int value) {
        SwitchNode node = new SwitchNode(value);
        steps.add(node);
        return new SwitchBuilder(node);
    }

    // switch-case 构建器（用于嵌套复杂逻辑时）
    public class SwitchBuilder {
        private final SwitchNode node;
        public SwitchBuilder(SwitchNode node) {
            this.node = node;
        }
        public SwitchBuilder caseOf(int caseValue, Consumer<FluentControlFlow> caseBlock) {
            FluentControlFlow subFlow = FluentControlFlow.of();
            caseBlock.accept(subFlow);
            node.addCase(caseValue, subFlow);
            return this;
        }
        public FluentControlFlow defaultCase(Consumer<FluentControlFlow> defaultBlock) {
            FluentControlFlow subFlow = FluentControlFlow.of();
            defaultBlock.accept(subFlow);
            node.setDefault(subFlow);
            return FluentControlFlow.this;
        }
    }

    // ===================== try-catch(-finally) 控制结构 =====================

    /**
     * 重载版本：直接传入 try 块、catch 块和 finally 块（如果不需要 finally，可以传入空的 Runnable）。
     */
    public FluentControlFlow tryCatch(Runnable tryAction,
                                      Class<? extends Throwable> catchType,
                                      Runnable catchAction,
                                      Runnable finallyAction) {
        TryCatchNode node = new TryCatchNode();
        FluentControlFlow tryFlow = FluentControlFlow.of().action(tryAction);
        FluentControlFlow catchFlow = FluentControlFlow.of().action(catchAction);
        FluentControlFlow finallyFlow = FluentControlFlow.of().action(finallyAction);
        node.setTryFlow(tryFlow);
        node.setCatch(catchType, catchFlow);
        node.setFinally(finallyFlow);
        steps.add(node);
        return this;
    }

    /**
     * 重载版本：无 finally 块
     */
    public FluentControlFlow tryCatch(Runnable tryAction,
                                      Class<? extends Throwable> catchType,
                                      Runnable catchAction) {
        return tryCatch(tryAction, catchType, catchAction, () -> {});
    }

    /**
     * Builder 版本：适用于需要嵌套复杂逻辑的 try-catch 结构。
     */
    public TryCatchBuilder tryCatch() {
        TryCatchNode node = new TryCatchNode();
        steps.add(node);
        return new TryCatchBuilder(node);
    }

    // try-catch 构建器（嵌套复杂逻辑时使用）
    public class TryCatchBuilder {
        private final TryCatchNode node;
        public TryCatchBuilder(TryCatchNode node) {
            this.node = node;
        }
        public TryCatchBuilder tryBlock(Consumer<FluentControlFlow> tryBlock) {
            FluentControlFlow subFlow = FluentControlFlow.of();
            tryBlock.accept(subFlow);
            node.setTryFlow(subFlow);
            return this;
        }
        public TryCatchBuilder catchException(Class<? extends Throwable> type, Consumer<FluentControlFlow> catchBlock) {
            FluentControlFlow subFlow = FluentControlFlow.of();
            catchBlock.accept(subFlow);
            node.setCatch(type, subFlow);
            return this;
        }
        public FluentControlFlow finallyBlock(Consumer<FluentControlFlow> finallyBlock) {
            FluentControlFlow subFlow = FluentControlFlow.of();
            finallyBlock.accept(subFlow);
            node.setFinally(subFlow);
            return FluentControlFlow.this;
        }
    }

    // ===================== for 循环 =====================

    /**
     * for 循环：从 start 到 end（不包含 end）。
     */
    public FluentControlFlow forLoop(int start, int end, IntConsumer action) {
        steps.add(() -> {
            for (int i = start; i < end; i++) {
                action.accept(i);
            }
        });
        return this;
    }

    // ===================== while 循环 =====================

    /**
     * while 循环：只要条件为 true 就执行 action。
     */
    public FluentControlFlow whileLoop(BooleanSupplier condition, Runnable action) {
        steps.add(() -> {
            while (condition.getAsBoolean()) {
                action.run();
            }
        });
        return this;
    }

    // ===================== do-while 循环 =====================

    /**
     * do-while 循环：先执行 action，再判断条件是否继续。
     */
    public FluentControlFlow doWhile(Runnable action, BooleanSupplier condition) {
        steps.add(() -> {
            do {
                action.run();
            } while (condition.getAsBoolean());
        });
        return this;
    }

    // ===================== 最终执行 =====================

    /**
     * 当调用 execute() 时，所有之前构建的节点会按顺序执行。
     */
    @Override
    public void execute() {
        for (Executable step : steps) {
            step.execute();
        }
    }

}
