//package org.server.commands.serverCommands;
//
//import org.server.utility.managers.CollectionManager;
//import org.server.utility.managers.FileManager;
//
///**
// * Команда сохранения коллекции в файл.
// */
//public class SaveCommand extends ServerCommand {
//    public CollectionManager collectionManager;
//    public FileManager fileManager;
//
//    public SaveCommand(String CommandName, CollectionManager collectionManager, FileManager fileManager) {
//        super(CommandName);
//        this.collectionManager = collectionManager;
//        this.fileManager = fileManager;
//    }
//
//    @Override
//    public boolean go() {
//        return this.fileManager.writeCollectionToFile(this.collectionManager);
//    }
//
//}
