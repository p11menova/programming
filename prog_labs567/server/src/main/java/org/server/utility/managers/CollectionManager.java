package org.server.utility.managers;

import org.common.models.DBModels.TicketWithMetadata;
import org.common.models.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;


public class CollectionManager {
    // Для синхронизации доступа к коллекции использовать синхронизацию чтения и записи с помощью synchronized
    // !! HASHTABLE IS ALREADY SYNCHRONIZED,  SO ONLY ONE THREAD CAN ACCESS OR MODIFY IT AT A TIME !!
    /**
     * Хранимая коллекция
     */
    private static final TreeMap<Integer, TicketWithMetadata> TicketsCollection = new TreeMap<>();
    /**
     * Дата инициализации коллекции
     */
    private static LocalDateTime InitilizationDate = LocalDateTime.now();

    /**
     * Возвращает элемент коллекции с заданным айди
     *
     * @param id id элемента коллекции
     * @return элемент коллекции с заданным айди
     */
    public TicketWithMetadata getById(int id) {
        return TicketsCollection.get(id);
    }

    /**
     * Добавляет элемент в коллекцию
     *
     * @param ticket элемент добавляемый в коллекцию
     */
    public synchronized void addToCollection(TicketWithMetadata ticket) {
        if (!isIdTaken(ticket.getTicket().get_id())) {
            TicketsCollection.put(ticket.getTicket().get_id(), ticket);
        }
    }
    public synchronized void updateElem(TicketWithMetadata ticket){
        getTicketsCollection().put(ticket.getTicket().get_id(), ticket);

    }

    /**
     * Заполняет коллекцию данными из файла
     *
     * @param fileParsingManager менеджер работы с файлом
     */
//    public void loadCollectionFromFile(FileManager fileParsingManager) {
//        fileParsingManager.addDataToCollection(this);
//        InitilizationDate = LocalDateTime.now();
//    }

    /**
     * Возвращает размер коллекции
     *
     * @return размер коллекции
     */
    public static int getCollectionLength() {
        return TicketsCollection.size();
    }

    /**
     * Возвращает дату инициализации коллекции
     *
     * @return дата инициализации коллекции в формате yy-MM-dd hh:mm:ss
     */
    public static String getCreationDate() {
        return DateTimeFormatter.ofPattern("yy-MM-dd hh:mm:ss").format(InitilizationDate);
    }

    /**
     * Возвращает информацию о коллекции: хранимый тип, кол-во элементов, дата инициализации
     *
     * @return информация о коллекции
     */
    public static String getInfo() {
        return "информация о коллекции:\n-хранимый тип:" + Ticket.class.toString() +
                "\n-количество элементов:" + CollectionManager.getCollectionLength()
                + "\n-дата инициализации:" + CollectionManager.getCreationDate();
    }

    /**
     * Проверяет, занят ли заданный айди
     *
     * @param i id для проверки
     * @return true, если данный id занят, иначе else
     */
    public boolean isIdTaken(int i) {
        return !(TicketsCollection.get(i) == null);
    }

    /**
     * Удаляет элемент коллекции по данному id
     *
     * @param id id элемент с которым должен быть удален
     */
    public synchronized void removeWithId(int id) {
        System.out.println("remove with id" + id);
        TicketsCollection.remove(id);
    }

    /**
     * Очистить коллекцию
     */
    public void clear() {
        TicketsCollection.clear();
    }

    /**
     * Возвращает коллекцию
     *
     * @return хранимая коллекция TicketsCollection
     */
    public synchronized TreeMap<Integer, TicketWithMetadata> getTicketsCollection() {
        for(Map.Entry<Integer,TicketWithMetadata> entry : TicketsCollection.entrySet()) {
            if (entry.getValue() == null){
                TicketsCollection.remove(entry.getKey());
            }
        }
        return TicketsCollection;
    }
    public synchronized void setNull(int id){
        getTicketsCollection().put(id, null);

    }
    @Override
    public String toString() {
        if (getCollectionLength() == 0) return "коллекция еще пуста(";
        StringBuilder info = new StringBuilder();
        info.append(String.format("%12s | %6s | %10s | %14s | %20s | %6s | %14s | %8s | %20s | %6s",
                "author",
                "id",
                "name",
                "coords",
                "creation_date",
                "price",
                "is_refundable",
                "type",
                "person_birthday",
                "person_height"));
        info.append("\n");
        info.append("----------------------------------------------------------------------------------------------------------------------------------------------------\n");
        TreeMap<Integer, TicketWithMetadata> ht = getTicketsCollection();
        ht.forEach((key, value) -> info.append(ht.get(key).toString()));
        ;
        return info.toString();
    }
}
