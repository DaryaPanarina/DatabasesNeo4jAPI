package database.api_neo4j_cluster;

import POJO.*;
import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) {
        int id, amount, choice = 1;
        List<Student> result_student;
        List<Group> result_group;
        List<Scholarship> result_scholarship;
        List<Club> result_club;
        List<StudentClub> result_stClub;
        List<StudentScholarship> result_stSch;
        List<TeacherGroup> result_teachGr;
        List<ScholarshipStudent> result_schSt;
        Club clubNode;
        Scholarship scholarshipNode;
        String surname, name, patronymic, groupName;
        Student_crud student = new Student_crud();
        Teacher_crud teacher = new Teacher_crud();
        Group_crud group = new Group_crud();
        Club_crud club = new Club_crud();
        Scholarship_crud scholarship = new Scholarship_crud();
        Scanner scanner = new Scanner(System.in);
        
        LogManager.getLogManager().reset();
        while (choice > 0) {          
            System.out.println("\n0 - Завершение работы.");            
            //Student
            System.out.println("11 - Добавить студента.");
            System.out.println("12 - Поиск информации о студентах.");
            System.out.println("13 - Внести изменения в запись о студенте.");
            System.out.println("14 - Удалить студента.\n");
            
            //Teacher
            System.out.println("21 - Добавить преподавателя.");
            System.out.println("22 - Поиск информации о преподавателях.");
            System.out.println("23 - Изменить ФИО в записи о преподавателе.");
            System.out.println("24 - Удалить преподавателя.");
            System.out.println("25 - Группы преподавателя.\n");
            
            //Group
            System.out.println("31 - Добавить группу.");
            System.out.println("32 - Поиск информации о группах.");
            System.out.println("33 - Изменить данные в записи о группе.");
            System.out.println("34 - Удалить группу.\n");
            
            //Scholarship
            System.out.println("41 - Добавить тип стипендии.");
            System.out.println("42 - Поиск информации о стипендиях.");
            System.out.println("43 - Изменить данные в записи о типе стипендии.");
            System.out.println("44 - Удалить тип стипендии.");
            System.out.println("45 - Стипендии студентов.\n");
            
            //Club
            System.out.println("51 - Добавить студенческий клуб по интересам.");
            System.out.println("52 - Поиск информации о клубах.");
            System.out.println("53 - Изменить данные в записи о клубе.");
            System.out.println("54 - Удалить клуб.");
            System.out.println("55 - Студенты в клубе.\n");
            
            System.out.print("Выберите действие: ");
            choice = scanner.nextInt();
            
            switch (choice) {
                case 0:         //Exit
                    scanner.close();
                    break;
                case 11:        //Student
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    System.out.print("Введите фамилию: ");
                    surname = scanner.next();
                    System.out.print("Введите имя: ");
                    name = scanner.next();
                    System.out.print("Введите отчество: ");
                    patronymic = scanner.next();
                    System.out.print("Введите группу: ");
                    groupName = scanner.next();
                    if (student.createNode(id, surname, name, patronymic, groupName) == 0)
                        System.out.println("Студент добавлен.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 12:
                    System.out.println("1 - Найти студента по ID.");
                    System.out.println("2 - Получить количество студентов в базе.");
                    System.out.println("3 - Получить информацию о стипендии студента.");
                    System.out.println("4 - Получить список клубов, в которых состоит студент.");
                    System.out.print("Выберите действие: ");
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.print("Введите ID: ");
                            id = scanner.nextInt();
                            name = student.findById(id);
                            if (name.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат поиска: " + name);
                            break;
                        case 2:
                            id = student.getStudentsNumber();
                            if (id < 0) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Количество студентов: " + id);
                            break;
                        case 3:
                            System.out.print("Введите ID: ");
                            id = scanner.nextInt();
                            result_stSch = student.getStudentScholarship(id);
                            if (result_stSch.get(0).getType().equals("last")) {
                                System.out.println("Информация о стипендии студента отсутствует.");
                                break;
                            }                               
                            if (result_stSch.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат: ");
                            for (id = 0; id < result_stSch.size() - 1; id++) {
                                System.out.print("Стипенидия: " + result_stSch.get(id).getType()
                                        + ". Выплачивалась(выплачивается) с " + result_stSch.get(id).getSince());
                                if (!result_stSch.get(id).getUntil().equals("-"))
                                    System.out.println(" до " + result_stSch.get(id).getUntil());
                                else
                                    System.out.println();
                            }
                            break;
                        case 4:
                            System.out.print("Введите ID: ");
                            id = scanner.nextInt();
                            result_stClub = student.getStudentClubs(id);
                            if (result_stClub.get(0).getName().equals("last")) {
                                System.out.println("Студент не состоит в клубах.");
                                break;
                            }                               
                            if (result_stClub.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат: ");
                            for (id = 0; id < result_stClub.size() - 1; id++)
                                System.out.println("Клуб: " + result_stClub.get(id).getName() + ". Статус: " + result_stClub.get(id).getPosition());
                            break;
                    }
                    break;
                case 13:
                    System.out.print("Введите ID студента: ");
                    id = scanner.nextInt();
                    System.out.println("Если изменение поля не требуется, введите '-'");
                    System.out.print("Введите ФИО: ");
                    scanner.nextLine();
                    name = scanner.nextLine();
                    System.out.print("Введите группу: ");
                    groupName = scanner.next();
                    if (student.updateFullNameById(id, name, groupName) == 0)
                        System.out.println("Запись о студенте изменена.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 14:
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    if (student.deleteNodeById(id) == 0)
                        System.out.println("Студент удален.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 21:        //Teacher
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    System.out.print("Введите фамилию: ");
                    surname = scanner.next();
                    System.out.print("Введите имя: ");
                    name = scanner.next();
                    System.out.print("Введите отчество: ");
                    patronymic = scanner.next();
                    if (teacher.createNode(id, surname, name, patronymic) == 0)
                        System.out.println("Преподаватель добавлен.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 22:
                    System.out.println("1 - Найти преподавателя по ID.");
                    System.out.println("2 - Получить количество преподавателей в базе.");
                    System.out.print("Выберите действие: ");
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.print("Введите ID: ");
                            id = scanner.nextInt();
                            name = teacher.findById(id);
                            if (name.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат поиска: " + name);
                            break;
                        case 2:
                            id = teacher.getTeachersNumber();
                            if (id < 0) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Количество преподавателей: " + id);
                            break;
                    }
                    break;
                case 23:
                    System.out.print("Введите ID преподавателя, по которому обновить ФИО: ");
                    id = scanner.nextInt();
                    System.out.print("Введите ФИО: ");
                    scanner.nextLine();
                    name = scanner.nextLine();
                    if (teacher.updateFullNameById(id, name) == 0)
                        System.out.println("ФИО преподавателя изменено.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 24:
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    if (teacher.deleteNodeById(id) == 0)
                        System.out.println("Преподаватель удален.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 25:
                    System.out.println("1 - Добавить группу преподавателю.");
                    System.out.println("2 - Убрать группу у преподавателя.");
                    System.out.println("3 - Получить список групп преподавателя.");
                    System.out.print("Выберите действие: ");
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.print("Введите ID преподавателя: ");
                            id = scanner.nextInt();
                            System.out.print("Введите название группы: ");
                            groupName = scanner.next();
                            System.out.print("Введите преподаваемый предмет: ");
                            name = scanner.next();
                            if (teacher.createRelationship(id, groupName, name) == 0)
                                System.out.println("Группа назначена преподавателю.");
                            else
                                System.out.println("Ошибка во время выполения операции.");
                                    break;                        
                        case 2:
                            System.out.print("Введите ID преподавателя: ");
                            id = scanner.nextInt();
                            System.out.print("Введите название группы: ");
                            scanner.nextLine();
                            groupName = scanner.nextLine();
                            System.out.print("Введите преподаваемый предмет: ");
                            name = scanner.next();
                            if (teacher.deleteRelationship(id, groupName, name) == 0)
                                System.out.println("Группа откреплена.");
                            else
                                System.out.println("Ошибка во время выполения операции.");
                            break;
                        case 3:
                            System.out.print("Введите ID преподавателя: ");
                            id = scanner.nextInt();
                            result_teachGr = teacher.getTeacherGroups(id);
                            if (result_teachGr.get(0).getName().equals("last")) {
                                System.out.println("За преподавателем не закреплены группы.");
                                break;
                            }                               
                            if (result_teachGr.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат: ");
                            for (id = 0; id < result_teachGr.size() - 1; id++)
                                System.out.println("Группа: " + result_teachGr.get(id).getName()
                                        + ", преподаваемый предмет: " + result_teachGr.get(id).getSubject());
                            break;
                    } 
                    break;
                case 31:        //Group
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    System.out.print("Введите название учебной группы: ");
                    groupName = scanner.next();
                    if (group.createNode(id, groupName) == 0)
                        System.out.println("Группа добавлена.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 32:
                    System.out.println("1 - Найти название группы по ID.");
                    System.out.println("2 - Найти ID группы по названию.");
                    System.out.println("3 - Получить количество групп в базе.");
                    System.out.println("4 - Получить список всех групп в базе.");
                    System.out.println("5 - Получить список студентов в группе.");
                    System.out.println("6 - Получить список преподавателей группы.");
                    System.out.print("Выберите действие: ");
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.print("Введите ID: ");
                            id = scanner.nextInt();
                            groupName = group.findById(id);
                            if (groupName.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат поиска: " + groupName);
                            break;
                        case 2:
                            System.out.print("Введите название группы: ");
                            groupName = scanner.next();
                            id = group.findByName(groupName);
                            if (id < 0) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат поиска: " + id);
                            break;
                        case 3:
                            id = group.getGroupsNumber();
                            if (id < 0) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Количество групп: " + id);
                            break;
                        case 4:
                            result_group = group.getAllGroups();
                            if (result_group.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            if (result_group.get(0).getName().equals("last")) {
                                System.out.print("Не найдено записей о группах.");
                                break;
                            }
                            System.out.println("Количество групп: " + (result_group.size()-1) + "\nСписок:");
                            for (id = 0; id < result_group.size() - 1; id++)
                                System.out.println(result_group.get(id).getId() + " " + result_group.get(id).getName());
                            break;
                        case 5:
                            System.out.print("Введите название группы: ");
                            groupName = scanner.next();
                            result_student = group.getGroupStudents(groupName);
                            if (result_student.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            if (result_student.get(0).getFullName().equals("last")) {
                                System.out.println("В группе не значится ни один студент.");
                                break;
                            }
                            System.out.println("Количество студентов в группе: " + (result_student.size()-1) + "\nСписок:");
                            for (id = 0; id < result_student.size() - 1; id++)
                                System.out.println(result_student.get(id).getId() + " " + result_student.get(id).getFullName());
                            break;
                        case 6:
                            System.out.print("Введите название группы: ");
                            groupName = scanner.next();
                            result_teachGr = group.getGroupTeachers(groupName);
                            if (result_teachGr.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            if (result_teachGr.get(0).getName().equals("last")) {
                                System.out.print("Не найдено записей о преподавателях группы.");
                                break;
                            }
                            System.out.println("Количество преподавателей группы: " + (result_teachGr.size()-1) + "\nСписок:");
                            for (id = 0; id < result_teachGr.size() - 1; id++)
                                System.out.println(result_teachGr.get(id).getName() + " ведёт \"" + result_teachGr.get(id).getSubject() + "\"");
                            break;    
                    }
                    break;
                case 33:
                    System.out.print("Введите ID группы, название которой обновится: ");
                    id = scanner.nextInt();
                    System.out.print("Введите новое название группы: ");
                    groupName = scanner.next();
                    if (group.updateNameById(id, groupName) == 0)
                        System.out.println("Название группы изменено.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;                   
                case 34:
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    if (group.deleteNodeById(id) == 0)
                        System.out.println("Группа удалена.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 41:        //Scholarship
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    System.out.print("Введите название типа стипендии: ");
                    name = scanner.next();
                    System.out.print("Введите размер стипендии: ");
                    amount = scanner.nextInt();
                    if (scholarship.createNode(id, name, amount) == 0)
                        System.out.println("Тип стипендии добавлен.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 42:
                    System.out.println("1 - Найти запись о стипендии по ID.");
                    System.out.println("2 - Найти запись о стипендии по названию.");
                    System.out.println("3 - Получить количество типов стипендий в базе.");
                    System.out.println("4 - Получить список всех типов стипендий в базе.");
                    System.out.print("Выберите действие: ");
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.print("Введите ID: ");
                            id = scanner.nextInt();
                            scholarshipNode = scholarship.findByIdOrType(id, "");
                            if (scholarshipNode == null) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат поиска. Тип: " + scholarshipNode.getType()+ "; размер: " + scholarshipNode.getAmount());
                            break;
                        case 2:
                            System.out.print("Введите тип стипендии: ");
                            scanner.nextLine();
                            name = scanner.nextLine();
                            scholarshipNode = scholarship.findByIdOrType(-1, name);
                            if (scholarshipNode == null) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат поиска. ID: " + scholarshipNode.getId() + "; размер: " + scholarshipNode.getAmount());
                            break;
                        case 3:
                            id = scholarship.getScholarshipsNumber();
                            if (id < 0) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Количество типов стипендий: " + id);
                            break;
                        case 4:
                            result_scholarship = scholarship.getAllScholarships();
                            if (result_scholarship.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            if (result_scholarship.get(0).getType().equals("last")) {
                                System.out.print("Не найдено записей о преподавателях группы.");
                                break;
                            }
                            System.out.println("Количество типов стипендий: " + (result_scholarship.size()-1) + "\nСписок:");
                            for (id = 0; id < result_scholarship.size() - 1; id++)
                                System.out.println(result_scholarship.get(id).getId() + " " + result_scholarship.get(id).getType() + " " + result_scholarship.get(id).getAmount());
                            break;
                    }
                    break;
                case 43:
                    System.out.print("Введите ID стипендии, данные о которой обновятся: ");
                    id = scanner.nextInt();
                    System.out.println("Если изменение поля не требуется, введите '-1'");
                    System.out.print("Введите новое название типа стипендии: ");
                    name = scanner.next();
                    System.out.print("Введите новый размер стипендии: ");
                    amount = scanner.nextInt();
                    if (name.equals("-1"))
                        name = "";                     
                    if (scholarship.updateTypeOrAmountById(id, name, amount) == 0)
                        System.out.println("Запись о стипендии изменена.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 44:
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    if (scholarship.deleteNodeById(id) == 0)
                        System.out.println("Тип стипендии удален.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 45:
                    System.out.println("1 - Назначить студенту стипендию.");
                    System.out.println("2 - Изменить запись о выплатах стипендии студенту.");
                    System.out.println("3 - Получить список студентов, получающих стипендию.");
                    System.out.print("Выберите действие: ");
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.print("Введите ID студента: ");
                            id = scanner.nextInt();
                            System.out.print("Введите название стипендии: ");
                            groupName = scanner.next();
                            System.out.println("Введите период выплат (в формате dd.mm.yyyy). Если окончание выплат неопределено введите '-'.");
                            System.out.print("Начало выплат: ");
                            name = scanner.next();
                            System.out.print("Окончание выплат: ");
                            surname = scanner.next();
                            if (scholarship.createRelationship(id, groupName, name, surname) == 0)
                                System.out.println("Стипендия назначена.");
                            else
                                System.out.println("Ошибка во время выполения операции.");
                                    break;
                        case 2:
                            System.out.print("Введите ID студента: ");
                            id = scanner.nextInt();
                            System.out.print("Введите название стипендии: ");
                            groupName = scanner.next();
                            System.out.print("Введите предыдущую дату начала выплат (в формате dd.mm.yyyy): ");
                            patronymic = scanner.next();
                            System.out.println("Введите период выплат (в формате dd.mm.yyyy). Если изменение поля не требуется, введите '-1'.");
                            System.out.print("Начало выплат: ");
                            name = scanner.next();
                            System.out.print("Окончание выплат: ");
                            surname = scanner.next();
                            if (scholarship.updateRelationship(id, groupName, patronymic, name, surname) == 0)
                                System.out.println("Запись обновлена.");
                            else
                                System.out.println("Ошибка во время выполения операции.");
                            break;
                        case 3:
                            System.out.print("Введите тип стипендии: ");
                            scanner.nextLine();
                            name = scanner.nextLine();
                            result_schSt = scholarship.getScholarshipStudents(name);
                            if (result_schSt.get(0).getFullName().equals("last")) {
                                System.out.println("Данную стипендию не получает ни один студент.");
                                break;
                            }                               
                            if (result_schSt.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат: ");
                            for (id = 0; id < result_schSt.size() - 1; id++)
                                System.out.println("Студент: " + result_schSt.get(id).getId()
                                        + " " + result_schSt.get(id).getFullName()
                                        + ", группа " + result_schSt.get(id).getGroup());
                            break;
                    } 
                    break;
                case 51:        //Club
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    System.out.print("Введите название клуба по интересам: ");
                    scanner.nextLine();
                    name = scanner.nextLine();
                    System.out.print("Введите адрес клуба: ");
                    surname = scanner.nextLine();
                    if (club.createNode(id, name, surname) == 0)
                        System.out.println("Клуб добавлен.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 52:
                    System.out.println("1 - Найти клуб по ID.");
                    System.out.println("2 - Найти ID клуба по названию.");
                    System.out.println("3 - Получить количество клубов в базе.");
                    System.out.println("4 - Получить список всех клубов в базе.");
                    System.out.print("Выберите действие: ");
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.print("Введите ID: ");
                            id = scanner.nextInt();
                            clubNode = club.findByIdOrName(id, "");
                            if (clubNode == null) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат поиска. Название: " + clubNode.getName() + "; адрес: " + clubNode.getAddress());
                            break;
                        case 2:
                            System.out.print("Введите название клуба: ");
                            scanner.nextLine();
                            name = scanner.nextLine();
                            clubNode = club.findByIdOrName(-1, name);
                            if (clubNode == null) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат поиска. ID: " + clubNode.getId() + "; адрес: " + clubNode.getAddress());
                            break;
                        case 3:
                            id = club.getClubsNumber();
                            if (id < 0) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Количество клубов: " + id);
                            break;
                        case 4:
                            result_club = club.getAllClubs();
                            if (result_club.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            if (result_club.get(0).getName().equals("last")) {
                                System.out.println("Нет записей о клубах.");
                                break;
                            }
                            System.out.println("Количество клубов: " + (result_club.size()-1) + "\nСписок:");
                            for (id = 0; id < result_club.size() - 1; id++)
                                System.out.println(result_club.get(id).getId() + " " + result_club.get(id).getName() + " " + result_club.get(id).getAddress());
                            break;
                    }
                    break;
                case 53:
                    System.out.print("Введите ID клуба, данные о котором обновятся: ");
                    id = scanner.nextInt();
                    System.out.println("Если изменение поля не требуется, введите '-'");
                    System.out.print("Введите новое название клуба: ");
                    scanner.nextLine();
                    name = scanner.nextLine();
                    System.out.print("Введите новый адрес клуба: ");
                    surname = scanner.nextLine();
                    if (name.equals("-"))
                        name = "";
                    if (surname.equals("-"))
                        surname = "";
                    if (club.updateNameOrAddressById(id, name, surname) == 0)
                        System.out.println("Запись о клубе изменена.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 54:
                    System.out.print("Введите ID: ");
                    id = scanner.nextInt();
                    if (club.deleteNodeById(id) == 0)
                        System.out.println("Клуб удален.");
                    else
                        System.out.println("Ошибка во время выполения операции.");
                    break;
                case 55:
                    System.out.println("1 - Добавить студента в клуб.");
                    System.out.println("2 - Изменить статус студента в клубе.");
                    System.out.println("3 - Удалить студента из клуба.");
                    System.out.println("4 - Получить список студентов в клубе.");
                    System.out.print("Выберите действие: ");
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.print("Введите ID студента: ");
                            id = scanner.nextInt();
                            System.out.print("Введите название клуба: ");
                            scanner.nextLine();
                            groupName = scanner.nextLine();
                            System.out.print("Введите статус студента в клубе(MEMBER или CHAIRMAN): ");
                            name = scanner.next();
                            if (club.createRelationship(id, groupName, name) == 0)
                                System.out.println("Студент добавлен в клуб.");
                            else
                                System.out.println("Ошибка во время выполения операции.");
                                    break;
                        case 2:
                            System.out.print("Введите ID студента: ");
                            id = scanner.nextInt();
                            System.out.print("Введите название клуба: ");
                            scanner.nextLine();
                            groupName = scanner.nextLine();
                            System.out.print("Введите новый статус студента в клубе(MEMBER или CHAIRMAN): ");
                            name = scanner.next();
                            if (club.updateRelationship(id, groupName, name) == 0)
                                System.out.println("Статус студента в клубе изменен.");
                            else
                                System.out.println("Ошибка во время выполения операции.");
                            break;
                        case 3:
                            System.out.print("Введите ID студента: ");
                            id = scanner.nextInt();
                            System.out.print("Введите название клуба: ");
                            scanner.nextLine();
                            name = scanner.nextLine();
                            if (club.deleteRelationship(id, name) == 0)
                                System.out.println("Студент удален из клуба.");
                            else
                                System.out.println("Ошибка во время выполения операции.");
                            break;
                        case 4:
                            System.out.print("Введите название клуба: ");
                            scanner.nextLine();
                            name = scanner.nextLine();
                            result_stClub = club.getClubStudents(name);
                            if (result_stClub.get(0).getName().equals("last")) {
                                System.out.println("В клубе не состоит ни один студент.");
                                break;
                            }                               
                            if (result_stClub.isEmpty()) {
                                System.out.println("Ошибка во время выполения операции.");
                                break;
                            }
                            System.out.println("Результат: ");
                            for (id = 0; id < result_stClub.size() - 1; id++)
                                System.out.println("Студент: " + result_stClub.get(id).getName()
                                        + ", группа " + result_stClub.get(id).getGroup()
                                        + ". Статус: " + result_stClub.get(id).getPosition());
                            break;
                    } 
                    break;
            }
        }
    }
}