package com.automwrite.assessment.strategies.toneapplication;

/*
Implement logic on whether a given text is a candidate for tone conversion.
For example, an address should not be a candidate for tone conversion.
 */
public interface TextToneConversionValidityStrategy {

    public boolean shouldToneOfTextBeConverted(String text);
}
