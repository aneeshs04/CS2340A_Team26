package com.example.myapplication.model;

import com.example.myapplication.model.Observer;

public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
