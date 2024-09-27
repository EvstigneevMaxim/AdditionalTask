package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class FileOrganizer {

    public static void main(String[] args) {
        // Проверяем, передан ли путь к каталогу
        if (args.length != 1) {
            System.out.println("Заданный каталог: java FileOrganizer <directory_path>");
            return;
        }

        // Получаем путь к каталогу
        String directoryPath = args[0];
        File directory = new File(directoryPath);

        // Проверяем, существует ли каталог и является ли он директорией
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Вы указали неверный путь к каталогу");
            return;
        }

        // Организуем файлы в каталоге
        organizeFilesByType(directory);
    }

    public static void organizeFilesByType(File directory) {
        // Храним файлы по типам
        Map<String, StringBuilder> fileTypeMap = new HashMap<>();

        // Получаем список всех файлов в каталоге
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // Получаем расширение файла
                    String extension = getFileExtension(file);

                    // Создаем подкаталог для данного расширения, если его еще нет
                    File subDirectory = new File(directory, extension);
                    if (!subDirectory.exists()) {
                        subDirectory.mkdir();
                    }

                    // Перемещаем файл в соответствующий подкаталог
                    try {
                        Path sourcePath = file.toPath();
                        Path destinationPath = new File(subDirectory, file.getName()).toPath();
                        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                        // Добавляем информацию о файле в карту
                        fileTypeMap.computeIfAbsent(extension, k -> new StringBuilder())
                                .append(file.getName())
                                .append("\n");
                    } catch (IOException e) {
                        System.out.println("Не удалось переместить файл: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }

            // Выводим список файлов по типам
            for (Map.Entry<String, StringBuilder> entry : fileTypeMap.entrySet()) {
                System.out.println("Файлы по типам " + entry.getKey() + ":");
                System.out.println(entry.getValue().toString());
            }
        }
    }

    // Метод для получения расширения файла
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "unknown";  // Файлы без расширения
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
