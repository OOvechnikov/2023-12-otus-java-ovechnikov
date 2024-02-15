package ru.otus.calculator;

import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Summator {

    private Integer counter = 0;
    private Integer sum = 0;
    private Integer prevValue = 0;
    private Integer prevPrevValue = 0;
    private Integer sumLastThreeValues = 0;
    private Integer someValue = 0;

    // !!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        counter++;
        if (counter % 6_600_000 == 0) {
            counter = 0;
        }

        val dataValue = data.getValue();
        sum += dataValue;

        sumLastThreeValues = dataValue + prevValue + prevPrevValue;
        prevPrevValue = prevValue;
        prevValue = dataValue;

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (dataValue + 1) - sum);
            someValue = Math.abs(someValue) + counter;
        }
    }

}
