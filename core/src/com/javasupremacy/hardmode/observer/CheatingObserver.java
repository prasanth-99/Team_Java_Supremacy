package com.javasupremacy.hardmode.observer;

import com.javasupremacy.hardmode.screens.BackgroundScreen;

public abstract class CheatingObserver {
    protected BackgroundScreen subject;
    public abstract void updateCheating();
}
