package com.automwrite.assessment.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Tone {
    FORMAL("Formal"),
    CASUAL("Casual"),
    GRANDILOQUENT("Grandiloquent");

    private final String tone;

    Tone(String tone) {
        this.tone = tone;
    }

    public String getTone(){
        return tone;
    }

    public static Tone fromTone(String tone){
        return Tone.valueOf(tone.toUpperCase());
    }

    public static List<String> getTones(){
        return Arrays.stream(Tone.values()).map(Tone::getTone).collect(Collectors.toList());
    }
}
