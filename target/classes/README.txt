Программа предназначена для обрезки pay.log/io.log по указанным шаблонам.

Необходима предустановленная JRE/JDK версии не менее 1.8.0
При обработке файлов от 100мб и больше, рекомендую выставить в файле
LogHandler.l4j.ini в -Xmx512m (либо больше, если не жалко оперативной памяти).

Использование.
1) Выбрать файл для обработки
2) Выбрать папку для сохранения результата
(Не обязательно, по-умолчанию сохранит в папку из которой был выбран файл)
3) Шаблон по которому проводить обработку
4) И ввести поисковой запрос.(GUID/Регулярное выражение)

ВНИМАНИЕ!!! 
Поисковой запрос чувствителен к регистру!!!
Результат сохранится:
для шаблона GUIDa - "Введенный_гуид.txt"
для регулярного выражения - "REGEX.txt"