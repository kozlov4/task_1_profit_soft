# Java Core Task: Statistics Parser

Консольна утиліта для багатопоточного парсингу JSON-файлів та генерації XML-статистики.
Розроблено в рамках курсу ProfITsoft (Блок 1).

## Опис проекту
Програма призначена для обробки великого масиву даних про книги. Вона сканує вказану папку, зчитує JSON-файли у багатопоточному режимі та формує звіт за обраним атрибутом (наприклад, підрахунок книг конкретного автора або популярність тегів).

Технологічний стек:
- **Java 21**
- **Maven**
- **Jackson** (для потокового парсингу JSON)
- **Java Util Concurrent** (ExecutorService для багатопоточності)

---

## Основні сутності
Предметна область: **Технічна література (DevBook)**.

Основна сутність `DevBook` має такі атрибути:
- `title` (String) — Назва книги.
- `author` (String) — Автор книги (відношення "багато-до-одного").
- `year_published` (int) — Рік видання.
- `tags` (List<String>) — Список категорій/тегів (атрибут з кількома значеннями).

---

## Приклади файлів

### Вхідний файл (JSON)
Файли повинні містити масив об'єктів. Програма підтримує потокове читання, тому розмір файлу може бути великим.

```json
[
  {
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "year_published": 2018,
    "tags": ["Java", "Programming", "Best Practices"]
  },
  {
    "title": "Clean Code",
    "author": "Robert Martin",
    "year_published": 2008,
    "tags": ["Clean Code", "Refactoring", "Java"]
  }
]
```
### Вихідний файл (XML)
Приклад результату виконання програми по атрибуту `tags` (файл `statistics_by_tags.xml`):

```xml
<?xml version="1.0" ?>
<statistics>
  <item>
    <value>Java</value>
    <count>2</count>
  </item>
  <item>
    <value>Programming</value>
    <count>1</count>
  </item>
  <item>
    <value>Clean Code</value>
    <count>1</count>
  </item>
  ...
</statistics>
```
### 1. Збірка проекту
Переконайтеся, що встановлено Java 21 та Maven.

```shell
mvn clean package
2. Запуск
Shell

java -jar target/task_1_kozlov_serge-1.0-SNAPSHOT.jar <шлях_до_папки> <атрибут>
Параметри:

<шлях_до_папки> — шлях до директорії з .json файлами.

<атрибут> — поле, по якому збирати статистику. Доступні варіанти: author, year, tags.

Приклад:

Shell

java -jar target/task_1_kozlov_serge-1.0-SNAPSHOT.jar "src/main/resources/books" "author"

Результати експериментів з продуктивністю
Було проведено тестування швидкодії програми при різній кількості потоків. Умови тесту: Обробка масиву даних, що складається з великої кількості JSON-файлів.


Кількість потоків,Час виконання (мс),Прискорення (відносно 1 потоку)
1 потік,~2800 ms,1.0x (База)
2 потоки,~1500 ms,1.86x
4 потоки,~800 ms,3.5x
8 потоків,~780 ms,3.58x





