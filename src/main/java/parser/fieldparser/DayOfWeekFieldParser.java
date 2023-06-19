package parser.fieldparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DayOfWeekFieldParser extends FieldParser {

    @Override
    protected int getMin() {
        return 0;
    }

    @Override
    protected int getMax() {
        return 6;
    }

}
