package day7;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7 {
    public static void main(String[] args) {
        List<Equation> equations;
        try (var lines = Files.lines(new File("./input").toPath())){
            equations = lines
                    .map(Day7::parseEquation)
                    .toList();
        } catch (IOException error) {
            throw new RuntimeException(error);
        }

        long resultA = 0L;
        List<Operator> operatorsForA = Operator.getSubList(0, 2);

        for (Equation equation : equations) {
            if (possible(equation, operatorsForA)) {
                resultA += equation.getResult();
            }
        }
        System.out.println("A: " + resultA);

        long resultB = 0L;
        for (Equation equation : equations) {
            if (possible(equation, Operator.entries())) {
                resultB += equation.getResult();
            }
        }
        System.out.println("B: " + resultB);
    }

    public static class Equation {
        private final long result;
        private final List<Long> input;

        public Equation(long result, List<Long> input) {
            this.result = result;
            this.input = input;
        }

        public long getResult() {
            return result;
        }

        public List<Long> getInput() {
            return input;
        }
    }

    public static Equation parseEquation(String line) {
        String[] parts = line.split(": ");
        long result = Long.parseLong(parts[0]);
        List<Long> input = Stream.of(parts[1].split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return new Equation(result, input);
    }

    public enum Operator {
        Addition(Long::sum),
        Multiplication((lhs, rhs) -> lhs * rhs),
        Concatenation((lhs, rhs) -> Long.parseLong(Long.toString(lhs) + rhs));

        private final OperatorFunction apply;

        Operator(OperatorFunction apply) {
            this.apply = apply;
        }

        public long apply(long lhs, long rhs) {
            return apply.apply(lhs, rhs);
        }

        @FunctionalInterface
        public interface OperatorFunction {
            long apply(long lhs, long rhs);
        }

        public static List<Operator> getSubList(int fromIndex, int toIndex) {
            return List.of(values()).subList(fromIndex, toIndex);
        }

        public static List<Operator> entries() {
            return List.of(values());
        }
    }

    public static class OperatorChains implements Iterator<List<Operator>> {
        private final List<Operator> availableOperators;
        private final int length;
        private final int period;
        private int state = 0;

        public OperatorChains(List<Operator> availableOperators, int length) {
            this.availableOperators = availableOperators;
            this.length = length;
            this.period = (int) Math.pow(availableOperators.size(), length);
        }

        @Override
        public boolean hasNext() {
            return state < period;
        }

        @Override
        public List<Operator> next() {
            List<Operator> result = new ArrayList<>();
            int chunk = state;
            for (int chunkIndex = 0; chunkIndex < length; ++chunkIndex) {
                int operatorIndex = chunk % availableOperators.size();
                chunk /= availableOperators.size();
                result.add(availableOperators.get(operatorIndex));
            }
            ++state;
            return result;
        }
    }

    public static boolean possible(Equation equation, List<Operator> operators) {
        for (OperatorChains chain = new OperatorChains(operators, equation.getInput().size() - 1); chain.hasNext(); ) {
            if (correct(equation, chain.next())) {
                return true;
            }
        }
        return false;
    }

    public static boolean correct(Equation equation, List<Operator> chain) {
        long result = equation.getInput().get(0);
        for (int index = 0; index < equation.getInput().size() - 1; ++index) {
            Operator operator = chain.get(index);
            long nextInputValue = equation.getInput().get(index + 1);
            result = operator.apply(result, nextInputValue);
        }
        return result == equation.getResult();
    }
}