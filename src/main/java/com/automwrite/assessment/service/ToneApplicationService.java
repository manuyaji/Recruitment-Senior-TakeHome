package com.automwrite.assessment.service;

import com.automwrite.assessment.enums.Tone;

import java.util.concurrent.CompletableFuture;

public interface ToneApplicationService<DOCTYPE> {

    public void applyTone(String newDocName, DOCTYPE doc, Tone newTone);

    public CompletableFuture<Void> asyncApplyTone(String newDocName, DOCTYPE doc, Tone newTone);

}
