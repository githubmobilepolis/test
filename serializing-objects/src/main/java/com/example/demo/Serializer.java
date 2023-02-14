package com.example.demo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * В данном классе реализуются методы 4х видов записи и чтения.
 */
public class Serializer {

    // default сериализация
    private <T> void serialize(List<T> animals, String fileName) {
        //получаем файл по имени
        Path file = Paths.get(fileName);
        //проверяем, содержится ли он
        if (Files.notExists(file)) {
            return;
        }
        try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(file))) {
            outputStream.writeInt(animals.size()); // Считываем размер листа объектов Animal
            // Пишем объекты Animal из листа
            for (T animal : animals) {
                outputStream.writeObject(animal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // default десериализация
    @SuppressWarnings("unchecked")
    private <T> List<T> deserialize(String fileName) {
        Path file = Paths.get(fileName);
        if (Files.notExists(file)) {
            return null;
        }
        List<T> animals = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            int size = ois.readInt(); // читаем размер
            // Считываются объекты и добавляются в List
            for (int i = 0; i < size; i++) {
                animals.add((T) ois.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return animals;
    }
    /**
     * Реализована простая сериализация, с помощью специального потока для сериализации объектов
     * @param animals Список животных для сериализации
     * @param fileName файл в который "пишем" животных
     */
    public void defaultSerialize(List<Animal> animals, String fileName) {
        serialize(animals, fileName);
    }

    /**
     * Реализована простая дисериализация, с помощью специального потока для дисериализации объектов
     * @param fileName файл из которого "читаем" животных
     * @return список животных
     */
    public List<Animal> defaultDeserialize(String fileName) {
        return deserialize(fileName);
    }


    /**
     * Реализована простая ручная сериализация, с помощью специального потока для сериализации объектов и специальных методов
     * @param animals Список животных для сериализации
     * @param fileName файл в который "пишем" животных
     */
    public void serializeWithMethods(List<AnimalWithMethods> animals, String fileName) {
        serialize(animals, fileName);
    }

    /**
     * Реализована простая ручная дисериализация, с помощью специального потока для дисериализации объектов
     * и специальных методов
     * @param fileName файл из которого "читаем" животных
     * @return список животных
     */
    public List<AnimalWithMethods> deserializeWithMethods(String fileName) {
        return deserialize(fileName);
    }

    /**
     * Реализована простая ручная сериализация, с помощью специального потока для сериализации объектов и интерфейса Externalizable
     * @param animals Список животных для сериализации
     * @param fileName файл в который "пишем" животных
     */
    public void serializeWithExternalizable(List<AnimalExternalizable> animals, String fileName) {
        serialize(animals, fileName);
    }

    /**
     * Реализована простая ручную дисериализация, с помощью специального потока для дисериализации объектов
     * и интерфейса Externalizable
     *
     * @param fileName файл из которого "читаем" животных
     * @return список животных
     */
    public List<AnimalExternalizable> deserializeWithExternalizable(String fileName) {
        return deserialize(fileName);
    }

    /**
     * Реализована ручная сериализацию, с помощью высокоуровневых потоков. Сами ручками пишем поля,
     * без использования методов writeObject
     * @param animals  Список животных для сериализации
     * @param fileName файл, в который "пишем" животных
     */
    public void customSerialize(List<Animal> animals, String fileName) {
        Path file = Paths.get(fileName);
        if (Files.notExists(file)) {
            return;
        }
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(file)))) {
            dos.writeInt(animals.size()); // записываем размер листа Animal
            for (Animal animal : animals) {
                AnimalByte animalByte = new AnimalByte(animal); // Создаем AnimalByte, где записан бит для полей объекта
                dos.writeByte(animalByte.writeByte());
                // если Animal null, не пишем
                if (animalByte.animalIsNull()) {
                    continue;
                }
                if (animalByte.nameIsNotNull()) {
                    dos.writeUTF(animal.getName());
                }
                dos.writeInt(animal.getAge());
                if (animalByte.animalTypeIsNotNull()) {
                    dos.writeUTF(animal.getAnimalType().name());
                }
                Population population = animal.getPopulation();
                PopulationByte populationByte = new PopulationByte(population);
                dos.writeByte(populationByte.writeByte());
                if (populationByte.populationIsNull()) {
                    continue;
                }
                if (populationByte.nameIsNotNull()) {
                    dos.writeUTF(population.getName());
                }
                dos.writeLong(population.getSize());
                dos.writeInt(population.getDensity());
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Реализована ручная дисериализацию, с помощью высокоуровневых потоков. Сами ручками читаем поля,
     * без использования методов readObject
     * @param fileName файл из которого "читаем" животных
     * @return список животных
     */
    public List<Animal> customDeserialize(String fileName) {
        Path file = Paths.get(fileName);
        if (Files.notExists(file)) {
            return null;
        }
        List<Animal> animals = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                AnimalByte animalByte = new AnimalByte(dis.readByte());
                if (animalByte.animalIsNull()) {
                    animals.add(null);
                    continue;
                }
                String name = null;
                if (animalByte.nameIsNotNull()) {
                    name = dis.readUTF();
                }
                int age = dis.readInt();
                boolean friendly = animalByte.isFriendly();
                boolean warmBlooded = animalByte.isWarmBlooded();
                AnimalType animalType = null;
                if (animalByte.animalTypeIsNotNull()) {
                    animalType = AnimalType.valueOf(dis.readUTF());
                }
                PopulationByte populationByte = new PopulationByte(dis.readByte());
                if (populationByte.populationIsNull()) {
                    animals.add(new Animal(name, age, friendly, warmBlooded, animalType, null));
                    continue;
                }
                if (populationByte.nameIsNotNull()) {
                    animals.add(new Animal(name, age, friendly, warmBlooded, animalType,
                            new Population(dis.readUTF(), dis.readLong(), dis.readInt())));
                } else {
                    animals.add(new Animal(name, age, friendly, warmBlooded, animalType,
                            new Population(null, dis.readLong(), dis.readInt())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return animals;
    }

}
