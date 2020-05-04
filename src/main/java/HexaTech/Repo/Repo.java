/**
 * @file Repo
 * @version 0.0.1
 * @type java
 * @data 2020-04-25
 * @author Luca Marcon
 * @email hexatech016@gmail.com
 * @license MIT
 */

package HexaTech.Repo;

import HexaTech.Filesystem.FileSystemInterface;
import HexaTech.Entities.BDL;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class used to submit actions from user to file system.
 */
public class Repo implements RepoInterface {
    List<String> list;
    FileSystemInterface fileSystem;

    /**
     * Repo class constructor.
     * @param fileSystemInterface FileSystemInterface - used to communicate with file system.
     */
    public Repo(FileSystemInterface fileSystemInterface){
        list =new ArrayList<>();
        this.fileSystem=fileSystemInterface;
    }

    /**
     * Returns the list of added documents' paths.
     * @return List<String> - added documents.
     */
    public List<String> getLista(){
        return list;
    }

    /**
     * Loads a new document from file system.
     * @throws IOException if an error accurs during file loading process.
     */
    public void returnPath() throws IOException {
        String temp=fileSystem.importPath();
        if(!temp.equalsIgnoreCase("")) {
            list.add(temp);
            saveDoc();
        }//if
    }//returnPath

    /**
     * Saves the doucment's path into a backup file.
     * @throws IOException if occurs an error while creating the file or writing into it.
     */
    public void saveDoc() throws IOException {
        StringBuilder temp= new StringBuilder();
        for(String stringa: list)
            temp.append(stringa).append("\n");
        this.fileSystem.saveDoc(temp.toString(), ".\\temp.txt");
    }//saveDoc

    /**
     * Loads content from a backup file and restore it.
     * @throws IOException if the backup file doesn't exist.
     */
    public void loadBackUp() throws IOException {
        Scanner s = new Scanner(new File(".\\temp.txt"));
        while (s.hasNextLine()){
            list.add(s.nextLine());
        }//while
        s.close();
    }//loadBackUp

    /**
     * Deletes the specified document.
     * @param doc string - path to the document to be deleted.
     * @return boolean - false if an error occurs, true if not.
     */
    public boolean delete(String doc){
        return fileSystem.deleteDoc(doc);
    }

    /**
     * Extrapolates content from a document.
     * @param path string - document's path.
     * @return string - document's content.
     * @throws IOException if the specified document doesn't exist.
     */
    public String returnContentFromTxt(String path) throws IOException {
        return fileSystem.getContentFromPath(path);
    }

    /**
     * Saves the BDL object into different files.
     * @param bdl BDL - object to be saved.
     * @throws IOException
     */
    public void saveBDL(BDL bdl) throws IOException {
        fileSystem.saveDoc(bdl.toString(),".\\BDLreadble.txt");
        fileSystem.saveDoc(bdl.sostToCVS(),".\\BDLsost.csv");
        fileSystem.saveDoc(bdl.verbToCVS(),".\\BDLverbs.csv");
        fileSystem.saveDoc(bdl.predToCVS(),".\\BDLpred.csv");
    }

    /**
     * Loads BDL content from different files and store a new BDL object.
     * @throws IOException if an error occurs while loading one or more file.
     */
    public void getBDLFromContentPath() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String sosts=fileSystem.getContentFromPath(".\\BDLsost.txt");
        String verbs=fileSystem.getContentFromPath(".\\BDLverbs.txt");
        String pred=fileSystem.getContentFromPath(".\\BDLpred.txt");
        Map<String,Integer> sost=mapper.readValue(sosts, HashMap.class);
        Map<String,Integer> verb=mapper.readValue(verbs, HashMap.class);
        Map<String,Integer> preds=mapper.readValue(pred, HashMap.class);
        BDL BDLtoGet=new BDL(sost,verb,preds);
        System.out.println(BDLtoGet.toString());
    }//getBDLFromContentPath

    /**
     * Verifies is the backup document still exists.
     * @return boolean - true if the document exists, false if not.
     */
    public boolean checkThereAreDoc() {
        return fileSystem.existsDoc(".\\temp.txt");
    }

}//Repo
