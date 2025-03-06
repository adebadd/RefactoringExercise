import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {
    private RandomFile application;
    private File file;
    private String generatedFileName;
    private boolean changesMade;

    public FileManager() {
        application = new RandomFile();
        createRandomFile();
    }

    public RandomFile getApplication() {
        return application;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File f) {
        file = f;
    }

    public String getGeneratedFileName() {
        return generatedFileName;
    }

    public boolean isChangesMade() {
        return changesMade;
    }

    public void setChangesMade(boolean changes) {
        changesMade = changes;
    }

    public void openFile(EmployeeDetails parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open");
        fc.setFileFilter(new FileNameExtensionFilter("dat files (*.dat)", "dat"));
        File newFile;
        if (file.length() != 0 || parent.isChange()) {
            int returnVal = JOptionPane.showOptionDialog(parent, "Do you want to save changes?", "Save",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            if (returnVal == JOptionPane.YES_OPTION) {
                saveFile(parent);
            }
        }
        int returnVal = fc.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            newFile = fc.getSelectedFile();
            if (file.getName().equals(generatedFileName)) {
                file.delete();
            }
            file = newFile;
            application.openReadFile(file.getAbsolutePath());
            parent.firstRecord();
            parent.displayRecords(parent.getCurrentEmployee());
            application.closeReadFile();
        }
    }

    public void saveFile(EmployeeDetails parent) {
        if (file.getName().equals(generatedFileName)) {
            saveFileAs(parent);
        } else {
            if (parent.isChange()) {
                int returnVal = JOptionPane.showOptionDialog(parent, "Do you want to save changes?", "Save",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                if (returnVal == JOptionPane.YES_OPTION) {
                    if (!parent.getIdField().getText().equals("")) {
                        application.openWriteFile(file.getAbsolutePath());
                        parent.setCurrentEmployee(parent.getChangedDetails());
                        application.changeRecords(parent.getCurrentEmployee(), parent.getCurrentByteStart());
                        application.closeWriteFile();
                    }
                }
            }
            parent.displayRecords(parent.getCurrentEmployee());
            parent.setEnabled(false);
        }
    }

    public void saveFileAs(EmployeeDetails parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save As");
        fc.setFileFilter(new FileNameExtensionFilter("dat files (*.dat)", "dat"));
        fc.setApproveButtonText("Save");
        fc.setSelectedFile(new File("new_Employee.dat"));
        int returnVal = fc.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File newFile = fc.getSelectedFile();
            if (!parent.checkFileName(newFile)) {
                newFile = new File(newFile.getAbsolutePath() + ".dat");
                application.createFile(newFile.getAbsolutePath());
            } else {
                application.createFile(newFile.getAbsolutePath());
            }
            try {
                Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if (file.getName().equals(generatedFileName)) {
                    file.delete();
                }
                file = newFile;
            } catch (IOException e) {
            }
            changesMade = false;
        }
    }

    public void exitApp(EmployeeDetails parent) {
        if (file.length() != 0) {
            if (changesMade) {
                int returnVal = JOptionPane.showOptionDialog(parent, "Do you want to save changes?", "Save",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                if (returnVal == JOptionPane.YES_OPTION) {
                    saveFile(parent);
                    if (file.getName().equals(generatedFileName)) {
                        file.delete();
                    }
                    System.exit(0);
                } else if (returnVal == JOptionPane.NO_OPTION) {
                    if (file.getName().equals(generatedFileName)) {
                        file.delete();
                    }
                    System.exit(0);
                }
            } else {
                if (file.getName().equals(generatedFileName)) {
                    file.delete();
                }
                System.exit(0);
            }
        } else {
            if (file.getName().equals(generatedFileName)) {
                file.delete();
            }
            System.exit(0);
        }
    }

    private void createRandomFile() {
        generatedFileName = getFileName() + ".dat";
        file = new File(generatedFileName);
        application.createFile(file.getName());
    }

    private String getFileName() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
        StringBuilder sb = new StringBuilder();
        java.util.Random rnd = new java.util.Random();
        while (sb.length() < 20) {
            int index = (int) (rnd.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
