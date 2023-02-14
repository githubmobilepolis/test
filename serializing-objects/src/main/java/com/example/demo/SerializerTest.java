package com.example.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
/**
 * Для тестирования надо создать список из n(вводятся пользователем) разных объектов (заполняются объекты с помощью рандома).
 * В списке животных попадаются null-ы
 * Потом получившийся список записывается в файл. Далее этот список читается из файла.
 * Результатом теста: размер файла, время записи и время чтения.
 * Время считывается через System.currentTimeMillis().
 */

public class SerializerTest {

    public SerializerTest(int numberOfObj) {
        NUMBER_OF_OBJECT = numberOfObj;
    }

    private static final Path FILE_PATH = Paths.get("src", "test", "resources", "object");
    private final static String fileName = "./src/test/resources/object/serializeTest.bin";
    private static final Serializer SERIALIZER = new Serializer();
    private static final Random RANDOM = new Random();
    private final int NUMBER_OF_OBJECT;

    @Before
    public void setUp() throws Exception {
        Files.createDirectories(FILE_PATH);
        Files.createFile(Paths.get(fileName));
    }

    @After
    public void tearDown() throws Exception {
        // В конце тестирования файл удаляется
        Files.deleteIfExists(Paths.get(fileName));
    }

    public long defaultSerializeTime;
    public long defaultDeserializeTime;
    public long defaultMemory;

    @Test
    public void testDefaultSerialize() throws IOException {
        // Создается лист животных
        List<Animal> animalList = new ArrayList<>();
        // Генерируется n рандомных животных
        for (int i = 0; i < NUMBER_OF_OBJECT; i++) {
            animalList.add(RandomAnimal.getAnimal());
        }
        // Считывается время сериализации и десериализации
        long startInput = System.currentTimeMillis();
        SERIALIZER.defaultSerialize(animalList, fileName);
        long finishInput = System.currentTimeMillis();
        List<Animal> deserializedAnimalList = SERIALIZER.defaultDeserialize(fileName);
        long finishOutput = System.currentTimeMillis();
        // Тестируются данные - записалось ли то, что записывали
        assertEquals(animalList, deserializedAnimalList);

        setDefaultSerializeTime(finishInput - startInput);
        setDefaultDeserializeTime(finishOutput - finishInput);
        setDefaultMemory(Files.size(Paths.get(fileName)));
    }

    private void setDefaultSerializeTime(long serializeTime) {
        this.defaultSerializeTime = serializeTime;
    }

    private void setDefaultDeserializeTime(long serializeTime) {
        this.defaultDeserializeTime = serializeTime;
    }

    private void setDefaultMemory(long memory) {
        this.defaultMemory = memory;
    }

    public long customSerializeTime;
    public long customDeserializeTime;
    public long customMemory;

    @Test
    public void testCustomSerialize() throws IOException {
        List<Animal> animalList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_OBJECT; i++) {
            animalList.add(RandomAnimal.getAnimal());
        }
        long startInput = System.currentTimeMillis();
        SERIALIZER.customSerialize(animalList, fileName);
        long finishInput = System.currentTimeMillis();
        List<Animal> deserializedAnimalList = SERIALIZER.customDeserialize(fileName);
        long finishOutput = System.currentTimeMillis();
        assertEquals(animalList, deserializedAnimalList);

        setCustomSerializeTime(finishInput - startInput);
        setCustomDeserializeTime(finishOutput - finishInput);
        setCustomMemory(Files.size(Paths.get(fileName)));
    }

    private void setCustomSerializeTime(long serializeTime) {
        this.customSerializeTime = serializeTime;
    }

    private void setCustomDeserializeTime(long serializeTime) {
        this.customDeserializeTime = serializeTime;
    }

    private void setCustomMemory(long memory) {
        this.customMemory = memory;
    }

    public long externalSerializeTime;
    public long externalDeserializeTime;
    public long externalMemory;

    @Test
    public void testSerializeWithExternalize() throws IOException {
        List<AnimalExternalizable> animalExternalizeList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_OBJECT; i++) {
            animalExternalizeList.add(RandomAnimal.getAnimalExternalize());
        }
        long startInput = System.currentTimeMillis();
        SERIALIZER.serializeWithExternalizable(animalExternalizeList, fileName);
        long finishInput = System.currentTimeMillis();
        List<AnimalExternalizable> desAnimalExternalizeList = SERIALIZER.deserializeWithExternalizable(fileName);
        long finishOutput = System.currentTimeMillis();
        assertEquals(animalExternalizeList, desAnimalExternalizeList);

        setExternalSerializeTime(finishInput - startInput);
        setExternalDeserializeTime(finishOutput - finishInput);
        setExternalMemory(Files.size(Paths.get(fileName)));
    }

    private void setExternalSerializeTime(long serializeTime) {
        this.externalSerializeTime = serializeTime;
    }

    private void setExternalDeserializeTime(long serializeTime) {
        this.externalDeserializeTime = serializeTime;
    }

    private void setExternalMemory(long memory) {
        this.externalMemory = memory;
    }

    public long methodsSerializeTime;
    public long methodsDeserializeTime;
    public long methodsMemory;

    @Test
    public void testSerializeWithMethods() throws IOException {
        List<AnimalWithMethods> animalWithMethodsList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_OBJECT; i++) {
            animalWithMethodsList.add(RandomAnimal.getAnimalWithMethods());
        }
        long startInput = System.currentTimeMillis();
        SERIALIZER.serializeWithMethods(animalWithMethodsList, fileName);
        long finishInput = System.currentTimeMillis();
        List<AnimalWithMethods> desAnimalWithMethodsList = SERIALIZER.deserializeWithMethods(fileName);
        long finishOutput = System.currentTimeMillis();
        assertEquals(animalWithMethodsList, desAnimalWithMethodsList);

        setMethodsSerializeTime(finishInput - startInput);
        setMethodsDeserializeTime(finishOutput - finishInput);
        setMethodsMemory(Files.size(Paths.get(fileName)));
    }

    private void setMethodsSerializeTime(long serializeTime) {
        this.methodsSerializeTime = serializeTime;
    }

    private void setMethodsDeserializeTime(long serializeTime) {
        this.methodsDeserializeTime = serializeTime;
    }

    private void setMethodsMemory(long memory) {
        this.methodsMemory = memory;
    }

    private static class RandomAnimal {
        private static final int MIN_NAME_LENGTH = 3;
        private static final int MAX_NAME_LENGTH = 10;
        private static final int MAX_AGE = 100;
        private static final int ANIMAL_COUNT = AnimalType.values().length;

        public static Animal getAnimal() {
            if (RANDOM.nextBoolean()) {
                return null;
            }
            return new Animal(RandomAnimal.getName(), RandomAnimal.getAge(),
                    RandomAnimal.getIsFriendly(), RandomAnimal.getIsWarmBlooded(),
                    RandomAnimal.getAnimalType(), RandomAnimal.getPopulation());
        }

        public static AnimalExternalizable getAnimalExternalize() {
            if (RANDOM.nextBoolean()) {
                return null;
            }
            return new AnimalExternalizable(RandomAnimal.getName(), RandomAnimal.getAge(),
                    RandomAnimal.getIsFriendly(), RandomAnimal.getIsWarmBlooded(),
                    RandomAnimal.getAnimalType(), RandomAnimal.getPopulationExternalizable());
        }

        public static AnimalWithMethods getAnimalWithMethods() {
            if (RANDOM.nextBoolean()) {
                return null;
            }
            return new AnimalWithMethods(RandomAnimal.getName(), RandomAnimal.getAge(),
                    RandomAnimal.getIsFriendly(), RandomAnimal.getIsWarmBlooded(),
                    RandomAnimal.getAnimalType(), RandomAnimal.getPopulationWithMethods());
        }

        private static String getName() {
            char[] name = new char[RANDOM.nextInt(MAX_NAME_LENGTH - MIN_NAME_LENGTH) + MIN_NAME_LENGTH];
            name[0] = (char) (RANDOM.nextInt(26) + 'A');
            for (int i = 1; i < name.length; i++) {
                name[i] = (char) (RANDOM.nextInt(26) + 'a');
            }
            return new String(name);
        }

        private static int getAge() {
            return RANDOM.nextInt(MAX_AGE);
        }

        private static boolean getIsFriendly() {
            return RANDOM.nextBoolean();
        }

        private static boolean getIsWarmBlooded() {
            return RANDOM.nextBoolean();
        }

        private static AnimalType getAnimalType() {
            return AnimalType.values()[RANDOM.nextInt(ANIMAL_COUNT)];
        }

        private static Population getPopulation() {
            if (RANDOM.nextBoolean()) {
                return null;
            }
            return new Population(getName(), RANDOM.nextInt(Integer.MAX_VALUE) + 1, RANDOM.nextInt(Integer.MAX_VALUE) + 1);
        }

        private static PopulationExternalizable getPopulationExternalizable() {
            if (RANDOM.nextBoolean()) {
                return null;
            }
            return new PopulationExternalizable(getName(), RANDOM.nextInt(Integer.MAX_VALUE) + 1, RANDOM.nextInt(Integer.MAX_VALUE) + 1);
        }

        private static PopulationWithMethods getPopulationWithMethods() {
            if (RANDOM.nextBoolean()) {
                return null;
            }
            return new PopulationWithMethods(getName(), RANDOM.nextInt(Integer.MAX_VALUE) + 1, RANDOM.nextInt(Integer.MAX_VALUE) + 1);
        }
    }
}