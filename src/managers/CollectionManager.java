package managers;

import data.*;
import exceptions.*;
import data.generators.IdGenerator;
import system.Request;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

//import static data.managers.commands.ExecuteScriptCommand.scripts;

/**
 * The CollectionManager class manages a collection of Flat objects.
 */
public class CollectionManager {
    private static ArrayDeque<Flat> arrayDeque = new ArrayDeque<>();
    private static LocalDate date = LocalDate.now();

    /**
     * Constructor initializes the collection and sets the creation date.
     */
    public CollectionManager() {
        arrayDeque = new ArrayDeque<>();

    }


    public static Boolean isIdExist(Long id){
        for (Flat flat : arrayDeque) {
            if (Objects.equals(flat.getId(), id)) {
                return true;
            }
        }
        return false;
    }
    public static String getInfo(){
        StringBuilder line = new StringBuilder();
        line.append("Type - " + CollectionManager.getArrayDeque().getClass().getName()+"\n");
        line.append("Count of Flats - " + CollectionManager.getArrayDeque().size()+ "\n");
        line.append("Init date - " + date);
        return line.toString();
    }

    /**
     * Sets a new collection of ArrayDeque.
     *
     * @param arrayDeque the new collection
     */
    public static void setArrayDeque(ArrayDeque<Flat> arrayDeque) {
        CollectionManager.arrayDeque = arrayDeque;
    }

    /**
     * Adds a new Flat object to the collection.
     *
     * @param flat the new Flat object
     */
    public static void add(Flat flat) {
        arrayDeque.add(flat);
    }

    /**
     * Clears the collection.
     */
    public static void clear() {
        arrayDeque.clear();
    }

    /**
     * Gets the current collection.
     *
     * @return the current collection
     */
    public static ArrayDeque<Flat> getArrayDeque() {
        return arrayDeque;
    }


    /**
     * Saves the collection to a file specified by the given file path.
     *
     * @param filepath the path to the file for saving the collection
     */
    public static void saveCollection(String filepath) {
        try {
            try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(filepath))) {
                CSVConverter.serializeToCSV(arrayDeque, writer);
            } catch (FileNotFoundException e) {
                System.out.println("Файл " + filepath + " не найден. Коллекция не сохранена");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом " + filepath + ". Коллекция не сохранена");
        }
    }

    /**
     * Counts the number of Flat objects in the collection with a View greater than the specified value.
     *
     * @param request the command arguments where the second element is the view to compare against
     * @throws ArrayDeqIsEmpty if the collection is empty
     */
    public static String countGreaterThanView(Request request) throws ArrayDeqIsEmpty {
        int counter = 0;
        if (!request.getPassword().equals("")) {
            if (View.contains(request.getPassword().toUpperCase())) {
                View ourView = View.valueOf(request.getPassword().toUpperCase());
                int ourNumber = ourView.ordinal();
                if (CollectionManager.getArrayDeque().isEmpty()) {
                    throw new ArrayDeqIsEmpty(null);
                } else {
                    for (Flat flat : CollectionManager.getArrayDeque()) {
                        if (flat.getView().ordinal() > ourNumber) {
                            counter++;
                        }
                    }
                }
                return counter + " элементов с view больше " + request.getPassword();
            } else {
                return "Такого View нету. Выберите View: STREET, YARD, PARK, NORMAL";
            }
        }
        return null;
    }
    public static Set<String> scripts = new HashSet<>();
    /**
     * Executes a script from a file specified by the given arguments.
     *
     * @throws FileNotFoundException if the script file is not found
     */
    public static String executeScript(Request request) throws FileNotFoundException {
        String file_path = request.getPassword();
        StringBuilder result = new StringBuilder();

        Scanner scanner = new Scanner(new FileReader("/Users/lizakazantseva/IdeaProjects/laba_6_server/src/" + file_path));
        try{
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                request.setMessage(line);
                if(line.split(" ")[0].equals("add")){
                    String name = scanner.nextLine();
                    Double coordinateX = Double.valueOf(scanner.nextLine());
                    Long coordinateY = Long.valueOf(scanner.nextLine());
                    Coordinates coordinates = new Coordinates(coordinateX, coordinateY);
                    Long area = Long.valueOf(scanner.nextLine());
                    int numberOfRooms = Integer.valueOf(scanner.nextLine());
                    Integer livingSpace = Integer.valueOf(scanner.nextLine());
                    Long numberOfBathrooms = Long.valueOf(scanner.nextLine());
                    View view = View.valueOf(scanner.nextLine().toUpperCase());
                    House house = new House();

                    String nameHouse = scanner.nextLine();
                    Integer yearHouse = Integer.valueOf(scanner.nextLine());
                    Long numberOfFloors = Long.valueOf(scanner.nextLine());
                    Long numberOfFlatOnFloor = Long.valueOf(scanner.nextLine());
                    house.setName(nameHouse);
                    house.setYear(yearHouse);
                    house.setNumberOfFloors(numberOfFloors);
                    house.setNumberOfFlatsOnFloor(numberOfFlatOnFloor);

                    Flat flat = new Flat(IdGenerator.generateid());
                    flat.setName(name);
                    flat.setCoordinates(coordinates);
                    flat.setArea(area);
                    flat.setNumberOfRooms(numberOfRooms);
                    flat.setLivingSpace(livingSpace);
                    flat.setNumberOfBathrooms(numberOfBathrooms);
                    flat.setView(view);
                    flat.setHouse(house);
                    CollectionManager.add(flat);
                    result.append("Команда add выполнена. Элемент добавлен\n");


                }  else if(line.split(" ")[0].equals("update")){
                    Long neededId = Long.valueOf(scanner.nextLine());
                    Object[] narray = CollectionManager.getArrayDeque().toArray();
                    String name = scanner.nextLine();
                    Double coordinateX = Double.valueOf(scanner.nextLine());
                    Long coordinateY = Long.valueOf(scanner.nextLine());
                    Coordinates coordinates = new Coordinates(coordinateX, coordinateY);
                    Long area = Long.valueOf(scanner.nextLine());
                    int numberOfRooms = Integer.valueOf(scanner.nextLine());
                    Integer livingSpace = Integer.valueOf(scanner.nextLine());
                    Long numberOfBathrooms = Long.valueOf(scanner.nextLine());
                    View view = View.valueOf(scanner.nextLine());
                    House house = new House();
                    String nameHouse = scanner.nextLine();
                    Integer yearHouse = Integer.valueOf(scanner.nextLine());
                    Long numberOfFloors = Long.valueOf(scanner.nextLine());
                    Long numberOfFlatOnFloor = Long.valueOf(scanner.nextLine());

                    house.setName(nameHouse);
                    house.setYear(yearHouse);
                    house.setNumberOfFloors(numberOfFloors);
                    house.setNumberOfFlatsOnFloor(numberOfFlatOnFloor);

                    Flat newflat = new Flat(IdGenerator.generateid());
                    newflat.setName(name);
                    newflat.setCoordinates(coordinates);
                    newflat.setArea(area);
                    newflat.setNumberOfRooms(numberOfRooms);
                    newflat.setLivingSpace(livingSpace);
                    newflat.setNumberOfBathrooms(numberOfBathrooms);
                    newflat.setView(view);
                    newflat.setHouse(house);

                    narray[(int) (neededId - 1)] = newflat;

                    ArrayDeque<Flat> updatedArraydeque = new ArrayDeque<>();

                    for (Object flat : narray) {
                        updatedArraydeque.add((Flat) flat);
                    }
                    CollectionManager.setArrayDeque(updatedArraydeque);
                    result.append("Команда update выполнена. Элемент обновлен\n");

                }
                else{
                    CommandManager.startExecuting(request);
                    result.append("Команда " + line.split(" ")[0] + " выполнена\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("нет такого файла");
        } catch (UnknownCommandException | InvalidInputException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result.toString();
    }

    /**
     * Filters and prints Flat objects with a living space less than the specified value.
     * @param request the command arguments where the second element is the living space to compare against
     * @throws ArrayDeqIsEmpty if the collection is empty
     */
    public static String filterLessThanLivingSpace(Request request) throws ArrayDeqIsEmpty {
        try{
            if (!request.getPassword().equals("")) {
                int ourLivingSpace = Integer.parseInt(request.getPassword());
                if (CollectionManager.getArrayDeque().isEmpty()) {
                    throw new ArrayDeqIsEmpty("Коллекция пуста");
                } else {
                    for (Flat flat : CollectionManager.getArrayDeque()) {
                        if (flat.getLivingSpace() < ourLivingSpace) {
                            return flat.toString();
                        }else{
                            return "Wrong living_space format";
                        }
                    }
                }
            }else{
                return "Wrong living_space format";
            }
        }catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }



    /**
     * Removes a Flat object from the collection by its ID.
     * @param request the command arguments where the second element is the ID of the Flat to remove
     */

    /**
     * Prints all Flat objects in the collection.
     * @param request the command arguments (unused)
     */
    public static String showCommand(Request request) {
        StringBuilder line = new StringBuilder();
        ArrayDeque<Flat> flats = CollectionManager.getArrayDeque();
        if (flats.isEmpty()) {
            return "Нет квартир";
        } else {
            for (Flat flat : flats) {
                line.append(flat);
            }
        }
        return line.toString();
    }

    /**
     * Updates a Flat object in the collection by its ID.
     *
     * @param request the command arguments where the second element is the ID of the Flat to update
     */

    public static void updateCommand(Request request){
        long id = Long.parseLong(request.getPassword());
        try {
            for (Flat flat : arrayDeque) {
                if (flat.getId() == id) {
                    arrayDeque.remove(flat);
                    Flat newFlat = request.getFlat();
                    newFlat.setId(id);
                    arrayDeque.add(newFlat);
                    return;
                }else{
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

