package day7;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day7 {
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
        List<Operator> operatorsForA = Arrays.asList(Operator.values()).subList(0, 2);
        for (Equation equation : equations) {
            if (possible(equation, operatorsForA)) {
                resultA += equation.result();
            }
        }
        System.out.println("A: " + resultA);

        long resultB = 0L;
        for (Equation equation : equations) {
            if (possible(equation, Arrays.asList(Operator.values()))) {
                resultB += equation.result();
            }
        }
        System.out.println("B: " + resultB);
    }

    record Equation(long result, List<Long> input) {}

    static Equation parseEquation(String line) {
        String[] parts = line.split(": ");
        long result = Long.parseLong(parts[0]);
        List<Long> input = Stream.of(parts[1].split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return new Equation(result, input);
    }

    enum Operator {
        Addition(Long::sum),
        Multiplication((lhs, rhs) -> lhs * rhs),
        Concatenation((lhs, rhs) -> Long.parseLong(Long.toString(lhs) + rhs));

        private final BiFunction<Long, Long, Long> implementation;

        Operator(BiFunction<Long, Long, Long> implementation) {
            this.implementation = implementation;
        }

        public long apply(long lhs, long rhs) {
            return implementation.apply(lhs, rhs);
        }
    }

    static class OperatorChains implements Iterator<List<Operator>> {
        final List<Operator> availableOperators;
        final int length;
        final int period;
        int state = 0;

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

    static boolean possible(Equation equation, List<Operator> operators) {
        for (OperatorChains chain = new OperatorChains(operators, equation.input().size() - 1); chain.hasNext(); ) {
            if (correct(equation, chain.next())) {
                return true;
            }
        }
        return false;
    }

    static boolean correct(Equation equation, List<Operator> chain) {
        long result = equation.input().get(0);
        for (int index = 0; index < equation.input().size() - 1; ++index) {
            Operator operator = chain.get(index);
            long nextInputValue = equation.input().get(index + 1);
            result = operator.apply(result, nextInputValue);
        }
        return result == equation.result();
    }
}
