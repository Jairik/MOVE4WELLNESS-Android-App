package com.example.move4wellness;/* Manager Status Callback Interface - Uses a callback mechanism to get the Manager's value
through the asynchronous operations of firestore
* Author: JJ McCauley
* Creation Date: 5/9/24 */

public interface ManagerStatusCallback {
    void OnCallBack(boolean isManager);
}
