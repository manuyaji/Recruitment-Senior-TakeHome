package com.automwrite.assessment.service;

import com.automwrite.assessment.enums.Tone;

public interface ToneExampleService {

    public String getExampleForTone(Tone tone, int maxWords);
}
