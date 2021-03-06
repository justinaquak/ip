import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    /**
     * The reading of data as well as the loading of data in the file is handled by this method
     * @throws IOException when an error occurs when reading the file
     */
    public static void readFile() throws IOException {
        File data = new File("data/savedata.txt");
        ArrayList<Task> tasks = new ArrayList<>();

        Scanner sc = new Scanner(data);
        sc.nextLine(); // skip the first introductory line
        while (sc.hasNext()) {
            String taskInput = sc.nextLine();
            String[] taskComponents = taskInput.split(" \\| ");
            switch (taskComponents[0]){
                case "E":
                    Event newEvent = readEvent(taskComponents);
                    Task.addNewTask(newEvent);
                    break;
                case "D":
                    Deadline newDeadline = readDeadline(taskComponents);
                    Task.addNewTask(newDeadline);
                    break;
                case "T":
                    Todo newTask = readTodo(taskComponents);
                    Task.addNewTask(newTask);
                    break;
            }
        }
    }

    /**
     * @param filePath is the file's destination path
     * @param textToAppend is the contents to be added to the file's current contents
     * @throws IOException if any error occurs in the process
     */
    public static void appendToFile(String filePath, String textToAppend) throws IOException {
        FileWriter fw = new FileWriter(filePath, true); // create a FileWriter in append mode
        fw.write(textToAppend + '\n');
        fw.close();
    }

    /**
     * Whenever the user adds or removes tasks, this method modifies the file
     * @throws IOException when an error occurs with reading the file
     */
    public static void saveToFile() throws IOException {
        String filePath = "data/savedata.txt";
        FileWriter firstLineWriter = new FileWriter(filePath);
        firstLineWriter.write("Duke's Most Recent Task List\n");
        firstLineWriter.close();

        for (int i = 0; i < Task.getTaskNumber(); i++){
            Task task = Task.getTaskList().get(i);
            String textToAppend = "";
            if (task instanceof Event) {
                textToAppend = writeEvent((Event) task);
            } else if (task instanceof Deadline) {
                textToAppend = writeDeadline((Deadline) task);
            } else {
                textToAppend = writeTodo((Todo) task);
            }
            appendToFile(filePath, textToAppend);
        }
    }


    /**
     * @param taskComponents are the elements in Todo
     * @return todo object and adds it to the main list
     */
    public static Todo readTodo(String[] taskComponents) {
        Todo newTodo = new Todo(taskComponents[2].trim());
        boolean isDone = taskComponents[1].equals("1");
        if (isDone) {
            newTodo.markAsDone();
            Task.increaseNumberOfCompletedTasks();
        }
        return newTodo;
    }

    /**
     * @param taskComponents are the elements in Deadline
     * @return a valid deadline to the main list
     */
    public static Deadline readDeadline(String[] taskComponents) {
        String[] deadlineData = taskComponents[2].split("\\|\\|");
        Deadline newDeadline = new Deadline(deadlineData[0].trim(), deadlineData[1].trim());
        boolean isDone = taskComponents[1].equals("1");
        if (isDone) {
            newDeadline.markAsDone();
            Task.increaseNumberOfCompletedTasks();
        }
        return newDeadline;
    }

    /**
     * @param taskComponents are the elements in Event
     * @return a valid deadline to the main list
     */
    public static Event readEvent(String[] taskComponents) {
        String[] eventData = taskComponents[2].split("\\|\\|");
        Event newEvent = new Event(eventData[0].trim(), eventData[1].trim());
        boolean isDone = taskComponents[1].equals("1");
        if (isDone) {
            newEvent.markAsDone();
            Task.increaseNumberOfCompletedTasks();
        }
        return newEvent;
    }

    /**
     * @param todo is the task to be saved in the storage file
     * @return a String
     */
    public static String writeTodo(Todo todo) {

        return "T | " +
                isDoneWriter(todo) +
                todo.getTaskDescription();
    }

    /**
     * @param deadline is the Deadline to be saved in the storage file
     * @return a String
     */
    public static String writeDeadline(Deadline deadline) {

        return "D | " +
                isDoneWriter(deadline) +
                deadline.getTaskDescription() +
                " || " +
                deadline.getDateTime();

    }

    /**
     * @param event is the Event to be saved in the storage file
     * @return a String
     */
    public static String writeEvent(Event event) {
        return "E | " +
                isDoneWriter(event) +
                event.getTaskDescription() +
                " || " +
                event.getDateTime();
    }

    /**
     * @param task is the task in question, to check whether it is done
     * @return a String to indicate if the task has been completed
     */
    public static String isDoneWriter(Task task){
        if (task.getIsDone()) {
            return "1 | ";
        }
        else {
            return "0 | ";
        }
    }

}