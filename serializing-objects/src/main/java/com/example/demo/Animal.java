package com.example.demo;


import java.io.Serializable;
import java.util.Objects;
/**
 * Класс содержит поля типа String - имя, ade - возраст животного, boolean friendly и warmBlooded - дружелюбность и
 * теплокровность, enum AnimalType - тип животного, Population - объект популяции животного
 */
public class Animal implements Serializable {
    private final String name;
    private final int age;
    private final boolean friendly;
    private final boolean warmBlooded;
    private final AnimalType animalType;
    private final Population population;

    public Animal(String name, int age, boolean friendly, boolean warmBlooded, AnimalType animalType, Population population) {
        this.name = name;
        this.age = age;
        this.friendly = friendly;
        this.warmBlooded = warmBlooded;
        this.animalType = animalType;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public boolean isWarmBlooded() {
        return warmBlooded;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public Population getPopulation() {
        return population;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Animal animal = (Animal) o;
        return Objects.equals(animal.name, name) && animal.age == age
                && animal.friendly == friendly && animal.warmBlooded == warmBlooded
                && animal.animalType == animalType && Objects.equals(animal.population, population);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", age=" + age + '\'' +
                ", friendly=" + friendly + '\'' +
                ", warm-blooded=" + warmBlooded + '\'' +
                ", population=" + population + '\'' +
                ", animalType=" + animalType +
                '}';
    }

}
