public class AddRecordService {
    private FileManager fileManager;

    public AddRecordService(FileManager fm) {
        fileManager = fm;
    }

    public Employee createEmployee(int id, String pps, String surname, String firstName,
                                   char gender, String department, double salary, boolean fullTime) {
        return new Employee(id, pps, surname, firstName, gender, department, salary, fullTime);
    }

    public void saveEmployee(Employee e) {
        fileManager.getApplication().openWriteFile(fileManager.getFile().getAbsolutePath());
        long pos = fileManager.getApplication().addRecords(e);
        fileManager.getApplication().closeWriteFile();
    }
}
