# NewsPortal
БД MAMP, скачиваем, устанавливаем, запускаем сервер, нажимаем кнопку OpenWebStart page, Слева, где MySQL находим данные для подключения.
Нажимаем на phpMAdmin, создаем новую Базу.

Переходим к проекту, в aplication.properties прописываем заместо 3306/news_database порт и название созданной вами БД, в username и password - user и password (если они отличаются).

Запуск:
Сначала запускаем MySQL Server, нажимая в MAMP'е Start Servers.
Далее проект можно запускать из под IDEA или же собрать maven'ом и запустить jar файл (в таком случае, чтобы закрыть придётся снимать процесс Java SE из диспетчера задач.

Переходим по lacalhost:8080.

После открытия сайта лучше перейти обратно в phphMyAdmin и поменять тип данных атрибута text с varchar на text
