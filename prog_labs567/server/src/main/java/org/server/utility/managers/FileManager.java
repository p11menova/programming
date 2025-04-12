//package org.server.utility.managers;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.*;
//import com.fasterxml.jackson.databind.exc.InvalidFormatException;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//
//import org.example.models.Ticket;
//import org.example.utility.ZonedDateTimeDeserializer;
//import org.server.App;
//import org.server.models.Tickets;
//
//import java.io.*;
//import java.time.LocalDateTime;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Hashtable;
//
///**
// * Класс управления работы с файлом коллекции: прочитать коллекцию из файла, загрузить коллекцию в файл (расширение .xml)
// */
//public class FileManager {
//    /**
//     * Название файла
//     */
//    public String filename;
//    public String DEFAULT_FILENAME = "tickets_collection.xml";
//
//    /**
//     * Конструктор FileManager'a
//     *
//     * @param filename название файла, хранящего коллекцию
//     */
//    public FileManager(String filename) {
//        this.filename = filename;
//
//    }
//
//    /**
//     * Читает текст из файла
//     *
//     * @return текст из файла
//     */
//    public String readFileText() {
//        try {
//            if (this.filename == null || this.filename.isBlank()) {
//                App.logger.warning("значение переменной окружения не установлено. коллекция не будет заполнена данными из файла");
//                return "";
//            }
//            File file = new File(this.filename);
//            if (!file.canRead()) {
//                App.logger.severe("файл не имеет прав на чтение(. коллекция не может быть прочитана");
//                return "";
//            }
//            FileReader reader = new FileReader(this.filename);
//            BufferedReader br = new BufferedReader(reader);
//            String line;
//            StringBuilder file_text = new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                file_text.append(line);
//            }
//            return file_text.toString();
//        } catch (IOException e) {
//            App.logger.severe(e.getMessage());
//        }
//        return "";
//
//    }
//
//    /**
//     * Преобразует данные из файла в экземпляры коллекции и добавляет их в неё
//     *
//     * @param collectionManager менеджер управления коллекцией
//     */
//    public void addDataToCollection(CollectionManager collectionManager) {
//        try {
//            if (filename == null || filename.isBlank()){
//                App.logger.warning("переменная окружения с названием файла не задана.\nколлекция не заполнится данными из файла(");
//                return;
//            }
//            XmlMapper xmlMapper = new XmlMapper();
//            xmlMapper.registerModule(new JavaTimeModule());
//            SimpleModule module = new SimpleModule();
//            LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
//
//            module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
//            module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
//            xmlMapper.registerModule(module);
//
//            String readContent = this.readFileText().trim().replaceFirst("\uFFFD", "");
//            Tickets data = xmlMapper.readValue(readContent, Tickets.class);
//            if (data.addToCollectionIfOkay(collectionManager))
//                App.logger.info("тооп! валидные билеты из файла " + new File(filename).getName() + " добавлены в коллекцию!");
//            else App.logger.info("все билеты в файле оказались не валидны и не добавлены в коллекцию(");
//
//
//        } catch (InvalidFormatException e) {
//            App.logger.severe("введены невалидные данные(( (несоответствие типа поля)");
//        } catch (IOException e) {
//            App.logger.severe(e.getMessage());
//        }
//
//    }
//    /**
//     * Записывает коллекцию в файл
//     *
//     * @param collectionManager менеджер коллекции
//     * @return true, если запись прошла успешно, иначе false
//     */
//
//    public boolean writeCollectionToFile(CollectionManager collectionManager) {
//        XmlMapper xmlMapper = new XmlMapper();
//
//        xmlMapper.registerModule(new JavaTimeModule());
//        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//       // xmlMapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy HH:mm"));
//
//        Hashtable<Integer, Ticket> ht = collectionManager.getTicketsCollection();
//        Tickets tickets = new Tickets();
//        System.out.println(tickets);
//        ht.forEach((k, v) -> tickets.addToList(v));
//
//        try {
//            String data = xmlMapper.writeValueAsString(tickets);
//            File file;
//            if (this.filename == null || this.filename.isBlank()) {
//                App.logger.warning("переменная окружения с названием файла не задана.\nколлекция будет записана в файл с названием по умолчанию:" + DEFAULT_FILENAME);
//                file = new File(DEFAULT_FILENAME);
//            } else {
//                file = new File(this.filename);
//            }
//            if (!file.canWrite()) {
//                App.logger.severe("файл не имеет прав на запись( коллекция не будет записана(");
//                return false;
//            }
//            OutputStream outputStream = new FileOutputStream(file);
//
//            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
//            if (data == null) return true;
//
//            bufferedOutputStream.write(data.getBytes());
//            bufferedOutputStream.flush();
//            bufferedOutputStream.close();
//
//
//            App.logger.info("топчик! коллекция успешно сохранена в файл " + file.getName());
//            return true;
//        } catch (JsonProcessingException e) {
//            App.logger.severe(e.getMessage());
//        } catch (FileNotFoundException e) {
//            App.logger.severe("файл с таким названием не найден.. или нет прав доступа(");
//        } catch (IOException e) {
//            App.logger.severe("ошибка сохранения коллекции в файл(");
//        }
//        return false;
//    }
//}
//
//
