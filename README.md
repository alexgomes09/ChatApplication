## Chat Application

A chat application using RoomDB, demonstrating the MVVM Clean architecture pattern.

<hr>

[<img src="https://i.ibb.co/vZg7qtC/Screenshot-2023-09-30-at-4-31-34-PM.png" width="20%">](https://github.com/alexgomes09/ChatApplication/raw/main/assets/recording.mp4)

<span>
<img src="https://github.com/alexgomes09/ChatApplication/blob/main/assets/new_chat.png" width="300">
<img src="https://raw.githubusercontent.com/alexgomes09/ChatApplication/main/assets/chat_list.png" width="300">
<img src="https://github.com/alexgomes09/ChatApplication/blob/main/assets/chat_message.png?raw=true" width="300">
</span>

<hr/>

### Known Issue
- Project gradle sometime goes out of sync due to kapt annotation bug. Please sync the project when following error is thrown.

<code>Each bind variable in the query must have a matching method parameter. Cannot find method parameters for :chatItemId. - com.example.data.dao.ChatDao.getAllChatByChatItemId(long)
</code>