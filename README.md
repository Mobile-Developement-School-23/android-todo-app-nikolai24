# ToDo App - Домашнее задание 6
- Экран создания/редактирования задачи на XML - ветка homework6  
- Экран создания/редактирования задачи переписанный на Jetpack Compose - ветка compose
- В обоих версиях одинаковая функциональность, отличается только EditItemFragment ( XML / Jetpack Compose ).
- В приложении поддерживается дневная и ночная тема. 
- Создан экран настроек SettingsFragment, в котором можно выбрать тему: "Всегда светлая", "Всегда тёмная" или "Как в системе".
- Для Compose создана AppTheme функция, в которой определяется тема приложения.
- Цвета вынесены в палитру цветов. 
- Цвет текста зависит от темы. Если текст на тёмном фоне - он светлый. А если на светлом фоне то наоборот.
- Переопределены основные цвета-аттрибуты (primary, secondary и т.д.)
- Стили текстовых View (типографика) вынесены в отдельный файл. Для TextView и других текстовых компонент используются ссылки на необходимый стиль.
- Поддержан эффект нажатия на кнопки, меняется цвет при нажатии.
- Добавлена анимация перехода/возвращения между фрагментами.  
- Создан не блокирующий снекбар отмены удаления. В снекбаре есть текст “Удалено дело: Имя_Дела” + кнопка отменить. Снекбар появляется и скрывается с анимацией выезда снизу экрана. Скрытие происходит по истечению 5 секунд.
