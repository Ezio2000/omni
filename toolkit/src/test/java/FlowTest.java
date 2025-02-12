import org.omni.toolkit.design.flow.FluentControlFlow;

public class FlowTest {

    public static void main(String[] args) {
        int value = 2;
        final int[] counter = {0};

        FluentControlFlow.of()
                // if-else 示例
                .ifElse(() -> value > 0,
                        () -> {
                            System.out.println("if-else: 条件 (value > 0) 为 true");
                            // 嵌套 switch-case 示例（使用 builder 版本）
                            FluentControlFlow.of()
                                    .switchCase(value)
                                    .caseOf(1, flow -> flow.action(() -> System.out.println("switch-case: value 为 1")))
                                    .caseOf(2, flow -> flow.action(() -> System.out.println("switch-case: value 为 2")))
                                    .caseOf(3, flow -> flow.action(() -> System.out.println("switch-case: value 为 3")))
                                    .defaultCase(flow -> flow.action(() -> System.out.println("switch-case: 未匹配到 case")))
                                    .execute();
                        },
                        () -> System.out.println("if-else: 条件 (value > 0) 为 false"))
                // try-catch 示例（使用重载版本）
                .tryCatch(() -> {
                            System.out.println("try-catch: 尝试执行除法运算...");
                            // 当 value==2 时，此处除数为0会抛出异常
                            int result = 10 / (value - 2);
                            System.out.println("try-catch: 计算结果 = " + result);
                        }, ArithmeticException.class,
                        () -> System.out.println("try-catch: 捕获到 ArithmeticException"),
                        () -> System.out.println("try-catch: finally 块执行"))
                // for 循环
                .forLoop(0, 3, i -> System.out.println("forLoop: 当前 i = " + i))
                // while 循环
                .whileLoop(() -> counter[0] < 3, () -> {
                    System.out.println("whileLoop: counter = " + counter[0]);
                    counter[0]++;
                })
                // do-while 循环（至少执行一次）
                .doWhile(() -> System.out.println("doWhile: 此循环至少执行一次"), () -> false)
                // 添加一个额外动作
                .action(() -> System.out.println("action: 额外的简单动作"))
                // 最后执行所有构建的节点
                .execute();
    }

}
