## Менеджер задач  
### Приложение для планирования текущей деятельности.  
Простейшей сущностью системы является ***задача*** (англ. task). У задачи есть следующие свойства:  
![Макет задачи](https://github.com/MaksimOrekhoff/java-sprint2-hw/blob/master/src/resource/task.png)
1) Название, кратко описывающее суть задачи (например, «Переезд»).  
2) Описание, в котором раскрываются детали.  
3) Уникальный идентификационный номер задачи, по которому её можно будет найти.  
4) Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:  
4.1) NEW — задача только создана, но к её выполнению ещё не приступили.  
4.2) IN_PROGRESS — над задачей ведётся работа.  
4.3) DONE — задача выполнена.  
Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на ***подзадачи*** (англ. subtask). Большую задачу, которая делится на подзадачи, мы будем называть ***эпиком*** (англ. epic).  
Таким образом, в нашей системе задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. Для них должны выполняться следующие условия:  
* Для каждой подзадачи известно, в рамках какого эпика она выполняется.  
* Каждый эпик знает, какие подзадачи в него входят.  
* Завершение всех подзадач эпика считается завершением эпика.  
В классах-менеджерах реализованы следующие функции:  
1) Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
2) Методы для каждого из типа задач(Задача/Эпик/Подзадача):  
* Получение списка всех задач.  
* Удаление всех задач.  
* Получение по идентификатору.  
* Создание. Сам объект должен передаваться в качестве параметра.  
* Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.  
* Удаление по идентификатору.
3) Дополнительные методы:  
*Получение списка всех подзадач определённого эпика.
4) Управление статусами осуществляется по следующему правилу:  
* Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.  
5) Для эпиков:  
* если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.  
* если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.  
* во всех остальных случаях статус должен быть IN_PROGRESS.  

Трекер отображает последние просмотренные пользователем задачи. Для этого методы **getHistory()** в TaskManager — он возвращает последние 10 просмотренных задач. Просмотром будем считаться вызов у менеджера методов получения задачи по идентификатору — **getTask()**, **getSubtask()** и **getEpic()**.  
Пример формирования истории просмотров задач после вызовов методов менеджера:  
![История просмотров](https://github.com/MaksimOrekhoff/java-sprint2-hw/blob/master/src/resource/history.png)
Если какая-либо задача просматривалась несколько раз, в истории отобразится только последний просмотр. Предыдущий просмотр удаляется сразу же после появления нового — за **O(1)**. Константное время выполнения операции может гарантировать связный список **CustomLinkedList**. Однако его стандартная реализация в данном случае не подойдёт. Поэтому предстоит написать собственную.
**CustomLinkedList** позволяет удалить элемент из произвольного места за **О(1)**. Чтобы выполнить условие, создана стандартная **HashMap**. Её ключом будет **id** задачи, просмотр которой требуется удалить, а значением — место просмотра этой задачи в списке, то есть узел связного списка. С помощью номера задачи можно получить соответствующий ему узел связного списка и удалить его.  
![Связный список](https://github.com/MaksimOrekhoff/java-sprint2-hw/blob/master/src/resource/LinkedList.png)  

## Реализации интерфейса менеджера задач  

### Первая реализация менеджера
**InMemoryTaskManager** Хранение данных происходит в полях класса в коллекциях **HashMap**  

### Вторая реализация менеджера 
**FileBackedTasksManager** такой класс менеджера, который будет после каждой операции автоматически сохранять все задачи и их состояние в специальный файл.
У него будет такая же система классов и интерфейсов, как и у **InMemoryTaskManager**. Новый и старый менеджеры будут отличаться только деталями реализации методов: один хранит информацию в оперативной памяти, другой — в файле.  
![FileBackedTasksManager](https://github.com/MaksimOrekhoff/java-sprint2-hw/blob/master/src/resource/fileback.png)  
### Третья реализация менеджера
Основная логика приложения реализована, теперь можно сделать для него API. Доступ к методам менеджера через HTTP-запросы, где эндпоинты будут соответствовать вызовам базовых методов интерфейса **TaskManager**. Вот как это должно будет выглядеть.  
![HttpTaskTasksManager](https://github.com/MaksimOrekhoff/java-sprint2-hw/blob/master/src/resource/httptask.png)
API должен работать так, чтобы все запросы по пути */tasks/<ресурсы>* приходили в интерфейс **TaskManager**. Путь для обычных задач — */tasks/task*, для подзадач — */tasks/subtask*, для эпиков — */tasks/epic*. Получить все задачи сразу можно будет по пути */tasks/*, а получить историю задач по пути */tasks/history*.  
Для получения данных GET-запросы. Для создания и изменения — POST-запросы. Для удаления — DELETE-запросы. Задачи передаются в теле запроса в формате JSON. Идентификатор **id** задачи следует передавать параметром запроса.
В результате для каждого метода интерфейса **TaskManager** создан отдельный эндпоинт, который можно вызвать по HTTP.
