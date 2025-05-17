import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static ArrayList<Integer> variables = new ArrayList<>(Collections.nCopies(5, 0));

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в калькулятор (для завершения работы введите пустую строку)!");
        Scanner input = new Scanner(System.in);

        System.out.println("Введите выражение:");
        String expression = input.nextLine();
        while (!expression.isEmpty()) {
            if (expression.contains("=")) {
                System.out.println(equalCalc(expression));
            }
            else {
                System.out.println(calc(expression));
            }
            System.out.println("Введите выражение:");
            expression = input.nextLine();
        }
        System.out.println("Калькулятор завершил работу");
    }

    public static String calc(String exp) {
        List<Integer> stack = new ArrayList<>();
        Boolean isSign = Boolean.FALSE;
        for (String symbol: exp.split("\\s+")) {
            if (isDigital(symbol)) {
                if (isSign) {
                    return "Ошибка: не всё выражения в постфиксной записи";
                }
                stack.add(Integer.parseInt(symbol));
            }
            else if (symbol.charAt(0) == 'x') {
                String check = isCorrect(symbol);
                if (check.equals("correct")) {
                    String ind = symbol.substring(1);
                    Integer int_ind = Integer.parseInt(ind);
                    Integer value = variables.get(int_ind - 1);
                    stack.add(value);
                }
                else {
                    return check;
                }
            }
            else if (isLiteral(symbol)) {
                return "Ошибка: некорректный ввод: вместо '" + symbol + "' должно быть числовое значение";
            }
            else if ("+-*/".contains(symbol)) {
                isSign = Boolean.TRUE;
                if (stack.size() < 2) {
                    return "Ошибка: недостаточно числовых значений для произведения операции '" +  symbol + "'";
                }
                Integer first = stack.get(stack.size() - 2);
                Integer second = stack.getLast();
                String result = simpleCalc(first, second, symbol);
                if (result.equals("zero")) {
                    return "Ошибка при вычислении: обнаружено деление на 0";
                } else {
                    stack.removeLast();
                    stack.removeLast();
                    stack.add(Integer.parseInt(result));
                }
            }
            else if (symbol.charAt(0) == '-' & isDigital(symbol.substring(1))) {
                return "Ошибка: введено отрицательное число";
            }
            else {
                return "Ошибка: программа не может обработать оператор: '" + symbol + "'";
            }
        }
        if (isSign & stack.size() > 1) {
            return "Ошибка: не достаточно числовых значений";
        }
        if (isSign | stack.size() == 1) {
            return Integer.toString(stack.getFirst());
        }
        return "Ошибка: не обнаружен знак операции";
    }

    public static String equalCalc(String exp) {
        String[] parts = exp.split(" = ");
        String value = calc(parts[parts.length - 1]);
        if (isDigital(value) | (value.charAt(0) == '-' & isDigital(value.substring(1)))) {
            for (Integer number = 0; number < (parts.length - 1); number++) {
                String check = isCorrect(parts[number]);
                if (check.equals("correct")) {
                    Integer ind = Integer.parseInt(parts[number].substring(1));
                    variables.set(ind - 1, Integer.parseInt(value));
                }
                else {
                    return check;
                }
            }
        }
        return value;
    }

    public static Boolean isDigital(String str) {
        Boolean check = Boolean.TRUE;
        for (char symbol: str.toCharArray()) {
            if (!(Character.isDigit(symbol))) {
                check = Boolean.FALSE;
                break;
            }
        }
        return check;
    }

    public static Boolean isLiteral(String str) {
        Boolean check = Boolean.FALSE;
        for (char symbol: "qwertyuiopasdfghjklzxcvbnmйцукенгшщзхъфывапролджэячсмитьбю".toCharArray()) {
            if (str.contains(Character.toString(symbol))) {
                check = Boolean.TRUE;
            }
        }
        return check;
    }

    public static String simpleCalc(Integer first, Integer second, String sign) {
        switch (sign) {
            case "+": return Integer.toString(first + second);
            case "-": return Integer.toString(first - second);
            case "*": return Integer.toString(first * second);
            default:
                if (second.equals(0)) {
                    return "zero";
                }
                return Integer.toString(first / second);
        }
    }

    public static String isCorrect(String symbol) {
        String ind = symbol.substring(1);
        if (!isDigital(ind) | !(symbol.charAt(0) == 'x')) {
            return "Ошибка: некорректное название переменной '" + symbol.charAt(0) + ind +
                    "' (поддерживаются только х1, х2, х3, х4, х5)";
        }
        Integer int_ind = Integer.parseInt(ind);
        if (int_ind < 1 | int_ind > 5 | !isDigital(ind)) {
            return "Ошибка: некорректное название переменной 'x" + ind +
                    "' (поддерживаются только х1, х2, х3, х4, х5)";
        }
        return "correct";
    }
}
