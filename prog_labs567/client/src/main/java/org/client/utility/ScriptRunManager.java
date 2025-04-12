package org.client.utility;


import org.client.exceptions.NoSuchElementException;
import org.client.exceptions.WrongFileRightException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class ScriptRunManager {
    public static Stack<String> scriptNames = new Stack<>();
    public static Stack<Scanner> scriptScanners = new Stack<>();

    public static boolean contains(String name){
        return scriptNames.contains(name);
    }
    public static void openFileScanner(String filename) throws FileNotFoundException, WrongFileRightException {
        File file = new File(filename);
        if (!file.canRead()) throw new WrongFileRightException();
        Scanner scanner = new Scanner(file);
        scriptNames.push(filename);
        scriptScanners.push(scanner);
    }
    public static Scanner getScriptScanner() throws NoSuchElementException, EmptyStackException {
        if (!scriptScanners.peek().hasNextLine())
            ScriptRunManager.pop(); // если в предыдущем сканере закончились строки переключаемся на след
        return scriptScanners.peek();
    }
    public static void pop(){
        scriptNames.pop();
        scriptScanners.pop();
    }
}
