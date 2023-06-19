package parser.fieldparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DayOfWeekFieldParser extends FieldParser {

    private List<String> dayOfWeekStrings = new ArrayList<>(List.of("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"));
    @Override
    protected int getMin() {
        return 0;
    }

    @Override
    protected int getMax() {
        return 6;
    }



    public Set<Integer> parseField(String fieldExpression) throws IllegalArgumentException {
        if(fieldExpression == null || fieldExpression.length() == 0)
            throw new IllegalArgumentException("Field expression cannot be null or empty");

        Set<Integer> parsedResult = new TreeSet<>();

        if(fieldExpression.length() == 1 && fieldExpression.contains(ALL_CHAR)) {
            parseAllNumbers(parsedResult);
            return parsedResult;
        }

        // Split expression with LIST_CHAR to handle list at beginning
        String[] subExpressions = fieldExpression.split(LIST_CHAR);
        for(String subExpression: subExpressions) {
            if(subExpression.contains(RANGE_CHAR)) {
                parseRange(subExpression, parsedResult);
            } else if (subExpression.contains(STEP_CHAR)) {
                parseStep(subExpression, parsedResult);
            } else {
                parseNumber(subExpression, parsedResult);
            }
        }

        return parsedResult;
    }

    // Parse range expression
    @Override
    protected void parseRange(String rangeStr, Set<Integer> numberSet) throws IllegalArgumentException {
        String[] rangeNumbers = rangeStr.split(RANGE_CHAR);
        if(rangeNumbers.length != 2) throw new IllegalArgumentException("Illegal range field");
        int start = parseNumber(rangeNumbers[0], NumberRangeType.NORMAL);
        int end = parseNumber(rangeNumbers[1], NumberRangeType.NORMAL);
        for(int i = start; i <= end; i++) numberSet.add(i);
    }

    // Parse step expression
    @Override
    protected void parseStep(String stepStr, Set<Integer> numberSet) throws IllegalArgumentException {
        String[] stepNumbers = stepStr.split(STEP_CHAR);
        if(stepNumbers.length != 2) throw new IllegalArgumentException("Illegal step field");
        int start = getMin();
        if(!(stepNumbers[0].length() == 1 && stepNumbers[0].contains(ALL_CHAR)))
            start = parseNumber(stepNumbers[0], NumberRangeType.NORMAL);

        int step = parseNumber(stepNumbers[1], NumberRangeType.STEP);
        for(int i = start; i <= getMax(); i += step) numberSet.add(i);
    }

    // To get all the allowed number due to asterisk
    @Override
    protected void parseAllNumbers(Set<Integer> numberSet) {
        for(int i = getMin(); i <= getMax(); i++) numberSet.add(i);
    }

    // Parse number and add to number set
    @Override
    protected void parseNumber(String numStr, Set<Integer> numberSet) throws IllegalArgumentException {
        numberSet.add(parseNumber(numStr, NumberRangeType.NORMAL));
    }

    // Parse number according to number type
    @Override
    protected int parseNumber(String numStr, NumberRangeType rangeType) throws IllegalArgumentException {
        try {
            boolean allAlphabets = true;
            allAlphabets = isAllAlphabets(numStr, allAlphabets);
            if (allAlphabets) {
                int number = parseDayOfWeek(numStr);
                if(!isInRange(number, rangeType)) throw new IllegalArgumentException("Number not in range");
                return number;

            }else{
                int number = Integer.parseInt(numStr, 10);
                if(!isInRange(number, rangeType)) throw new IllegalArgumentException("Number not in range");
                return number;
            }


        } catch (Exception ex){
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    private int parseDayOfWeek(String numStr) throws Exception {
        String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (int i = 0; i < days.length; i++) {
            if (numStr.equalsIgnoreCase(days[i])) {
                return i;
            }
        }
        throw new Exception("Invalid day of week. Please provide valid fields"); // Invalid day of week
    }

    private boolean isAllAlphabets(String numStr, boolean allAlphabets) {
        for (char c : numStr.toCharArray()) {
            if (!Character.isLetter(c)) {
                allAlphabets = false;
            }
        }
        return allAlphabets;
    }

    // For normal case number should bound by min and max. For step case handle negative and divide by zero error
    @Override
    protected boolean isInRange(int number, NumberRangeType rangeType) {
        if(rangeType == NumberRangeType.STEP)
            return (number > 0);
        return (number >= getMin()) && (number <= getMax());
    }
}
